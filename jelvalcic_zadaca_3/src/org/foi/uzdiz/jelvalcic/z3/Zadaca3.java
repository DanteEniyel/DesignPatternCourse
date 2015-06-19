/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.uzdiz.jelvalcic.z3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Glavna klasa
 * @author Dante Eniyel
 */
public class Zadaca3 {

    /**
     * @param args the command line arguments
     */
    public static String link;
    public static int intervalObnavljanja;
    public static URLPodaci pocetnaStranica;
    public static List<URLPodaci> posjeceneStranice = new ArrayList();
    public static URLPodaci up;
    public static SupportSingleton support;
    public static Dretva dretva;

    public static HistoryCR backHead = null;
    public static HistoryCR backTail = null;
    public static String backLink = "";

    public static void main(String[] args) throws IOException, InterruptedException {

        if (!ucitajArgumente(args)) {//provjera dal su ispravno upisani parametri
            System.out.println("Pocetna stranica ne postoji ili je unesen neispravan broj parametara!");
            System.exit(-1);
        }
//---------------SINGLETON------------------------        
        support = SupportSingleton.getInstance();
//------------------------------------------------
        
        up = new URLPodaci(link);
        pocetnaStranica = up;
        posjeceneStranice.add(up);//dodajemo stranicu koju smo posjetili
        up.setPoveznice(support.nadjiLinkove(link));//u listu poveznica stavljamo poveznice koje se nalaze na posjecenoj stranici

        commandInterpreter();
        dretva.interrupt();

    }

/**
 * Metoda Command interpreter koja obraduje komande
 * @throws IOException 
 */    
    private static void commandInterpreter() throws IOException {
        String odgovor = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int unos;

        dretva = new Dretva(link, intervalObnavljanja, up);
        dretva.start();

        System.out.println("------------- IZBORNIK ----------------\n"
                + "\t-B-\tIspis ukupnog broja poveznica\n"
                + "\t-I-\tIspis adresa poveznica s rednim brojem\n"
                + "\t-J n-\tPrijelaz na poveznicu s rednim brojem n\n"
                + "\t-R-\tObnovi vazecu web stranicu\n"
                + "\t-S-\tIspis statistike\n"
                + "\t-P-\tPovratak na prethodnu aktivnu stranicu\n"
                + "\t-Q-\tKraj programa");
        try {
            odgovor = br.readLine().toUpperCase();
        } catch (IOException ex) {
            Logger.getLogger(Zadaca3.class.getName()).log(Level.SEVERE, null, ex);
        }

        while (!(odgovor.equals("Q"))) {
            switch (odgovor) {
                case "B":
                    System.out.println("Ukupni broj poveznica na stranici: " + up.getPoveznice().size());
                    break;

                case "I":
                    List<String> pomocna = up.getPoveznice();
                    for (int i = 0; i < pomocna.size(); i++) {
                        int rbr = i + 1;
                        System.out.println(rbr + " " + pomocna.get(i));
                    }
                    break;

                case "J":

                    unos = support.unosBroja("Redni broj linka koji zelite ucitati: ");
                    pomocna = up.getPoveznice();

                    if (!((unos < 1) || (unos > pomocna.size()))) {
//-------CHAIN OF RESPONSIBILITY----------------BACK FUNKCIONALNOST---------------------------------------
                        if (backHead == null) {
                            backHead = new HistoryCR(link);
                            backTail = backHead;
                        } else {
                            HistoryCR tmpBack = new HistoryCR(link);//trenutni link na kojem smo sada//novi zadnji

                            HistoryCR tmpZadnji = backHead;//stari zadnji
                            HistoryCR tZ = backHead;

                            while ((tmpZadnji = tmpZadnji.getNext()) != null) {//trazi se posljednji
                                tZ = tmpZadnji;
                            }

                            tZ.setNext(tmpBack);
                            tmpBack.setPrevious(tZ);
                            backTail = tmpBack;
                        }

//------------------------------------------------BACK FUNKCIONALNOST---------------------------------------    
                        
                        link = pomocna.get(unos - 1);

//-------CHAIN OF RESPONSIBILITY-------------------------------------------------------                        
                        URLPodaci pomUp = new URLPodaci(link);
                        up.setSlijedeci(pomUp);
                        up = pomUp;//nova aktivna stranica
//-------------------------------------------------------------------------------------                        
                        posjeceneStranice.add(up);//dodajemo stranicu koju smo posjetili
                        up.setPoveznice(support.nadjiLinkove(link));//u listu poveznica stavljamo poveznice koje se nalaze na posjecenoj stranici

                        dretva.interrupt();//prekidamo staru dretvu
                        dretva = new Dretva(link, intervalObnavljanja, up);//pokretanje dove dretve s odabranim linkom
                        dretva.start();
                    } else {
                        System.out.println("Takav redni broj ne postoji!");
                    }

                    break;

                case "R":
                    List<String> temp = null;
                    temp = support.usporediLinkove(link, up.getPoveznice());
                    if (temp != null) {
                        up.setPoveznice(temp);
//-------CHAIN OF RESPONSIBILITY-------------------------------------------------------                        
                        pocetnaStranica.msgOsvjeziStranicu(link, true, 1);

                        System.out.println("Doslo je do promjene na stranici od zadnjeg osvjezenja.");
                    } else {
//-------CHAIN OF RESPONSIBILITY-------------------------------------------------------                        
                        pocetnaStranica.msgOsvjeziStranicu(link, true, 0);
                    }

                    break;

                case "S":
                    System.out.println("Sve aktivne stranice: \n");
                    for (URLPodaci p : posjeceneStranice) {
                        System.out.println("\tURL: " + p.getLink());
                        System.out.println("\tVrijeme zadrzavanja: " + p.getTrajanjePosjeta() / 1000 + " s");
                        System.out.println("\tBroj rucnih osvjezenja: " + p.getRucnoOsvjezavanje());
                        System.out.println("\tBroj automatskih osvjezenja: " + p.getAutomatskoOsvjezavanje());
                        System.out.println("\tBroj promjena: " + p.getBrojPromjena());
                        System.out.println("--------");
                    }
                    System.out.println("");
                    break;

                case "P":
                    HistoryCR t = backTail;
                    int i = 1;
                                        
                    while (t != null) {
                        System.out.println(i + " " + t.getLink());
                        i++;
                        t = t.getPrevious();
                    }
                    unos = support.unosBroja("Redni broj linka na koji se zelite vratiti (ili 0 - bez promjene stranice): ");
                    if (unos > 0) {
                        backLink = "";
//-------CHAIN OF RESPONSIBILITY----------------BACK FUNKCIONALNOST---------------------------------------                        
                        backTail.msgGoBack(unos);
//------------------------------------BACK FUNKCIONALNOST---------------------------------------  
                        if (!backLink.isEmpty()) {
                            link = backLink;

                            URLPodaci rl = support.vratiURLPodatke(link, posjeceneStranice);
                            if (rl != null) {
                                up = rl;
                            } else {
//-------CHAIN OF RESPONSIBILITY-------------------------------------------------------                        
                                URLPodaci pomUp = new URLPodaci(link);
                                up.setSlijedeci(pomUp);
                                up = pomUp;//nova aktivna stranica
//-------------------------------------------------------------------------------------   
                                up.setPoveznice(support.nadjiLinkove(link));//u listu poveznica stavljamo poveznice koje se nalaze na posjecenoj stranici

                            }
                            
                            System.out.println("Aktivna stranica: " + link);
                            
                            dretva.interrupt();//prekidamo staru dretvu
                            dretva = new Dretva(link, intervalObnavljanja, up);//pokretanje dove dretve s odabranim linkom
                            dretva.start();
                        }

                    }
                    break;

                case "Q":

                    break;

                default:

                    break;
            }
            try {
                odgovor = br.readLine().toUpperCase();
            } catch (IOException ex) {
                Logger.getLogger(Zadaca3.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

/**
 * Metoda za dohvacanje statusa stranice kojom provjeravamo da li stranica postoji
 * @param link - url stranice koju provjeravamo
 * @return kod koji govori status stranice
 * @throws MalformedURLException
 * @throws IOException 
 */    
    private static int getStatusStranice(String link) throws MalformedURLException, IOException {

        URL u = new URL(link);
        HttpURLConnection huc = (HttpURLConnection) u.openConnection();
        huc.setRequestMethod("GET");  //OR huc.setRequestMethod("HEAD"); //  
        huc.connect();
        int code = huc.getResponseCode();
        //System.out.println(code);

        return code;
    }

/**
 * Metoda koja provjerava ispravnost unesenih argumenata
 * @param args - upisani argumenti koji dolaze s konzole
 * @return vraca se status 200 koji oznacava da je stranica funkcionalna
 * @throws MalformedURLException
 * @throws IOException 
 */    
    private static boolean ucitajArgumente(String[] args) throws MalformedURLException, IOException {
        int status;

        if (args.length != 2) {
            System.out.println("Neispravni broj parametara!");
            return false;
        }

        intervalObnavljanja = Integer.parseInt(args[1]);
        if (intervalObnavljanja < 2 || intervalObnavljanja > 600) {
            System.out.println("Interval spavanja dretve treba biti izmedu 2 i 600 sekundi!");
            return false;
        }

        status = getStatusStranice(args[0]);//dobivamo status stranice da provjerimo da nije broken link ili sl

        link = args[0];
        return status == 200;// true ako je status jednak 200
    }

}
