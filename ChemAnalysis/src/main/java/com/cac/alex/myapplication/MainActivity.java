package com.cac.alex.myapplication;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Author: Alex Li
 * Main Activity class, defines the 3 main functions of QuantEx
 * 1. (pubchembtn) Users search the PubChem database using Volley to access its PUG REST API
 * 2. (formulabtn) Allows users to input values into certain equations and find a remaining unknown value
 * 3. (configbtn) Users input element and charge and are given its electron configuration
 */

public class MainActivity extends AppCompatActivity {
    protected Button pubchembtn;
    protected Button formulabtn;
    protected Button configbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //SharedPreferences used to store CID used when searching by formula
        SharedPreferences pref = this.getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        //Declare the three buttons used by this application
        pubchembtn  = (Button) findViewById(R.id.pubchem_btn);
        formulabtn  = (Button) findViewById(R.id.formula_button);
        configbtn  = (Button) findViewById(R.id.configuration_btn);


        /*Creates a RequestQueue for Volley, the service QuantEx uses to send
        http requests to PubChem's PUG REST API*/
        final RequestQueue queue = Volley.newRequestQueue(this);

        /*Creates an alert dialog that allows user to search a specific
        chemical compound/element by name or formula
        Layout: analysis_alert */
        pubchembtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                View promptsView = createDialog(R.layout.analysis_alert, -1);
                final EditText chemInput = (EditText) promptsView.findViewById(R.id.chemInput);
                final Button searchbtn = (Button) promptsView.findViewById(R.id.pubchem_btn);
                final Button formulabtn = (Button) promptsView.findViewById(R.id.formulasearchbtn);

                /*searchbtn searches PubChem  by chemical name (ie the name NaHCO3 is Sodium Bicarbonate)
                and returns chemical properties and PNG of compound's structure using 2 Volley requests
                 */
                searchbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //Scrubs input - replaces spaces, decapitalizes
                        String input = chemInput.getText().toString().toLowerCase().replaceAll("\\s+", "");

                        /*jsonurl = url that retrieves chemical properties in JSON form
                        Specifically Molecular Formula, Molar Mass, and IUPACName*/
                        String jsonurl = "https://pubchem.ncbi.nlm.nih.gov/rest/pug/compound/name/" +
                                input +
                                "/property/MolecularFormula,MolecularWeight,IUPACName/JSON";

                        //pngurl = url that retrieves chemical structure in png form
                        String pngurl = "https://pubchem.ncbi.nlm.nih.gov/rest/pug/compound/name/" +
                                input + "/PNG";


                        //View that holds the alert dialog
                        final View searchview = createDialog(R.layout.search_alert, -1);

                        //Listview holds properties from jsonurl's request
                        final ListView propertyLV = (ListView) searchview.findViewById(R.id.propertyLV);

                        //TextView for name of element/compound
                        TextView compoundLabel = (TextView) searchview.
                                findViewById(R.id.CompoundLabel);

                        //StructureImage is an ImageView for holding pngurl's request
                        final ImageView StructureImage = (ImageView) searchview.findViewById(R.id.StructureImage);

                        //Adds the http request to the queue for JSON object and structure
                        queue.add(generateProperties(jsonurl, propertyLV, compoundLabel));
                        queue.add(generateStructure(pngurl, StructureImage));
                    }
                });


                /* formulabtn searches PubChem by chemical formula (ie the chemical formula of Sodium Bicarbonate
                is NaHCO3) and displays chemical properties and PNG of compound's structure using 3 Volley requests.
                PubChem's PUG REST API doesn't allow you to search directly by formula, but returns
                list of closely matching CID's. To get properties and structure, the 1st Volley requests asks for
                a JSONObject with the closely matching CID's. The first CID listed (the best match) is used to make
                2 Volley requests directly for chemical properties and PNG of compound structure */
                formulabtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String input = chemInput.getText().toString();
                        String url  = "https://pubchem.ncbi.nlm.nih.gov/rest/pug/compound/fastformula/" +
                                input +
                                "/cids/JSON";

                        JsonObjectRequest formulaJSONRequest = new JsonObjectRequest
                                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                                    @Override
                                    public void onResponse(JSONObject formulaJSON) {
                                        try {
                                            JSONArray cidsArray = formulaJSON.getJSONObject("IdentifierList").
                                                    getJSONArray("CID");

                                            String firstCID = Integer.toString(cidsArray.getInt(0));


                                            editor.putString(getString(R.string.first_cid),
                                                    firstCID);
                                            editor.commit();
                                        }
                                        catch (JSONException e) {
                                            Log.v("JSON error", e.toString());
                                        }

                                    }
                                }, new Response.ErrorListener() {

                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        // TODO: Handle error
                                        editor.putString(getString(R.string.first_cid),
                                                "STOP!");
                                        editor.commit();


                                    }
                                });

                        queue.add(formulaJSONRequest);

                        /* When formulaJSONRequest finishes successfully, this listener method creates
                        2 Volley Requests using the best match CID to get properties and structure */
                        RequestQueue.RequestFinishedListener listener =
                                new RequestQueue.RequestFinishedListener()
                                { @Override public void onRequestFinished(Request request)
                                {
                                    if(request.equals(formulaJSONRequest))
                                    {
                                        String firstCID = pref.getString(getString(R.string.first_cid), "");
                                        String cidsPropertyURL = "https://pubchem.ncbi.nlm.nih.gov/rest/pug/compound/cid/" +
                                                firstCID +
                                                "/property/MolecularFormula,MolecularWeight,IUPACName/JSON";

                                        String cidsPNGURL = "https://pubchem.ncbi.nlm.nih.gov/rest/pug/compound/cid/" +
                                                firstCID  + "/PNG";

                                        //View that holds the alert dialog
                                        final View searchview = createDialog(R.layout.search_alert, -1);

                                        //Listview holds properties from jsonurl's request
                                        final ListView propertyLV = (ListView) searchview.findViewById(R.id.propertyLV);

                                        //StructureImage is an ImageView for holding pngurl's request
                                        final ImageView StructureImage = (ImageView) searchview.findViewById(R.id.StructureImage);

                                        TextView compoundLabel = (TextView) searchview.
                                                findViewById(R.id.CompoundLabel);

                                        queue.add(generateProperties(cidsPropertyURL, propertyLV, compoundLabel));
                                        queue.add(generateStructure(cidsPNGURL, StructureImage));

                                    }
                                }
                                };

                        queue.addRequestFinishedListener(listener);

                    }
                });


            }
        });

        /*Creates custom alert dialog which lists equations from the Formula class
        Layout: formula_alert
         */
        formulabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                View equationView = createDialog(R.layout.formula_alert, -1);
                final ListView equationLV = (ListView) equationView.findViewById(R.id.equationLV);

                /*FormulaAdapter is an extension of the ArrayAdapter class, which allows us to pass custom
                views to the ListView equationLV
                */
                FormulaAdapter formulaAdapter = new FormulaAdapter(MainActivity.this, R.layout.equation_adapter,
                        Formula.formulaList);
                equationLV.setAdapter(formulaAdapter);

            }
        });

        /*configbtn creates an alert dialog that allows the user to
        find an element/ion's electron configuration
        Layout: config_layout*/
        configbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View view1  = createDialog(R.layout.config_layout, -1);

                //Declaring the Views that make up the alert dialog
                EditText elementText = (EditText) view1.findViewById(R.id.elementText);
                EditText chargeText  = (EditText) view1.findViewById(R.id.chargeText);
                TextView resultText  = (TextView) view1.findViewById(R.id.answerText);
                Button quantBtn  = (Button) view1.findViewById(R.id.quantbtn);

                quantBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    /*Displays the electron configuration by getting element text  from elementText and
                    and gets the charge from chargeText (which must be converted to integer)
                     */
                    public void onClick(View view) {
                        String element = elementText.getText().toString();
                        int charge = Integer.parseInt(chargeText.getText().toString());

                        /*Class Main is a utility class which holds the static quantumNumbers method
                        which computes quantum numbers (god ben why did you name your class Main)*/
                        resultText.setText(Main.quantumNumbers(element, charge*-1));

                    }
                });


            }
        });








    }

    /*Returns a Volley StringRequest for a JSON Object of chemical properties
    and defines behavior after completing successful request*/
    public StringRequest generateProperties(String url, ListView propertyLV,
                                            TextView compooundLabel) {
        StringRequest propertiesRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ArrayList<Property> propertylist = new ArrayList<Property>();
                        try {
                            //Retrives JSON Object that holds requested chemical properties
                            JSONObject PropertyTable = new JSONObject(response);
                            JSONObject PropObj = PropertyTable.getJSONObject("PropertyTable")
                                    .getJSONArray("Properties").getJSONObject(0);

                            /*Iterates through PropOBJ (which holds chem properties) to create
                            Properties (See Property.java) that populate propertyLV via PropertyListAdapter*/
                            Iterator<String> Names = PropObj.keys();
                            while (Names.hasNext()) {
                                String propertytag = Names.next();
                                //Scrubs input - MolecularWeight becomes Molecular Weight
                                String cleantag = propertytag.
                                        replaceAll("\\d+", "").replaceAll("(.)([A-Z])", "$1 $2");
                                /*CID is always passed in the JSON object, we don't add it to propertyLV
                                and IUPACName is used to set compoundLabel text*/
                                if (!propertytag.equals("CID") && !propertytag.equals("IUPACName")) {
                                    Property newproperty = new Property(cleantag,
                                            PropObj.get(propertytag).toString());
                                    propertylist.add(newproperty);
                                }
                                //Use IUPACName to set compoundLabel text
                                else if (propertytag.equals("IUPACName")) {
                                    Property IUPACProperty = new Property(propertytag,
                                            PropObj.get(propertytag).toString());
                                    propertylist.add(IUPACProperty);
                                    compooundLabel.setText(capitalize(PropObj.get(propertytag).toString()));
                                }


                                /*Gets electron configuration of chemical, if not an element then
                                NullPointerException will be caught and electron configuration will
                                not be added to PropertyList
                                 */
                                if (propertytag.equals("MolecularFormula")) {
                                    String molecularFormula = PropObj.get(propertytag).toString();
                                    try {
                                        int atomic  = PeriodicTable.periodicTable.get(molecularFormula);
                                        String eConfig = Main.quantumNumbers(molecularFormula, 0);
                                        Property eConfigProp = new Property("Electron Configuration", eConfig);
                                        propertylist.add(eConfigProp);
                                    }
                                    catch (NullPointerException e){
                                        Log.v("quantumNumbers", "MolecularFormula wasn't an element");

                                    }

                                }
                            }
                        } catch (JSONException e) {
                            Log.v("Error: ", e.toString());
                        }

                        //Custom adapter to display Properties in the alert dialog's list view
                        PropertyListAdapter adapter =
                                new PropertyListAdapter(MainActivity.this,
                                        R.layout.adapter_view_layout,
                                        propertylist);
                        propertyLV.setAdapter(adapter);


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Request didn't work", Toast.LENGTH_SHORT).show();
            }
        });

        return propertiesRequest;

    }

    //Returns a Volley ImageRequest for the chemical's structure in PNG
    public ImageRequest generateStructure(String pngurl, ImageView StructureImage) {
        ImageRequest structureRequest = new ImageRequest(
                pngurl, // Image URL
                new Response.Listener<Bitmap>() { // Bitmap listener
                    @Override
                    public void onResponse(Bitmap response) {
                        // Do something with response
                        StructureImage.setImageBitmap(response);
                    }
                },
                0, // Image width
                0, // Image height
                ImageView.ScaleType.CENTER_CROP, // Image scale type
                Bitmap.Config.RGB_565, //Image decode configuration
                new Response.ErrorListener() { // Error listener
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Do something with error response
                        Toast.makeText(MainActivity.this,
                                "Chemical Structure not found", Toast.LENGTH_SHORT).show();
                    }
                });

        return structureRequest;
    }

    /*Method creates alertDialog with a given layout and defines the onClick behavior
    of the cancel button if the alert has one */
    public View createDialog(int layout, int cancelid) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                MainActivity.this);
        //Inflates dialog to View, which is passed to AlertDialog
        LayoutInflater li = LayoutInflater.from(MainActivity.this);
        View promptsView = li.inflate(layout, null);
        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        /*-1 is not a valid id of a btn, so if cancelid != -1, then the btn is initialized
        and it's onclick is defined
         */
        if (cancelid != -1) {

            Button cancelbtn = (Button) promptsView.findViewById(cancelid);
            cancelbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.cancel();
                }
            });
        }
        return promptsView;
    }

    /*This method capitalizes the first letter of a string and every letter following
    a space (eg sodium bicarbonate turns into Sodium Bicarbonate)*/
    public String capitalize(String input) {
        char[] chars = input.toCharArray();

        // all ways make first char a cap
        chars[0] = Character.toUpperCase(chars[0]);

        // then capitalize if space on left.
        for(int x=1; x<chars.length; x++) {
            if(chars[x-1] == ' '){
                chars[x] = Character.toUpperCase(chars[x]);
            }
        }

        return new String(chars);
    }















}
