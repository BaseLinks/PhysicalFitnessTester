package com.kangear.bodycompositionanalyzer;

import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class BaseActivity extends AppCompatActivity {
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            //Log.d(TAG, "ACTION_UP!");
            WatchDog.getInstance(this).feed(Color.WHITE);
        }
        return super.dispatchTouchEvent(event);
    }

//    @Override
//    public boolean onKeyUp(int keyCode, KeyEvent event) {
//        WatchDog.getInstance(this).feed(Color.WHITE);
//        return super.onKeyUp(keyCode, event);
//    }
}
