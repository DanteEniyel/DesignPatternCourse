/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.uzdiz.jv.z4.cache;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.uzdiz.jv.z4.evictor.Evictor;
import org.foi.uzdiz.jv.z4.main.Parametar;
import org.jsoup.nodes.Document;
import org.foi.uzdiz.jv.z4.main.SupportSingleton;
import org.foi.uzdiz.jv.z4.main.URLPodaci;
import org.foi.uzdiz.jv.z4.main.Zadaca4;
import org.jsoup.Jsoup;

/**
 * Klasa uzorka Cache
 * @author Dante Eniyel
 */
public class Cache implements CacheInterfejs {

    private SupportSingleton support;
    private Parametar p;
    private URLPodaci pocetnaStranica;
    private Date vrijemePristupa;
    public Evictor evictor;

/**
 * Konstruktor
 * @param pp - parametri
 * @param pocetna - pocetna stranica
 */
    public Cache(Parametar pp, URLPodaci pocetna) {
        support = SupportSingleton.getInstance();
        this.p = pp;
        this.pocetnaStranica = pocetna;
        evictor = new Evictor(p);
    }

/**
 * Metoda za dohvacanje stranice
 * @param url - link stranice
 * @return - stranica 
 */    
    @Override
    public Document getStranica(String url) {
        File f = new File(p.getDirektorij() + "\\" + support.stranicaDatName(url));
        Document doc = null;

        if (f.exists()) {
            //deserijalizacija
            doc = support.ucitajDokument(p, support.stranicaDatName(url));

//---------------CHAIN OF RESPONSIBILITY ELEMENT---------------------
            //salje se poruka kada se pristupa stranici iz spremnika i zapisuju se odgovarajuce statistike
            vrijemePristupa = new Date();
            pocetnaStranica.msgZapisiKoristenjeStraniceIzSpremnika(url, vrijemePristupa.getTime());
            vrijemePristupa = null;
//-------------------------------------------------------------------            
        } else {
            //citati s neta--- stranica se prvi puta zapisuje

            try {
                doc = Jsoup.connect(url).get();
                if (evictor.napraviMjestaUSpremistu(doc.text().getBytes().length)) {

                    //ako smo dohvatili dokument serializiramo ga
//---------------CHAIN OF RESPONSIBILITY ELEMENT---------------------
                    //salje se poruka kada se stranica zapisuje prvi puta u spremnik i zapisuju se odgovarajuce statistike
                    vrijemePristupa = new Date();
                    pocetnaStranica.msgZapisiVrijemeZapisivanjaUSpremink(url, vrijemePristupa.getTime());
                    vrijemePristupa = null;
//-------------------------------------------------------------------                
                    String html = doc.toString();//zato jer Document nije Serializable/ Document datoteku pretveramo u String da ju
                    //mozemo serilizirati
                    support.zapisiObjekt(html, p, support.stranicaDatName(url));
                    Zadaca4.dnevnik.zapisiPisanjeUSpremik(url);
                } else {
                    System.out.println("Nema mjesta, prevelika stranica!");
                }

            } catch (IOException ex) {
                Logger.getLogger(SupportSingleton.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return doc;
    }

/**
 * Metoda koja zapisuje nove podatke ako je doslo do promjene na stranici
 * @param sadrzaj - sadrzaj stranice
 * @param imeDatoteke - ime datiteke spremista
 */    
    @Override
    public void updateStranica(String sadrzaj, String imeDatoteke) {
        
        support.zapisiObjekt(sadrzaj, p, imeDatoteke);
    }

    @Override
    public void releaseStranica(String url, Document doc) {

    }

}
