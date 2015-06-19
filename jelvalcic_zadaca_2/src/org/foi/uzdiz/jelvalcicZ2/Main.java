/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.uzdiz.jelvalcicZ2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.uzdiz.jelvalcicZ2.objekti.Caretaker;
import org.foi.uzdiz.jelvalcicZ2.objekti.Klub;
import org.foi.uzdiz.jelvalcicZ2.objekti.ParSaRezultatom;
import org.foi.uzdiz.jelvalcicZ2.objekti.Parovi;
import org.foi.uzdiz.jelvalcicZ2.objekti.PovijestParova;
import org.foi.uzdiz.jelvalcicZ2.objekti.RangLista;
import org.foi.uzdiz.jelvalcicZ2.objekti.RedRangListe;

/**
 * Glavna klasa programa
 * @author Dante Eniyel
 */
public class Main {

    public static boolean kraj = false;
    public static List<Klub> klubovi = new ArrayList();
    public static int intervalSpavanjaDretve;
    public static int intervalKontroleRangListe;
    public static int pragIntervalaZaIspadanje;
    //-----Memento uzorak
    public static Caretaker caretakerParovi = new Caretaker();
    public static Caretaker caretakerRangLista = new Caretaker();
    //-------------------
    public static int brojKola = 0;
    public static int brojRangListe = 0;
    private static Dretva dr;
    public static Klub prviKlub;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        if (!ucitajArgumente(args)) {//provjera dal su ispravno upisani parametri
            System.out.println("Primjer ispravne sintakse za parametre je : 'putanja\\datoteka.txt 30 5 3'");
            System.exit(-1);
        }

        /*for (Klub k : klubovi) {
         System.out.println(k.getSifraKluba() + " " + k.getNazivKluba() + " ");
         }*/
        
        dr = new Dretva();
        //pokretanje dretve
        dr.start();
        try {
            //pozivanje user interface metode
            uI();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

/**
 * Metoda za ucitavanje argumenata i njihovu provjeru
 * @param args - Argumenti komandne linije
 * @return - true - parametri su prosli provjere, false - parametri nisu prosli provjeru
 */    
    public static boolean ucitajArgumente(String[] args) {
        try {
            if (args.length != 4) {
                System.out.println("Neispravni broj parametara!");
                return false;
            }

            intervalSpavanjaDretve = Integer.parseInt(args[1]);
            if (intervalSpavanjaDretve < 2 || intervalSpavanjaDretve > 600) {
                System.out.println("Interval spavanja dretve treba biti izmedu 2 i 600 sekundi!");
                return false;
            }

            intervalKontroleRangListe = Integer.parseInt(args[2]);
            if (intervalKontroleRangListe < 1 || intervalKontroleRangListe > 20) {
                System.out.println("Interval provjere rang liste treba biti izmedu 1 i 20!");
                return false;
            }

            pragIntervalaZaIspadanje = Integer.parseInt(args[3]);
            if (pragIntervalaZaIspadanje < 1 || pragIntervalaZaIspadanje > 10) {
                System.out.println("Prag intervala nakon kojih klub ispada treba biti izmedu 1 i 10!");
                return false;
            }

            //provjera postojanja datoteke
            File dat = new File(args[0]);
            if (!dat.exists()) {
                System.out.println("Datoteka s navedenim imenom ne postoji! (" + args[0] + ")");
                return false;
            }

            BufferedReader br = null;
            String red;
            br = new BufferedReader(new FileReader(args[0]));

            int sifra;
            String naziv;

            //punjenje radne liste za klubove
            while ((red = br.readLine()) != null) {
                sifra = Integer.parseInt(red.substring(1, 5).trim());
                naziv = red.substring(5, red.length());
                Klub klub = new Klub(sifra, naziv);
                klubovi.add(klub);
            }

            br.close();
//------------CHAIN OF COMMAND ELEMENT------------------
            prviKlub = klubovi.get(0);
            prviKlub.setNext(klubovi.get(1));
            for(int i = 1; i < klubovi.size()-1; i++){//zato jer zadnji nece imati link na sljedeceg i da predzadnji bude linkan na zadnjeg
                Klub k = klubovi.get(i);
                k.setNext(klubovi.get(i+1));//da linkamo na slijedeceg
            }
//------------------------------------------------------
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    /**
     * Metoda za unos broja s konzole
     *
     * @param opis Poruka koja opisuje sto treba unijeti
     * @return Vraca se uneseni parametar
     * @throws IOException
     */
    private static int unosBroja(String opis) throws IOException {
        System.out.print(opis + " ");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String odabraniBroj = br.readLine();
        int odBroj = Integer.parseInt(odabraniBroj);

        return odBroj;
    }

/**
 * Metoda ispisa korisnickog sucelja, odn. izbornika
 * @throws IOException 
 */
    public static void uI() throws IOException {
        String odgovor = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Parovi p = new Parovi();
        RangLista r = new RangLista();
        int unos;

        System.out.println("------------- IZBORNIK ----------------\n"
                + "\t1.\tIspis svih arhiviranih redosljeda klubova\n"
                + "\t2.\tIspis određenog redoslijeda klubova\n"
                + "\t3.\tIspis rezultata određenog kola prema rednom broju redosljeda klubova\n"
                + "\t4.\tIspis svih povijesnih rezultata kluba\n"
                + "\t9.\tPrekid dretve\n"
                + "\tQ.\tKraj programa");
        try {
            odgovor = br.readLine().toUpperCase();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        while (!(odgovor.equals("Q"))) {
            switch (odgovor) {
//--------------PREKID DRETVE-----------------
                case "9":
                    if (!kraj) {
                        dr.interrupt();
                    }
                    break;
//--------------ISPIS SVIH ARHIVIRANIH REDOSLJEDA KLUBOVA-------------
                case "1":

                    if (kraj) {
                        for (int i = 0; i < caretakerRangLista.getBrojStanja(); i++) {
                            //vracanje iz meneta
                            r.restoreFromMemento(caretakerRangLista.getMemento(i));
                            System.out.println("Poredak" + "\tSifra" + "\tNaziv" + "\t\tBodovi ");
                            for (RedRangListe rrl : r.getRangLista()) {
                                System.out.println(rrl.getPoredak() + "\t" + rrl.getSifraKluba() + "\t" + getNazivKlubaPoSifri(rrl.getSifraKluba()) + "\t\t" + rrl.getUkupniBodovi());
                            }
                            System.out.println("\n");
                        }
                        
                    } else {
                        System.out.println("Please stop the thread, would you?");
                    }

                    /*p.restoreFromMemento(caretakerParovi.getMemento(0));
                     for (ParSaRezultatom psr : p.getParovi()) {
                     System.out.println("Domacin: " + psr.getPrviKlub() + " Gost: " + psr.getDrugiKlub() + " Rezultat susreta: " + psr.getRezultatSusreta());
                     }*/
                    break;
//---------------ISPIS ODREDENOG REDOSLJEDA KLUBOVA---------------
                case "2":
                    if (kraj) {

                        unos = unosBroja("Unesite redni broj rang liste  (Max " + caretakerRangLista.getBrojStanja() + "): ");

                        if (unos > caretakerRangLista.getBrojStanja()) {
                            System.out.println("Molim unesite broj koji odgovara kolicini rang listi...");
                        }else{
                            //vracanje iz mementa
                            r.restoreFromMemento(caretakerRangLista.getMemento(unos-1));
                            System.out.println("Poredak" + "\tSifra" + "\tNaziv" + "\t\tBodovi ");
                            for (RedRangListe rrl : r.getRangLista()) {
                                System.out.println(rrl.getPoredak() + "\t" + rrl.getSifraKluba() + "\t" + getNazivKlubaPoSifri(rrl.getSifraKluba()) + "\t\t" + rrl.getUkupniBodovi());
                            }
                        
                        System.out.println("\n");
                        }
                        
                    } else {
                        System.out.println("Please stop the thread, would you?");
                    }
                    break;
//----------------------ISPIS REZULTATA KOLA PREMA ODREDENOM REDOSLJEDU KLUBOVA------------------ 
                case "3":
                    if (kraj) {

                        unos = unosBroja("Unesite redni broj rang liste  (Max " + caretakerRangLista.getBrojStanja() + "): ");

                        if (unos > caretakerRangLista.getBrojStanja()) {
                            System.out.println("Molim unesite broj koji odgovara kolicini rang listi...");
                        }else{
                            RedRangListe neZnamKojiViseJeOvoRed;
                            r.restoreFromMemento(caretakerRangLista.getMemento(unos-1));
                            neZnamKojiViseJeOvoRed = r.dohvatiRed(0);
                            //vracanje iz mementa
                            p.restoreFromMemento(caretakerParovi.getMemento(neZnamKojiViseJeOvoRed.getBrojKola()-1));
                            
                            System.out.println("Domacin" + "\t\tGost" + "\tRezultat susreta");
                            for (ParSaRezultatom parRez : p.getParovi()) {
                                System.out.println(getNazivKlubaPoSifri(parRez.getPrviKlub()) 
                                                + "\t\t" + getNazivKlubaPoSifri(parRez.getDrugiKlub()) + "\t\t" + parRez.getRezultatSusreta());
                            }
                        
                        System.out.println("\n");
                        }
                        
                    } else {
                        System.out.println("Please stop the thread, would you?");
                    }
                    break;
//----------------ISPIS SVIH POVIJESNIH REZULTATA KLUBA-------------------------------                    
                case "4":
                    if (kraj) {

                        unos = unosBroja("Unesite sifru kluba: ");

                        if (getNazivKlubaPoSifri(unos).isEmpty()) {
                            System.out.println("Nema kluba s tom sifrom...");
                        }else{
                            ParSaRezultatom par;
                            for(Klub kl : klubovi){
                                if(kl.getSifraKluba() == unos){
                                    System.out.println("Kolo" + "\tDomacin" + "\tGost" + "\tRezultat susreta");
                                    for(PovijestParova pp : kl.getPp()){
                                        //vracanje iz meneta
                                        p.restoreFromMemento(caretakerParovi.getMemento(pp.getBrojKola()-1));
                                        par = p.dohvatiPar(pp.getRedniBrojUParovima() -1 );
                                        System.out.println(pp.getBrojKola() + "\t" + getNazivKlubaPoSifri(par.getPrviKlub()) 
                                                + "\t" + getNazivKlubaPoSifri(par.getDrugiKlub()) + "\t\t" + par.getRezultatSusreta());                     
                                    }
                                }
                            }
                            System.out.println("\n");
                        }
                        
                    } else {
                        System.out.println("Please stop the thread, would you?");
                    }
                    break;    
//--------------------PREKID PROGRAMA-----------------------                    
                case "Q":

                    break;

                /*case "Q":
                    
                 break;*/
            }
            try {
                odgovor = br.readLine().toUpperCase();
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (!kraj) {
            dr.interrupt();
        }
    }

/**
 * Metoda za dohvacanje naziva kluba prema sifri
 * @param sifraKluba - sifra kluba za koji se trazi naziv
 * @return String - naziv kluba
 */
    private static String getNazivKlubaPoSifri(int sifraKluba) {
        String naziv = "";

        for (Klub k : klubovi) {
            if (sifraKluba == k.getSifraKluba()) {
                naziv = k.getNazivKluba();
                break;
            }
        }

        return naziv;
    }

}
