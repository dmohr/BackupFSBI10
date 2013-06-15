package com.demo.tree.checkbox;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Peter Riehm
 */
public class SicherungsObjekt {
    
    private ArrayList<String> quellpfade;   // Array der zu sichernden Ordner
    private String zielpfad;                // Zielpfad, wo hingesichert werden soll
    private String zieldatei;               // Präfix der Sicherungsdatei
    private int kompression;                // Kompressionsstärke der Sicherungsdatei
    private boolean checksumme;             // Checksumme für spätere Verwendung
    
    public SicherungsObjekt() throws FileNotFoundException, IOException
    {
        quellpfade = new ArrayList<>();
        zielpfad = "";
        zieldatei = "";
        kompression = 0;
        checksumme = false;
        // Voreinstellungen aus der Settings-Datei lesen
        LeseVoreinstellungen();
    }
    // Getter und Setter
    public ArrayList<String> getQuellpfade() {
        return quellpfade;
    }

    public String getZielpfad() {
        return zielpfad;
    }
    
    public String getZieldatei() {
        return zieldatei;
    }

    public int getKompression() {
        return kompression;
    }

    public boolean isChecksumme() {
        return checksumme;
    }

    public void setQuellpfade(ArrayList<String> quellpfade) {
        this.quellpfade = quellpfade;
    }

    public void setZielpfad(String zielpfad) {
        this.zielpfad = zielpfad;
    }
    
    public void setZieldatei(String zieldatei) {
        this.zieldatei = zieldatei;
    }

    public void setKompression(int kompression) {
        this.kompression = kompression;
    }

    public void setChecksumme(boolean checksumme) {
        this.checksumme = checksumme;
    }
    // Voreinstellungen aus der Settingsdatei lesen (von Dirk Mohr)
    protected void LeseVoreinstellungen() throws FileNotFoundException, IOException
    {
        File file = new File(System.getProperty("user.dir")+ "\\settings.txt");
        
        if (file.exists())
        {
           
           FileReader fr;
            fr = new FileReader(System.getProperty("user.dir")+ "\\settings.txt");
            BufferedReader br = new BufferedReader(fr);

            setKompression(Integer.parseInt(br.readLine()));
            setZieldatei(br.readLine());
            setZielpfad(br.readLine());
            setChecksumme(Boolean.parseBoolean(br.readLine()));
        }

    }
}
