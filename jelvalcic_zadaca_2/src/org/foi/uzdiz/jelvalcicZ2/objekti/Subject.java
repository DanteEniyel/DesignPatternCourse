/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.uzdiz.jelvalcicZ2.objekti;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.foi.uzdiz.jelvalcicZ2.interfejsi.ObserverInterfejs;
import org.foi.uzdiz.jelvalcicZ2.interfejsi.SubjectInterfejs;

/**
 * Klasa objekta Subject za Observer uzorak
 * @author Dante Eniyel
 */
public class Subject implements SubjectInterfejs {

    private List observers = new ArrayList();
    private ParSaRezultatom state;

/**
 * Konstruktor
 */    
    public Subject() {
        state = new ParSaRezultatom(0, 0, 0);
    }

    @Override
    public void addObserver(ObserverInterfejs o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(ObserverInterfejs o) {
        observers.remove(o);
    }

    @Override
    public ParSaRezultatom getState() {
        return state;
    }

    @Override
    public void setState(ParSaRezultatom state) {
        this.state = state;
        notifyObservers();

    }

    public void notifyObservers() {
        Iterator i = observers.iterator();
        while (i.hasNext()) {
            ObserverInterfejs o = (ObserverInterfejs) i.next();
            o.update(this);
        }
    }

}
