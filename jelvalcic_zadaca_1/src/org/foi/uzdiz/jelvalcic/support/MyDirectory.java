/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.foi.uzdiz.jelvalcic.support;

import java.io.File;
import java.util.ArrayList;
import org.foi.uzdiz.jelvalcic.Jelvalcic_zadaca_1;

/**
 *
 * @author jelvalcic
 * Klasa za Composite (Composite uzorak)
 */
public class MyDirectory implements MyAbstractFile{

    private ArrayList dFiles = new ArrayList();
    private String dName;
    private int dRedniBroj;
    private MyAbstractFile dRoditelj;
    private int dSize;
    private String dFullName;

    public MyDirectory(String dName, int dRedniBroj) {
        this.dName = dName;
        this.dRedniBroj = dRedniBroj;

    }

    public ArrayList getdFiles() {
        return dFiles;
    }

    public void setdFiles(ArrayList dFiles) {
        this.dFiles = dFiles;
    }

    public String getdName() {
        return dName;
    }

    public void setdName(String dName) {
        this.dName = dName;
    }

    public int getdRedniBroj() {
        return dRedniBroj;
    }

    public void setdRedniBroj(int dRedniBroj) {
        this.dRedniBroj = dRedniBroj;
    }

    public MyAbstractFile getdRoditelj() {
        return dRoditelj;
    }

    public void setdRoditelj(MyAbstractFile dRoditelj) {
        this.dRoditelj = dRoditelj;
    }

    public int getdSize() {
        return dSize;
    }

    public void setdSize(int dSize) {
        this.dSize = dSize;
    }

    public String getdFullName() {
        return dFullName;
    }

    public void setdFullName(String dFullName) {
        this.dFullName = dFullName;
    }
    
    public void add(Object obj) {
        dFiles.add(obj);
    }
    
    @Override
    public String getName() {
        return dName;
    }

    @Override
    public String getRoditeljName() {
        if (dRoditelj != null) {
            return dRoditelj.getName();
        } else {
            return "";
        }
    }

    @Override
    public int getRedniBroj() {
        return dRedniBroj;
    }

/**
 * Metoda za osnovni ispis
 * @param g_indent Indent za strukturirani ispis
 */    
    
    @Override
    public void ls(String g_indent) {
        //ispisi svoj naziv
        g_indent = g_indent + " ";
        System.out.println(g_indent + Jelvalcic_zadaca_1.tmpS(this.getRedniBroj()) + "-" + this.getName());
        g_indent = g_indent + " ";
        for (int i = 0; i < dFiles.size(); ++i) {
            // Leverage the "lowest common denominator"
            MyAbstractFile obj = (MyAbstractFile) dFiles.get(i);
            obj.ls(g_indent);
        }
        
    }

/**
 * Metoda za detaljan ispis
 * @param g_indent Indent za strukturirani ispis
 */    
    
    @Override
    public void lsIspis(String g_indent) {
        //ispisi svoj naziv
        g_indent = g_indent + " ";
        System.out.println(g_indent + Jelvalcic_zadaca_1.tmpS(this.getRedniBroj()) + "-" + this.getName());
        g_indent = g_indent + " ";
        for (int i = 0; i < dFiles.size(); ++i) {
            // Leverage the "lowest common denominator"
            MyAbstractFile obj = (MyAbstractFile) dFiles.get(i);
            obj.lsIspis(g_indent);
        }
        
    }
    
/**
 * Metoda koja dohvaca element prema rednom broju
 * @param broj Redni broj direktorija
 * @return Objekt odabranog elementa
 */    
    
    @Override
    public MyAbstractFile getNodeByNumber(int broj) {
        MyAbstractFile odabraniElement = null;
        if (broj == dRedniBroj) {
            odabraniElement = this;
        } else {
            for (int i = 0; i < dFiles.size(); ++i) {

                MyAbstractFile obj = (MyAbstractFile) dFiles.get(i);
                odabraniElement = obj.getNodeByNumber(broj);//ako je došao do direktorija ispituje elemente unutar njega
                if (odabraniElement != null) {//ako je pronađen element odmah se prekida
                    break;
                }
            }
        }

        return odabraniElement;
    }
    
/**
 * Metoda za ispis roditelja odabranog elementa
 */    
    
    @Override
    public void lsParent() {
        if (dRoditelj != null) {
            System.out.println(this.getRedniBroj() + "-" + this.getName() + " -> " + this.dRoditelj.getRedniBroj() + "-" + this.dRoditelj.getName());
            dRoditelj.lsParent();
        }
    }

/**
 * Metoda za brisanje dijece direktorija
 * @param dijeteBr Redni broj djeteta
 */    
    
    public void obrisiDijete(int dijeteBr) {
        for (int i = 0; i < dFiles.size(); i++) {
            if (dFiles.get(i) instanceof MyFile) {
                MyFile tmp = (MyFile)dFiles.get(i);
                if (tmp.getRedniBroj() == dijeteBr) {
                    File praviFile = new File(tmp.getfFullName());
                    if(praviFile.exists()){//kontrola da ne bi probao brisati kada se datoteka premješta
                        praviFile.delete();
                    }
                    dFiles.remove(i);
                    break;
                }
            }else{
                MyDirectory tmp2 = (MyDirectory)dFiles.get(i);
                if(tmp2.getdRedniBroj() == dijeteBr){
                    File praviFile2 = new File(tmp2.getdFullName());
                    praviFile2.delete();
                    dFiles.remove(i);
                    break;
                }
            }
        }
    }

/**
 * Metoda za brisanje elementa
 * @param broj Redni broj elementa
 */    
    
    @Override
    public void delete(int broj) {
        while(!(dFiles.isEmpty())){//dok je puna lista uvijek ce biti 0-tog elementa
            if(dFiles.get(0) instanceof MyDirectory){
                MyDirectory md = (MyDirectory)dFiles.get(0);
                md.delete(0);
                
            }else{
                MyFile mf = (MyFile)dFiles.get(0);
                mf.delete(mf.getfRedniBroj());
            }
        }
        //kad se obrise zadnji element u listi onda se brisemo iz roditeljske liste
        if(this.getdRoditelj() != null){
            MyDirectory md2 = (MyDirectory)this.getdRoditelj();
            System.out.println("Brisem element br: " + this.getRedniBroj() + "-" + this.getName());
            md2.obrisiDijete(this.getRedniBroj());//ovo tu
        }else{
            System.out.println("Nemres brisati root!");
        }
        
    }
    
/**
 * Metoda za kopiranje elementa
 * @param novoIme Novo ime elementa
 */    

    @Override
    public void copyFile(String novoIme) {
        System.out.println("I don't do that!");
    }

/**
 * Metoda za premjestanje elementa u drugi direktorij
 * @param rBrDirektorij Redni broj direktorija u koji se premjesta
 * @param rooDir Root direktorij
 */    
    
    @Override
    public void moveFile(int rBrDirektorij, MyAbstractFile rooDir) {
         System.out.println("I don't do that!");
    }
    
}
