package com.bootleg.swipeview.lib.factories;

import com.bootleg.swipeview.lib.interfaces.GenericObject;
import com.bootleg.swipeview.lib.interfaces.ItemView;

public abstract class AdapterFactory {

    public abstract ItemView onCreateView(GenericObject item);
}
