package com.kangear.bodycompositionanalyzer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import static com.kangear.bodycompositionanalyzer.WelcomeActivity.exitAsFail;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class IDActivity extends Com2Activity {
    private Button mDotButton;
    private Button mSoftwareBoardButton;
    private String TAG = "IDActivity";
    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id);
        hideSystemUI(getWindow().getDecorView());
        setView(false, getWindow().getDecorView(), null);
        mEditText = findViewById(R.id.tizhibi_edittext);
        mEditText.setText("");
    }

    /**
     * check is can next
     * @return
     */
    @Override
    public boolean canNext(String str) {
        return str.length() > 0;
    }

    @Override
    public void onNextButtonClick() {
        super.onNextButtonClick();
        Log.i(TAG, "onNextButtonClick");
        WelcomeActivity.getPerson().setId(mEditText.getText().toString());
        WelcomeActivity.startAge(this);
        finish();
    }

    @Override
    public void onBackButtonClick() {
        super.onBackButtonClick();
        Log.i(TAG, "onBackButtonClick");
        exitAsFail(this);
    }

    @Override
    public void onKeyBoardButtonClick() {
        super.onKeyBoardButtonClick();
        Log.i(TAG, "onKeyBoardButtonClick");
    }
}
