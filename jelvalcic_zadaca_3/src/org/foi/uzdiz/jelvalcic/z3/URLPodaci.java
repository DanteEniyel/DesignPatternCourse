/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.uzdiz.jelvalcic.z3;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasa objekta URLPodaci
 * @author Dante Eniyel
 */
public class URLPodaci {

    private String link;
    private long trajanjePosjeta = 0;
    private int rucnoOsvjezavanje = 0;
    private int automatskoOsvjezavanje = 0;
    private int brojPromjena = 0;
    private List<String> poveznice = new ArrayList<>();

    public URLPodaci(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public long getTrajanjePosjeta() {
        return trajanjePosjeta;
    }

    public void setTrajanjePosjeta(long trajanjePosjeta) {
        this.trajanjePosjeta = trajanjePosjeta;
    }

    public int getRucnoOsvjezavanje() {
        return rucnoOsvjezavanje;
    }

    public void setRucnoOsvjezavanje(int rucnoOsvjezavanje) {
        this.rucnoOsvjezavanje = rucnoOsvjezavanje;
    }

    public int getAutomatskoOsvjezavanje() {
        return automatskoOsvjezavanje;
    }

    public void setAutomatskoOsvjezavanje(int automatskoOsvjezavanje) {
        this.automatskoOsvjezavanje = automatskoOsvjezavanje;
    }

    public int getBrojPromjena() {
        return brojPromjena;
    }

    public void setBrojPromjena(int brojPromjena) {
        this.brojPromjena = brojPromjena;
    }

    public List<String> getPoveznice() {
        return poveznice;
    }

    public void setPoveznice(List<String> poveznice) {
        this.poveznice = poveznice;
    }

    public URLPodaci getSlijedeci() {
        return slijedeci;
    }

//------------CHAIN OF RESPONSIBILITY---------------------------    
    protected URLPodaci slijedeci = null;

    public void setSlijedeci(URLPodaci slijedeci) {
        this.slijedeci = slijedeci;
    }

    //Command interpreter message
/**
 * Metoda poruke za chain of responsibility
 * @param link - aktivna stranica
 * @param rucnaPromjena - true ako se dogodila rucna promjena
 * @param brojPromjena - broj promjena koji se dogodio 
 */    
    public void msgOsvjeziStranicu(String link, boolean rucnaPromjena, int brojPromjena) {

        if (this.link.equalsIgnoreCase(link)) {
            if (rucnaPromjena == true) {
                rucnoOsvjezavanje++;
            } else {
                automatskoOsvjezavanje++;
            }
            this.brojPromjena += brojPromjena;
        }else{
            if(this.slijedeci != null){
                this.slijedeci.msgOsvjeziStranicu(link, rucnaPromjena, brojPromjena);
            }
        }

    }
/**
 * Metoda poruke za chain of responsibility
 * @param link - aktivna stranica
 * @param vrijeme - vrijeme trajanja posjeta stranici
 */    
    public void msgZapisiVrijeme(String link, long vrijeme){
        if (this.link.equalsIgnoreCase(link)) {
            trajanjePosjeta += vrijeme;
        }else{
            if(this.slijedeci != null){
                this.slijedeci.msgZapisiVrijeme(link, vrijeme);
            }
        }
    }

//--------------------------------------------------------------    
}
