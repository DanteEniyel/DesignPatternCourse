/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.foi.uzdiz.jelvalcicZ2.objekti;

/**
 * Klasa objekta PovijestParova
 * @author Dante Eniyel
 */
public class PovijestParova {
    
    private int brojKola;
    private int redniBrojUParovima;

/**
 * Konstruktor
 * @param brojKola - broj kola u kojem je par generiran
 * @param redniBrojUParovima - index u listi gdje se nalazi taj par
 */
    public PovijestParova(int brojKola, int redniBrojUParovima) {
        this.brojKola = brojKola;
        this.redniBrojUParovima = redniBrojUParovima;
    }

    public int getBrojKola() {
        return brojKola;
    }

    public int getRedniBrojUParovima() {
        return redniBrojUParovima;
    }
        
}
