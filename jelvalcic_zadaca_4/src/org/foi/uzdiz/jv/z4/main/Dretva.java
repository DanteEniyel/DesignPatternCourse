/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.uzdiz.jv.z4.main;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.foi.uzdiz.jv.z4.main.Zadaca4.cache;
import static org.foi.uzdiz.jv.z4.main.Zadaca4.posjeceneStranice;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Klasa dretve koja obavlja automatsko osvjezavanje aktivne stranice
 *
 * @author Dante Eniyel
 */
public class Dretva extends Thread {
    
    private String url;
    private int interval;
    private boolean kraj = false;
    private Date vrijemePocetak;
    private Date vrijemeKraj;
    private URLPodaci aktivnaStranica;
    private String stariSadrzaj;
    private String noviSadrzaj;
    private SupportSingleton support;
    
    public Dretva(String link, int intervalObnavljanja, URLPodaci aktivnaStranica, Document stariSadrzaj) {
        
        this.url = link;
        this.interval = intervalObnavljanja;
        this.aktivnaStranica = aktivnaStranica;
        this.stariSadrzaj = stariSadrzaj.toString();
        support = SupportSingleton.getInstance();
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
            Document doc = null;
            try {
                doc = Jsoup.connect(url).get();
                noviSadrzaj = doc.toString();
            } catch (IOException ex) {
                Logger.getLogger(Dretva.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            if (!(stariSadrzaj.equalsIgnoreCase(noviSadrzaj))) {
                
                aktivnaStranica.setPoveznice(support.nadjiLinkove(doc));//ako je doslo do promjene zapisuju se novi linkovi za aktivnu stranicu
                stariSadrzaj = noviSadrzaj; //za slijedeći prolaz usporedbe
                Zadaca4.ai.getAndIncrement();//lock - nema pristupanje podacima (umjesto semafora)
                cache.updateStranica(noviSadrzaj, support.stranicaDatName(url));//sprema se novi sadrzaj stranice u spremiste
                Zadaca4.ai.getAndDecrement();//unlock - dozvoljen pristup
//-------CHAIN OF RESPONSIBILITY-------------------------------------------------------
                posjeceneStranice.pocetnaStranica.msgOsvjeziStranicu(url, 1);
                
                System.out.println("Doslo je do promjene na stranici od zadnjeg osvjezenja.");
            } else {
//-------CHAIN OF RESPONSIBILITY-------------------------------------------------------
                posjeceneStranice.pocetnaStranica.msgOsvjeziStranicu(url, 0);
            }
            try {
                sleep(interval * 1000);
                vrijemeKraj = new Date();
//-------CHAIN OF RESPONSIBILITY-------------------------------------------------------
                posjeceneStranice.pocetnaStranica.msgZapisiVrijeme(url, vrijemeKraj.getTime() - vrijemePocetak.getTime());
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
