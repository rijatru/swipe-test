package com.bootleg.swipetest.test.swipetest.ui.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;

import com.bootleg.swipetest.test.swipetest.databinding.ViewSwipeLayoutBinding;

public class SwipeView extends FrameLayout {

    private Context context;
    private ViewSwipeLayoutBinding binding;
    private float dX;
    private float x1;
    private boolean notAnimating = true;

    public SwipeView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public SwipeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initAttrs(attrs);
        init();
    }

    public SwipeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initAttrs(attrs);
        init();
    }

    private void initAttrs(AttributeSet attrs) {

    }

    private void init() {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        binding = ViewSwipeLayoutBinding.inflate(inflater, this, true);

        initSwipeListener();
    }

    private void initSwipeListener() {

        binding.containerFront.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:

                        if (notAnimating) {

                            x1 = view.getX();

                            dX = x1 - event.getRawX();
                        }

                        break;

                    case MotionEvent.ACTION_MOVE:

                        view.animate()
                                .x(event.getRawX() + dX)
                                .setDuration(0)
                                .start();
                        break;
                    case MotionEvent.ACTION_UP:

                        long eventDuration = SystemClock.elapsedRealtime() - event.getDownTime();

                        Log.d("Test", "velocity: " + (Math.abs(x1 - Math.abs(view.getX())) / eventDuration) + " p/ms " + "eventDuration: " + eventDuration);

                        view.animate()
                                .x(x1)
                                .setDuration(400)
                                .setInterpolator(new OvershootInterpolator())
                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationStart(Animator animation) {

                                        notAnimating = false;
                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        notAnimating = true;
                                    }
                                })
                                .start();
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
    }
}
