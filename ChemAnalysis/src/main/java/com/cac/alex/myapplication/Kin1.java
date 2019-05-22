package com.cac.alex.myapplication;

import java.util.*;

/**
 *Author: Purab Angreji
 * Utility class (wow sure are a lot of those in this app right?) used to calculate
 * 1st Kinematic equation.
 * Not commented by author
 */
public class Kin1 {

    //Array of variables the equation uses to populate the input fields of EquationInputListAdapter
  public static ArrayList<String> kin1vars() {
    ArrayList<String> kin1vars = new ArrayList<String>();
    kin1vars.add("Acceleration");
    kin1vars.add("Initial Velocity");
    kin1vars.add("Time"); 
    kin1vars.add("Initial Position");
    kin1vars.add("Final Position");

    return kin1vars;
  }

  public static ArrayList<String> kin1units() {
    ArrayList<String> kin1units = new ArrayList<String>();
    kin1units.add("Acceleration");
    kin1units.add("Velocity");
    kin1units.add("Time");
    kin1units.add("Distance");
    kin1units.add("Distance");

    return kin1units;
  }

  public static ArrayList<Double> kin1 (double a, String aunits, 
  double Vi, String Viunits,
  double t, String tunits,
  double Xi, String Xiunits,
  double Xf, String Xfunits) {
    ArrayList<Double> ans = new ArrayList<Double>();
    ans.clear();

    if (aunits.equals("unknown")) {
      ans.add((Xf-(Vi*t)-Xi)/(.5*t*t));
      return ans;
    }
    else if (Viunits.equals("unknown")) {
      ans.add((Xf-(.5*a*t*t)-Xi)/t);
      return ans;
    }
    else if (tunits.equals("unknown")) {
      ans.add((-Vi - Math.sqrt((Vi*Vi)-(2*a*(Xi-Xf))))/(a));
      ans.add((-Vi + Math.sqrt((Vi*Vi)-(2*a*(Xi-Xf))))/(a));
      return ans;
    }
    else if (Xiunits.equals("unknown")) {
      ans.add((Xf-(.5*a*t*t)-(Vi*t)));
      return ans; 
    }
    else if (Xfunits.equals("unknown")) {
      ans.add(((.5*a*t*t)+(Vi*t)+Xi));
      return ans; 
    }
    else {
      System.out.println("No unknowns!");
      return ans;
    }
  }

  public static ArrayList<Double>  kin1arrays(ArrayList<Double> values, ArrayList<String> units) {
    return kin1(values.get(0), units.get(0),
            values.get(1), units.get(1),
            values.get(2), units.get(2),
            values.get(3), units.get(3),
            values.get(4), units.get(4));


  }
}