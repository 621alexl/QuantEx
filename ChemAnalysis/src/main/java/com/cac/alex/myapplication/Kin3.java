package com.cac.alex.myapplication;

import java.util.*;

/**
 * Author: Purab Angreji
 * Exact same purpose as Kin1 but for the third Kinematic equation
 */

public class Kin3 {

  public static ArrayList<String> kin3vars() {
    ArrayList<String> kin3vars = new ArrayList<String>();
    kin3vars.add("Acceleration");
    kin3vars.add("Initial Velocity");
    kin3vars.add("Final Velocity");
    kin3vars.add("Initial Position");
    kin3vars.add("Final Position");

    return kin3vars;
  }

  public static ArrayList<String> kin3units() {
    ArrayList<String> kin3vars = new ArrayList<String>();
    kin3vars.add("Acceleration");
    kin3vars.add("Velocity");
    kin3vars.add("Velocity");
    kin3vars.add("Distance");
    kin3vars.add("Distance");

    return kin3vars;
  }

  public static ArrayList<Double> kin3 (double a, String aunits, 
  double Vi, String Viunits,
  double Vf, String Vfunits,
  double Xi, String Xiunits,
  double Xf, String Xfunits) {
    ArrayList<Double> ans = new ArrayList<Double>();
    ans.clear();

    if (aunits.equals("unknown")) {
      ans.add(((Vf*Vf)-(Vi*Vi))/(2*(Xf-Xi)));
      return ans;
    }
    else if (Viunits.equals("unknown")) {
      ans.add(-(Math.sqrt((Vf*Vf)-(2*a*(Xf-Xi)))));
      ans.add((Math.sqrt((Vf*Vf)-(2*a*(Xf-Xi)))));
      return ans;
    }
    else if (Vfunits.equals("unknown")) {
      ans.add(-(Math.sqrt((Vi*Vi)+(2*a*(Xf-Xi)))));
      ans.add((Math.sqrt((Vi*Vi)+(2*a*(Xf-Xi)))));
      return ans;
    }
    else if (Xiunits.equals("unknown")) {
      ans.add((Xf-(((Vf*Vf)-(Vi*Vi))/(2*a))));
      return ans;
    }
    else if (Xfunits.equals("unknown")) {
      ans.add(((((Vf*Vf)-(Vi*Vi))/(2*a))+Xi));
      return ans;
    }
    else {
      System.out.println("No unknowns!");
      return ans;
    }
  }

  public static ArrayList<Double> kin3arrays(ArrayList<Double> values, ArrayList<String> units) {
    return kin3(values.get(0), units.get(0),
            values.get(1), units.get(1),
            values.get(2), units.get(2),
            values.get(3), units.get(3),
            values.get(4), units.get(4));

  }
}