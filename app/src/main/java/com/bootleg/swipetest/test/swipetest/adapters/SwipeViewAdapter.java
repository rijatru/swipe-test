package com.bootleg.swipetest.test.swipetest.adapters;

import com.bootleg.swipetest.test.swipetest.factories.AdapterFactory;
import com.grability.base.interfaces.GenericItem;
import com.grability.base.interfaces.GenericItemView;

import java.util.ArrayList;

public class SwipeViewAdapter {

    protected AdapterFactory factory;

    private ArrayList<GenericItem> items = new ArrayList<>();

    public SwipeViewAdapter(AdapterFactory factory) {
        this.factory = factory;
    }

    public GenericItemView getView(int index) {

        return this.factory.onCreateView(items.get(index));
    }

    public void setItems(ArrayList<GenericItem> items) {
        this.items.addAll(items);
    }

    public GenericItem getItem(int position) {
        return this.items.get(0);
    }

    public int getNumberItems() {
        return this.items.size();
    }
}
