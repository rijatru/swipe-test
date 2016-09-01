package com.bootleg.swipetest.test.swipetest.model;

import com.bootleg.swipetest.test.swipetest.interfaces.GenericObject;

public class DataObject implements GenericObject {

    private int number;

    public DataObject(int number) {

        this.number = number;
    }

    public String getStringNumber() {
        return String.valueOf(this.number);
    }

    @Override
    public int getType() {
        return 0;
    }
}
