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
import android.view.ViewTreeObserver;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;

import com.bootleg.swipetest.test.swipetest.databinding.ViewSwipeLayoutBinding;

public class SwipeView extends FrameLayout {

    private Context context;
    private ViewSwipeLayoutBinding binding;
    private float dX;
    private float x1;
    private float y1;
    private float w1;
    private float y1Mid;
    private float w1Mid;
    private boolean notAnimating = true;
    private float yTranslationScale = 0.125f;
    private float xyScale = 0.90f;

    AnimatorListenerAdapter moveListener;

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



        initViews();
        initMoveListener();
        initSwipeListener();
    }

    private void initViews() {

        binding.containerMid.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                binding.containerMid.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                binding.containerMid.setY(binding.containerMid.getY() - binding.containerMid.getHeight() * yTranslationScale);
                binding.containerMid.setScaleX(xyScale);
                binding.containerMid.setScaleY(xyScale);
                binding.containerMid.setZ(xyScale);

                y1Mid = binding.containerMid.getY();
                w1Mid = binding.containerMid.getWidth() * xyScale;
            }
        });
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
                            y1 = view.getY();
                            w1 = view.getWidth();

                            dX = x1 - event.getRawX();
                        }

                        break;

                    case MotionEvent.ACTION_MOVE:

                        view.setX(event.getRawX() + dX);

                        moveMidView(setViewAlpha(view));

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

    private float setViewAlpha(View view) {

        float outsideSection;
        float alpha = 1;

        if (view.getX() + view.getWidth() > getRootView().getRight()) {

            outsideSection = view.getX() + view.getWidth() - getRootView().getRight();

            alpha = 1 - outsideSection / view.getWidth();

        } else if (view.getX() < getRootView().getX()) {

            outsideSection = Math.abs(view.getX());

            alpha = 1 - outsideSection / view.getWidth();
        }

        view.setAlpha(alpha);

        return alpha;
    }

    private void moveMidView(float percent) {

        percent = (1 - percent);

        if (percent < 1) {

            float dY = y1 - y1Mid;
            float dw = w1 - w1Mid;

            float y = y1Mid + dY * percent;

            Log.d("Test", "distance: " + dY + percent + " y: " + y);

            binding.containerMid.setY(y);

            percent = (w1Mid + dw * percent) / w1;

            binding.containerMid.setScaleX(percent);
            binding.containerMid.setScaleY(percent);
        }
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

            binding.containerMid.animate()
                    .y(y1)
                    .scaleY(1)
                    .scaleX(1)
                    .setDuration(400)
                    .setInterpolator(new OvershootInterpolator());

        } else {

            view.animate()
                    .x(x1)
                    .alpha(1)
                    .setDuration(400)
                    .setInterpolator(new OvershootInterpolator())
                    .setListener(moveListener)
                    .start();

            binding.containerMid.animate()
                    .y(y1Mid)
                    .scaleY(xyScale)
                    .scaleX(xyScale)
                    .setDuration(400)
                    .setInterpolator(new OvershootInterpolator());
        }
    }
}
