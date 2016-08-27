package com.bootleg.swipetest.test.swipetest.ui.views.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bootleg.swipetest.test.swipetest.R;
import com.bootleg.swipetest.test.swipetest.databinding.FragmentSwipeViewFragmentBinding;
import com.bootleg.swipetest.test.swipetest.interfaces.FragmentView;
import com.bootleg.swipetest.test.swipetest.ui.views.viewmodels.SwipeViewFragmentViewModel;

public class SwipeViewFragment extends FragmentView {

    private FragmentSwipeViewFragmentBinding binding;
    private SwipeViewFragmentViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_swipe_view_fragment, container, false);

        viewModel = new SwipeViewFragmentViewModel();

        binding.setViewModel(viewModel);

        return binding.getRoot();
    }

    @Override
    public boolean canBack() {
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public String getName() {
        return SwipeViewFragment.class.getName();
    }
}
