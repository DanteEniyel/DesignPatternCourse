/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.foi.uzdiz.jelvalcicZ2.objekti;

/**
 * Klasa objekta RedRangListe
 * @author Dante Eniyel
 */
public class RedRangListe {

    private int sifraKluba;
    private int ukupniBodovi;
    private int poredak;
    private int brojKola;
    
/**
 * Konstruktor
 * @param sifraKluba
 * @param ukupniBodovi
 * @param poredak
 * @param brojKola 
 */    
    public RedRangListe(int sifraKluba, int ukupniBodovi, int poredak, int brojKola) {
        this.sifraKluba = sifraKluba;
        this.ukupniBodovi = ukupniBodovi;
        this.poredak = poredak;
        this.brojKola = brojKola;
    }

    public int getSifraKluba() {
        return sifraKluba;
    }

    public void setSifraKluba(int sifraKluba) {
        this.sifraKluba = sifraKluba;
    }

    public int getUkupniBodovi() {
        return ukupniBodovi;
    }

    public void setUkupniBodovi(int ukupniBodovi) {
        this.ukupniBodovi = ukupniBodovi;
    }

    public int getPoredak() {
        return poredak;
    }

    public void setPoredak(int poredak) {
        this.poredak = poredak;
    }

    public int getBrojKola() {
        return brojKola;
    }

    public void setBrojKola(int brojKola) {
        this.brojKola = brojKola;
    }
}
