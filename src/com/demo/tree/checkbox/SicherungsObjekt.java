package com.demo.tree.checkbox;


import java.util.ArrayList;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Peter Riehm
 */
public class SicherungsObjekt {
    
    private ArrayList<String> quellpfade;
    private String zielpfad;
    private int kompression;
    private boolean checksumme;
    
    public SicherungsObjekt()
    {
        quellpfade = new ArrayList<>();
        zielpfad = "";
        kompression = 0;
        checksumme = false;
    }
    
    public ArrayList<String> getQuellpfade() {
        return quellpfade;
    }

    public String getZielpfad() {
        return zielpfad;
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

    public void setKompression(int kompression) {
        this.kompression = kompression;
    }

    public void setChecksumme(boolean checksumme) {
        this.checksumme = checksumme;
    }
}
