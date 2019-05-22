package com.cac.alex.myapplication;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Author: Alex Li
 * Creates a formula object, which consists of a title (eg "1st Kinematic Equation") and
 * html text which, with the  mathscribe library, creates formatted math text.
 * Used in conjuction with FormulaAdapter to display formulas in list views
*/
public class Formula {
    private String title;
    private String html;

    //public static String mathscribePath = ;
    private  Formula(String title, String html) {
        this.title = title;
        this.html = html;
    }

    //The necessary boiler plate text required to create properly formatted html for MathScribe
    private  static String jqBoiler = "<html><head>"
            + "<link rel='stylesheet' href='"+"file:///android_asset/mathscribe/"+"jqmath-0.4.3.css'>"
            + "<script src='"+"file:///android_asset/mathscribe/"+"jquery-1.4.3.min.js'></script>"
            + "<script src='"+"file:///android_asset/mathscribe/"+"jqmath-etc-0.4.6.min.js'></script>"
            + "</head><body>";

    //html text for each equation
    private  static String firstKinematic = "$$x_t=1/2at^2+v_0+x_0$$</body>";
    private static String secondKinematic = "$$v = v_0 + at$$</body>";
    private  static String thirdKinematic = "$$v^2=v_0^2+2a(x_f-x_i)$$</body>";
    private  static String einsteinEquation = "$$E=mc^2$$</body>";
    private  static String rydbergEquation = "$$1/λ=R_H(1/n^2_1-1/n^2_2)$$</body>";
    private  static String coulombEquation = "$$F_e = K_e{|q_1q_2|}/r^2$$</body>";




    public static ArrayList<Formula> formulaList = getFormulas();

    //In order to initialize formulaList with the proper formula objects
    private  static ArrayList<Formula> getFormulas() {
        ArrayList<Formula> formulaList = new ArrayList<>();
        formulaList.add(new Formula("1st Kinematic Equation", jqBoiler + firstKinematic));
        formulaList.add(new Formula("2nd Kinematic Equation", jqBoiler + secondKinematic));
        formulaList.add(new Formula("3rd Kinematic Equation", jqBoiler + thirdKinematic));
        formulaList.add(new Formula("Einstein's Equation", jqBoiler + einsteinEquation));
        formulaList.add(new Formula("Rydberg Equation", jqBoiler + rydbergEquation));
        formulaList.add(new Formula("Coulomb's  Law", jqBoiler + coulombEquation));

        return  formulaList;

    }


    public static HashMap<String, Integer> formulaIDs() {
        HashMap<String, Integer> formulaIDs = new HashMap<>();
        formulaIDs.put("1st Kinematic Equation", R.id.first_kinematic_id);
        formulaIDs.put("2nd Kinematic Equation", R.id.second_kinematic_id);
        formulaIDs.put("3rd Kinematic Equation", R.id.third_kinematic_id);
        formulaIDs.put("Einstein's Equation", R.id.einstein_id);
        formulaIDs.put("Rydberg Equation", R.id.rydberg_id);
        formulaIDs.put("Coulomb's  Law", R.id.coulomb_id);
        return  formulaIDs;
    }





    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHTML() {
        return html;
    }

    public void setHTML(String html) {
        this.html = html;
    }
}
