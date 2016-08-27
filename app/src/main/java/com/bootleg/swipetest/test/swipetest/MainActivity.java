package com.bootleg.swipetest.test.swipetest;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.bootleg.swipetest.test.swipetest.ui.views.activities.BaseFragmentActivity;
import com.bootleg.swipetest.test.swipetest.ui.views.fragments.SwipeViewFragment;

public class MainActivity extends BaseFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_base);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        addFragment(new SwipeViewFragment());
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
