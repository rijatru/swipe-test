package com.bootleg.swipetest.test.swipetest.adapters;

import com.bootleg.swipetest.test.swipetest.factories.AdapterFactory;
import com.bootleg.swipetest.test.swipetest.interfaces.GenericObject;
import com.bootleg.swipetest.test.swipetest.interfaces.ItemView;

import java.util.ArrayList;

public class SwipeViewAdapter {

    protected AdapterFactory factory;

    private ArrayList<GenericObject> items = new ArrayList<>();

    public SwipeViewAdapter(AdapterFactory factory) {
        this.factory = factory;
    }

    public ItemView getView(int index) {

        return this.factory.onCreateView(items.get(index));
    }

    public void setItems(ArrayList<GenericObject> items) {
        this.items.addAll(items);
    }

    public GenericObject getItem(int position) {
        return this.items.get(0);
    }

    public int getNumberItems() {
        return this.items.size();
    }
}
