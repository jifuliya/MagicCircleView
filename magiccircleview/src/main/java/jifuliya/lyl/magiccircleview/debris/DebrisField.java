package jifuliya.lyl.magiccircleview.debris;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import jifuliya.lyl.magiccircleview.BaseField;
import jifuliya.lyl.magiccircleview.MagicUtils;

public class DebrisField extends BaseField {

    public static final String TAG = "DebrisField";

    private List<DebrisAnim> mExplosions = new ArrayList<>();
    private int[] mExpandInset = new int[2];

    private ValueAnimator animator;

    public DebrisField(Context context) {
        super(context);
        init();
    }

    public DebrisField(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DebrisField(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public void init() {
        Arrays.fill(mExpandInset, MagicUtils.dp2Px(32));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (DebrisAnim explosion : mExplosions) {
            explosion.draw(canvas);
        }
    }

    public void explode(Bitmap bitmap, Rect bound, long startDelay, long duration) {
        final DebrisAnim explosion = new DebrisAnim(this, bitmap, bound);
        explosion.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mExplosions.remove(animation);
            }
        });
        explosion.setStartDelay(startDelay);
        explosion.setDuration(duration);
        mExplosions.add(explosion);
        explosion.start();
    }

    @Override
    public void explode(final View view) {
        Rect r = new Rect();
        view.getGlobalVisibleRect(r);
        int[] location = new int[2];
        getLocationOnScreen(location);
        r.offset(-location[0], -location[1]);
        r.inset(-mExpandInset[0], -mExpandInset[1]);
        int startDelay = 100;
        animator = ValueAnimator.ofFloat(0f, 1f).setDuration(150);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            Random random = new Random();

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                view.setTranslationX((random.nextFloat() - 0.5f) * view.getWidth() * 0.05f);
                view.setTranslationY((random.nextFloat() - 0.5f) * view.getHeight() * 0.05f);

            }
        });
        animator.start();
        view.animate().setDuration(150).setStartDelay(startDelay).scaleX(0f).scaleY(0f).alpha(0f).start();
        explode(MagicUtils.createBitmapFromView(view), r, startDelay, DebrisAnim.DEFAULT_DURATION);
    }

    private void resetAttachView(View view){
        view.animate().scaleX(1f).scaleY(1f).alpha(1f).start();
    }

    @Override
    public void reset(View view) {
        mExplosions.clear();
        invalidate();
        animator.cancel();

        resetAttachView(view);
    }

    public static DebrisField attach2Window(Activity activity) {
        ViewGroup rootView = activity.findViewById(Window.ID_ANDROID_CONTENT);
        DebrisField explosionField = new DebrisField(activity);
        rootView.addView(explosionField, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return explosionField;
    }

}