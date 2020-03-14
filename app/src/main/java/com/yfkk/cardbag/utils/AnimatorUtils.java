package com.yfkk.cardbag.utils;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * View动画
 * <p>
 * Created by litao on 2018/2/9.
 */

public class AnimatorUtils {

    public static final int DURATION = 250;

    /**
     * 属性动画，缩放动画
     *
     * @param view
     * @param startScale
     * @param endScale
     */
    public static void startAnimatorScale(View view, float startScale, float endScale) {
        AnimatorSet animatorSetsuofang = new AnimatorSet();//组合动画
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", startScale, endScale);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", startScale, endScale);
        animatorSetsuofang.setDuration(DURATION);
        animatorSetsuofang.setInterpolator(new DecelerateInterpolator());
        animatorSetsuofang.play(scaleX).with(scaleY);//两个动画同时开始
        animatorSetsuofang.start();
    }


    /**
     * 弹性缩放动画（放大再回弹缩小）
     *
     * @param view
     */
    public static void startAnimatorScaleElastic(View view, float range) {
        AnimatorSet animatorSetsuofang = new AnimatorSet();//组合动画
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, range, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, range, 1f);
        animatorSetsuofang.setDuration(600);
        animatorSetsuofang.setInterpolator(new OvershootInterpolator(1f));
        animatorSetsuofang.play(scaleX).with(scaleY);//两个动画同时开始
        animatorSetsuofang.start();
    }

    /**
     * 位移动画
     *
     * @param view
     */
    public static void startAnimatorTranslateY(View view, int fromYDelta, int toYDelta, int startOffset) {
        Animation animation = new TranslateAnimation(0, 0, fromYDelta, toYDelta);
        animation.setDuration(DURATION);
        animation.setStartOffset(startOffset);
        animation.setFillAfter(true);
        view.startAnimation(animation);
    }

    /**
     * Scroll位移动画
     *
     * @param view
     */
    public static void startAnimatorScrollToX(Context context, final View view, int fromXDelta, int toXDelta) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(fromXDelta, toXDelta);
        valueAnimator.setDuration(Math.abs(fromXDelta - toXDelta) / StringUtils.dpToPx(context, 2) + 150);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                int currentValue = (Integer) animator.getAnimatedValue();
                view.scrollTo(currentValue, view.getScrollY());
            }
        });
        valueAnimator.start();
    }

    /**
     * Scroll位移动画
     *
     * @param view
     */
    public static void startAnimatorScrollToY(Context context, final View view, int fromYDelta, int toYDelta) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(fromYDelta, toYDelta);
        valueAnimator.setDuration(Math.abs(fromYDelta - toYDelta) / StringUtils.dpToPx(context, 2) + 150);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                int currentValue = (Integer) animator.getAnimatedValue();
                view.scrollTo(view.getScrollX(), currentValue);
            }
        });
        valueAnimator.start();
    }

    /**
     * 显示和隐藏动画(直接改变View高度，需要设置VISIBLE 或 GONE)
     *
     * @param view
     * @param visibility
     */
    public static void startAnimatorVisibilityHeight(final View view, final int visibility, final int height) {
        if ((view.getVisibility() == View.GONE || (view.getTag() != null && (int) view.getTag() == View.GONE)) && visibility == View.VISIBLE) {
            // 重现
            ValueAnimator valueAnimator = ValueAnimator.ofInt(0, height);
            valueAnimator.setDuration(DURATION);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    int currentValue = (Integer) animator.getAnimatedValue();
                    view.getLayoutParams().height = currentValue;
                    view.requestLayout();
                }
            });
            valueAnimator.start();
            view.setVisibility(visibility);
        } else if (view.getVisibility() == View.VISIBLE && visibility == View.GONE) {
            //消失
            ValueAnimator valueAnimator = ValueAnimator.ofInt(height, 0);
            valueAnimator.setDuration(DURATION);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    int currentValue = (Integer) animator.getAnimatedValue();
                    view.getLayoutParams().height = currentValue;
                    view.requestLayout();
                    if (currentValue == 0 && view.getTag() != null && (int) view.getTag() == View.GONE) {
                        view.setVisibility(visibility);
                    }
                }
            });
            valueAnimator.start();
        }
        view.setTag(visibility);
    }

    /**
     * 显示和隐藏动画(直接改变View高度)
     *
     * @param view
     */
    public static void startAnimatorHeight(Context context, final View view, final int startHeight, final int endHeight) {
        // 重现
        ValueAnimator valueAnimator = ValueAnimator.ofInt(startHeight, endHeight);
        valueAnimator.setDuration(Math.abs(startHeight - endHeight) / StringUtils.dpToPx(context, 2.5f) + 250);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                int currentValue = (Integer) animator.getAnimatedValue();
                view.getLayoutParams().height = currentValue;
                view.requestLayout();
            }
        });
        valueAnimator.start();
    }

    /**
     * 改变View宽度
     *
     * @param view
     */
    public static void startAnimatorWidth(Context context, final View view, final int startWidth, final int endWidth) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(startWidth, endWidth);
        valueAnimator.setDuration(Math.abs(startWidth - endWidth) / StringUtils.dpToPx(context, 2) + 150);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                int currentValue = (Integer) animator.getAnimatedValue();
                view.getLayoutParams().width = currentValue;
                view.requestLayout();
            }
        });
        valueAnimator.start();
    }

    /**
     * 透明度动画(0-1)
     *
     * @param view
     */
    public static void startAnimatorAlpha(final View view, float startAlpha, float endAlpha) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(startAlpha, endAlpha);
        alphaAnimation.setDuration(DURATION);
        alphaAnimation.setFillAfter(true);
        view.startAnimation(alphaAnimation);
    }

    /**
     * 透明度动画(0-1)
     *
     * @param view
     */
    public static void startAnimatorAlpha(final View view, float startAlpha, float endAlpha, int duration, int startOffset) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(startAlpha, endAlpha);
        alphaAnimation.setDuration(duration);
        alphaAnimation.setStartOffset(startOffset);
        view.startAnimation(alphaAnimation);
    }

    public static void startAnimatorAlphaLoop(final View view, float startAlpha, float endAlpha, int duration, int startOffset) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(startAlpha, endAlpha);
        alphaAnimation.setDuration(duration);
        alphaAnimation.setRepeatCount(Animation.INFINITE);
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        alphaAnimation.setStartOffset(startOffset);
        view.startAnimation(alphaAnimation);
    }

    /**
     * 属性动画，透明度动画(0-1)
     * （注意，如果设置INVISIBLE,则此动画无效。可以在配置文件中设置alpha为0，来达到不可见的效果）
     *
     * @param view
     */
    public static void startValueAnimatorAlpha(final View view, float startAlpha, float endAlpha,int duration, int startOffset) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt((int) (startAlpha * 100), (int) (endAlpha * 100));
        valueAnimator.setDuration(DURATION);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                int currentValue = (Integer) animator.getAnimatedValue();
                view.setAlpha(currentValue / 100f);
                view.requestLayout();
            }
        });
        valueAnimator.start();
    }

    /**
     * 旋转动画
     *
     * @param view
     */
    public static void startAnimatorRotation(View view, float startRotation, float endRotation) {
        ObjectAnimator rotate = ObjectAnimator.ofFloat(view, "rotation", startRotation, endRotation);
        rotate.setDuration(DURATION);
        rotate.start();
    }

    /**
     * 揭露动画
     */
    public static void startAnimatorReveal(Context context, View view, int duration) {
        float hypot = StringUtils.dpToPx(context, 360);
        if (view.getHeight() > 0 && view.getWidth() > 0) {
            hypot = (float) Math.hypot(view.getHeight(), view.getWidth());
        }
        float startRadius = 0;
        float endRadius = hypot;
        startAnimatorReveal(context, view, view.getWidth() / 2, view.getHeight() / 2, startRadius, endRadius, duration);
    }

    /**
     * 揭露动画
     */
    public static void startAnimatorReveal(Context context, View view, int centerX, int centerY, float startRadius, float endRadius, int duration) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            float hypot = StringUtils.dpToPx(context, 360);
            if (view.getHeight() > 0 && view.getWidth() > 0) {
                hypot = (float) Math.hypot(view.getHeight(), view.getWidth());
            }

            Animator animator = ViewAnimationUtils.createCircularReveal(
                    view, (int) centerX, (int) centerY,
                    startRadius,
                    endRadius);
            animator.setDuration(duration);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.start();
        } else {
            ObjectAnimator.ofFloat(view, "alpha", 0f, 1f).start();
        }
    }

    /**
     * 翻转动画
     */
    public static void flipAnimatorXViewShow(final View oldView, final View newView) {
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(oldView, "rotationX", 0, 90);
        final ObjectAnimator animator2 = ObjectAnimator.ofFloat(newView, "rotationX", -90, 0);
        animator2.setInterpolator(new OvershootInterpolator(2.0f));

        animator1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                oldView.setVisibility(View.GONE);
                animator2.setDuration(200).start();
                newView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator1.setDuration(200).start();
    }

    public static void flipAnimatorYViewShow(final View oldView, final View newView) {
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(oldView, "rotationY", 0, 90);
        final ObjectAnimator animator2 = ObjectAnimator.ofFloat(newView, "rotationY", -90, 0);
        animator2.setInterpolator(new OvershootInterpolator(2.0f));

        animator1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                oldView.setVisibility(View.GONE);
                animator2.setDuration(200).start();
                newView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator1.setDuration(200).start();
    }


    /**
     * 属性动画，textView字体大小动画
     */
    public static void startValueAnimatorTextSize(final TextView textView, float startSize, float endSize) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt((int) (startSize * 100), (int) (endSize * 100));
        valueAnimator.setDuration(150);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                int currentValue = (Integer) animator.getAnimatedValue();
                textView.setTextSize(currentValue / 100f);
                textView.requestLayout();
            }
        });
        valueAnimator.start();
    }

    /**
     * 左右抖动动画
     */
    public static void startAnimatorShake(Context context, final View view) {
        TranslateAnimation animation = new TranslateAnimation(0, StringUtils.dpToPx(context, 5), 0, 0);
        animation.setInterpolator(new CycleInterpolator(4));
        animation.setDuration(400);
        view.startAnimation(animation);
    }

    /**
     * 旋转抖动动画
     */
    public static void startAnimatorShakeRotation(View view, float scaleSmall, float scaleLarge, float shakeDegrees, long duration) {
        if (view == null) {
            return;
        }

        //先变小后变大
        PropertyValuesHolder scaleXValuesHolder = PropertyValuesHolder.ofKeyframe(View.SCALE_X,
                Keyframe.ofFloat(0f, 1.0f),
                Keyframe.ofFloat(0.25f, scaleSmall),
                Keyframe.ofFloat(0.5f, scaleLarge),
                Keyframe.ofFloat(0.75f, scaleLarge),
                Keyframe.ofFloat(1.0f, 1.0f)
        );
        PropertyValuesHolder scaleYValuesHolder = PropertyValuesHolder.ofKeyframe(View.SCALE_Y,
                Keyframe.ofFloat(0f, 1.0f),
                Keyframe.ofFloat(0.25f, scaleSmall),
                Keyframe.ofFloat(0.5f, scaleLarge),
                Keyframe.ofFloat(0.75f, scaleLarge),
                Keyframe.ofFloat(1.0f, 1.0f)
        );

        //先往左再往右
        PropertyValuesHolder rotateValuesHolder = PropertyValuesHolder.ofKeyframe(View.ROTATION,
                Keyframe.ofFloat(0f, 0f),
                Keyframe.ofFloat(0.1f, -shakeDegrees),
                Keyframe.ofFloat(0.2f, shakeDegrees),
                Keyframe.ofFloat(0.3f, -shakeDegrees),
                Keyframe.ofFloat(0.4f, shakeDegrees),
                Keyframe.ofFloat(0.5f, -shakeDegrees),
                Keyframe.ofFloat(0.6f, shakeDegrees),
                Keyframe.ofFloat(0.7f, -shakeDegrees),
                Keyframe.ofFloat(0.8f, shakeDegrees),
                Keyframe.ofFloat(0.9f, -shakeDegrees),
                Keyframe.ofFloat(1.0f, 0f)
        );

        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, scaleXValuesHolder, scaleYValuesHolder, rotateValuesHolder);
        objectAnimator.setDuration(duration);
        objectAnimator.start();
    }

    /**
     * 缩放，位移（Margin，父控件必须是RelativeLayout）的组合动画
     *
     * @param view
     */
    public static void startAnimatorScaleMargin(final View view, final float startScale, final float endScale
            , final int startLeftMargin, final int endLeftMargin, final int startTopMargin, final int endTopMargin, int duration) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(startScale, endScale);
        valueAnimator.setDuration(duration);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                float currentValue = (float) animator.getAnimatedValue();
                view.setScaleX(currentValue);
                view.setScaleY(currentValue);
                // 执行进度（0-1f）
                float progress = (currentValue - startScale) / (endScale - startScale);
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                layoutParams.leftMargin = (int) (startLeftMargin + (endLeftMargin - startLeftMargin) * progress);
                layoutParams.topMargin = (int) (startTopMargin + (endTopMargin - startTopMargin) * progress);
                view.requestLayout();
            }
        });
        valueAnimator.start();
    }


}
