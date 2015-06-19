/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.uzdiz.jelvalcic.z3;

import java.util.Date;
import java.util.List;
import static org.foi.uzdiz.jelvalcic.z3.Zadaca3.link;
import static org.foi.uzdiz.jelvalcic.z3.Zadaca3.pocetnaStranica;
import static org.foi.uzdiz.jelvalcic.z3.Zadaca3.support;

/**
 * Klasa dretve koja obavlja automatsko osvjezavanje aktivne stranice
 * @author Dante Eniyel
 */
public class Dretva extends Thread {

    private String url;
    private int interval;
    private boolean kraj = false;
    private Date vrijemePocetak;
    private Date vrijemeKraj;
    private URLPodaci aktivnaStranica;

    public Dretva(String link, int intervalObnavljanja, URLPodaci aktivnaStranica) {

        this.url = link;
        this.interval = intervalObnavljanja;
        this.aktivnaStranica = aktivnaStranica;
    }

    @Override
    public synchronized void start() {
        System.out.println("Pocela dretva!");
        super.start();
    }

    @Override
    public void run() {

        while (!kraj) {
            vrijemePocetak = new Date();
            List<String> temp = null;
            temp = support.usporediLinkove(link, aktivnaStranica.getPoveznice());
            if (temp != null) {
                aktivnaStranica.setPoveznice(temp);
//-------CHAIN OF RESPONSIBILITY-------------------------------------------------------
                pocetnaStranica.msgOsvjeziStranicu(link, false, 1);

                System.out.println("Doslo je do promjene na stranici od zadnjeg osvjezenja.");
            } else {
//-------CHAIN OF RESPONSIBILITY-------------------------------------------------------
                pocetnaStranica.msgOsvjeziStranicu(link, false, 0);
            }
            try {
                sleep(interval * 1000);
                vrijemeKraj = new Date();
//-------CHAIN OF RESPONSIBILITY-------------------------------------------------------
                Zadaca3.pocetnaStranica.msgZapisiVrijeme(url, vrijemeKraj.getTime() - vrijemePocetak.getTime());
            } catch (InterruptedException ex) {

            }

        }

    }

    @Override
    public void interrupt() {

        kraj = true;
        super.interrupt();
    }

}
