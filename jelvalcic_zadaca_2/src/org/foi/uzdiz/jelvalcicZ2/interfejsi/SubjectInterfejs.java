/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.uzdiz.jelvalcicZ2.interfejsi;

import org.foi.uzdiz.jelvalcicZ2.objekti.ParSaRezultatom;

/**
 * Sucelje za ostvarenje Observer uzorka
 * @author Dante Eniyel
 */
public interface SubjectInterfejs {

    public void addObserver(ObserverInterfejs o);

    public void removeObserver(ObserverInterfejs o);

    public ParSaRezultatom getState();

    public void setState(ParSaRezultatom state);

}
