package com.bootleg.swipetest.test.swipetest.factories;

import com.grability.base.interfaces.GenericItem;
import com.grability.base.interfaces.GenericItemView;

public abstract class AdapterFactory {

    public abstract GenericItemView onCreateView(GenericItem item);
}
