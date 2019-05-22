package com.cac.alex.myapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Author: Alex Li
 * Custom adapter used to display Property objects in a list view
 */

public class PropertyListAdapter extends ArrayAdapter<Property> {
    private Context mContext;
    private int mResource;
    private int lastPosition = -1;

    /**
     * Holds variables in a View
     */
    private static class ViewHolder {
        TextView name;
        TextView result;
    }

    /**
     * Default constructor for the PropertyListAdapter
     * @param context
     * @param resource
     * @param objects
     */
    public PropertyListAdapter(Context context, int resource, ArrayList<Property> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get the property  information
        String name = getItem(position).getName();
        String result = getItem(position).getResult();


        //Create the  object with the information
        Property property = new Property(name,result);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);
        TextView nameTV = (TextView) convertView.findViewById(R.id.nameTV);


        TextView resultTV = (TextView) convertView.findViewById(R.id.resultTV);
        nameTV.setText(property.getName());
        resultTV.setText(property.getResult());


        return convertView;
    }
}
