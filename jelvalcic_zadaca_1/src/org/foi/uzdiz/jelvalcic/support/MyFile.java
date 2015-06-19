/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.uzdiz.jelvalcic.support;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.uzdiz.jelvalcic.Jelvalcic_zadaca_1;

/**
 *
 * @author jelvalcic
 */
public class MyFile implements MyAbstractFile {

    private String fName;
    private int fRedniBroj;
    private MyAbstractFile fRoditelj;
    private long fSize;
    private String fFullName;

    public MyFile(String fName, int fRedniBroj) {
        this.fName = fName;
        this.fRedniBroj = fRedniBroj;
        this.fRoditelj = null;
        this.fSize = 0;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public int getfRedniBroj() {
        return fRedniBroj;
    }

    public void setfRedniBroj(int fRedniBroj) {
        this.fRedniBroj = fRedniBroj;
    }

    public MyAbstractFile getfRoditelj() {
        return fRoditelj;
    }

    public void setfRoditelj(MyAbstractFile fRoditelj) {
        this.fRoditelj = fRoditelj;
    }

    public long getfSize() {
        return fSize;
    }

    public void setfSize(long fSize) {
        this.fSize = fSize;
    }

    public String getfFullName() {
        return fFullName;
    }

    public void setfFullName(String fFullName) {
        this.fFullName = fFullName;
    }

/**
 * 
 * @return 
 */    
    
    @Override
    public String getName() {
        return fName;
    }

    @Override
    public String getRoditeljName() {
        return fRoditelj.getName();
    }

    @Override
    public int getRedniBroj() {
        return fRedniBroj;
    }

/**
 * Metoda za osnovni ispis
 * @param g_indent Indent za strukturirani ispis
 */    
    
    @Override
    public void ls(String g_indent) {

        System.out.println(g_indent + Jelvalcic_zadaca_1.tmpS(this.getRedniBroj()) + "-" + this.getName());
    }

/**
 * Metoda za detaljan ispis
 * @param g_indent Indent za strukturirani ispis
 */    
    
    @Override
    public void lsIspis(String g_indent) {
        System.out.println(g_indent + Jelvalcic_zadaca_1.tmpS(this.getRedniBroj()) + "-" + this.getName() + " (" + Long.toString(this.getfSize()) + " B)");
    }

/**
 * Metoda za dohvacanje elementa prema rednom broju
 * @param broj Redni broj elementa
 * @return Odabrani element
 */    
    
    @Override
    public MyAbstractFile getNodeByNumber(int broj) {
        MyAbstractFile odabraniElement = null;

        if (broj == fRedniBroj) {
            odabraniElement = this;
        }

        return odabraniElement;
    }

/**
 * Metoda za ispis roditelja odabranog elementa
 */    
    
    @Override
    public void lsParent() {
        if (fRoditelj != null) {
            System.out.println(this.getRedniBroj() + "-" + this.getName() + " -> " + this.fRoditelj.getRedniBroj() + " - " + this.fRoditelj.getName());
            fRoditelj.lsParent();
        }
    }

/**
 * Metoda za brisanje odabraneog elementa
 * @param broj Redni broj elementa
 */    
    
    @Override
    public void delete(int broj) {
        if (fRoditelj != null) {
            System.out.println("Brisem datoteku br. " + broj + "-" + this.getName());
            MyDirectory tmp = (MyDirectory) fRoditelj;
            tmp.obrisiDijete(broj);
        }
    }

/**
 * Metoda za kopiranje odabranog elementa i postavljanja novog imena
 * @param novoIme Novo ime elementa koji se kopira
 */
    
    @Override
    public void copyFile(String novoIme) {
        MyDirectory md = (MyDirectory) getfRoditelj();//uzimamo roditelja datoteke da u njegovu strukturu mozemo zapisati kopiranu datoteku
        if (novoIme.isEmpty()) {
            System.out.println("Novo ime datoteke nije zadano!");
        } else {
            for (int i = 0; i < md.getdFiles().size(); i++) {
                MyAbstractFile maf = (MyAbstractFile) md.getdFiles().get(i);//dohvacamo svaku datoteku u direktoriju da provjerimo da li postoji 
                if (maf.getName().toUpperCase().equals(novoIme.toUpperCase())) {
                    System.out.println("Odabrano ime vec postoji, molim odabrati drugo!");
                    return;
                }
            }

            MyFile mf = new MyFile(novoIme, ++Jelvalcic_zadaca_1.redniBroj);//radi brzine//kreiramo novu datoteku na temelju novog imena
            mf.setfSize(this.getfSize());
            mf.setfFullName(md.getdFullName() + "\\" + novoIme);//postavljamo putanju kopirane datoteke
            //postavljanje odredisne i pocetne putanje
            File fromFile = new File(this.getfFullName());
            File toFile = new File(mf.getfFullName());
            try {
                //kopiranje datoteke
                Files.copy(fromFile.toPath(), toFile.toPath());
            } catch (IOException ex) {
                Logger.getLogger(MyFile.class.getName()).log(Level.SEVERE, null, ex);
            }
            md.add(mf);
        }
    }

/**
 * Metoda za premjestanje odabranog elementa
 * @param rBrDirektorij Redni broj direktorija u koji se premjesta
 */    
    
    @Override
    public void moveFile(int rBrDirektorij, MyAbstractFile rooDir) {
        MyAbstractFile maf = null;
        maf = Jelvalcic_zadaca_1.rootDir.getNodeByNumber(rBrDirektorij);
        if (maf == null) {
            System.out.println("Ne postoji odredisni direktorij!");
            return;
        }

        if (maf instanceof MyFile) {
            System.out.println("Nije dozvoljeno premijestati datoteku u datoteku!");
            return;
        }

        MyDirectory md = (MyDirectory) maf;
        Path fromF = Paths.get(this.getfFullName());
        Path toDir = Paths.get(md.getdFullName() + "\\" + this.getName());
        try {
            Files.move(fromF, toDir, REPLACE_EXISTING);
        } catch (IOException ex) {
            Logger.getLogger(MyFile.class.getName()).log(Level.SEVERE, null, ex);
        }

        md.add(this);
        if (fRoditelj != null) {
            MyDirectory tmp = (MyDirectory) fRoditelj;
            tmp.obrisiDijete(this.getfRedniBroj());
        }

    }
}
