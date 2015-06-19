/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.foi.uzdiz.jelvalcic.fileSystem;

import java.io.File;
import org.foi.uzdiz.jelvalcic.support.MyAbstractFile;

/**
 *
 * @author jelvalcic
 * Klasa Concrete Factory za FAT datotecni sustav (Abstract Factory)
 */
public class MyFATFactory implements MyFileSystemFactory{
    
    
    private static int redniBroj = 1;

    @Override
    public int getRedniBroj() {
        return redniBroj;
    }
    
/**
 * Metoda za punjenje virtualnog ds-a
 * @param path Putanja direktorija
 * @param f Direktorij
 */    
    
    public static void procitajStrukturu(File path, MyFATDirectory f) {//OVO TU NE DIRAJ VIŠE!!!!!
        MyFATDirectory fr = f;
        File[] files = path.listFiles();
        for (int i = 0; i < files.length; i++) { //odredujemo da li je datoteka ili direktorij
            if (files[i].isFile()) { //ako je file

                MyFATFile d = new MyFATFile(files[i].getName(), ++redniBroj);
                d.setfRoditelj(fr);
                d.setfSize(files[i].length());
                d.setfFullName(files[i].getAbsolutePath());
                f.add(d);
            } else if (files[i].isDirectory()) {//ako je direktorij
                File p = new File(files[i].getAbsolutePath());
                MyFATDirectory childFolder = new MyFATDirectory(files[i].getName(), ++redniBroj);//direktorij koji se našao u direktoriju
                childFolder.setdRoditelj(fr);
                childFolder.setdFullName(p.getAbsolutePath());
                f.add(childFolder);
                procitajStrukturu(p, childFolder);//rekurzija, ulazimo u procesiranje u child folder
            }
        }
    }
    
/**
 * Metoda koja kreira rootNode odgovarajuce vrste(ovisno o factory, ovaj slucaj FAT) i popunjava strukturu
 * @param path Putanja direktorija
 * @return Root Node
 */
    
    @Override
    public MyAbstractFile createRootNode(String path) {
        MyFATDirectory rDir = new MyFATDirectory(path, 0);
        File froot = new File(path);
        procitajStrukturu(froot, rDir);
        return rDir;
    }
    
}
