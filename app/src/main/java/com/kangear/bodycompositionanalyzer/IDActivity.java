package com.kangear.bodycompositionanalyzer;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.kangear.bodycompositionanalyzer.application.App;
import com.kangear.bodycompositionanalyzer.entry.SchoopiaStudent;
import com.kangear.bodycompositionanalyzer.tool.SerialDevice;

import java.nio.charset.StandardCharsets;

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
        final Context mContext = this;
        setContentView(R.layout.activity_id);
        hideSystemUI(getWindow().getDecorView());
        setView(false, getWindow().getDecorView(), null);
        mEditText = findViewById(R.id.tizhibi_edittext);
        mEditText.setText("");
        setNumberLength(18);
        Log.i(TAG, "onCreate");

        SerialDevice.getInstance(getApplicationContext()).setOnResponse(new SerialDevice.Response() {
            public void onResponse(byte[] content) {
                String msg = new String(content, StandardCharsets.UTF_8);
                Log.e(TAG, "" + msg);
                Gson gson = new Gson();
                SchoopiaStudent student = gson.fromJson(msg, SchoopiaStudent.class);
                final Person p = SchoopiaStudent.toPerson(student);
                Log.e(TAG, "SchoopiaStudent: " + student);
                Log.e(TAG, "Person: " + p);

                runOnUiThread(() -> mEditText.setText(p.getName()));

                App.getRecord().setName(p.getName());
                App.getRecord().setAge(p.getAge());
                App.getRecord().setGender(p.getGender());
                App.getRecord().setHeight(p.getHeight());

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                runOnUiThread(() -> {
                    WelcomeActivity.doTest(mContext);
                    finish();
                });
            }
        });
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
        WelcomeActivity.getRecord().setName(mEditText.getText().toString());
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SerialDevice.getInstance(getApplicationContext()).setOnResponse(null);
    }
}
