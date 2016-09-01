package com.bootleg.swipeview.lib.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;

import com.bootleg.swipetest.test.swipetest.databinding.ViewSwipeLayoutBinding;
import com.bootleg.swipeview.lib.adapters.SwipeViewAdapter;

import java.util.ArrayList;

public class SwipeView extends FrameLayout {

    private int STANDARD_ANIMATION_DURATION = 300;

    private Context context;
    private ViewSwipeLayoutBinding binding;

    private float dX;
    private float frontX;
    private float yTranslation = 0.125f;
    private float xyScaleFront = 1.0f;
    private float xyScaleMid = 0.90f;
    private float xyScaleBack = 0.80f;
    private float xyScaleLast = 0.70f;

    private ArrayList<ViewGroup> viewsArray = new ArrayList<>();
    private ArrayList<Float> viewsArrayY = new ArrayList<>();
    private ArrayList<Float> viewsArrayW = new ArrayList<>();
    private ArrayList<Float> viewsXyScale = new ArrayList<>();

    private float RIGHT_PERCENT_THRESHOLD = 0.5f;
    private float LEFT_PERCENT_THRESHOLD = 0.5f;
    private float VELOCITY_THRESHOLD = 1.4f;

    private long t1;
    private long t2;

    private int size;

    private SwipeViewAdapter adapter;

    private SwipeViewInterface listener;
    private AnimatorListenerAdapter animationListener;
    private boolean onAnimation;

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

        size = binding.mainContainer.getChildCount();

        for (int i = 0; i < size; i++) {

            viewsArray.add((ViewGroup) binding.mainContainer.getChildAt(i));
        }

        initViews();
        initMoveListener();
        initSwipeListener();
    }

    private void initViews() {

        viewsXyScale.add(xyScaleFront);
        viewsXyScale.add(xyScaleMid);
        viewsXyScale.add(xyScaleBack);
        viewsXyScale.add(xyScaleLast);

        viewsArray.get(0).getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                viewsArray.get(0).getViewTreeObserver().removeOnGlobalLayoutListener(this);

                viewsArray.get(0).setZ(size * size);
                viewsArrayY.add(viewsArray.get(0).getY());
                viewsArrayW.add(viewsArray.get(0).getWidth() * xyScaleFront);

                size = viewsArray.size();

                for (int i = 1; i < size; i++) {

                    float dX = i < 2 ? 1 : 2;

                    float y = viewsArray.get(i).getY() - viewsArray.get(i).getHeight() * yTranslation * dX;

                    viewsArray.get(i).setY(y);
                    viewsArray.get(i).setScaleX(viewsXyScale.get(i));
                    viewsArray.get(i).setScaleY(viewsXyScale.get(i));
                    viewsArray.get(i).setZ(size * (size - i));

                    viewsArrayY.add(y);
                    viewsArrayW.add(viewsArray.get(i).getWidth() * viewsXyScale.get(i));
                }
            }
        });
    }

    private void initMoveListener() {

        animationListener = new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {

                if (onAnimation) {

                    onAnimation = false;

                    if (adapter.hasMoreItems()) {

                        viewsArray.get(0).addView((View) adapter.getNextView());

                        moveToBack(viewsArray.get(0));
                    }

                    adapter.moveCurrentPosition();

                    viewsArray = sortViews(viewsArray);

                    initSwipeListener();

                } else {

                    initSwipeListener();
                }
            }
        };
    }

    private void moveToBack(View view) {

        view.setX(frontX);
        view.setY(viewsArrayY.get(size - 1));
        view.setScaleX(xyScaleLast);
        view.setScaleY(xyScaleLast);
        view.setZ(size);
        view.setAlpha(1);
    }

    private ArrayList<ViewGroup> sortViews(ArrayList<ViewGroup> viewsArray) {

        ViewGroup tempView = viewsArray.get(0);

        for (int i = 0; i < size - 1; i++) {

            viewsArray.set(i, viewsArray.get(i + 1));
        }

        viewsArray.set(size - 1, tempView);

        return viewsArray;
    }

    private void initSwipeListener() {

        viewsArray.get(0).setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:

                        t1 = System.currentTimeMillis();

                        frontX = view.getX();

                        dX = frontX - event.getRawX();

                        break;

                    case MotionEvent.ACTION_MOVE:

                        view.setX(event.getRawX() + dX);

                        moveViews(setViewAlpha(view));

                        break;

                    case MotionEvent.ACTION_UP:

                        t2 = System.currentTimeMillis();

                        setActionUp(view);

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

    private void moveViews(float percent) {

        float dY;
        float dw;
        float y;

        float newPercent;

        percent = (1 - percent);

        if (percent < 1) {

            for (int i = 0; i < size - 1; i++) {

                dY = viewsArrayY.get(i) - viewsArrayY.get(i + 1);
                dw = viewsArrayW.get(i) - viewsArrayW.get(i + 1);
                y = viewsArrayY.get(i + 1) + dY * percent;

                viewsArray.get(i + 1).setY(y);

                newPercent = (viewsArrayW.get(i + 1) + dw * percent) / viewsArrayW.get(0);

                viewsArray.get(i + 1).setScaleX(newPercent);
                viewsArray.get(i + 1).setScaleY(newPercent);
            }
        }
    }

    private void setActionUp(View view) {

        float time = t2 - t1;

        float distance = view.getX() < 0 ? Math.abs(view.getX()) + frontX : view.getX() - frontX;

        float velocity = distance / time;

        float remainingDistance = view.getX() > 0 ? getRootView().getRight() - view.getX() : view.getWidth() - Math.abs(view.getX());

        int animDuration = (int) Math.abs(remainingDistance / velocity);

        viewsArray.get(0).setOnTouchListener(null);

        if (view.getX() > getRootView().getRight() * RIGHT_PERCENT_THRESHOLD
                || view.getX() + view.getWidth() < getRootView().getRight() * LEFT_PERCENT_THRESHOLD
                || Math.abs(velocity) > VELOCITY_THRESHOLD) {

            onAnimation = true;

            viewsArray.get(0).animate()
                    .x((view.getX() < 0 ? -1 : 1) * getRootView().getWidth())
                    .alpha(0)
                    .z(size * size)
                    .setDuration(animDuration > STANDARD_ANIMATION_DURATION ? 30 : animDuration)
                    .setListener(animationListener)
                    .setInterpolator(null);

            for (int i = 0; i < size - 1; i++) {

                viewsArray.get(i + 1).animate()
                        .y(viewsArrayY.get(i))
                        .scaleY(viewsXyScale.get(i))
                        .scaleX(viewsXyScale.get(i))
                        .z(size * (size - i))
                        .setDuration(animDuration > STANDARD_ANIMATION_DURATION ? STANDARD_ANIMATION_DURATION : animDuration)
                        .setListener(null)
                        .setInterpolator(new OvershootInterpolator());
            }

            listener.onItemSwiped();

        } else {

            viewsArray.get(0).animate()
                    .x(frontX)
                    .alpha(1)
                    .setDuration(STANDARD_ANIMATION_DURATION)
                    .setListener(animationListener)
                    .setInterpolator(new OvershootInterpolator());

            for (int i = 0; i < size - 1; i++) {

                viewsArray.get(i + 1).animate()
                        .y(viewsArrayY.get(i + 1))
                        .scaleY(viewsXyScale.get(i + 1))
                        .scaleX(viewsXyScale.get(i + 1))
                        .setDuration(STANDARD_ANIMATION_DURATION)
                        .setListener(null)
                        .setInterpolator(null);
            }
        }
    }

    public void setAdapter(SwipeViewAdapter adapter) {

        this.adapter = adapter;

        int size = viewsArray.size();

        for (int i = 0; i < size; i++) {

            viewsArray.get(i).addView((View) adapter.getView(i));

            this.adapter.setNewIndex(i);
        }
    }

    public void setListener(SwipeViewInterface listener) {
        this.listener = listener;
    }

    public interface SwipeViewInterface {

        void onItemSwiped();
    }
}
