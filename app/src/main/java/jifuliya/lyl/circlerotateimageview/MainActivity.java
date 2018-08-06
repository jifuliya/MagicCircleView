package jifuliya.lyl.circlerotateimageview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import jifuliya.lyl.magiccircleview.MagicCircleView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private MagicCircleView magicCircleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        magicCircleView = findViewById(R.id.magic);
        magicCircleView.setClickUpListener(new MagicCircleView.ClickUpListener() {
            @Override
            public void clickUp() {
                Log.i(TAG, "click-up");
                ChangeActivity.startChangeActivity(MainActivity.this);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        magicCircleView.reset();
    }
}
