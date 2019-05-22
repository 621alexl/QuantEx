package com.cac.alex.myapplication;

import java.lang.*;
import java.util.ArrayList;

/**
 * Author: Andreas Tsantilas
 * Utility class used to calculate einstein's equation, coulomb's law, and the rydberg equation
 */


public class Qmath{
    //Speed of light in a vacuum (meters/sec)
    public static final double c = 2.99792458e9;

    //Coulomb's constant (Nm^2/C^2)
    public static final double ke = 8.9875517873681764e9;


//Einstein's equation E = mc^2
    public static double einstein(double E, String EUnits, double m, String mUnits){
      

        double result = 0;

        //Energy Solution
        if(EUnits.equals("unknown")){
            result = m * Math.pow(Qmath.c,2);
            return result;
        }

        //mass  Solution
        else if(mUnits.equals("unknown")){
            result = E/(Math.pow(Qmath.c, 2));
            return result;
        }

        return result;
    }

    public static double einsteinArrays(ArrayList<Double>values, ArrayList<String> units) {
        return einstein(values.get(0), units.get(0),
                values.get(1), units.get(1));
    }


//Return names of einstein parameters
    public static ArrayList<String> einsteinNames(){
 
        ArrayList<String> namesE = new ArrayList<String>();
        namesE.add("Energy");
        namesE.add("Mass");

        return namesE;        
    }

//Return Types of einstein parameters
    public static ArrayList<String> einsteinTypes(){
        ArrayList<String> typesE = new ArrayList<String>();
        typesE.add("Energy");
        typesE.add("Mass");

        return typesE;
    }

    //Equation of photon emission, when an electron drops/gains an energy 
    public static Double rydberg(double wvlen, String wvUnits, double ni, String NIunits, double nf, String NFunits){
        final double R = 10973731.6;
        double result = 1.0;

        
        if (wvUnits.equals("unknown")){
            result = Math.pow(ni*nf, 2)/(R*(nf*nf-ni*ni));
            return result;
        }

        else if (NIunits.equals("unknown")){
            result = Math.round(Math.sqrt((wvlen*R*nf*nf)/(wvlen*R + nf*nf)));
            return result;

        }

        else if (NFunits.equals("unknown")){
            result = Math.round(Math.sqrt((wvlen*R*ni*ni)/(wvlen*R - ni*ni)));
            return result;
        }

        else{
            System.out.println("No Unknowns!");
            return -1.0;
        }
    }


    public static double rydbergArrays(ArrayList<Double> values, ArrayList<String> units) {
        return rydberg(values.get(0), units.get(0),
                values.get(1), units.get(1),
                values.get(2), units.get(2));
    }

//Returns names of rydberg parameters
    public static ArrayList<String> rydbergNames(){

        ArrayList<String> namesR = new ArrayList<String>();
        namesR.add("Wavelength");
        namesR.add("Init. Level");
        namesR.add("Final Level");
    
        return namesR;
   
    }

//Returns types of rydberg parameters
    public static ArrayList<String> rydbergTypes(){
        ArrayList<String> typesR = new ArrayList<String>();
        typesR.add("Distance");
        typesR.add("Energy lvl");
        typesR.add("Energy lvl");

        return typesR;

    }

//Coulomb's law, electrical law
    public static double coulomb(double force, String FUnits, double q1, String Q1Units, double q2, String Q2UNits, double r, String RUnits){
        double result = 0;
        
        if (FUnits.equals("unknown")){
            result = ke*(q1*q2)/(r*r);
            return result;
        }

        else if (Q1Units.equals("unknown")){
            result = (force*r*r)/(q2*ke);
            return result;
        }

        else if (Q2UNits.equals("unknonw")){
            result = (force*r*r)/(q1*ke);
            return result;
        }

        else if (RUnits.equals("unknown")){
            result = Math.sqrt((ke*q1*q2)/force);
            return result;
        }
        
        else{
            return -1.0;
        }
    }

    public static double coulombArrays(ArrayList<Double> values, ArrayList<String> units) {
        return coulomb(values.get(0), units.get(0),
                values.get(1), units.get(1),
                values.get(2), units.get(2),
                values.get(3), units.get(3));
    }

//returns names of coulomb parameters

    public static ArrayList<String> coulombNames(){
        ArrayList<String> namesC = new ArrayList<String>();
        namesC.add("Force");
        namesC.add("Charge 1");
        namesC.add("Charge 2");
        namesC.add("Distance");
    
        return namesC;
    }


//returns types of coulomb parameters
    public static ArrayList<String> coulombTypes(){

       ArrayList<String> typesC = new ArrayList<String>();
        typesC.add("Force");
        typesC.add("Charge");
        typesC.add("Charge");
        typesC.add("Distance");

        return typesC;

    }

}