package com.bootleg.swipeview.sample.models;

import com.bootleg.swipeview.interfaces.GenericObject;

public class DataObject implements GenericObject {

    private int number;

    public DataObject(int number) {

        this.number = number;
    }

    public String getStringNumber() {
        return String.valueOf(this.number);
    }
}
