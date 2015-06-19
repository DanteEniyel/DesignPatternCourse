/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.uzdiz.jelvalcic.z3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
 * @author Dante Eniyel
 */
class SupportSingleton {

    private static volatile SupportSingleton INSTANCE;

// Private constructor suppresses generation of a (public) default constructor    
    public SupportSingleton() {
    }

/**
 * Metoda za dohvacanje instance klase
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
 * @param stranica - url aktivne stranice
 * @return lista linkova koji se nalaze na aktivnoj stranici
 */    
    public List<String> nadjiLinkove(String stranica) {
        List<String> linkovi = new ArrayList<>();

        Document doc = null;
        try {
            doc = Jsoup.connect(stranica).get();
        } catch (IOException ex) {
            Logger.getLogger(SupportSingleton.class.getName()).log(Level.SEVERE, null, ex);
        }

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
 * Metoda za usporedjivanje linkova
 * @param link - url aktivne stranice
 * @param stariLinkovi - linkovi aktivne stranice prije osvjezenja
 * @return 
 */    
    public List<String> usporediLinkove(String link, List<String> stariLinkovi){ 
        List<String> noviLinkovi = null;
        noviLinkovi = nadjiLinkove(link);
        
        if(stariLinkovi.size() == noviLinkovi.size()){
            for(int i = 0; i < stariLinkovi.size(); i++){
                if(!(stariLinkovi.get(i).equalsIgnoreCase(noviLinkovi.get(i)))){
                    //System.out.println("Stari: " + stariLinkovi.get(i) + " - Novi: " + noviLinkovi.get(i));
                    return noviLinkovi;
                }
            }
            noviLinkovi = null;
        }
        
        return noviLinkovi;
        
    }

/**
 * Metoda koja cita korisnikov unos broja
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
}
