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
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;

import com.bootleg.swipetest.test.swipetest.databinding.ViewSwipeLayoutBinding;

public class SwipeView extends FrameLayout {

    private Context context;
    private ViewSwipeLayoutBinding binding;
    private float dX;
    private float x1;
    private boolean notAnimating = true;

    AnimatorListenerAdapter moveListener;

    private int DIRECION_LEFT = 0;
    private int DIRECION_RIGHT = 1;

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

        initMoveListener();
        initSwipeListener();
    }

    private void initMoveListener() {

        moveListener = new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {

                notAnimating = false;
            }

            @Override
            public void onAnimationEnd(Animator animation) {

                notAnimating = true;
            }
        };
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

                        view.setX(event.getRawX() + dX);

                        setViewAlpha(view);

                        break;

                    case MotionEvent.ACTION_UP:

                        setActionUp(view, event);

                        break;

                    default:

                        return false;
                }

                return true;
            }
        });
    }

    private void setViewAlpha(View view) {

        float outsideSection;
        float alpha = 1;

        if (view.getX() + view.getWidth() > getRootView().getRight()) {

            outsideSection = view.getX() + view.getWidth() - getRootView().getRight();

            alpha = 1 - outsideSection / view.getWidth();

            Log.d("Test RIGHT", "outsideSection: " + outsideSection + " " + alpha);

        } else if (view.getX() < getRootView().getX()) {

            outsideSection = Math.abs(view.getX());

            alpha = 1 - outsideSection / view.getWidth();

            Log.d("Test LEFT", "outsideSection: " + outsideSection + " " + alpha);
        }

        view.setAlpha(alpha);
    }

    private void setActionUp(View view, MotionEvent event) {

        float time = SystemClock.elapsedRealtime() - event.getDownTime();

        float distance = view.getX() < 0 ? Math.abs(view.getX()) + x1 : view.getX() - x1;

        float velocity = distance / time;

        int animDuration = (int) ((view.getX() > 0 ? getRootView().getRight() - view.getX() : view.getRight()) / velocity);

        if (view.getX() > getRootView().getRight() / 2 || view.getX() + view.getWidth() < getRootView().getRight() / 2) {

            view.animate()
                    .x((view.getX() < 0 ? -1 : 1) * getRootView().getRight())
                    .alpha(0)
                    .setDuration(animDuration > 300 ? 300 : animDuration)
                    .setListener(moveListener)
                    .start();

        } else {

            view.animate()
                    .x(x1)
                    .alpha(1)
                    .setDuration(400)
                    .setInterpolator(new OvershootInterpolator())
                    .setListener(moveListener)
                    .start();
        }
    }
}
