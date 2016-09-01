package com.bootleg.swipetest.test.swipetest;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.bootleg.swipetest.test.swipetest.adapters.SwipeViewAdapter;
import com.bootleg.swipetest.test.swipetest.databinding.ActivityMainBinding;
import com.bootleg.swipetest.test.swipetest.factories.AdapterFactory;
import com.bootleg.swipetest.test.swipetest.interfaces.GenericObject;
import com.bootleg.swipetest.test.swipetest.interfaces.ItemView;
import com.bootleg.swipetest.test.swipetest.model.DataObject;
import com.bootleg.swipetest.test.swipetest.ui.activities.BaseFragmentActivity;
import com.bootleg.swipetest.test.swipetest.ui.views.DataView;
import com.bootleg.swipetest.test.swipetest.ui.views.SwipeView;

import java.util.ArrayList;

public class MainActivity extends BaseFragmentActivity {

    private SwipeView.SwipeViewInterface swipeViewInterface;
    private ActivityMainBinding binding;
    private SwipeViewAdapter swipeViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initListeners();

        initAdapters();
    }

    private void initAdapters() {

        swipeViewAdapter = new SwipeViewAdapter(new AdapterFactory() {
            @Override
            public ItemView onCreateView(GenericObject item) {

                DataView view = new DataView(MainActivity.this);

                view.bind(item);

                return view;
            }
        });

        swipeViewAdapter.setItems(getMockObjects());

        binding.swipeView.setAdapter(swipeViewAdapter);

        binding.swipeView.setListener(swipeViewInterface);
    }

    private ArrayList<GenericObject> getMockObjects() {

        ArrayList<GenericObject> items = new ArrayList<>();

        for (int i = 0; i < 10; i++) {

            items.add(new DataObject(i));
        }

        return items;
    }

    private void initListeners() {

        this.swipeViewInterface = new SwipeView.SwipeViewInterface() {
            @Override
            public void onElementSwiped() {


            }
        };
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
