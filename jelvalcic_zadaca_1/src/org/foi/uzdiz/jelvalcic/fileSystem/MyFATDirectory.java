/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.foi.uzdiz.jelvalcic.fileSystem;

import org.foi.uzdiz.jelvalcic.support.MyDirectory;

/**
 *
 * @author jelvalcic
 * Klasa konkretnog produkta za FAT datotecni sustav za direktorij (Abstract Factory uzorak)
 */
public class MyFATDirectory extends MyDirectory{

    public MyFATDirectory(String dName, int dRedniBroj) {
        super(dName, dRedniBroj);
    }
    
}
