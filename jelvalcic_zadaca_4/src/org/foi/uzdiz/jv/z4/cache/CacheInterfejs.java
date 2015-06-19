/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.foi.uzdiz.jv.z4.cache;

import org.jsoup.nodes.Document;

/**
 * Sucelje uzorka Cache
 * @author Dante Eniyel
 */
public interface CacheInterfejs {
    public Document getStranica(String url);
    public void updateStranica(String sadrzaj, String imeDatoteke);
    public void releaseStranica(String url, Document doc);
}
