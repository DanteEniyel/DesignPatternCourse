/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.foi.uzdiz.jelvalcicZ2.objekti;

import java.util.ArrayList;
import java.util.List;
import org.foi.uzdiz.jelvalcicZ2.interfejsi.SubjectInterfejs;

/**
 * Klasa objekta Parovi
 * @author Dante Eniyel
 */
public class Parovi extends Observer{
    
    private List<ParSaRezultatom> parovi = new ArrayList<>();
    //int brojRangListe;

    public Parovi() {
    }

    /*public int getBrojRangListe() {
        return brojRangListe;
    }

    public void setBrojRangListe(int brojRangListe) {
        this.brojRangListe = brojRangListe;
    }*/
    
    public void dodajPar(ParSaRezultatom par){
        parovi.add(par);
    }
    
    public ParSaRezultatom dohvatiPar(int brojReda){
                
        return parovi.get(brojReda);
    }

    public List<ParSaRezultatom> getParovi() {
        return parovi;
    }

    public void setParovi(List<ParSaRezultatom> parovi) {//state za originator
        this.parovi = parovi;
    }

//--------------------OBSERVER ELEMENT----------------------
    @Override
    public void update(SubjectInterfejs o) {
        //System.out.println("Dodan par...\nDomacin: " + o.getState().prviKlub + " Gost: " + o.getState().drugiKlub + " Rezultat susreta: " + o.getState().rezultatSusreta);
        parovi.add(o.getState());//Par sa rezultatom (dodajPar() na drugi naci prilagoden za observera)
    }
//----------------------------------------------------------
    
//--------------------MEMENTO ELEMENT-----------------------
/**
 * Metoda mementa za parove
 */    
    private static class MementoParovi {

        private List<ParSaRezultatom> paroviStanje = new ArrayList<>();

        public MementoParovi(List<ParSaRezultatom> paroviStanje) {
            this.paroviStanje = paroviStanje;
        }

        public List<ParSaRezultatom> getSpremljenoStanjeParovi() {
            return paroviStanje;

        }
    }
/**
 * Metoda za spremanje u memento
 * @return - novi element mementa
 */
    public Object saveToMemento() {
        //System.out.println("Originator: Saving to Memento.");
        return new MementoParovi(parovi);
    }
/**
 * Metoda za vracanje iz mementa
 * @param m - objekt koji vracamo
 */
    public void restoreFromMemento(Object m) {
        if (m instanceof MementoParovi) {
            MementoParovi memento = (MementoParovi) m;
            parovi = memento.getSpremljenoStanjeParovi();
        }
    }
//---------------------------------------------------------------    
    
}
