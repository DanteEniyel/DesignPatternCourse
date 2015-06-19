/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.uzdiz.jelvalcicZ2;

import static java.lang.Math.round;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import static org.foi.uzdiz.jelvalcicZ2.Main.brojKola;
import static org.foi.uzdiz.jelvalcicZ2.Main.brojRangListe;
import static org.foi.uzdiz.jelvalcicZ2.Main.caretakerParovi;
import static org.foi.uzdiz.jelvalcicZ2.Main.caretakerRangLista;
import static org.foi.uzdiz.jelvalcicZ2.Main.intervalKontroleRangListe;
import static org.foi.uzdiz.jelvalcicZ2.Main.intervalSpavanjaDretve;
import static org.foi.uzdiz.jelvalcicZ2.Main.klubovi;
import static org.foi.uzdiz.jelvalcicZ2.Main.kraj;
import static org.foi.uzdiz.jelvalcicZ2.Main.pragIntervalaZaIspadanje;
import static org.foi.uzdiz.jelvalcicZ2.Main.prviKlub;
import org.foi.uzdiz.jelvalcicZ2.interfejsi.SubjectInterfejs;
import org.foi.uzdiz.jelvalcicZ2.objekti.Klub;
import org.foi.uzdiz.jelvalcicZ2.objekti.ParSaRezultatom;
import org.foi.uzdiz.jelvalcicZ2.objekti.Parovi;
import org.foi.uzdiz.jelvalcicZ2.objekti.PovijestParova;
import org.foi.uzdiz.jelvalcicZ2.objekti.RangLista;
import org.foi.uzdiz.jelvalcicZ2.objekti.RedRangListe;
import org.foi.uzdiz.jelvalcicZ2.objekti.Subject;

/**
 *
 * @author Dante Eniyel
 * Klasa dretve unutar koje se gnereiraju kola
 * i pozivaju promjene
 */
public class Dretva extends Thread {

    private List<Integer> aktivniKlubovi = new ArrayList<>();
    private RangLista trenutnaRangLista = new RangLista();
    private List<RedRangListe> tmpRL = new ArrayList<>();
    private Date vrijemePocetka;
    private Date vrijemeKraja;
    private SubjectInterfejs s = new Subject();


    public Dretva() {
        for (Klub k : klubovi) {//kreira se pocetna rang lista samo sa siframa klubova
            RedRangListe rrl = new RedRangListe(k.getSifraKluba(), 0, 0, 0);
            trenutnaRangLista.getRangLista().add(rrl);
        }
    }

    @Override
    public synchronized void start() {
        System.out.println("Dretva je pokrenuta... Neka igre pocnu!");
        super.start();
    }

    @Override
    public void run() {
        s.addObserver(trenutnaRangLista);//subscribe
        while (!kraj) {
            vrijemePocetka = new Date();
            
            brojKola++;
            //System.out.println("Work, work!");

            aktivniKlubovi.clear();
            for (Klub k : klubovi) {
                if (!k.isIspaoIzNatjecanja()) {
                    aktivniKlubovi.add(k.getSifraKluba());
                }

            }
            if (aktivniKlubovi.size() > 2) {//dok ima vise od dva kluba
                tmpRL.clear();
                for (RedRangListe l : trenutnaRangLista.getRangLista()) {
                    RedRangListe tmp = new RedRangListe(l.getSifraKluba(), l.getUkupniBodovi(), l.getPoredak(), brojKola);
                    tmpRL.add(tmp);
                }

                RangLista rL = new RangLista();
                Parovi p = new Parovi();
                s.addObserver(p);
                int brojPara = 0;
                while (aktivniKlubovi.size() > 1) {//dok postoji klubova za generiranje parova, znaci vise od 1
                    brojPara++;
                    //generiranje parova
                    int prviKlub = (int) (Math.random() * aktivniKlubovi.size());
                    int prviKlubSifra = aktivniKlubovi.get(prviKlub);//pamtimo sifru kluba prije nego ga izbacimo iz liste da nema puno if-ova
                    aktivniKlubovi.remove(prviKlub);
                    int drugiKlub = (int) (Math.random() * aktivniKlubovi.size());
                    int drugiKlubSifra = aktivniKlubovi.get(drugiKlub);
                    aktivniKlubovi.remove(drugiKlub);

                    //generiranje rezultata
                    int rezultat = (int) (Math.random() * 3);//da dode u obzir i rezultat 2
                    ParSaRezultatom psr = new ParSaRezultatom(prviKlubSifra, drugiKlubSifra, rezultat);
                    s.setState(psr);//p.dodajPar(psr); i updateRangListu(prviKlubSifra, drugiKlubSifra, rezultat);/ postavljanje stanja subjekta za observere
                    updateKlubPovijestParova(psr, brojPara);
                    updateEfikasnostKlubova(psr);
                    //System.out.println("Domacin: " + prviKlubSifra + " Gost: " + drugiKlubSifra + " Rezultat susreta: " + rezultat);

                }
                s.removeObserver(p);
                //promjena poretka u rang listi prema rezultatima
                Collections.sort(trenutnaRangLista.getRangLista(), new komparatorUkupnihBodova());
                updateRedniBroj();
                if (isPromjenaRL()) {
                    brojRangListe++;
                    rL.setRangLista(trenutnaRangLista.getRangLista());
                    List<RedRangListe> tmp = new ArrayList<>();
                    for(RedRangListe t : trenutnaRangLista.getRangLista()){
                        RedRangListe rt = new RedRangListe(t.getSifraKluba(), t.getUkupniBodovi(), t.getPoredak(), brojKola);
                        tmp.add(rt);
                    }
                    rL.setRangLista(tmp);
                    //spremanje u meneto (Rang lista)
                    caretakerRangLista.addMemento(rL.saveToMemento());
                }
                /*System.out.println(" ---- ");
                for (RedRangListe r : trenutnaRangLista) {
                    System.out.println(r.getPoredak() + " " + r.getSifraKluba() + " " + r.getUkupniBodovi());
                }*/
                //p.setBrojRangListe(brojRangListe);
                //spremanje u memento(Parovi)
                caretakerParovi.addMemento(p.saveToMemento());

                if ((brojKola % intervalKontroleRangListe) == 0) {//znaci da je vrijeme za provjeru(ostatak je 0)
                    provjeriStanjeKlubova();
                }

                System.out.println("Kraj kola " + brojKola);
                
                vrijemeKraja = new Date();
            } else {
                kraj = true;
            }
            if (!kraj) {
                try {
                    //System.out.println(vrijemeKraja.getTime() - vrijemePocetka.getTime());
                    sleep((intervalSpavanjaDretve  * 1000) - (vrijemeKraja.getTime() - vrijemePocetka.getTime()));
                } catch (InterruptedException ex) {
                    System.out.println("You interrupted my beauty sleep!");
                }
            }else{
                interrupt();
            }
        }
        s.removeObserver(trenutnaRangLista);//unsubscribe
        //System.out.println("We lost him!");
        System.out.println("Igre su gotove!");

    }

    @Override
    public void interrupt() {
        kraj = true;
        super.interrupt();
    }


/**
 * Metoda za provjeru promjena u rang listi
 * @return - true - ako je doslo do promjene, false - ako nije doslo do promjene
 */    
    private boolean isPromjenaRL() {
        boolean rez = false;
        for (RedRangListe rRL1 : trenutnaRangLista.getRangLista()) {
            for (RedRangListe rRL2 : tmpRL) {
                if (rRL1.getSifraKluba() == rRL2.getSifraKluba()) {
                    if((rRL1.getUkupniBodovi() != rRL2.getUkupniBodovi())){
                        //ako je doslo do barem jedne promjene u bodovima mijenja se rang lista i arhivirati ce se
                    rez = true;
                    break;
                    }
                }
            }
            if (rez == true) {
                break;
            }
        }
        return rez;
    }

/**
 * Metoda za promjenu poretka nakon sto se dogodi promjena
 */    
    private void updateRedniBroj() {
        int i = 0;
        int j = 0;
        int ps = -1;
        for (RedRangListe l : trenutnaRangLista.getRangLista()) {
            j++;
            if (l.getUkupniBodovi() != ps) {
                ps = l.getUkupniBodovi();
                i = j;
            }
            l.setPoredak(i);

        }
    }

/**
 * Metoda za promjenu povijesti parova
 * @param psr - trenutno generirani par u kolu
 * @param brojPara - indeks u listi parova kola
 */    
    private void updateKlubPovijestParova(ParSaRezultatom psr, int brojPara) {
        for (Klub k : klubovi) {
            if ((k.getSifraKluba() == psr.getPrviKlub()) || (k.getSifraKluba() == psr.getDrugiKlub())) {
                PovijestParova pp = new PovijestParova(brojKola, brojPara);
                k.dodajRedPovijesti(pp);
            }
        }
    }

/**
 * Metoda za dohvat ukupnih bodova pojedinog kluba(za racunanje efikasnosti)
 * @param sifraKluba - sifra kluba za koji se trazi ukupan broj bodova
 * @return int - ukupni bodovi kluba
 */    
    private int brojUkupnihBodovaKluba(int sifraKluba) {
        int ukupniBodovi = 0;

        for (RedRangListe rrL : trenutnaRangLista.getRangLista()) {
            if (rrL.getSifraKluba() == sifraKluba) {
                ukupniBodovi = rrL.getUkupniBodovi();
                break;
            }
        }

        return ukupniBodovi;
    }

/**
 * Metoda za racunanje i postavljanje nove efikasnosti kluba
 * @param par - trenutno generirani par u kolu
 */    
    private void updateEfikasnostKlubova(ParSaRezultatom par) {
        float prethodnaEfikasnost;
        float novaEfikasnost;
        for (Klub k : klubovi) {
            if ((k.getSifraKluba() == par.getPrviKlub()) || (k.getSifraKluba() == par.getDrugiKlub())) {
                prethodnaEfikasnost = k.getEfikasnostKluba();
                if (k.getPp().size() > 0) {
                    novaEfikasnost = (float) brojUkupnihBodovaKluba(k.getSifraKluba()) / k.getPp().size();
                    int t = round(novaEfikasnost * 100);
                    novaEfikasnost = (float) (t / 100.0);

                } else {
                    novaEfikasnost = 0;
                }
                if (prethodnaEfikasnost != novaEfikasnost) {
                    System.out.println("Klub: " + k.getNazivKluba() + " je promijenio efikasnost za " + (novaEfikasnost - prethodnaEfikasnost));
                    k.setEfikasnostKluba(novaEfikasnost);
                }
            }
        }
    }

/**
 * Metoda za vracanje poretka kluba
 * @param sifraKluba - sifra kluba za koji se trazi trenutni poredak
 * @return int - trenutni poredak kluba
 */    
    private int poredakKluba(int sifraKluba) {
        int poredak = 0;

        for (RedRangListe rrL : trenutnaRangLista.getRangLista()) {
            if (rrL.getSifraKluba() == sifraKluba) {
                poredak = rrL.getPoredak();
                break;
            }
        }

        return poredak;
    }

/**
 * Metoda koja provjerava trenutno stanje klubova(da li je ispao iz natjecanja)
 * Salje se poruka klubu (Chain of responsibility uzorak)
 */    
    private void provjeriStanjeKlubova() {
        int prethodniPoredak;
        int trenutniPoredak;

        for (Klub k : klubovi) {
            prethodniPoredak = k.getPoredakPosljednjeProvjere();
            trenutniPoredak = poredakKluba(k.getSifraKluba());

            if (!k.isIspaoIzNatjecanja()) {
                if (prethodniPoredak < trenutniPoredak) {//natjecatelj je slabiji
                    k.setSlabNatjecatelj(k.getSlabNatjecatelj() + 1);//povecavamo za provjeru u kojoj je bio slab

                } else if (prethodniPoredak > trenutniPoredak) {
                    k.setSlabNatjecatelj(0);//natjecatelj je postao normalan

                } else {
                    if (k.getSlabNatjecatelj() > 0) {
                        k.setSlabNatjecatelj(k.getSlabNatjecatelj() + 1);//ako je poredak isti ali je slabi natjecatelj opet povecava za provjeru
                    }
                }
                k.setPoredakPosljednjeProvjere(trenutniPoredak);//spremamo trenutni poredak za slijedecu provjeru

                if (k.getSlabNatjecatelj() >= pragIntervalaZaIspadanje) {
                    prviKlub.message(k.getSifraKluba());//slanje poruke za chain of responsibility uzorka
                }
            }

        }
    }
}

/**
 * Klasa kojom se ostvaruje sortiranje rang liste prema ukupnim bodovima
 * @author Dante Eniyel
 */
class komparatorUkupnihBodova implements Comparator<RedRangListe> {

    @Override
    public int compare(RedRangListe rezPrvi, RedRangListe rezDrugi) {
        return rezDrugi.getUkupniBodovi() - rezPrvi.getUkupniBodovi();
    }
}
