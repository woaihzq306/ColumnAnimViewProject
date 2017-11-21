package com.lixiaodaoaaa.view.pieview.flower;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.PointF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.gcssloop.graphics.R;

import java.util.Random;

/**
 * Created by lixiaodaoaaa on 2017/8/7.
 */

public class FlowerLayout extends RelativeLayout {

    private int width, height;

    private Random random;
    private int[] imageResourceIds;
    private int imageViewHeight;
    private int imageViewWidth;


    private Interpolator[] interpolatorS;

    public FlowerLayout(Context context) {
        super(context);
        initData();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initData() {
        random = new Random();
        imageResourceIds = new int[]{R.drawable.logo_sinaweibo, R.drawable.logo_sinaweibo, R.drawable.logo_sinaweibo};
        imageViewHeight = getResources().getDrawable(R.drawable.logo_sinaweibo).getIntrinsicHeight();
        imageViewWidth = getResources().getDrawable(R.drawable.logo_sinaweibo).getIntrinsicWidth();
        interpolatorS = new Interpolator[]{
                new AccelerateInterpolator(), new DecelerateInterpolator(), new AccelerateDecelerateInterpolator(), new AnticipateInterpolator()
                , new AnticipateOvershootInterpolator(), new BounceInterpolator(), new LinearInterpolator(),
                new OvershootInterpolator()
        };
    }


    public FlowerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData();
    }

    public FlowerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
    }

    //add flower******************************************************************
    public void addFlower() {
        ImageView imageView = new ImageView(getContext());
        imageView.setImageResource(imageResourceIds[random.nextInt(imageResourceIds.length - 1)]);
        RelativeLayout.LayoutParams relativePara = new RelativeLayout.LayoutParams
                (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        relativePara.addRule(ALIGN_PARENT_BOTTOM);
        relativePara.addRule(CENTER_HORIZONTAL);
        imageView.setLayoutParams(relativePara);
        setAnimation(imageView);
        addView(imageView);
    }


    public void setAnimation(ImageView imageView) {

        AnimatorSet allAnimation = new AnimatorSet();

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(ObjectAnimator.ofFloat(imageView, "alpha", 0.3f, 1.0f)
                , ObjectAnimator.ofFloat(imageView, "scaleX", 0.3f, 1.0f)
                , ObjectAnimator.ofFloat(imageView, "scaleY", 0.3f, 1.0f)
        );
        animatorSet.setDuration(334);
        allAnimation.playSequentially(animatorSet, setImageViewBeziAnimation(imageView));
        allAnimation.setInterpolator(interpolatorS[random.nextInt(interpolatorS.length - 1)]);
        allAnimation.start();
    }

    public Animator setImageViewBeziAnimation(final ImageView imageView) {

        PointF startPoint = new PointF((width - imageViewWidth) / 2, height - imageViewHeight);

        int newHeight = height - imageViewHeight;
        PointF endPoint = new PointF(random.nextInt(width - imageViewWidth), -imageViewHeight);
        PointF controlPoint1 = new PointF(random.nextInt(width), random.nextInt(newHeight / 2 + newHeight / 2));
        PointF controlPoint2 = new PointF(random.nextInt(width), random.nextInt(newHeight / 2));


        FlowerTypeEvaluator flowerTypeEvaluator = new FlowerTypeEvaluator(startPoint, controlPoint1, controlPoint2, endPoint);

        ValueAnimator valueAnimator = ObjectAnimator.ofObject(flowerTypeEvaluator, startPoint, endPoint);
        valueAnimator.setDuration(13 * 1000);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF pointF = (PointF) animation.getAnimatedValue();
                imageView.setX(pointF.x);
                imageView.setY(pointF.y);

                final float t = animation.getAnimatedFraction();
                imageView.setAlpha(1 - t);

            }
        });

        valueAnimator.setInterpolator(getInterpolator());

        return valueAnimator;
    }

    private TimeInterpolator getInterpolator() {
        return null;
    }

}