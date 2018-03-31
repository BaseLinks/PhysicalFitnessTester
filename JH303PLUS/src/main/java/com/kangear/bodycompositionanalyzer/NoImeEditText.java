package com.kangear.bodycompositionanalyzer;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;

/**
 * Created by tony on 17-12-18.
 */

public class NoImeEditText extends android.support.v7.widget.AppCompatEditText {
    public NoImeEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onCheckIsTextEditor() {
        return false;
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == 1) {
            Log.i("main_activity", "键盘向下 ");
            super.onKeyPreIme(keyCode, event);
            return false;
        }
        return super.onKeyPreIme(keyCode, event);
    }
}