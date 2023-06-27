package com.kangear.bodycompositionanalyzer.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.widget.EditText;

import com.kangear.bodycompositionanalyzer.WatchDog;

@SuppressLint("AppCompatCustomView")
public class WatchDogEditText extends EditText {
    public WatchDogEditText(Context context) {
        super(context);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        WatchDog.getInstance(getContext()).feed(Color.BLUE);
    }
}
