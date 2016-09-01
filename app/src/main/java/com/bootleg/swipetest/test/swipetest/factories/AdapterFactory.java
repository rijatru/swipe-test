package com.bootleg.swipetest.test.swipetest.factories;

import com.bootleg.swipetest.test.swipetest.interfaces.GenericObject;
import com.bootleg.swipetest.test.swipetest.interfaces.ItemView;

public abstract class AdapterFactory {

    public abstract ItemView onCreateView(GenericObject item);
}
