package com.cac.alex.myapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Author: Alex Li
 * Custom adapter to display EquationInputs
 */

public class EquationInputListAdapter extends ArrayAdapter<EquationInput> {

    private Context mContext;
    private int mResource;
    private int lastPosition = -1;

    private static class ViewHolder {
        TextView input;
        Spinner unit;
    }


    public EquationInputListAdapter (Context context, int resource, ArrayList<EquationInput> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        EquationInput equationInput = getItem(position);

        EquationInputListAdapter.ViewHolder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {
            viewHolder = new EquationInputListAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);

            viewHolder.input = (TextView) convertView.findViewById(R.id.inputTV);
            viewHolder.unit = (Spinner) convertView.findViewById(R.id.unitSpinner);
            ArrayList<String> unitList = EquationInput.unitMap.get(equationInput.getUnit());
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),
                    R.layout.spinner_item, unitList);
            viewHolder.unit.setAdapter(dataAdapter);

            viewHolder.input.setHint(equationInput.getInput());

            viewHolder.unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if (position == dataAdapter.getPosition("unknown")) {
                        viewHolder.input.setEnabled(false);
                        viewHolder.input.setText("");
                    }
                    else {
                        viewHolder.input.setEnabled(true);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                    // sometimes you need nothing here
                }
            });

        }
        else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (EquationInputListAdapter.ViewHolder) convertView.getTag();
        }
        return convertView;
    }
}
