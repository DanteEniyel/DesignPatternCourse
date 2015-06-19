/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.uzdiz.jv.z4.evictor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.foi.uzdiz.jv.z4.main.Parametar;
import org.foi.uzdiz.jv.z4.main.SupportSingleton;
import org.foi.uzdiz.jv.z4.main.URLPodaci;
import org.foi.uzdiz.jv.z4.main.Zadaca4;
import static org.foi.uzdiz.jv.z4.main.Zadaca4.posjeceneStranice;
import org.foi.uzdiz.jv.z4.observer.Subject;
import org.foi.uzdiz.jv.z4.observer.SubjectInterfejs;

/**
 * Klasa uzorka Evictor
 * @author Dante Eniyel
 */
public class Evictor {

    private int kapacitet;
    private boolean kb;
    private String strategija;
    private String putanjaSpremista;
    private SubjectInterfejs s = new Subject();
    private SupportSingleton support;

/**
 * Konstruktor
 * @param p - parametri
 */    
    public Evictor(Parametar p) {
        this.kapacitet = p.getKapacitetSpremista();
        this.kb = p.isKb();
        this.strategija = p.getStrategijaIzbacivanja();
        this.putanjaSpremista = p.getDirektorij();
        //registriramo observere
        s.addObserver(Zadaca4.posjeceneStranice);
        s.addObserver(Zadaca4.dnevnik);
        //----------------------
        support = SupportSingleton.getInstance();

    }

/**
 * Metoda koja osigurava slobodno mjesto u spremniku za novu stranicu ako je on pun
 * @param velicinaStranice - velicina stranice koja se zeli spremiti
 * @return - da li je uspjesno napravljeno mjesto
 */    
    public boolean napraviMjestaUSpremistu(int velicinaStranice) {
        if (!imaMjesta(velicinaStranice)) {
            System.out.println("Nema mjesta u spremistu, primjeniti ce se odgovarajuca metoda izbacivanja!");

            if (!kb) {//ako nisu kilobajti
                if (strategija.equalsIgnoreCase("NS")) {//FIFO brojcano
                    strategijaNSnotKB();//ako nisu kilobajti i ide najstarija stranica van
                } else {
                    strategijaNKnotKB();//ako nisu kilobajti i ide najkorištenija stranica van
                }
            } else {
                if ((velicinaStranice / 1024) < kapacitet) {
                    if (strategija.equalsIgnoreCase("NS")) {
                        strategijaNSKB(velicinaStranice);
                    } else {
                        strategijaNKKB(velicinaStranice);
                    }
                } else {
                    System.out.println("Velicina stranice premasuje ukupni kapacitet spremista!");
                    return false;
                }
            }
        }

        return true;
    }

/**
 * Metoda koja provjerava da li ima mjesta u spremistu
 * @param velicinaStranice - velicina stranice koja se sprema
 * @return - da li ima mjesta u spremistu
 */    
    private boolean imaMjesta(int velicinaStranice) {

        if (kb) {//ako se gleda po kb
            if (((getFileSize() + velicinaStranice) / 1024) < kapacitet) {
                return true;
            } else {
                return false;
            }
        } else {//ako se gleda po broju datoteka
            if (getFileCount() < kapacitet) {
                return true;
            } else {
                return false;
            }
        }
    }

/**
 * Metoda strategije najstarija stranica po broju elemenata u spremistu
 */    
    private void strategijaNSnotKB() {

        if (posjeceneStranice.posjeceneStranice.size() == 0) {
            return;
        } else {
            long zaUsporedit = posjeceneStranice.posjeceneStranice.get(0).getVrijemeSpremanja();
            URLPodaci tmp;

            tmp = posjeceneStranice.posjeceneStranice.get(0);

            for (URLPodaci u : posjeceneStranice.posjeceneStranice) {
                if ((zaUsporedit < u.getVrijemeSpremanja()) && u.isEvictable()) {//ako je vrijeme zapisa manje i ako se objekt moze izbaciti
                    zaUsporedit = u.getVrijemeSpremanja();
                    tmp = u;
                }
            }

            //cleanup
            //brisanje stranice iz spremista
            String punaPutanjaStranice = putanjaSpremista + "\\" + support.stranicaDatName(tmp.getLink());
            File f = new File(punaPutanjaStranice);

            if (f.exists()) {
                while(Zadaca4.ai.get() != 0);//čeka se dok se ne spusti lock, odn ai postane 0
                f.delete();
            }
            tmp.beforeEviction();//objekt ispisuje poruku o brisanju iz spremnika
            //brisanje stranice iz objekta i slanje poruke dnevniku
            s.setState(tmp);
        }
    }

/**
 * Metoda strategije najstarija stranica po velicini u kilobajtima u spremistu
 * @param velicina - velicina stranice koja se zeli spremiti
 */    
    private void strategijaNSKB(int velicina) {
        if (posjeceneStranice.posjeceneStranice.size() == 0) {
            return;
        } else {
            long zaUsporedit = posjeceneStranice.posjeceneStranice.get(0).getVrijemeSpremanja();
            URLPodaci tmp;

            while ((((kapacitet * 1024) - getFileSize()) < velicina) || (posjeceneStranice.posjeceneStranice.isEmpty())) {
                tmp = posjeceneStranice.posjeceneStranice.get(0);
                for (URLPodaci u : posjeceneStranice.posjeceneStranice) {
                    if ((zaUsporedit < u.getVrijemeSpremanja()) && u.isEvictable()) {//ako je vrijeme zapisa manje i ako se objekt moze izbaciti
                        zaUsporedit = u.getVrijemeSpremanja();
                        tmp = u;
                    }
                }

            //cleanup
                //brisanje stranice iz spremista
                String punaPutanjaStranice = putanjaSpremista + "\\" + support.stranicaDatName(tmp.getLink());
                File f = new File(punaPutanjaStranice);

                if (f.exists()) {
                    while(Zadaca4.ai.get() != 0);//čeka se dok se ne spusti lock, odn ai postane 0
                    f.delete();
                }
                tmp.beforeEviction();//objekt ispisuje poruku o brisanju iz spremnika
                //brisanje stranice iz objekta i slanje poruke dnevniku
                s.setState(tmp);
            }
        }
    }

/**
 * Metoda strategije najkoristenija stranica po broju elemenata u spremistu 
 */    
    private void strategijaNKnotKB() {
        if (posjeceneStranice.posjeceneStranice.size() == 0) {
            return;
        } else {
            int zaUsporedit = posjeceneStranice.posjeceneStranice.get(0).getBrojKoristenjaIzDirektorija();
            URLPodaci tmp;
            tmp = posjeceneStranice.posjeceneStranice.get(0);
            for (URLPodaci u : posjeceneStranice.posjeceneStranice) {
                if ((zaUsporedit < u.getBrojKoristenjaIzDirektorija()) && u.isEvictable()) {//ako je vrijeme zapisa manje i ako se objekt moze izbaciti
                    zaUsporedit = u.getBrojKoristenjaIzDirektorija();
                    tmp = u;
                }
            }

            //cleanup
            //brisanje stranice iz spremista
            String punaPutanjaStranice = putanjaSpremista + "\\" + support.stranicaDatName(tmp.getLink());
            File f = new File(punaPutanjaStranice);

            if (f.exists()) {
                while(Zadaca4.ai.get() != 0);//čeka se dok se ne spusti lock, odn ai postane 0
                f.delete();
            }
            tmp.beforeEviction();//objekt ispisuje poruku o brisanju iz spremnika
            //brisanje stranice iz objekta i slanje poruke dnevniku
            s.setState(tmp);
        }

    }
    
/**
 * Metoda strategije najkoristenija stranica po velicini u kilobajtima u spremistu
 * @param velicina - velicina stranice koja se zeli spremiti 
 */
    private void strategijaNKKB(int velicina) {
        if (posjeceneStranice.posjeceneStranice.size() == 0) {
            return;
        } else {
            long zaUsporedit = posjeceneStranice.posjeceneStranice.get(0).getBrojKoristenjaIzDirektorija();
            URLPodaci tmp;

            while ((((kapacitet * 1024) - getFileSize()) < velicina) || (posjeceneStranice.posjeceneStranice.isEmpty())) {
                tmp = posjeceneStranice.posjeceneStranice.get(0);
                for (URLPodaci u : posjeceneStranice.posjeceneStranice) {
                    if ((zaUsporedit < u.getBrojKoristenjaIzDirektorija()) && u.isEvictable()) {//ako je vrijeme zapisa manje i ako se objekt moze izbaciti
                        zaUsporedit = u.getBrojKoristenjaIzDirektorija();
                        tmp = u;
                    }
                }

            //cleanup
                //brisanje stranice iz spremista
                String punaPutanjaStranice = putanjaSpremista + "\\" + support.stranicaDatName(tmp.getLink());
                File f = new File(punaPutanjaStranice);

                if (f.exists()) {
                    while(Zadaca4.ai.get() != 0);//čeka se dok se ne spusti lock, odn ai postane 0
                    f.delete();
                }
                tmp.beforeEviction();//objekt ispisuje poruku o brisanju iz spremnika
                //brisanje stranice iz objekta i slanje poruke dnevniku
                s.setState(tmp);
            }
        }
    }

/**
 * Metoda za praznjenje spremnika
 * @param posjeceneStranice - stranice koje se nalaze u spremniku
 */    
    public void prazniSpremnik(List<URLPodaci> posjeceneStranice) {//tekuca stranica se ne brise iz objekta
        List<URLPodaci> tmpPosjeceneStranice = new ArrayList<>();

        for (URLPodaci u : posjeceneStranice) {
            tmpPosjeceneStranice.add(u);
        }
        for (URLPodaci p : tmpPosjeceneStranice) {
            File f = new File(putanjaSpremista + "\\" + support.stranicaDatName(p.getLink()));
            if (f.exists()) {
                f.delete();
                s.setState(p);
            }
        }
    }

/**
 * Metoda koja broji elemente u spremistu
 * @return - broj elemenata u spremistu
 */    
    public int getFileCount() {//samo za one koji imaju ekstenziju .cache
        int broj = 0;
        File dir = new File(putanjaSpremista);
        File[] fl = dir.listFiles();

        for (int i = 0; i < fl.length; i++) {
            if (fl[i].getName().endsWith(".cache")) {
                broj++;
            }
        }

        return broj;
    }

/**
 * Metoda koja dohvaca velicinu elemenata u spremistu
 * @return - velicina elemenata u spremistu
 */    
    public long getFileSize() {
        long velicina = 0;
        File dir = new File(putanjaSpremista);
        File[] fl = dir.listFiles();

        for (int i = 0; i < fl.length; i++) {
            if (fl[i].getName().endsWith(".cache")) {
                velicina = velicina + fl[i].length();
            }
        }
        return velicina;
    }
}
