/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.foi.uzdiz.jv.z4.main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.foi.uzdiz.jv.z4.observer.Observer;
import org.foi.uzdiz.jv.z4.observer.SubjectInterfejs;

/**
 * Klasa objekta StraniceObjekt (sluzi za serializaciju)
 * @author Dante Eniyel
 */
public class StraniceObjekt extends Observer implements Serializable{
    public List<URLPodaci> posjeceneStranice = new ArrayList<>();
    public URLPodaci pocetnaStranica = null;//da se zapamti koja je pocetna stranica za slanje msg u chain
    public URLPodaci tekucaStranica = null;
    
    
    public StraniceObjekt() {
    }
    
/**
 * Metoda observera koja kada se dogodi promjena mice stranice iz objekta
 * @param o - objekt
 */   
    @Override
    public void update(SubjectInterfejs o) {
        if(!(o.getState().getLink().equalsIgnoreCase(tekucaStranica.getLink()))){//ako stranica nije tekuca stranica izbacuje se iz objekta
            posjeceneStranice.remove(o.getState());//mice se stranica iz objekta
            
            Zadaca4.support.rebuildChain(posjeceneStranice);
        }
        
    }
    
}
