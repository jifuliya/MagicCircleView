package jifuliya.lyl.circlerotateimageview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class ChangeActivity extends AppCompatActivity {

    public static void startChangeActivity(Context context) {
        Intent intent = new Intent(context, ChangeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);
    }
}
