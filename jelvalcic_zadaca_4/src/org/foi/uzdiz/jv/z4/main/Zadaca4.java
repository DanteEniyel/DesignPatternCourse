/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.uzdiz.jv.z4.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.uzdiz.jv.z4.cache.Cache;
import org.jsoup.nodes.Document;

/**
 *
 * @author Dante Eniyel
 */
public class Zadaca4 {

    /**
     * @param args the command line arguments
     */
    public static Parametar p;
    public static URLPodaci up;
    public static SupportSingleton support;
    public static StraniceObjekt posjeceneStranice = null;
    public static Dretva dretva;
    public static Cache cache;
    public static Dnevnik dnevnik;
    public static Document doc;
    public static AtomicInteger ai = new AtomicInteger(0);

    public static void main(String[] args) throws IOException {

        p = new Parametar();
        try {
            if (!p.ucitajParametre(args)) {
                System.out.println("Neispravni parametri!");
                System.exit(-1);
            }
        } catch (IOException ex) {
            System.out.println("Greska pri citanju parametara!");
        }

//---------------SINGLETON------------------------        
        support = SupportSingleton.getInstance();
//------------------------------------------------
        up = new URLPodaci(p.getUrl());//aktivna stranica
        if (support.ucitajObjekt(p) == null) {
            posjeceneStranice = new StraniceObjekt();//ako nije ucitan objekt instancira se novi
            posjeceneStranice.tekucaStranica = up;
            posjeceneStranice.pocetnaStranica = up;
        } else {
            posjeceneStranice = support.ucitajObjekt(p);//ucitava se objekt iz datoteke
            posjeceneStranice.tekucaStranica = up;
            //System.out.println("Ispis");
            //System.out.println("Link: " + posjeceneStranice.posjeceneStranice.get(0).getLink());
        }

        if (!support.provjeriDuplic(p.getUrl(), posjeceneStranice.posjeceneStranice)) {
            posjeceneStranice.posjeceneStranice.add(up);//dodajemo stranicu koju smo posjetili
            posjeceneStranice.tekucaStranica = up;
        }

        dnevnik = new Dnevnik(p.getDirektorij(), p.getImeDnevnika());//instancira se dnevnik
        cache = new Cache(p, posjeceneStranice.pocetnaStranica);//kreira se cache

        doc = cache.getStranica(p.getUrl());
        up.setPoveznice(support.nadjiLinkove(doc));//u listu poveznica stavljamo poveznice koje se nalaze na posjecenoj stranici
        if (p.isClean()) {
            cache.evictor.prazniSpremnik(posjeceneStranice.posjeceneStranice);
        }
        commandInterpreter();

        //------------------------------
        dretva.interrupt();
        System.out.println(support.zapisiObjekt(posjeceneStranice, p, p.getImeObjekta()));
    }

    /**
     * Metoda Command interpreter koja obraduje komande
     *
     * @throws IOException
     */
    private static void commandInterpreter() throws IOException {
        String odgovor = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        dretva = new Dretva(p.getUrl(), p.getIntervalObnavljanja(), up, doc);
        dretva.start();

        System.out.println("------------- IZBORNIK ----------------\n"
                + "\t-I-\tIspis adresa poveznica\n"
                + "\t-J n-\tPrijelaz na poveznicu s rednim brojem n\n"
                + "\t-S-\tIspis spremnika\n"
                + "\t-P-\tPraznjenje spremnika\n"
                + "\t-Q-\tKraj programa");
        try {
            odgovor = br.readLine().toUpperCase();
        } catch (IOException ex) {
            Logger.getLogger(Zadaca4.class.getName()).log(Level.SEVERE, null, ex);
        }

        while (!(odgovor.equals("Q"))) {
            switch (odgovor) {
                case "I":
                    tablicniIspisLinkova();
                    break;

                case "J":
                    idiNaStranicu();
                    break;

                case "S":
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy:HH:mm:SS");
                    String date;
                    long trajanje;
                    Date danas = new Date();
                    System.out.println("Sve aktivne stranice: \n");
                    for (URLPodaci pp : posjeceneStranice.posjeceneStranice) {
                        trajanje = (danas.getTime() - pp.getVrijemeSpremanja()) / 1000;
                        System.out.println("\tURL: " + pp.getLink());
                        System.out.println("\tVrijeme zadrzavanja: " + pp.getTrajanjePosjeta() / 1000 + " s");
                        System.out.println("\tBroj automatskih osvjezenja: " + pp.getAutomatskoOsvjezavanje());
                        System.out.println("\tBroj promjena: " + pp.getBrojPromjena());
                        System.out.println("\tBroj koristenja iz spremnika: " + pp.getBrojKoristenjaIzDirektorija());
                        System.out.println("\tVrijeme spremanja: " + dateFormat.format(pp.getVrijemeSpremanja()));
                        System.out.println("\tVrijeme zadnjeg koristenja: " + dateFormat.format(pp.getVrijemeZadnjegKoristenja()));
                        System.out.println("\tVrijeme zadrzavanja u spremniku: " + trajanje + " s");
                        System.out.println("--------");
                    }
                    System.out.println("");
                    break;

                case "P":
                    cache.evictor.prazniSpremnik(posjeceneStranice.posjeceneStranice);
                    break;

                case "Q":

                    break;

                default:

                    break;
            }
            try {
                odgovor = br.readLine().toUpperCase();
            } catch (IOException ex) {
                Logger.getLogger(Zadaca4.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

/**
 * Metoda za dodavanje razmaka kod tablicnog ispisa
 * @param koliko - koliko razmaka treba
 * @return - razmaci
 */    
    private static String spaces(int koliko) {
        String s = "";
        for (int i = 1; i <= koliko; i++) {
            s = s + " ";
        }
        return s;
    }

/**
 * Metoda za tablicni ispis linkova
 */    
    private static void tablicniIspisLinkova() {
        String crticeVodoravno = "---------------------------------------------------------------------------------";
        String crticeOkomito = "| RBR |   Linkovi                                                               |";
        int tmp;
        String tmpStr;
        List<String> pomocna = up.getPoveznice();
        System.out.println(crticeVodoravno);
        System.out.println(crticeOkomito);
        System.out.println(crticeVodoravno);
        for (int i = 0; i < pomocna.size(); i++) {
            int rbr = i + 1;
            if (pomocna.get(i).length() > 70) {
                tmpStr = pomocna.get(i).substring(0, 69);
            } else {
                tmpStr = pomocna.get(i);
            }
            tmp = 70 - tmpStr.length();
            System.out.println("| " + rbr + "\t| " + tmpStr + spaces(tmp) + "|");
            System.out.println(crticeVodoravno);
        }
    }

/**
 * Metoda za prijelaz na slijedecu stranicu
 */    
    private static void idiNaStranicu() {
        int unos;

        unos = support.unosBroja("Redni broj linka koji zelite ucitati: ");
        List<String> pomocna = up.getPoveznice();

        if (!((unos < 1) || (unos > pomocna.size()))) {

            String link = pomocna.get(unos - 1);

            up = new URLPodaci(link);//nova aktivna stranica    

            if (!support.provjeriDuplic(link, posjeceneStranice.posjeceneStranice)) {
                posjeceneStranice.posjeceneStranice.add(up);//dodajemo stranicu koju smo posjetili
                posjeceneStranice.tekucaStranica = up;
            }

//-------CHAIN OF RESPONSIBILITY-------------------------------------------------------                         
            posjeceneStranice.pocetnaStranica = support.rebuildChain(posjeceneStranice.posjeceneStranice);
//-------------------------------------------------------------------------------------                  
            doc = cache.getStranica(up.getLink());//dohvacamo sadrzaj stranice u obliku dokumenta
            up.setPoveznice(support.nadjiLinkove(doc));//u listu poveznica stavljamo poveznice koje se nalaze na posjecenoj stranici

            dretva.interrupt();//prekidamo staru dretvu
            dretva = new Dretva(link, p.getIntervalObnavljanja(), up, doc);//pokretanje nove dretve s odabranim linkom
            dretva.start();
        } else {
            System.out.println("Takav redni broj ne postoji!");
        }
    }

}
