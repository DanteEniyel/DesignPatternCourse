/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.uzdiz.jelvalcic.z3;

/**
 * Metoda objekta HistoryCR (za funkcionalnost back)
 * @author Dante Eniyel
 */
public class HistoryCR {

    private String link;
    private HistoryCR previous = null;
    private HistoryCR next = null;

    public HistoryCR(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public HistoryCR getPrevious() {
        return previous;
    }

    public void setPrevious(HistoryCR previous) {
        this.previous = previous;
    }

    public HistoryCR getNext() {
        return next;
    }

    public void setNext(HistoryCR next) {
        this.next = next;
    }

/**
 * Metoda poruke za chain of responsibility
 * @param howMany - index stranice na koju se vracamo (odnosno koliko stranica unatrag se vracamo)
 */    
    public void msgGoBack(int howMany) {

        if (howMany == 1) {
            Zadaca3.backLink = this.link;

            if (previous != null) {

                Zadaca3.backTail = previous;

            } else {
                
                Zadaca3.backTail = null;
                Zadaca3.backHead = null;
            }
        } else {
            if (previous != null) {
                previous.msgGoBack(howMany - 1);

            }
        }

        if (previous != null) {

            
            previous.setNext(null);
            this.setPrevious(null);

        } else {
            this.setNext(null);
            
        }
        /*System.out.println(link + " ");
        if (Zadaca3.backHead != null) {
            System.out.println(Zadaca3.backHead.getLink() + " " + Zadaca3.backHead.getLink());
        }*/
    }

}
