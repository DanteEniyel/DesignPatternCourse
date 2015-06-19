/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.foi.uzdiz.jv.z4.observer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.foi.uzdiz.jv.z4.main.URLPodaci;

/**
 * Klasa Subject (dio Observer uzorka)
 * @author Dante Eniyel
 */
public class Subject implements SubjectInterfejs{
    private List observers = new ArrayList();
    private URLPodaci state;

    @Override
    public void addObserver(ObserverInterfejs o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(ObserverInterfejs o) {
        observers.remove(o);
    }
    
    @Override
    public void setState(URLPodaci state) {
        this.state = state;
        notifyObservers();
    }
    
    
    @Override
    public URLPodaci getState() {
        return this.state;
    }
    
    public void notifyObservers() {
        Iterator i = observers.iterator();
        while (i.hasNext()) {
            ObserverInterfejs o = (ObserverInterfejs) i.next();
            o.update(this);
        }
    }


    
    
}
