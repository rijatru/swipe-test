package com.bootleg.swipeview.sample.ui.viewmodels;

import android.databinding.ObservableField;

import com.bootleg.swipeview.lib.interfaces.GenericObject;
import com.bootleg.swipeview.sample.models.DataObject;

public class DataViewViewModel {

    public ObservableField<String> text = new ObservableField<>();

    public DataViewViewModel(GenericObject item) {

        this.text.set(((DataObject) item).getStringNumber());
    }
}
