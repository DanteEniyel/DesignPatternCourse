/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.uzdiz.jv.z4.main;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Klasa objekta parametar
 * @author Dante Eniyel
 */
public class Parametar {

    private String url = "";
    private String direktorij = "";
    private int intervalObnavljanja = 0;
    private int kapacitetSpremista = 0;
    private boolean kb = false;//da li je KB ili broj datoteka ako je prazno
    private String strategijaIzbacivanja = ""; //NS - FIFO ili NK - najvise koristeni
    private boolean clean = false;// clean - inicijalno praznjenje spremnika
    
    private String imeDnevnika = "dnevnik.txt";
    private String imeObjekta = "urlPodaci.ser";

    public Parametar() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDirektorij() {
        return direktorij;
    }

    public void setDirektorij(String direktorij) {
        this.direktorij = direktorij;
    }

    public int getIntervalObnavljanja() {
        return intervalObnavljanja;
    }

    public void setIntervalObnavljanja(int intervalObnavljanja) {
        this.intervalObnavljanja = intervalObnavljanja;
    }

    public int getKapacitetSpremista() {
        return kapacitetSpremista;
    }

    public void setKapacitetSpremista(int kapacitetSpremista) {
        this.kapacitetSpremista = kapacitetSpremista;
    }

    public boolean isKb() {
        return kb;
    }

    public void setKb(boolean kb) {
        this.kb = kb;
    }

    public String getStrategijaIzbacivanja() {
        return strategijaIzbacivanja;
    }

    public void setStrategijaIzbacivanja(String strategijaIzbacivanja) {
        this.strategijaIzbacivanja = strategijaIzbacivanja;
    }

    public boolean isClean() {
        return clean;
    }

    public void setClean(boolean clean) {
        this.clean = clean;
    }

    public String getImeDnevnika() {
        return imeDnevnika;
    }

    public String getImeObjekta() {
        return imeObjekta;
    }

    
    
    /**
     * Metoda za dohvacanje statusa stranice kojom provjeravamo da li stranica
     * postoji
     *
     * @param link - url stranice koju provjeravamo
     * @return kod koji govori status stranice
     * @throws MalformedURLException
     * @throws IOException
     */
    private static int getStatusStranice(String url) throws MalformedURLException, IOException {

        URL u = new URL(url);
        HttpURLConnection huc = (HttpURLConnection) u.openConnection();
        huc.setRequestMethod("GET");  //OR huc.setRequestMethod("HEAD"); //  
        huc.connect();
        int code = huc.getResponseCode();
        //System.out.println(code);

        return code;
    }

/**
 * Metoda za ucitavanje parametara
 * @param parametri - parametri
 * @return - da li su parametri ucitani
 * @throws IOException 
 */    
    public boolean ucitajParametre(String[] parametri) throws IOException {

        if (parametri.length < 5) {
            System.out.println("Premalo parametara!");
            return false;
        }
//provjera url        
        int status;
        status = getStatusStranice(parametri[0]);//dobivamo status stranice da provjerimo da nije broken link ili sl
        if (status != 200) {
            System.out.println("Stranica ne postoji ili je unesen neispravan URL!");
            return false;
        } else {
            this.url = parametri[0];
        }
//--------------------------------------        

//provjera direktorij
        File f = new File(parametri[1]);
        if (f.exists() && f.isDirectory()) {
            this.direktorij = parametri[1];
        } else {
            System.out.println("Direktorij ne postoji!");
            return false;
        }
//-------------------------------------        

//provjera interval obnavljanja        
        int broj = Integer.parseInt(parametri[2]);
        if (broj < 1) {
            System.out.println("Interval mora biti veci od 0!");
            return false;
        } else {
            this.intervalObnavljanja = broj;
        }
//-------------------------------------

//provjera kapacitet spremista
        broj = Integer.parseInt(parametri[3]);
        if (broj < 1) {
            System.out.println("Kapacitet mora biti veci od 0!");
            return false;
        } else {
            this.kapacitetSpremista = broj;
        }
//--------------------------------------

//provjera broj ili KB
        int brParam = 4;
        String s = parametri[brParam].toUpperCase();
        if ("KB".equals(s)) {
            this.kb = true;
            brParam++;
        }
//provjera strategija izbacivanja        
        s = parametri[brParam].toUpperCase();
        if (("NS".equals(s)) || ("NK".equals(s))) {
            this.strategijaIzbacivanja = s;
            brParam++;
        } else {
            System.out.println("Potrebno je odabrati strategiju izbacivanja!");
            return false;
        }
//---------------------------------------- 

//provjera clean        
        if (brParam < parametri.length) {
            if ("clean".equals(parametri[brParam].toLowerCase())) {
                this.clean = true;
            } else {
                System.out.println("Neispravno upisan parametar!");
                return false;
            }
        }
//----------------------------------------
        return true;
    }

}
