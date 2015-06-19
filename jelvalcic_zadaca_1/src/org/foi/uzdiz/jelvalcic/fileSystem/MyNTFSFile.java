/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.foi.uzdiz.jelvalcic.fileSystem;

import org.foi.uzdiz.jelvalcic.Jelvalcic_zadaca_1;
import org.foi.uzdiz.jelvalcic.support.MyAbstractFile;
import org.foi.uzdiz.jelvalcic.support.MyFile;

/**
 *
 * @author jelvalcic
 * Klasa konkretnog produkta za NTFS datotecni sustav za detoteku (Abstract Factory uzorak)
 */
public class MyNTFSFile extends MyFile{
    
    String owner;
    boolean isSLink = false;
    String sLinkTarget = "";
    

    public MyNTFSFile(String fName, int fRedniBroj) {
        super(fName, fRedniBroj);
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public boolean isIsSLink() {
        return isSLink;
    }

    public void setIsSLink(boolean isSLink) {
        this.isSLink = isSLink;
    }

    public String getsLinkTarget() {
        return sLinkTarget;
    }

    public void setsLinkTarget(String sLinkTarget) {
        this.sLinkTarget = sLinkTarget;
    }

/**
 * Metoda za detaljni ispis NTFS datoteke
 * Override zato jer je drugacije za NTFS i FAT
 * @param g_indent Indent za strukturirani ispis
 */
    
    @Override
    public void lsIspis(String g_indent) {
        String s = "";
        s = g_indent + Jelvalcic_zadaca_1.tmpS(this.getRedniBroj()) + "-" + this.getName() + "(" + Long.toString(this.getfSize()) + " B) "
        + "Owner:" + this.getOwner();
        
        if(this.isIsSLink()){
            s = s + "[" + this.getsLinkTarget() + "]";
        }
        System.out.println(s);
    }
    
/**
 * Metoda za kopiranje i preimenovanje datoteke
 * Override zato jer mogu biti Symbolic Linkovi (na FAT nema)
 * @param novoIme Novo ime kopirane datoteke
 */
    
    @Override
    public void copyFile(String novoIme) {
        if(this.isIsSLink()){
            System.out.println("Nije implementirano kopiranje simbolickih linkova.");
        }else{
            super.copyFile(novoIme); 
        }    
    }

/**
     * Metoda za premjestanje datoteke
     * Override zato jer mogu biti Symbolic Linkovi (na FAT nema)
     * @param rBrDirektorij Redni broj direktorija u koji se premjesta
     */    
    @Override
    public void moveFile(int rBrDirektorij, MyAbstractFile rooDir) {
        if(this.isIsSLink()){
            System.out.println("Nije implementirano premjestanje simbolickih linkova.");
        }else{
            super.moveFile(rBrDirektorij, rooDir); 
        }
    }
  
    
}
