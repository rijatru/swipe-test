package com.bootleg.swipeview.lib.adapters;

import com.bootleg.swipeview.lib.factories.AdapterFactory;
import com.bootleg.swipeview.lib.interfaces.GenericObject;
import com.bootleg.swipeview.lib.interfaces.ItemView;

import java.util.ArrayList;

public class SwipeViewAdapter {

    protected AdapterFactory factory;

    private int newIndex;
    private ArrayList<GenericObject> items = new ArrayList<>();
    private int currentPosition = 0;

    public SwipeViewAdapter(AdapterFactory factory) {
        this.factory = factory;
    }

    public ItemView getView(int index) {

        return this.factory.onCreateView(items.get(index));
    }

    public ItemView getNextView() {

        newIndex++;

        return this.factory.onCreateView(items.get(newIndex));
    }

    public void setNewIndex(int newIndex) {
        this.newIndex = newIndex;
    }

    public void setItems(ArrayList<GenericObject> items) {
        this.items.addAll(items);
    }

    public boolean hasMoreItems() {

        return newIndex < items.size() - 1;
    }

    public void setCurrentPosition(int newPosition) {
        currentPosition = newPosition;
    }

    public void moveCurrentPosition() {
        this.currentPosition++;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }
}
