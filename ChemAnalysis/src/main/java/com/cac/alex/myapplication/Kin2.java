package com.cac.alex.myapplication;

import java.util.*;

/**
 * Author: Purab Angreji
 * Exact same purpose as Kin1 but for the second Kinematic equation
 */

public class Kin2 {

  public static ArrayList<String> kin2vars() {
    ArrayList<String> kin2vars = new ArrayList<String>();
    kin2vars.add("Acceleration");
    kin2vars.add("Initial Velocity");
    kin2vars.add("Final Velocity");
    kin2vars.add("Time"); 

    return kin2vars;
  }
    public static ArrayList<String> kin2units() {
        ArrayList<String> kin2units = new ArrayList<String>();
        kin2units.add("Acceleration");
        kin2units.add("Velocity");
        kin2units.add("Velocity");
        kin2units.add("Time");

        return kin2units;
    }
  
  

  public static ArrayList<Double> kin2 (double a, String aunits, 
  double Vi, String Viunits,
  double Vf, String Vfunits,
  double t, String tunits) {
    ArrayList<Double> ans = new ArrayList<Double>();
    ans.clear();

    if (aunits.equals("unknown")) {
      ans.add(((Vf-Vi) / t));
      return ans;
    }

    else if (Viunits.equals("unknown")) {
      ans.add((Vf - a*t));
      return ans;
    }

    else if (Vfunits.equals("unknown")) {
      ans.add((a*t + Vi));
      return ans;

    }
    else if (tunits.equals("unknown")) {
      ans.add(((Vf-Vi) / a));
      return ans;
    }

    else {
      System.out.println("No unknowns!");
      return ans;
    }
  }

  public static ArrayList<Double> kin2arrays(ArrayList<Double> values, ArrayList<String> units) {
      return kin2(values.get(0), units.get(0),
              values.get(1), units.get(1),
              values.get(2), units.get(2),
              values.get(3), units.get(3));

  }
}
