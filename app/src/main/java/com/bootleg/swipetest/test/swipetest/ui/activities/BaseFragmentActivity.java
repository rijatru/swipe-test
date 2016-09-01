package com.bootleg.swipetest.test.swipetest.ui.activities;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.bootleg.swipetest.test.swipetest.interfaces.FragmentListener;
import com.bootleg.swipetest.test.swipetest.interfaces.FragmentView;

import java.util.ArrayList;

public class BaseFragmentActivity extends AppCompatActivity implements FragmentListener {

    private ArrayList<FragmentView> pendingForClose = new ArrayList<>();

    public ArrayList<FragmentView> getPendingForOpen() {
        return pendingForOpen;
    }

    private ArrayList<FragmentView> pendingForOpen = new ArrayList<>();

    public FragmentManager getBaseFragmentManager() {
        return fragmentManager;
    }

    private FragmentManager fragmentManager;

    private ProgressDialog progressDialog;

    private boolean pause = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initFonts();
        setBackgroundActivity();

        fragmentManager = getSupportFragmentManager();

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
    }

    private void initFonts() {
       /* FontCache.add(FontCache.DIGITAL, "fonts/01 Digit.ttf", getApplicationContext());
        FontCache.add(FontCache.MULI_ITALIC, "fonts/Muli italic.ttf", getApplicationContext());
        FontCache.add(FontCache.MULI_REGULAR, "fonts/Muli regular.ttf", getApplicationContext());
        FontCache.add(FontCache.MULI_300, "fonts/Muli 300.ttf", getApplicationContext());
        FontCache.add(FontCache.MULI_300_ITALIC, "fonts/Muli 300italic.ttf", getApplicationContext());
        FontCache.add(FontCache.RUBIK_ITALIC, "fonts/Rubik italic.ttf", getApplicationContext());
        FontCache.add(FontCache.RUBIK_REGULAR, "fonts/Rubik regular.ttf", getApplicationContext());
        FontCache.add(FontCache.RUBIK_300_ITALIC, "fonts/Rubik 300italic.ttf", getApplicationContext());
        FontCache.add(FontCache.RUBIK_500, "fonts/Rubik 500.ttf", getApplicationContext());
        FontCache.add(FontCache.RUBIK_500_ITALIC, "fonts/Rubik 500italic.ttf", getApplicationContext());
        FontCache.add(FontCache.RUBIK_700, "fonts/Rubik 700.ttf", getApplicationContext());
        FontCache.add(FontCache.RUBIK_700_ITALIC, "fonts/Rubik 700italic.ttf", getApplicationContext());
        FontCache.add(FontCache.RUBIK_900, "fonts/Rubik 900.ttf", getApplicationContext());
        FontCache.add(FontCache.RUBIK_900_ITALIC, "fonts/Rubik 900italic.ttf", getApplicationContext());*/
    }

    protected void setBackgroundActivity() {
        getWindow().setBackgroundDrawable(null);
    }

    public void showProgressDialog(String message) {
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    public void dismissProgressDialog() {
        if (isProgressVisible()) {
            progressDialog.dismiss();
        }
    }

    protected boolean isProgressVisible() {
        return progressDialog != null && progressDialog.isShowing();
    }

    public boolean isPause() {
        return pause;
    }

    protected void replaceFragment(FragmentView fragmentView) {

        if (isPause()) {

            pendingForOpen.add(fragmentView);

        } else {

            fragmentView.setListener(this);

            FragmentTransaction ft = fragmentManager.beginTransaction();

            if (fragmentView.isAddOnStack()) {
                ft.addToBackStack(fragmentView.getName());
            }
            if (fragmentView.isAnimate()) {
                ft.setCustomAnimations(fragmentView.getEnter(), fragmentView.getExit(), fragmentView.getPopEnter(), fragmentView.getPopExit());
            }

            ft.replace(fragmentView.getContainer(), fragmentView, fragmentView.getName());

            ft.commit();
        }
    }

    protected void addFragment(FragmentView fragmentView) {

        try {

            if (isPause()) {

                pendingForOpen.add(fragmentView);

            } else {

                fragmentView.setListener(this);

                FragmentTransaction ft = fragmentManager.beginTransaction();

                if (fragmentManager.getBackStackEntryCount() >= 0) {

                    ft.setCustomAnimations(fragmentView.getEnter(), fragmentView.getExit(), fragmentView.getPopEnter(), fragmentView.getPopExit());
                }

                ft.add(fragmentView.getContainer(), fragmentView, fragmentView.getName());

                ft.addToBackStack(fragmentView.getName());

                ft.commit();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public FragmentView getActiveFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            return null;
        }
        String tag = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
        return (FragmentView) getSupportFragmentManager().findFragmentByTag(tag);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
    }

    @Override
    protected void onPause() {
        super.onPause();
        pause = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        pause = false;
        checkPendingView();
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public void onClose(FragmentView view) {
        if (!isPause()) {
            if (fragmentManager.getBackStackEntryCount() < 1) {
                finish();
            }
            fragmentManager.popBackStack(view.getName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } else {
            pendingForClose.add(view);
        }
    }

    @Override
    public void onBackPressed() {

        int countF = fragmentManager.getBackStackEntryCount();
        if (countF != 0) {
            String fragmentTag = fragmentManager.getBackStackEntryAt(countF - 1).getName();

            Fragment fragment = getSupportFragmentManager().findFragmentByTag(fragmentTag);

            if (fragment instanceof FragmentView) {
                FragmentView currentFragment = (FragmentView) fragment;
                if (currentFragment.canBack()) {
                    super.onBackPressed();
                } else {
                    currentFragment.backPressed();
                }
            }

        } else {

            onBackPressFromEmptyStack();
        }
    }

    public void onBackPressFromEmptyStack() {
        super.onBackPressed();
    }

    private void checkPendingView() {

        for (FragmentView fragmentView : pendingForOpen) {
            addFragment(fragmentView.setEnter(0).setPopEnter(0));
        }

        pendingForOpen.clear();

        pendingForOpen = new ArrayList<>();

        for (FragmentView fragmentView : pendingForClose) {
            onClose(fragmentView);
        }

        pendingForClose.clear();

        pendingForClose = new ArrayList<>();
    }
}
