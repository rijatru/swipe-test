package com.bootleg.swipetest.test.swipetest.model;

import com.grability.base.interfaces.GenericItem;

public class DataObject implements GenericItem{

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
