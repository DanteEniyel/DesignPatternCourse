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
 * Klasa objekta RangLista
 * @author Dante Eniyel
 */
public class RangLista extends Observer{

    List<RedRangListe> rangLista = new ArrayList<>();
    
    public RangLista() {
    }

    public List<RedRangListe> getRangLista() {
        return rangLista;
    }
    
    public void setRangLista(List<RedRangListe> rangLista) {
        this.rangLista = rangLista;
    }

/**
 * Metoda za dodavanje reda u listu Rang liste
 * @param red - red koji se dodaje
 */    
    public void dodajRed(RedRangListe red){
        rangLista.add(red);
    }
/**
 * Metoda za dohvacanje reda u rang listi
 * @param redniBroj - redni broj elementa
 * @return - red rang liste
 */    
    public RedRangListe dohvatiRed(int redniBroj){
        
        return rangLista.get(redniBroj);
    }

//----------------------------OBSERVER ELEMEN-------------------------
    @Override
    public void update(SubjectInterfejs o) {
        for (RedRangListe rl : this.rangLista) {
            if (o.getState().getRezultatSusreta() != 0) {
                if ((o.getState().getRezultatSusreta() == 1) && (rl.getSifraKluba() == o.getState().prviKlub)) {
                    rl.setUkupniBodovi(rl.getUkupniBodovi() + 3);
                    break;
                } else if ((o.getState().getRezultatSusreta() == 2) && (rl.getSifraKluba() == o.getState().drugiKlub)) {
                    rl.setUkupniBodovi(rl.getUkupniBodovi() + 3);
                    break;
                }
            } else {
                if ((rl.getSifraKluba() == o.getState().prviKlub) || (rl.getSifraKluba() == o.getState().drugiKlub)) {
                    rl.setUkupniBodovi(rl.getUkupniBodovi() + 1);
                }
            }
        }
         //System.out.println("Update odraden...");
    }
//--------------------------------------------------------------------
    
       
//-------------------------------MEMENTO ELEMENT------------------------
/**
 * Metoda mementa za rang listu
 */    
    private static class MementoRangLista {

        private List<RedRangListe> rangListaStanje = new ArrayList<>();

        public MementoRangLista(List<RedRangListe> rangListaStanje) {
            this.rangListaStanje = rangListaStanje;
        }

        public List<RedRangListe> getSpremljenoStanjeRangLista() {
            return rangListaStanje;

        }
    }
/**
 * Metoda za dodavanje rang liste u memento
 * @return - rang lista
 */
    public Object saveToMemento() {
        //System.out.println("Originator: Saving to Memento.");
        return new MementoRangLista(rangLista);
    }

/**
 * Metoda za dohvacanje rang liste iz mementa
 * @param m - objekt koji se dohvaca
 */    
    public void restoreFromMemento(Object m) {
        if (m instanceof MementoRangLista) {
            MementoRangLista memento = (MementoRangLista) m;
            rangLista = memento.getSpremljenoStanjeRangLista();
        }
    }
//-------------------------------------------------------------    
}
