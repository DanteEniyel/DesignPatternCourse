/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.uzdiz.jv.z4.main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.uzdiz.jv.z4.observer.Observer;
import org.foi.uzdiz.jv.z4.observer.SubjectInterfejs;

/**
 * Klasa objekta Dnevnik
 * @author Dante Eniyel
 */
public class Dnevnik extends Observer {

    private String putanjaDirektorija;
    private String imeDnevnika;
    private FileWriter fileWritter;
    private BufferedWriter bufferWritter;
    private SupportSingleton support;
    
/**
 * Konstruktor
 * @param putanjaDirektorija - putanja spremista
 * @param imeDnevnika - ime dnevnika
 * @throws IOException 
 */    
    public Dnevnik(String putanjaDirektorija, String imeDnevnika) throws IOException {
        this.putanjaDirektorija = putanjaDirektorija;
        this.imeDnevnika = imeDnevnika;
        this.support = SupportSingleton.getInstance();
    }
    
/**
 * Metoda koja zapisuje u dnevnik podatke o stranici koja se sprema u spremnik
 * @param url - link stranice koja se zapisuje
 */    
    public void zapisiPisanjeUSpremik(String url){
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy:HH:mm:SS");
        Date  date = new Date();
        
        try {
            fileWritter = new FileWriter(putanjaDirektorija + "\\" + imeDnevnika, true);
        } catch (IOException ex) {
            Logger.getLogger(Dnevnik.class.getName()).log(Level.SEVERE, null, ex);
        }
        bufferWritter = new BufferedWriter(fileWritter);
        
        
        String s = "Dodan dokument: ";
        s = s + support.stranicaDatName(url) + "\n";
        s = s + "\tDodan: " + dateFormat.format(date) + "\n\n";
        try {
            bufferWritter.write(s);
            bufferWritter.close();
        } catch (IOException ex) {
            Logger.getLogger(Dnevnik.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

/**
 * Metoda koja zapisuje podatke o izbacenoj stranici (dio ostvarenja Observer uzorka)
 * @param o - objekt
 */    
    @Override
    public void update(SubjectInterfejs o) {
        URLPodaci p = o.getState();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy:HH:mm:SS");
        Date  date = new Date();
        long trajanje = (date.getTime() - p.getVrijemeSpremanja()) / 1000;
        try {
            fileWritter = new FileWriter(putanjaDirektorija + "\\" + imeDnevnika, true);
        } catch (IOException ex) {
            Logger.getLogger(Dnevnik.class.getName()).log(Level.SEVERE, null, ex);
        }
        bufferWritter = new BufferedWriter(fileWritter);
        
        
        String s = "Izbacen dokument: ";
        s = s + support.stranicaDatName(p.getLink()) + "\n";
        s = s + "\tIzbacen: " + dateFormat.format(date) + "\n";
        s = s + "\tUkupno koristenja iz spremista: " + p.getBrojKoristenjaIzDirektorija() + "\n";
        s = s + "\tUkupno vremena provedeno u spremistu: " +  trajanje + "s \n\n";
        try {
            bufferWritter.write(s);
            bufferWritter.close();
        } catch (IOException ex) {
            Logger.getLogger(Dnevnik.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
