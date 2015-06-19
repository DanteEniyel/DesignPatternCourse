/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.foi.uzdiz.jelvalcic.fileSystem;

import org.foi.uzdiz.jelvalcic.Jelvalcic_zadaca_1;
import org.foi.uzdiz.jelvalcic.support.MyAbstractFile;
import org.foi.uzdiz.jelvalcic.support.MyDirectory;

/**
 *
 * @author jelvalcic
 * Klasa konkretnog produkta za NTFS datotecni sustav za direktorij (Abstract Factory uzorak)
 */
public class MyNTFSDirectory extends MyDirectory{
    String owner;
    boolean isSLink = false;
    String sLinkTarget = "";
    

    public MyNTFSDirectory(String dName, int dRedniBroj) {
        super(dName, dRedniBroj);
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
 * Metoda za detaljan ispis prilagoden NTFS DS-u
 * @param g_indent Indent za strukturirani ispis
 */    
    
    @Override
    public void lsIspis(String g_indent) {
        String s = "";
        //ispisi svoj naziv
        g_indent = g_indent + " ";
        s = g_indent + Jelvalcic_zadaca_1.tmpS(this.getRedniBroj()) + "-" + this.getName() + " Owner:" + this.getOwner();
        
        if(this.isIsSLink()){
            s = s + "[" + this.getsLinkTarget() + "]";
        }
        System.out.println(s);
        g_indent = g_indent + " ";
        for (int i = 0; i < this.getdFiles().size(); ++i) {
            // Leverage the "lowest common denominator"
            MyAbstractFile obj = (MyAbstractFile) this.getdFiles().get(i);
            obj.lsIspis(g_indent);//ispisuje slijedeci tekuci element na koji i pokazuje (moze biti datoteka ili direktorij)
                                    //ako je direktorij ponovo dode tu, ali sa drugim node-om (kao neka rekurzija)
        }
        
    }

}
