/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.foi.uzdiz.jelvalcicZ2.objekti;

/**
 * Klasa objekta ParSaRezultatom
 * @author Dante Eniyel
 */
public class ParSaRezultatom {

    int prviKlub;
    int drugiKlub;
    int rezultatSusreta;
    
/**
 * Konstruktor
 * @param prviKlub
 * @param drugiKlub
 * @param rezultatSusreta 
 */    
    public ParSaRezultatom(int prviKlub, int drugiKlub, int rezultatSusreta) {
        this.prviKlub = prviKlub;
        this.drugiKlub = drugiKlub;
        this.rezultatSusreta = rezultatSusreta;
    }

    public int getPrviKlub() {
        return prviKlub;
    }

    public void setPrviKlub(int prviKlub) {
        this.prviKlub = prviKlub;
    }

    public int getDrugiKlub() {
        return drugiKlub;
    }

    public void setDrugiKlub(int drugiKlub) {
        this.drugiKlub = drugiKlub;
    }

    public int getRezultatSusreta() {
        return rezultatSusreta;
    }

    public void setRezultatSusreta(int rezultatSusreta) {
        this.rezultatSusreta = rezultatSusreta;
    }
    
}
