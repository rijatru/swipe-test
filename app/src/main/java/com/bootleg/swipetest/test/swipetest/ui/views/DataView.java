package com.bootleg.swipetest.test.swipetest.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.bootleg.swipetest.test.swipetest.databinding.ViewDataBinding;
import com.bootleg.swipetest.test.swipetest.ui.viewmodels.DataViewViewModel;
import com.grability.base.interfaces.GenericItem;
import com.grability.base.interfaces.GenericItemView;

public class DataView extends FrameLayout implements GenericItemView {

    private ViewDataBinding binding;

    private DataViewViewModel viewModel;

    private Context context;

    public DataView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public DataView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initAttrs(attrs);
        init();
    }

    public DataView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initAttrs(attrs);
        init();
    }

    private void initAttrs(AttributeSet attrs) {

    }

    private void init() {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        binding = ViewDataBinding.inflate(inflater, this, true);
    }

    @Override
    public void bind(GenericItem item) {

    }

    public void setViewModel(DataViewViewModel viewModel) {

        this.viewModel = viewModel;

        binding.setViewModel(viewModel);
    }
}
