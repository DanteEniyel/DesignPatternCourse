/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.uzdiz.jelvalcic;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.uzdiz.jelvalcic.fileSystem.MyFATFactory;
import org.foi.uzdiz.jelvalcic.fileSystem.MyFileSystemFactory;
import org.foi.uzdiz.jelvalcic.fileSystem.MyNTFSFactory;
import org.foi.uzdiz.jelvalcic.support.MyAbstractFile;

/**
 *
 * @author jelvalcic
 * Main klasa koja vrši ulogu klijenta prema korištenim uzorcima dizajna
 */
public class Jelvalcic_zadaca_1 {

    /**
     * @param args the command line arguments
     */
    public static String DS_TIP = "";
    public static String root;
    public static String g_indent = "";
    public static int redniBroj;
    public static MyAbstractFile rootDir = null;
    public static BufferedReader brRoot = new BufferedReader(new InputStreamReader(System.in));

    public static MyFileSystemFactory mfsf = null;

    public static void main(String[] args) {
        DS_TIP = System.getenv("DS_TIP");//dohvacanje varijable okoline os-a

        if (DS_TIP == null) {//provjera da li postoji takva varijabla okoline
            System.out.println("Tip datotecnog sustava nije odreden u varijabli okruzenja OS-a!");
            System.exit(-1);
        }
        if (!(DS_TIP.toUpperCase().equals("NTFS") || DS_TIP.toUpperCase().equals("FAT"))) {//ako je definiran neki drugi ds tip
            System.out.print("Tip datotecnog sustava nije podrzan!");
            System.exit(-2);
        }
        System.out.println("Upisite putanju direktorija: ");
        try {
            root = brRoot.readLine();
        } catch (IOException ex) {
            Logger.getLogger(Jelvalcic_zadaca_1.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(" ");
        //root = "C:\\Users\\Dante Eniyel\\Desktop\\Unsorted2\\2.semestar\\UD\\proba\\jelvalcic_zadaca_1";
        File froot = new File(root);
        if (!(froot.exists() && froot.isDirectory())) {//provjera da li postoji taj direktorij
            System.out.println("Putanja ne postoji ili nije direktorij!");
            System.exit(-3);
        }

        switch (DS_TIP) {//na temelju postavljenog odabranog ds-a se instancira prikladan factory
            case "NTFS":
                mfsf = new MyNTFSFactory();
                break;

            case "FAT":
                mfsf = new MyFATFactory();
                break;

        }

        rootDir = mfsf.createRootNode(root);//kreiranje vrsnog cvora na temelju instanciranog factory-a (mi ne znamo kojeg)
        redniBroj = mfsf.getRedniBroj();//uzimamo posljednji zauzeti redni broj kako bi mogli dodati nove redne brojeve na nove elemente stabla (radi brzine)
        //pocetni ispis strukture direktorija
        rootDir.ls(g_indent);

        //odabir naredbi
        commandInterpreter();
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
     * Metoda za ispis mogucih operacija
     *
     * @return Vraca se popis mogucih komandi
     */
    private static String popisKomandi() {
        String komande = "\n\n-Ispis- (Ispisuje sve elemente u root direktoriju)"
                + "\n -Ispis el- (Ispisuje se stablo ili element na temelju odabranog elementa)"
                + "\n -Ispis roditelj- (Ispisuje se roditelj i nadredeni elementi odabranog elementa)"
                + "\n -Brisi- (Brise se odabrani element)"
                + "\n -Kopiraj i preimenuj- (Kopira se odabrani element i dodjeljuje mu se novo ime)"
                + "\n -Premjesti- (Premjesta se odabrani element u navedeni direktorij)"
                + "\n -Pomoc- (Ispis mogucih komandi)"
                + "\n -Q- (Izlaz iz programa) \n";

        return komande;
    }

    /**
     * Metoda za formatiranje integera za indent kod ispisa rednih brojeva
     * manjih od 10
     *
     * @param rBr Redni broj
     * @return Vraca se razmak koji se dodaje ispred rednog broja koji je manji od 10
     */
    public static String tmpS(int rBr) {
        String s;
        if (rBr < 10) {//ako je redni broj manji od 10 dodaj još jedan razmak u indent
            s = " " + Integer.toString(rBr);
        } else {
            s = Integer.toString(rBr);
        }
        return s;
    }

    /**
     * Metoda kojom se interpretiraju i izvrsavaju odabrane operacije
     */
    private static void commandInterpreter() {
        try {
            String odabir;
            String newFileName;
            String toDir;
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            MyAbstractFile temp = null;

            System.out.println(popisKomandi());
            System.out.println("\n" + "Odaberite komandu: ");
            odabir = br.readLine().toUpperCase();

            while (!(odabir.equals("Q"))) {
                switch (odabir) {
                    case "ISPIS": //ispisuje se cijelo stablo
                        rootDir.ls(g_indent);
                        break;
                    case "ISPIS EL": //ispis prema odabranom elementu(ali sa atributima ds-a)
                        temp = rootDir.getNodeByNumber(unosBroja("Od elementa: "));
                        if (temp != null) {
                            temp.lsIspis(g_indent);//ispisuje se sve unutar direktorija
                        } else {
                            System.out.println("Element nije naden!");
                        }
                        break;

                    case "ISPIS RODITELJ"://ispis roditelja odabranog elementa
                        temp = rootDir.getNodeByNumber(unosBroja("Broj elementa: "));//rootDir zato jer uvijek ide od pocetka
                        System.out.println("");
                        if (temp != null) {
                            temp.lsParent();//ispisuje se sve unutar direktorija
                        } else {
                            System.out.println("Element nije naden!");
                        }
                        break;

                    case "BRISI"://brisanje odabranog elementa
                        int brTmp = unosBroja("Broj elementa: ");
                        temp = rootDir.getNodeByNumber(brTmp);
                        System.out.println("");
                        if (temp != null) {
                            temp.delete(brTmp);//brisanje elementa
                        } else {
                            System.out.println("Element nije naden!");
                        }
                        break;

                    case "KOPIRAJ"://kopiranje odabranog elementa i dodavanje novog imena (samo za datoteke)
                        temp = rootDir.getNodeByNumber(unosBroja("Broj elementa: "));
                        System.out.print("\n Napisite novo ime: ");
                        newFileName = br.readLine();
                        System.out.println("");
                        if (temp != null) {
                            temp.copyFile(newFileName);
                        } else {
                            System.out.println("Element nije naden!");
                        }
                        break;

                    case "PREMJESTI"://premjestanje odabranog elementa u odreden direktorij (samo datoteke)
                        temp = rootDir.getNodeByNumber(unosBroja("Broj elementa: "));
                        System.out.print("\n U direktorij: ");
                        toDir = br.readLine();
                        System.out.println("");
                        if (temp != null) {
                            temp.moveFile(Integer.parseInt(toDir), rootDir);
                        } else {
                            System.out.println("Element nije naden!");
                        }
                        break;

                    case "POMOC"://ispis komandi
                        System.out.println(popisKomandi());
                        break;

                    default:
                        System.out.println("Nepoznata naredba!");
                        break;
                }
                System.out.println("");
                System.out.println("\n" + "Odaberite komandu: ");
                odabir = br.readLine().toUpperCase();
            }
        } catch (IOException ex) {
            Logger.getLogger(Jelvalcic_zadaca_1.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
