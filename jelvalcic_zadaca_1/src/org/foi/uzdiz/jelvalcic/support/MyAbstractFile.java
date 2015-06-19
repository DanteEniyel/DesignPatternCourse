/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.foi.uzdiz.jelvalcic.support;

/**
 *
 * @author jelvalcic
 * Sucelje za Component (Composite uzorak)
 */
public interface MyAbstractFile {
    public void ls(String g_indent);
    public void lsIspis(String g_indent);
    public void lsParent();
    public MyAbstractFile getNodeByNumber(int broj);
    public String getName();
    public String getRoditeljName();
    public int getRedniBroj();
    //public int getRoditeljRedniBroj();
    public void delete(int broj);
    public void copyFile(String novoIme);
    public void moveFile(int rBrDirektorij, MyAbstractFile rooDir);
}
