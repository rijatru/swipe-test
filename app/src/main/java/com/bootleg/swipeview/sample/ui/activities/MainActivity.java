package com.bootleg.swipeview.sample.ui.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.bootleg.swipetest.test.swipetest.R;
import com.bootleg.swipetest.test.swipetest.databinding.ActivityMainBinding;
import com.bootleg.swipeview.adapters.SwipeViewAdapter;
import com.bootleg.swipeview.factories.AdapterFactory;
import com.bootleg.swipeview.interfaces.GenericObject;
import com.bootleg.swipeview.interfaces.ItemView;
import com.bootleg.swipeview.views.SwipeView;
import com.bootleg.swipeview.sample.models.DataObject;
import com.bootleg.swipeview.sample.ui.views.DataView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final String CURRENT_POSITION = "current_position";

    private SwipeView.SwipeViewInterface swipeViewInterface;
    private ActivityMainBinding binding;
    private SwipeViewAdapter swipeViewAdapter;
    private int currentPosition = -999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (savedInstanceState != null) {

            currentPosition = savedInstanceState.getInt(CURRENT_POSITION);
        }

        initListeners();

        initAdapters();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putInt(CURRENT_POSITION, swipeViewAdapter.getCurrentPosition());

        super.onSaveInstanceState(savedInstanceState);
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

        if (currentPosition > 0) {

            swipeViewAdapter.setCurrentPosition(currentPosition);

            items.subList(0, currentPosition).clear();
        }

        return items;
    }

    private void initListeners() {

        this.swipeViewInterface = new SwipeView.SwipeViewInterface() {
            @Override
            public void onItemSwiped() {


            }
        };
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
