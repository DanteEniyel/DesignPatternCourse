/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.foi.uzdiz.jelvalcic.fileSystem;

import org.foi.uzdiz.jelvalcic.support.MyFile;

/**
 *
 * @author jelvalcic
 * Klasa konkretnog produkta za FAT datotecni sustav za detoteku (Abstract Factory uzorak)
 */
public class MyFATFile extends MyFile{

    public MyFATFile(String fName, int fRedniBroj) {
        super(fName, fRedniBroj);
    }
    
}
