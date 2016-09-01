package com.bootleg.swipeview.sample.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.bootleg.swipetest.test.swipetest.databinding.ViewDataBinding;
import com.bootleg.swipeview.lib.interfaces.GenericObject;
import com.bootleg.swipeview.lib.interfaces.ItemView;
import com.bootleg.swipeview.sample.ui.viewmodels.DataViewViewModel;

public class DataView extends FrameLayout implements ItemView {

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
    public void bind(GenericObject item) {

        DataViewViewModel viewModel = new DataViewViewModel(item);

        setViewModel(viewModel);
    }

    private void setViewModel(DataViewViewModel viewModel) {

        this.viewModel = viewModel;

        binding.setViewModel(viewModel);
    }
}
