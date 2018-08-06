package jifuliya.lyl.magiccircleview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import jifuliya.lyl.magiccircleview.debris.DebrisField;

public class MagicCircleView extends View {

    private static final String TAG = "MagicCircleView";

    private Drawable drawable;
    private Drawable defaultDrawable;
    private String magicAnim;

    private Bitmap imageBitmap;
    private Bitmap tailorBitmap;

    private int circleRadius;
    private int degree;
    private int speed;
    private int speedInterval;
    private boolean isVague;

    private int currentDegree = 0;
    private int cacheSpeed;

    private boolean isCancelClick = false;
    private boolean isRotate = false;

    private DebrisField debrisField;

    private ClickUpListener listener;

    public MagicCircleView(Context context) {
        super(context);
        initAnimator(context);
        getDrawableResourceToBitmap();
    }

    /**
     * @param context
     * @param attrs
     * @deprecated drawable: which user set image
     * defaultDrawable: user can set default image, if user do not set image which used, default will add
     * magicAnim: user can set it to choose a fav animation to show when click up
     * speed: user can set it to change the rotate speed
     * speedInterval: change the speed interval
     * degree: every rotate degree
     * isVague: when the view is rotating, if this is true, the view will be vaguer and vaguer, if this is false, nothing will be happen
     */
    public MagicCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MagicCircleView);
        drawable = typedArray.getDrawable(R.styleable.MagicCircleView_magic_drawable);
        defaultDrawable = typedArray.getDrawable(R.styleable.MagicCircleView_default_magic_drawable);
        magicAnim = typedArray.getString(R.styleable.MagicCircleView_magic_anim);
        speed = typedArray.getInt(R.styleable.MagicCircleView_magic_speed, 900);
        speedInterval = typedArray.getInt(R.styleable.MagicCircleView_magic_speed_interval, 50);
        degree = typedArray.getInt(R.styleable.MagicCircleView_magic_degree, 5);
        isVague = typedArray.getBoolean(R.styleable.MagicCircleView_magic_is_vague, false);

        if (magicAnim == null) {
            magicAnim = MagicConstant.ANIM_EMPTY;
        }

        typedArray.recycle();

        initAnimator(context);
        getDrawableResourceToBitmap();
    }

    public MagicCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initAnimator(Context context) {
        if (MagicConstant.ANIM_DEBRIS.equals(magicAnim)) {
            debrisField = DebrisField.attach2Window((Activity) context);
        } else if (MagicConstant.ANIM_EMPTY.equals(magicAnim)) {
            debrisField = null;
        }
        cacheSpeed = speed;
    }

    /**
     * switch drawable resource which user set to bitmap.
     */
    private void getDrawableResourceToBitmap() {
        if (drawable instanceof BitmapDrawable) {
            imageBitmap = ((BitmapDrawable) drawable).getBitmap();
            Log.i(TAG, "Image type is BitmapDrawable!");
        }

        if (imageBitmap == null) {
            if (defaultDrawable instanceof BitmapDrawable) {
                imageBitmap = ((BitmapDrawable) defaultDrawable).getBitmap();
            }
            Log.i(TAG, "you should set a useful image to instead of the default image!");
        }
    }

    private Bitmap getDrawableResourceToBitmapWithReturn() {
        Bitmap bitmap = null;
        if (drawable instanceof BitmapDrawable) {
            bitmap = ((BitmapDrawable) drawable).getBitmap();
            Log.i(TAG, "Image type is BitmapDrawable!");
        }

        if (imageBitmap == null) {
            if (defaultDrawable instanceof BitmapDrawable) {
                bitmap = ((BitmapDrawable) defaultDrawable).getBitmap();
            }
            Log.i(TAG, "you should set a useful image to instead of the default image!");
        }
        return bitmap;
    }

    /**
     * @param bitmap if bitmap size is less than container, tailor it.
     */
    private void tailorBitmap(Bitmap bitmap) {

        circleRadius = min(getWidth(), getHeight());

        if (!isVague) {
            bitmap = getDrawableResourceToBitmapWithReturn();
        }

        if (bitmap.getHeight() != circleRadius || bitmap.getWidth() != circleRadius) {
            tailorBitmap = Bitmap.createScaledBitmap(bitmap, circleRadius, circleRadius, false);
        } else tailorBitmap = bitmap;

        imageBitmap = Bitmap.createBitmap(tailorBitmap.getWidth(), tailorBitmap.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas tailorCanvas = new Canvas(imageBitmap);

        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, tailorBitmap.getWidth(), tailorBitmap.getHeight());

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        paint.setColor(Color.parseColor("#ffffff"));


        if (!isRotate) {
            tailorCanvas.rotate(0, tailorBitmap.getWidth() / 2,
                    tailorBitmap.getHeight() / 2);
            isRotate = true;
        } else {

            tailorCanvas.rotate(currentDegree, tailorBitmap.getWidth() / 2,
                    tailorBitmap.getHeight() / 2);

            if (!isVague) {
                currentDegree += degree;
            } else currentDegree = degree;
        }
        tailorCanvas.drawCircle(tailorBitmap.getWidth() / 2,
                tailorBitmap.getHeight() / 2, tailorBitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        tailorCanvas.drawBitmap(tailorBitmap, rect, rect, paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * get the min between var1 and var2
     *
     * @param var1
     * @param var2
     * @return min
     */
    private int min(int var1, int var2) {
        if (var1 <= var2) {
            return var1;
        } else {
            return var2;
        }
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (imageBitmap == null) {
            Log.e(TAG, "you do not set a image!");
            return;
        }

        tailorBitmap(imageBitmap);
        canvas.drawBitmap(imageBitmap, 0, 0, null);
    }

    /**
     * @param ev
     * @return
     * @deprecated MotionEvent.ACTION_DOWN: rotate the view always if user not cancel
     * MotionEvent.ACTION_UP: rotate over, if user set the anim, will explode the anim which user set
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isCancelClick = false;
                new Thread() {
                    public void run() {
                        super.run();
                        while (true) {
                            if (!isCancelClick) {
                                try {
                                    postInvalidate();

                                    if (speedInterval > 100 || speedInterval < 0) {
                                        Log.i(TAG, "speed interval must between 0 to  100!");
                                        throw new IllegalStateException();
                                    }

                                    speed += speedInterval;

                                    if (speed >= 1000) {
                                        Log.i(TAG, "speed can not be more than 1000!");
                                        speed = 999;
                                    } else if (speed < 0) {
                                        Log.i(TAG, "speed can not be less than 0!");
                                        speed = 1;
                                    }

                                    Thread.sleep(1000 - speed);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                break;
                            }
                        }
                    }
                }.start();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                isCancelClick = true;
                showAnim(this);
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        listener.clickUp();
                    }
                }, 1000);
                break;
        }
        return true;
    }

    /**
     * @param view
     * @deprecated add anim which user set in xml, if user do not set it , anim will not show.
     */
    private void showAnim(View view) {
        if (debrisField != null) {
            debrisField.explode(view);
        }
    }

    /**
     * reset animator, speed, and degree to default
     */
    public void reset() {
        if (debrisField != null) {
            debrisField.reset(this);
        }

        currentDegree = -degree;
        speed = cacheSpeed;
        invalidate();
        Log.i(TAG, "reset view success!");
    }

    public interface ClickUpListener {
        void clickUp();
    }

    public void setClickUpListener(ClickUpListener listener) {
        this.listener = listener;
    }
}
