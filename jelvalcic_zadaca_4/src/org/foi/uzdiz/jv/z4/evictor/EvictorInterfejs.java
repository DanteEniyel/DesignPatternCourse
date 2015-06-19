/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.foi.uzdiz.jv.z4.evictor;

/**
 * Sucelje uzorka Evictor
 * @author Dante Eniyel
 */
public interface EvictorInterfejs {
    public boolean isEvictable();
    public Object info();
    public void beforeEviction();
}
