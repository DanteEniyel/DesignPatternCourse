/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.uzdiz.jv.z4.main;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Klasa pomocnih metoda za rad programa
 *
 * @author Dante Eniyel
 */
public class SupportSingleton {

    private static volatile SupportSingleton INSTANCE;

// Private constructor suppresses generation of a (public) default constructor    
    public SupportSingleton() {
    }

    /**
     * Metoda za dohvacanje instance klase
     *
     * @return vraća se instanca klase ako postoji
     */
    public static SupportSingleton getInstance() {
        if (INSTANCE == null) {
            synchronized (SupportSingleton.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SupportSingleton();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Metoda koja dohvaca linkove koji se nalaze na aktivnoj stranci
     *
     * @param stranica - url aktivne stranice
     * @return lista linkova koji se nalaze na aktivnoj stranici
     */
    public List<String> nadjiLinkove(Document doc) {
        List<String> linkovi = new ArrayList<>();

        Elements links = doc.getElementsByTag("a");
        for (Element link : links) {
            String linkHref = link.attr("href");
            String linkText = link.text();

            if (linkHref.startsWith("http")) {
                linkovi.add(linkHref);
                //System.out.println("---------------------------------------------");
                //System.out.println(linkHref);
            }

        }
        return linkovi;
    }

    /**
     * Metoda koja cita korisnikov unos broja
     *
     * @param opis - uputstva korisniku
     * @return vraca se uneseni broj
     */
    public int unosBroja(String opis) {
        System.out.print(opis + " ");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String odabraniBroj = "";
        try {
            odabraniBroj = br.readLine();
        } catch (IOException ex) {
            Logger.getLogger(SupportSingleton.class.getName()).log(Level.SEVERE, null, ex);
        }
        int odBroj = 0;
        try {
            odBroj = Integer.parseInt(odabraniBroj);
        } catch (Exception e) {
            odBroj = 0;//ako dodje do greske kod unosa vrati se vrijednost 0
        }

        return odBroj;
    }

    /**
     * Metoda koja vraca URL podatke
     *
     * @param link - url aktivne stranice
     * @param urlPodaci - lista posjecenih stranica
     * @return vracaju se spremljeni linkovi za aktivnu stranicu
     */
    public URLPodaci vratiURLPodatke(String link, List<URLPodaci> urlPodaci) {
        URLPodaci p = null;

        for (URLPodaci pp : urlPodaci) {
            if (pp.getLink().equalsIgnoreCase(link)) {
                p = pp;
                break;
            }
        }

        return p;
    }

    /**
     * Metoda za serializaciju objekta stranica
     *
     * @param o - objekt
     * @param p - parametri
     * @return - poruka o statusu serializacije
     */
    public String zapisiObjekt(Object o, Parametar p, String imeDatoteke) {
        String poruka = "";
        String putanja = p.getDirektorij() + "/" + imeDatoteke;
        try {
            FileOutputStream fileOut = new FileOutputStream(putanja, false);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(o);
            out.close();
            fileOut.close();
            poruka = "Podaci su serializirani! Gl hf :D";

        } catch (IOException i) {
            i.printStackTrace();
        }

        return poruka;
    }

    /**
     * Metoda za deserializaciju objekta stranice
     *
     * @param p - parametri
     * @return - poruka o statusu deserializacije
     */
    public StraniceObjekt ucitajObjekt(Parametar p) {
        String poruka = "";
        String putanja = p.getDirektorij() + "\\" + p.getImeObjekta();
        StraniceObjekt so = null;
        try {
            FileInputStream fileIn = new FileInputStream(putanja);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            so = (StraniceObjekt) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException i) {
            System.out.println("Datoteka serializacije ne postoji!");

        } catch (ClassNotFoundException c) {
            System.out.println("Class not found");
            c.printStackTrace();
        }
        return so;
    }

    /**
     * Metoda za kreiranje imena datoteke pod kojim spremamo stranicu
     *
     * @param url - link stranice koju spremamo
     * @return - ime datoteke za stranicu koju spremamo
     */
    public String stranicaDatName(String url) {
        String imeDatoteke = url.replaceAll("http://", "").replaceAll("https://", "").replaceAll("/", "_").replaceAll("\\.", "-");
        if (imeDatoteke.length() >= 50) {
            imeDatoteke = imeDatoteke.substring(0, 49);
        }
        imeDatoteke = imeDatoteke + ".cache";

        return imeDatoteke;
    }

    /**
     * Metoda za deserializaciju objekta dokumenta stranice (tip Document)
     *
     * @param p - parametri
     * @return - sadrzaj stranice u Document obliku
     */
    public Document ucitajDokument(Parametar p, String imeDokumenta) {
        String poruka = "";
        String putanja = p.getDirektorij() + "\\" + imeDokumenta;
        Document doc = null;
        String tmpDoc = "";
        try {
            FileInputStream fileIn = new FileInputStream(putanja);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            tmpDoc = (String) in.readObject();
            in.close();
            fileIn.close();
            doc = Jsoup.parse(tmpDoc);//Zato jer Document nije Serializable/serializiranu String datoteku pretveramo u Document
            //tako da bi ju mogli citati
        } catch (IOException i) {
            System.out.println("Datoteka serializacije ne postoji!");

        } catch (ClassNotFoundException c) {
            System.out.println("Class not found");
            c.printStackTrace();
        }
        return doc;
    }

/**
 * Metoda za ponovnu izgradnju Chain of Responsibility
 * @param posjeceneStranice - lista posjecenih stranica (stranica koje su u spremistu)
 * @return - pocetna stranica za chain of responsibility
 */    
    public URLPodaci rebuildChain(List<URLPodaci> posjeceneStranice) {
        URLPodaci pocetnaStranica; 
        URLPodaci tekucaStranica;
        if (posjeceneStranice.size() == 0) {//ako je prazna lista posjecenih stranica prekida se metoda
            return null;
        } else {
            if (posjeceneStranice.size() == 1) {//ako je samo jedna posjecena stranica postavljamo slijedeceg na null
                pocetnaStranica = posjeceneStranice.get(0);//postavljamo pocetnu stranicu za chain
                pocetnaStranica.setSlijedeci(null);
                return pocetnaStranica;
            } else {
                pocetnaStranica = posjeceneStranice.get(0);//postavljamo pocetnu stranicu za chain
                tekucaStranica = posjeceneStranice.get(0);
                for (int i = 0; i < (posjeceneStranice.size() - 1); i++) {//ako ima vise stranica u listi rebuilda se chain of responsibility
                    tekucaStranica.setSlijedeci(posjeceneStranice.get(i+1));
                    tekucaStranica = posjeceneStranice.get(i+1);
                }
                tekucaStranica.setSlijedeci(null);
                return pocetnaStranica;
            }
        }
    }
    
/**
 * Metoda koja provjerava da li se stranica koja je ucitana nalazi u objektu da se ne zapise ponovno
 * @param url - link stranice koja se uvitava
 * @param up - lista stranica koje se nalaze u objektu
 * @return - da li postoji stranica u objektu
 */    
    public boolean provjeriDuplic(String url, List<URLPodaci> up){
        boolean postoji = false;
        
        for(URLPodaci t : up){
            if(t.getLink().equalsIgnoreCase(url)){
                postoji = true;
                break;
            }
        }
        return postoji;
    }
}
