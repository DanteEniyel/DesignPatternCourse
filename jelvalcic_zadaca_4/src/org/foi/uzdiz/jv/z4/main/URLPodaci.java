/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.foi.uzdiz.jv.z4.main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.foi.uzdiz.jv.z4.evictor.EvictorInterfejs;

/**
 * Klasa objekta URLPodaci
 * @author Dante Eniyel
 */
public class URLPodaci implements Serializable, EvictorInterfejs{
    
    
    private String link;
    private long trajanjePosjeta = 0;
    private int automatskoOsvjezavanje = 0;
    private int brojPromjena = 0;
    private List<String> poveznice = new ArrayList<>();
    
    private long vrijemeSpremanja = 0;
    private int brojKoristenjaIzDirektorija = 0;
    private long vrijemeZadnjegKoristenja = 0;
    
/**
 * Konstruktor
 * @param link - link stranice koja je aktivna/koja se ucitava
 */
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

    public long getVrijemeSpremanja() {
        return vrijemeSpremanja;
    }

    public void setVrijemeSpremanja(long vrijemeSpremanja) {
        this.vrijemeSpremanja = vrijemeSpremanja;
    }

    public int getBrojKoristenjaIzDirektorija() {
        return brojKoristenjaIzDirektorija;
    }

    public void setBrojKoristenjaIzDirektorija(int brojKoristenjaIzDirektorija) {
        this.brojKoristenjaIzDirektorija = brojKoristenjaIzDirektorija;
    }

    public long getVrijemeZadnjegKoristenja() {
        return vrijemeZadnjegKoristenja;
    }

    public void setVrijemeZadnjegKoristenja(long vrijemeZadnjegKoristenja) {
        this.vrijemeZadnjegKoristenja = vrijemeZadnjegKoristenja;
    }

    
    

//------------CHAIN OF RESPONSIBILITY---------------------------    
    protected URLPodaci slijedeci = null;

    public void setSlijedeci(URLPodaci slijedeci) {
        this.slijedeci = slijedeci;
    }

    //Command interpreter message
/**
 * Metoda poruke za chain of responsibility kada se biljezi broj promjena
 * @param link - aktivna stranica
 * @param brojPromjena - broj promjena koji se dogodio 
 */    
    public void msgOsvjeziStranicu(String link, int brojPromjena) {

        if (this.link.equalsIgnoreCase(link)) {
            
                automatskoOsvjezavanje++;
            
            this.brojPromjena += brojPromjena;
        }else{
            if(this.slijedeci != null){
                this.slijedeci.msgOsvjeziStranicu(link, brojPromjena);
            }
        }

    }
/**
 * Metoda poruke za chain of responsibility kada se biljezi vrijeme trajanja posjeta na stranici
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

/**
 * Metoda poruke za chain of responsibility kada se pristupa stranici u spremniku
 * @param link - aktivna stranica
 * @param vrijemePristupa - vrijeme pristupanja stranici koja se nalazi u spremistu
 */   
    public void msgZapisiKoristenjeStraniceIzSpremnika(String link, long vrijemePristupa){
        if (this.link.equalsIgnoreCase(link)) {
            vrijemeZadnjegKoristenja = vrijemePristupa;
            brojKoristenjaIzDirektorija ++;
        }else{
            if(this.slijedeci != null){
                this.slijedeci.msgZapisiKoristenjeStraniceIzSpremnika(link, vrijemePristupa);
            }
        }
    }

/**
 * Metoda poruke za chain of responsibility kada se stranica prvi puta zapisuje u spremnik
 * @param link - aktivna stranica
 * @param vrijemeZapisa - vrijeme zapisivanja stranice u spremiste
 */    
    public void msgZapisiVrijemeZapisivanjaUSpremink(String link, long vrijemeZapisa){
        if (this.link.equalsIgnoreCase(link)) {
            vrijemeSpremanja = vrijemeZapisa;
            vrijemeZadnjegKoristenja = vrijemeZapisa;
            brojKoristenjaIzDirektorija = 0;
            
        }else{
            if(this.slijedeci != null){
                this.slijedeci.msgZapisiVrijemeZapisivanjaUSpremink(link, vrijemeZapisa);
            }
        }
    }
//--------------------------------------------------------------  

    @Override
    public boolean isEvictable() {
        return true;
    }

    @Override
    public Object info() {
         
        return null;
    }

    @Override
    public void beforeEviction() {
        System.out.println("Stranica: " + this.link + " je izbacena iz spremnika!");
    }

}
