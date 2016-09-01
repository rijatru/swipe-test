package com.bootleg.swipetest.test.swipetest.ui.viewmodels;

import android.databinding.ObservableField;

import com.bootleg.swipetest.test.swipetest.model.DataObject;
import com.grability.base.interfaces.GenericItem;

public class DataViewViewModel {

    public ObservableField<String> text = new ObservableField<>();

    public DataViewViewModel(GenericItem item) {

        this.text.set(((DataObject) item).getStringNumber());
    }
}
