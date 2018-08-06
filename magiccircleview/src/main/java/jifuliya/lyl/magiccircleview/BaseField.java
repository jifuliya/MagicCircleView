package jifuliya.lyl.magiccircleview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class BaseField extends View implements IAnimField {

    private static final String TAG = "BaseField";

    public BaseField(Context context) {
        super(context);
    }

    public BaseField(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseField(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void init() {

    }

    @Override
    public void explode(View view) {

    }

    @Override
    public void reset(View view) {

    }
}
