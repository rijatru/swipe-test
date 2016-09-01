package com.bootleg.swipeview.factories;

import com.bootleg.swipeview.interfaces.GenericObject;
import com.bootleg.swipeview.interfaces.ItemView;

public abstract class AdapterFactory {

    public abstract ItemView onCreateView(GenericObject item);
}
