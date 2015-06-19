/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.foi.uzdiz.jelvalcic.fileSystem;

import org.foi.uzdiz.jelvalcic.support.MyAbstractFile;

/**
 *
 * @author jelvalcic
 * Sucelje za Concrete Factory (Abstract Factory uzorak)
 */
public interface MyFileSystemFactory {
    public abstract MyAbstractFile createRootNode(String path);
    public abstract int getRedniBroj();
}
