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

import java.util.ArrayList;

public class SwipeView extends FrameLayout {

    private Context context;
    private ViewSwipeLayoutBinding binding;
    private float dX;
    private float frontX;
    private float frontY;
    private float frontW;
    private float midY;
    private float midW;
    private float backY;
    private float backW;
    private boolean notAnimating = true;
    private boolean swiped;
    private float yTranslation = 0.125f;
    private float xyScaleFront = 1.0f;
    private float xyScaleMid = 0.90f;
    private float xyScaleBack = 0.80f;

    private ArrayList<View> viewsArray = new ArrayList<>();
    private ArrayList<Float> viewsArrayY = new ArrayList<>();
    private ArrayList<Float> viewsArrayW = new ArrayList<>();
    private ArrayList<Float> viewsXyScale = new ArrayList<>();

    private float RIGHT_PERCENT_THRESHOLD = 0.3f;
    private float LEFT_PERCENT_THRESHOLD = 0.7f;

    View frontView;
    View midView;
    View backView;

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

        int childCount = binding.mainContainer.getChildCount();

        for (int i = 0; i < childCount; i++) {

            viewsArray.add(binding.mainContainer.getChildAt(i));
        }

        frontView = viewsArray.get(0);
        midView = viewsArray.get(1);
        backView = viewsArray.get(2);

        initViews();
        initMoveListener();
        initSwipeListener();
    }

    private void initViews() {

        viewsXyScale.add(xyScaleFront);
        viewsXyScale.add(xyScaleMid);
        viewsXyScale.add(xyScaleBack);

        frontView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                frontView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                frontView.setZ(18);

                midView.setY(midView.getY() - midView.getHeight() * yTranslation);
                midView.setScaleX(xyScaleMid);
                midView.setScaleY(xyScaleMid);
                midView.setZ(12);

                midY = midView.getY();
                midW = midView.getWidth() * xyScaleMid;

                backView.setY(backView.getY() - backView.getHeight() * yTranslation * 1.9f);
                backView.setScaleX(xyScaleBack);
                backView.setScaleY(xyScaleBack);
                backView.setZ(6);

                backY = backView.getY();
                backW = backView.getWidth() * xyScaleBack;

                viewsArrayY.add(frontView.getY());
                viewsArrayY.add(midView.getY());
                viewsArrayY.add(backView.getY());

                viewsArrayW.add(frontView.getWidth() * 1f);
                viewsArrayW.add(midView.getWidth() * xyScaleMid);
                viewsArrayW.add(backView.getWidth() * xyScaleBack);
            }
        });
    }

    private void initMoveListener() {

        moveListener = new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {

                notAnimating = false;

                if (swiped) {

                    int size = viewsArray.size();

                    for (int i = 0; i < size; i++) {

                        viewsArray.get(i).setOnTouchListener(null);
                    }
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {

                notAnimating = true;

                if (swiped) {

                    swiped = false;

                    moveToBack(viewsArray.get(0));
                    sortViews();
                    initSwipeListener();
                }
            }
        };
    }

    private void moveToBack(View view) {

        view.setX(frontX);
        view.setY(viewsArrayY.get(2));
        view.setScaleX(xyScaleBack);
        view.setScaleY(xyScaleBack);
        view.setZ(6);
        view.animate()
                .alpha(1f)
                .setDuration(450);
    }

    private void sortViews() {

        View tempView = viewsArray.get(0);

        int size = viewsArray.size();

        for (int i = 0; i < size - 1; i++) {

            viewsArray.set(i, viewsArray.get(i + 1));
        }

        viewsArray.set(size - 1, tempView);
    }

    private void initSwipeListener() {

        viewsArray.get(0).setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:

                        if (notAnimating) {

                            frontX = view.getX();
                            frontY = view.getY();
                            frontW = view.getWidth();

                            dX = frontX - event.getRawX();
                        }

                        break;

                    case MotionEvent.ACTION_MOVE:

                        view.setX(event.getRawX() + dX);

                        moveViews(setViewAlpha(view));

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

    private void moveViews(float percent) {

        float dY;
        float dw;
        float y;

        float newPercent;

        percent = (1 - percent);

        if (percent < 1) {

            int size = viewsArray.size();

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

    private void setActionUp(View view, MotionEvent event) {

        float time = SystemClock.elapsedRealtime() - event.getDownTime();

        float distance = view.getX() < 0 ? Math.abs(view.getX()) + frontX : view.getX() - frontX;

        float velocity = distance / time;

        int animDuration = (int) ((view.getX() > 0 ? getRootView().getRight() - view.getX() : view.getRight()) / velocity);

        if (view.getX() > getRootView().getRight() * RIGHT_PERCENT_THRESHOLD || view.getX() + view.getWidth() < getRootView().getRight() * LEFT_PERCENT_THRESHOLD) {

            swiped = true;

            viewsArray.get(0).animate()
                    .x((view.getX() < 0 ? -1 : 1) * getRootView().getRight())
                    .alpha(0)
                    .setDuration(animDuration > 300 ? 300 : animDuration)
                    .setListener(moveListener)
                    .start();

            int size = viewsArray.size();

            for (int i = 0; i < size - 1; i++) {

                viewsArray.get(i + 1).animate()
                        .y(viewsArrayY.get(i))
                        .scaleY(viewsXyScale.get(i))
                        .scaleX(viewsXyScale.get(i))
                        .z(6 * (size - i))
                        .setDuration(400)
                        .setInterpolator(new OvershootInterpolator());
            }

        } else {

            viewsArray.get(0).animate()
                    .x(frontX)
                    .alpha(1)
                    .setDuration(400)
                    .setInterpolator(new OvershootInterpolator())
                    .setListener(moveListener)
                    .start();

            int size = viewsArray.size();

            for (int i = 0; i < size - 1; i++) {

                viewsArray.get(i + 1).animate()
                        .y(viewsArrayY.get(i + 1))
                        .scaleY(viewsXyScale.get(i + 1))
                        .scaleX(viewsXyScale.get(i + 1))
                        .setDuration(400)
                        .setInterpolator(new OvershootInterpolator());
            }
        }
    }
}
