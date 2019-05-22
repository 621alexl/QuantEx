package com.cac.alex.myapplication;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Author: Alex Li
 * Creates EquationInputs, objects that hold an input (23.2) and a unit (meters per second)
 * EquationInputs are used with EquationInputListAdapter to populate a ListView
 */
public class EquationInput {
    private String input;
    private String unit;

    public  static final HashMap<String, ArrayList<String>> unitMap = createMap();

    //Ran into issues when we attempted to initialize arrays & hashmaps in the values folder
    private  static HashMap<String, ArrayList<String>> createMap()
    {
        HashMap<String, ArrayList<String>> myMap = new HashMap<String,ArrayList<String>>();
        ArrayList<String> energyUnitList = new ArrayList<>();
        energyUnitList.add("Joules");
        energyUnitList.add("Calories");
        energyUnitList.add("Kilojoules");
        energyUnitList.add("Kilocalories");
        energyUnitList.add("unknown");

        myMap.put("Energy",energyUnitList);

        ArrayList<String> accelerationUnitList = new ArrayList<>();
        accelerationUnitList.add("m/s^2");
        accelerationUnitList.add("km/h^2");
        accelerationUnitList.add("mile/hr^2");
        accelerationUnitList.add("unknown");

        myMap.put("Acceleration", accelerationUnitList);

        ArrayList<String> velocityUnitList = new ArrayList<>();
        velocityUnitList.add("m/s");
        velocityUnitList.add("km/h");
        velocityUnitList.add("mile/hr");
        velocityUnitList.add("unknown");

        myMap.put("Velocity", velocityUnitList);

        ArrayList<String> timeUnitList = new ArrayList<>();
        timeUnitList.add("s");
        timeUnitList.add("hr");
        timeUnitList.add("minute");
        timeUnitList.add("unknown");

        myMap.put("Time", timeUnitList);

        ArrayList<String> distanceUnitList = new ArrayList<>();
        distanceUnitList.add("meters");
        distanceUnitList.add("miles");
        distanceUnitList.add("centimeters");
        distanceUnitList.add("fathoms");
        distanceUnitList.add("nanometers");

        distanceUnitList.add("unknown");

        myMap.put("Distance", distanceUnitList);

        ArrayList<String> massUnitList = new ArrayList<>();
        massUnitList.add("grams");
        massUnitList.add("pounds");
        massUnitList.add("metric tonnes");
        massUnitList.add("kilograms");
        massUnitList.add("unknown");

        myMap.put("Mass", massUnitList);

        ArrayList<String> forceUnitList = new ArrayList<>();
        forceUnitList.add("Newtons");
        forceUnitList.add("unknown");


        myMap.put("Force", forceUnitList);

        ArrayList<String> lvlUnitList = new ArrayList<>();
        lvlUnitList.add("Energy Level");
        lvlUnitList.add("unknown");

        myMap.put("Energy lvl", lvlUnitList);

        ArrayList<String> chargeUnitList = new ArrayList<>();
        chargeUnitList.add("Coulombs");
        chargeUnitList.add("unknown");

        myMap.put("Charge", chargeUnitList);


        return myMap;
    }

    public EquationInput(String input, String unit) {
        this.input = input;
        this.unit = unit;
    }

    public String getInput() {
        return input;
    }

    public void  setInput(String input) {
        this.input = input;
    }

    public String getUnit() {
        return unit;

    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public static ArrayList<EquationInput> pickInputs(String equation) {
        ArrayList<EquationInput> inputList = new ArrayList<EquationInput>();

        switch (equation) {
            case "1st Kinematic Equation":
                return makeInputs(Kin1.kin1vars(), Kin1.kin1units());

            case "2nd Kinematic Equation":
                return makeInputs(Kin2.kin2vars(), Kin2.kin2units());

            case "3rd Kinematic Equation":
                return makeInputs(Kin3.kin3vars(), Kin3.kin3units());

            case "Einstein\'s Equation":
                return  makeInputs(Qmath.einsteinNames(), Qmath.einsteinTypes());

            case "Rydberg Equation":
                return makeInputs(Qmath.rydbergNames(), Qmath.rydbergTypes());
            case "Coulomb's  Law":
                return makeInputs(Qmath.coulombNames(), Qmath.coulombTypes());
            default:
                return inputList;


        }

    }
    //Generates the EquationInput objects from ArrayLists of the equation' variables and units
    public static ArrayList<EquationInput> makeInputs(ArrayList<String> variables, ArrayList<String> units) {
        ArrayList<EquationInput> inputList = new ArrayList<EquationInput>();
        for (int i = 0; i < variables.size(); i++) {
            EquationInput field = new EquationInput(variables.get(i), units.get(i));
            inputList.add(field);
        }
        return inputList;
    }

    /*Searches the ListView created with the EquationInputListAdapter  for
    inputs (the numerical values users enter) and returns them */
    public static  ArrayList<Double> searchInputs (ListView equationInputLV) {

        ArrayList<Double> inputs = new ArrayList<>();

        for (int i =0; i < equationInputLV.getChildCount(); i++) {
            View horiz = equationInputLV.getChildAt(i);
            if (horiz instanceof LinearLayout) {
                    View editText = ((LinearLayout) horiz).getChildAt(0);
                    View spinner = ((LinearLayout) horiz).getChildAt(1);
                    if (spinner instanceof Spinner) {
                        if (((Spinner) spinner).getSelectedItem().toString().equals("unknown")) {
                            inputs.add(0.0);

                        }

                        else if (editText instanceof EditText) {
                            inputs.add(Double.parseDouble(((EditText) editText).getText().toString()));

                        }
                    }





            }
        }
        Log.v("Alex LEngth", "HI" + inputs.size());

        return inputs;

    }


    /*Iterates through ListView created with the EquationInputListAdapter to
    to find the unknown value and inserts answer there*/
    public static void outputEditText (ListView equationInputLV, String txt) {


        for (int i =0; i < equationInputLV.getChildCount(); i++) {
            View horiz = equationInputLV.getChildAt(i);
            if (horiz instanceof LinearLayout) {
                View editText = ((LinearLayout) horiz).getChildAt(0);
                View spinner = ((LinearLayout) horiz).getChildAt(1);

                if (editText instanceof EditText) {
                     if (!editText.isEnabled()) {
                         ((EditText) editText).setText(txt);
                         if (spinner instanceof Spinner) {
                             ((Spinner) spinner).setSelection(0);
                         }
                     }

                    }
                }

            }



    }

    //Ensures that 1 value has been set as unknown
    public static  boolean  checkUnknowns (ListView equationInputLV) {
        int counter = 0;
        for (int i =0; i < equationInputLV.getChildCount(); i++) {
            View horiz = equationInputLV.getChildAt(i);
            if (horiz instanceof LinearLayout) {
                View editText = ((LinearLayout) horiz).getChildAt(0);
                View spinner = ((LinearLayout) horiz).getChildAt(1);
                if (spinner instanceof Spinner) {
                    if (((Spinner) spinner).getSelectedItem().toString().equals("unknown")) {
                        counter += 1;
                    }


                }
                if (editText instanceof EditText) {
                    if (editText.isEnabled() && ((EditText) editText).getText().toString().equals("")){
                        counter += 1;
                    }
                }

            }
        }
        if (counter != 1 ) {
            return false;
        }
        else {
            return true;
        }

    }

    //Returns the units the user has selected. Curently QuantEx has no capabilities for unit conversion
    public  static ArrayList<String> searchUnits (ListView equationInputLV) {
        ArrayList<String> selectedUnits = new ArrayList<>();
        for (int i =0; i < equationInputLV.getChildCount(); i++) {
            View horiz = equationInputLV.getChildAt(i);
            if (horiz instanceof LinearLayout) {
                for (int a =0; a <((LinearLayout) horiz).getChildCount(); a++) {
                    View widget = ((LinearLayout) horiz).getChildAt(a);

                    if (widget instanceof Spinner) {
                        selectedUnits.add(((Spinner) widget).getSelectedItem().toString());

                    }
                }

            }
        }
        return selectedUnits;
    }


    public static String answersToString(ArrayList<String> answers) {
        String answerStr = "";
        for (int i = 0; i < answers.size(); i++) {
            answerStr += (answers.get(i)) + ", ";

        }
        return answerStr.substring(0, answerStr.length()-2);
    }



}
