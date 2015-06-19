/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.uzdiz.jelvalcicZ2.objekti;

import java.util.ArrayList;
import java.util.List;
import static org.foi.uzdiz.jelvalcicZ2.Main.caretakerParovi;
import static org.foi.uzdiz.jelvalcicZ2.Main.klubovi;

/**
 * Klasa objekta Klub
 * @author Dante Eniyel
 */
public class Klub {

    private int sifraKluba;
    private String nazivKluba;
    private int slabNatjecatelj;
    private boolean ispaoIzNatjecanja;
    private float efikasnostKluba;
    private int poredakPosljednjeProvjere;
    private List<PovijestParova> pp = new ArrayList<>();

/**
 * Konstruktor
 * @param sifraKluba
 * @param nazivKluba 
 */    
    public Klub(int sifraKluba, String nazivKluba) {
        this.sifraKluba = sifraKluba;
        this.nazivKluba = nazivKluba;
        this.slabNatjecatelj = 0;//(0 - normalan natjecatelj, > 0 - slab natjecatelj, ako je jednako pragu provjera onda ispada iz natjecanja)
        this.ispaoIzNatjecanja = false;
        this.efikasnostKluba = 0;
        this.poredakPosljednjeProvjere = 999;//za usporedbu sto veci(sto je manji poredak to je bolja pozicija)
        this.nextKlub = null;
    }

    public int getSifraKluba() {
        return sifraKluba;
    }

    public void setSifraKluba(int sifraKluba) {
        this.sifraKluba = sifraKluba;
    }

    public String getNazivKluba() {
        return nazivKluba;
    }

    public void setNazivKluba(String nazivKluba) {
        this.nazivKluba = nazivKluba;
    }

    public int getSlabNatjecatelj() {
        return slabNatjecatelj;
    }

    public void setSlabNatjecatelj(int slabNatjecatelj) {
        this.slabNatjecatelj = slabNatjecatelj;
    }

    public boolean isIspaoIzNatjecanja() {
        return ispaoIzNatjecanja;
    }

    public void setIspaoIzNatjecanja(boolean ispaoIzNatjecanja) {
        this.ispaoIzNatjecanja = ispaoIzNatjecanja;
    }

    public float getEfikasnostKluba() {
        return efikasnostKluba;
    }

    public void setEfikasnostKluba(float efikasnostKluba) {
        this.efikasnostKluba = efikasnostKluba;
    }

    public int getPoredakPosljednjeProvjere() {
        return poredakPosljednjeProvjere;
    }

    public void setPoredakPosljednjeProvjere(int poredakPosljednjeProvjere) {
        this.poredakPosljednjeProvjere = poredakPosljednjeProvjere;
    }
/**
 * Metoda za dohvacanje povijesti parova
 * @return - lista povijesti parova
 */
    public List<PovijestParova> getPp() {
        return pp;
    }

/**
 * Metoda za dodavanje povijesti parova
 * @param povijestP - par koji se sprema u povijest
 */
    public void dodajRedPovijesti(PovijestParova povijestP) {
        pp.add(povijestP);
    }
//---------------CHAIN OF RESPONSIBILITY ELEMENT-------------------
    // The next element in the chain of responsibility
    protected Klub nextKlub;

    public Klub setNext(Klub k) {
        nextKlub = k;
        return this;
    }

/**
 * Metoda za obradu poruke uzorka Chain of responsibility
 * @param sifra - sifra kluba
 */    
    public void message(int sifra) {
        Parovi p = new Parovi();
        ParSaRezultatom par;
        if (sifra == sifraKluba) {
            setIspaoIzNatjecanja(true);
            System.out.println("Sifra kluba: " + sifraKluba + " Naziv: " + nazivKluba + " Posljednji poredak: " + poredakPosljednjeProvjere);
            System.out.println("-----Povijest susreta----");
            System.out.println("Kolo" + "\tDomacin" + "\tGost" + "\tRezultat susreta");
            for (PovijestParova pp : getPp()) {
                p.restoreFromMemento(caretakerParovi.getMemento(pp.getBrojKola() - 1));
                par = p.dohvatiPar(pp.getRedniBrojUParovima() - 1);
                System.out.println(pp.getBrojKola() + "\t" + getNazivKlubaPoSifri(par.getPrviKlub())
                        + "\t" + getNazivKlubaPoSifri(par.getDrugiKlub()) + "\t\t" + par.getRezultatSusreta());
            }
            System.out.println("\n");
        } else {
            if (nextKlub != null) {
                nextKlub.message(sifra);
            }
        }
    }
    
//----------------------------------------------------------------
 
/**
 * Metoda za dohvacanje naziva kluba prema sifri
 * @param sifraKluba - sifra klubba za koji se trazi naziv
 * @return - String - naziv kluba
 */
    private static String getNazivKlubaPoSifri(int sifraKluba) {
        String naziv = "";

        for (Klub k : klubovi) {
            if (sifraKluba == k.getSifraKluba()) {
                naziv = k.getNazivKluba();
                break;
            }
        }

        return naziv;
    }
}
