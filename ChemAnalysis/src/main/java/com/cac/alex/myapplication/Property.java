package com.cac.alex.myapplication;

/**
 * Author: Alex Li
 * Property class used for the PropertyListAdapter to display chemical properties in a ListView
 */

public class Property {
    private String name;
    private String result;

    public Property(String name, String result) {
        this.name = name;
        this.result = result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

}
