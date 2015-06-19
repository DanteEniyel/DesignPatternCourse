/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.uzdiz.jelvalcicZ2.objekti;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasa za ostvarenje Memento uzorka
 * @author Dante Eniyel
 */
public class Caretaker {

    private List<Object> stanje = new ArrayList<>();

/**
 * Metoda za dodavanje mementa
 * @param m - objekt koji se sprema
 */
    public void addMemento(Object m) {
        stanje.add(m);
    }

/**
 * Metoda za dohvacanje mementa
 * @param index - indeks elementa u listi
 * @return Object - objekt koji se trazi
 */
    public Object getMemento(int index) {
        return stanje.get(index);
    }

/**
 * Metoda za dohvacanje broja spremljenih stanja
 * @return - velicina liste spremljenih stanja
 */    
    public int getBrojStanja(){
        
        return stanje.size();
    }
}
