package com.bootleg.swipetest.test.swipetest.ui.viewmodels;

import android.databinding.ObservableField;

import com.bootleg.swipetest.test.swipetest.interfaces.GenericObject;
import com.bootleg.swipetest.test.swipetest.model.DataObject;

public class DataViewViewModel {

    public ObservableField<String> text = new ObservableField<>();

    public DataViewViewModel(GenericObject item) {

        this.text.set(((DataObject) item).getStringNumber());
    }
}
