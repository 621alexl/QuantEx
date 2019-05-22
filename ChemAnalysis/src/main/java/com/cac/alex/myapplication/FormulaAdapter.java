package com.cac.alex.myapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Author: Alex Li
 * FormulaAdapter displays Formula objects in ListView
 * Also sets onClick events for Formula titles
 */
public class FormulaAdapter extends ArrayAdapter<Formula> {

    private Context mContext;
    private int mResource;
    private int lastPosition = -1;

    /**
     * Holds variables in a View
     */
    private static class ViewHolder {
        TextView title;
        WebView  formulaView;
    }


    public FormulaAdapter(Context context, int resource, ArrayList<Formula> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Formula  formula = getItem(position);



        //Create the person object with the information
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);

            //Initialize the TextView and WebView views for the ListView row
            viewHolder.title= (TextView) convertView.findViewById(R.id.formulaTitle);
            viewHolder.formulaView = (WebView) convertView.findViewById(R.id.formulaView);

            //Sets the webview to the html from the Formula object
            viewHolder.title.setText(formula.getTitle());
            WebSettings webSettings = viewHolder.formulaView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }



        viewHolder.formulaView.loadDataWithBaseURL("file:///android_asset/", formula.getHTML(),
                "text/html", "UTF-8", null);

        int id = Formula.formulaIDs().get(formula.getTitle());

        viewHolder.formulaView.setId(id);
        viewHolder.title.setText(formula.getTitle());

        /*
        Sets an onClick event to the Formula's title, which creates an
        alert dialog that lets user input values into an equation
        Layout File: field_input_layout
         */
        viewHolder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                //Inflates dialog to View, which is passed to AlertDialog
                LayoutInflater li = LayoutInflater.from(getContext());
                View promptsView = li.inflate(R.layout.field_input_layout, null);
                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);
                final AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                //Just a classic cancel btn, exits from alert
                Button cancelbtn = (Button) promptsView.findViewById(R.id.cancelbtn);
                cancelbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.cancel();
                    }
                });



                //equationInputLV uses a custom adapter to create rows of input fields
                final ListView equationInputLV = (ListView) promptsView.findViewById(R.id.equationInputLV);
                EquationInputListAdapter  adapter =
                        new EquationInputListAdapter(getContext(),
                                R.layout.field_view_layout,
                                EquationInput.pickInputs(viewHolder.title.getText().toString()));
                equationInputLV.setAdapter(adapter);


                Button analyzebtn = (Button) promptsView.findViewById(R.id.analyzeBtn);

                /*analyzebtn searches the input fields created with EquationInput.pickInputs and
                returns the unknown value, if there is no unknown value it doesn't calculate.
                This method is incredibly poorly written. I couldn't find a way to pair specific
                equations with their correct method without a switch case
                 */
                analyzebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Answers must come in as arrays because some equations return 2+ answers
                        ArrayList<Double> answers = new ArrayList<>();
                        ArrayList<String> roundedAnswers = new ArrayList<>();

                        //checkUnknowns searches the ListView to make sure one value is set to unknown
                        if (EquationInput.checkUnknowns(equationInputLV)) {

                            switch (viewHolder.title.getText().toString()) {
                                case ("1st Kinematic Equation"):
                                    answers = Kin1.kin1arrays(EquationInput.searchInputs(equationInputLV),
                                            EquationInput.searchUnits(equationInputLV));

                                    break;

                                case ("2nd Kinematic Equation"):
                                    answers = Kin2.kin2arrays(EquationInput.searchInputs(equationInputLV),
                                            EquationInput.searchUnits(equationInputLV));
                                    break;
                                case ("3rd Kinematic Equation"):
                                    answers = Kin3.kin3arrays(EquationInput.searchInputs(equationInputLV),
                                            EquationInput.searchUnits(equationInputLV));
                                    break;

                                case ("Einstein's Equation"):
                                    answers.add(Qmath.einsteinArrays(EquationInput.searchInputs(equationInputLV),
                                            EquationInput.searchUnits(equationInputLV)));
                                    break;


                                case ("Rydberg Equation"):
                                    answers.add(Qmath.rydbergArrays(EquationInput.searchInputs(equationInputLV),
                                            EquationInput.searchUnits(equationInputLV)));
                                    break;

                                case ("Coulomb's  Law"):
                                    Log.v("Alex", "wWHY THE FUCK IS THSI Ba");

                                    answers.add(Qmath.coulombArrays(EquationInput.searchInputs(equationInputLV),
                                            EquationInput.searchUnits(equationInputLV)));
                                    break;

                                default:
                                    Log.v("Alex Yikes", viewHolder.title.getText().toString());
                            }
                            //Rounds answers
                            DecimalFormat df = new DecimalFormat("#.####");
                            df.setRoundingMode(RoundingMode.CEILING);
                            for (Number n : answers) {
                                Double d = n.doubleValue();
                                roundedAnswers.add(df.format(d));
                            }
                            EquationInput.outputEditText(equationInputLV, EquationInput.answersToString(roundedAnswers));
                        }
                        else {
                            Toast.makeText(getContext(), "Only 1 unknown and no empty fields!", Toast.LENGTH_SHORT).show();
                        }


                    }
                });

            }
        });



        return convertView;

    }
}
