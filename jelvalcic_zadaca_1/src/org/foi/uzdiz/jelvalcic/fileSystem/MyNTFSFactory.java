/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.foi.uzdiz.jelvalcic.fileSystem;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.UserPrincipal;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.uzdiz.jelvalcic.support.MyAbstractFile;

/**
 *
 * @author jelvalcic
 * Klasa Concrete Factory za NTFS datotecni sustav (Abstract Factory)
 */
public class MyNTFSFactory implements MyFileSystemFactory{
    
    private static int redniBroj = 1;

    
    @Override
    public int getRedniBroj() {
        return redniBroj;
    }
    
/**
 * Metoda za punjenje virtualnog ds-a (override zbog dodatnig atributa koji se zapisuju)
 * @param path Putanja direktorija
 * @param f Direktorij
 */
    
    public static void procitajStrukturu(File path, MyNTFSDirectory f) {//OVO TU NE DIRAJ VIŠE!!!!!
        MyNTFSDirectory fr = f;
        File[] files = path.listFiles();
        UserPrincipal up;
        for (int i = 0; i < files.length; i++) { 
            //odredujemo da li je datoteka ili direktorij
            if (files[i].isFile()) { //ako je file

                MyNTFSFile d = new MyNTFSFile(files[i].getName(), ++redniBroj);//++redniBroj - povecaj prije koristenja
                d.setfRoditelj(fr);
                d.setfSize(files[i].length());
                d.setfFullName(files[i].getAbsolutePath());
                File fF = new File(files[i].getAbsolutePath());//za atribute NTFS-a
                
                try {
                    //dodatni atributi za NTFS
                    //OWNER
                    up = Files.getOwner(fF.toPath());
                    d.setOwner(up.getName());
                    //SYMBOLIC LINK
                    if(Files.isSymbolicLink(fF.toPath())){
                        d.setIsSLink(true);
                        d.setsLinkTarget(Files.readSymbolicLink(fF.toPath()).toString());//na koji file pokazuje
                    }
                } catch (IOException ex) {
                    Logger.getLogger(MyNTFSFactory.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                f.add(d);//dodajemo u strukturu kao file
            } else if (files[i].isDirectory()) {//ako je direktorij
                
                File p = new File(files[i].getAbsolutePath());
                MyNTFSDirectory childFolder = new MyNTFSDirectory(files[i].getName(), ++redniBroj);//direktorij koji se našao u direktoriju
                childFolder.setdRoditelj(fr);
                childFolder.setdFullName(p.getAbsolutePath());
                File fF = new File(files[i].getAbsolutePath());
                
                try {
                    //dodatni atributi za NTFS
                    //OWNER
                    up = Files.getOwner(fF.toPath());
                    childFolder.setOwner(up.getName());
                    //SYMBOLIC LINK
                    if(Files.isSymbolicLink(fF.toPath())){
                        childFolder.setIsSLink(true);
                        childFolder.setsLinkTarget(Files.readSymbolicLink(fF.toPath()).toString());
                    }
                } catch (IOException ex) {
                    Logger.getLogger(MyNTFSFactory.class.getName()).log(Level.SEVERE, null, ex);
                }
                f.add(childFolder);//dodajemo u strukturu kao directory
                procitajStrukturu(p, childFolder);//rekurzija, ulazimo u procesiranje u child folder
            }
        }
    }

/**
 * Metoda koja kreira rootNode odgovarajuce vrste(ovisno o factory, ovaj slucaj NTFS) i popunjava strukturu
 * @param path Putanja direktorija
 * @return  Root Node
 */
   
    @Override
    public MyAbstractFile createRootNode(String path) {
        //citanje strukture direktorija
        MyNTFSDirectory rDir = new MyNTFSDirectory(path, 0);
        File froot = new File(path);
        procitajStrukturu(froot, rDir);
        return rDir;
    }

}
