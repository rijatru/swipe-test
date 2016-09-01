package com.bootleg.swipetest.test.swipetest.interfaces;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;

import com.bootleg.swipetest.test.swipetest.R;
import com.grability.base.ui.factories.SnackBarFactory;

public abstract class FragmentView extends Fragment {

    protected FragmentListener fragmentListener;
    private int enter = R.anim.slide_in_right;
    private int exit = R.anim.slide_out_right;
    private int popEnter = R.anim.slide_in_right;
    private int popExit = R.anim.slide_out_right;
    private boolean addOnStack = false;
    private boolean animate = false;
    private int container = R.id.container;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
    }

    public void setListener(FragmentListener fragmentListener) {
        this.fragmentListener = fragmentListener;
    }

    protected void showProgressDialog(String message) {
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    protected void dismissProgressDialog() {
        if (isProgressVisible()) {
            progressDialog.dismiss();
        }
    }

    protected void showMessage(String message, View view) {
        SnackBarFactory.getSnackBar(SnackBarFactory.TYPE_ERROR, view, message, Snackbar.LENGTH_LONG).show();
    }


    protected boolean isProgressVisible() {
        return progressDialog != null && progressDialog.isShowing();
    }

    public abstract String getName();

    public boolean canBack() {
        return true;
    }

    public void backPressed() {
    }

    public boolean hideGridButton() {
        return false;
    }

    public boolean hideCheckoutButton() {
        return false;
    }

    public boolean hideBasket() {
        return false;
    }

    public boolean closeMenu() {
        return true;
    }

    public void close() {
        fragmentListener.onClose(this);
    }

    public int getEnter() {
        return enter;
    }

    public FragmentView setEnter(int enter) {
        this.enter = enter;
        return this;
    }

    public int getExit() {
        return exit;
    }

    public FragmentView setExit(int exit) {
        this.exit = exit;
        return this;
    }

    public int getPopEnter() {
        return popEnter;
    }

    public FragmentView setPopEnter(int popEnter) {
        this.popEnter = popEnter;
        return this;
    }

    public int getPopExit() {
        return popExit;
    }

    public FragmentView setPopExit(int popExit) {
        this.popExit = popExit;
        return this;
    }

    public boolean isAddOnStack() {
        return addOnStack;
    }

    public boolean isAnimate() {
        return animate;
    }

    public int getContainer() {
        return container;
    }

    public FragmentView addOnStack() {
        this.addOnStack = true;
        return this;
    }

    public FragmentView animate() {
        this.animate = true;
        return this;
    }

    public FragmentView id(int container) {
        this.container = container;
        return this;
    }
}
