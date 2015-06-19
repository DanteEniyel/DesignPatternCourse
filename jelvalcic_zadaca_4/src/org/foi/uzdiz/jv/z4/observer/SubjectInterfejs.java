/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.foi.uzdiz.jv.z4.observer;

import org.foi.uzdiz.jv.z4.main.URLPodaci;

/**
 * Sucelje za Subject (dio Observer uzorka)
 * @author Dante Eniyel
 */
public interface SubjectInterfejs {
    public void addObserver(ObserverInterfejs o);

    public void removeObserver(ObserverInterfejs o);

    public URLPodaci getState();

    public void setState(URLPodaci state);
}
