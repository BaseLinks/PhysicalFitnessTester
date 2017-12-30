package com.kangear.bodycompositionanalyzer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import static com.kangear.bodycompositionanalyzer.WelcomeActivity.WEIGHT_NEW_TEST;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.WEIGHT_VIP_TEST;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class WeightActivity extends AppCompatActivity {
    private static final String TAG = "WeightActivity";
    private View startView;
    private View stopView;
    private TextView mTextView;
    private static final int WEIGHT_ACTIVITY = 1;
    private static final int WEIGHT_STOP = 2;
    private static int bootTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight);
        hideSystemUI(getWindow().getDecorView());
        startView = findViewById(R.id.weight_start);
        stopView = findViewById(R.id.weight_stop);
        mTextView = findViewById(R.id.weight_textview);

        bootTag = getIntent().getIntExtra(WelcomeActivity.CONST_WEIGHT_TAG, WelcomeActivity.WEIGHT_INVALIDE);
        startTest();
        Log.i(TAG, "onCreate");
    }

    // This snippet hides the system bars.
    public static void hideSystemUI(View v) {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        v.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    /**
     * 开始
     */
    private void startTest() {
        startView.setVisibility(View.VISIBLE);
        stopView.setVisibility(View.GONE);
        mTextView.setText("");

        // star phread
        new Thread() {
            @Override
            public void run() {
                while(true) {
                    try {
                        sleep(800);
                        mHandler.sendEmptyMessage(WEIGHT_ACTIVITY);
                        int weight = 1;
                        if (mTextView.getText() != null) {
                            String str = mTextView.getText().toString();
                            try {
                                weight += Integer.valueOf(str);
                            } catch (Exception e) {
                            }
                        }

                        if (weight > 4) {
                            mHandler.sendEmptyMessage(WEIGHT_STOP);
                            break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case WEIGHT_ACTIVITY:
                    int weight = 1;
                    if (mTextView.getText() != null) {
                        String str = mTextView.getText().toString();
                        try {
                            weight += Integer.valueOf(str);
                        } catch (Exception e) {
                        }
                    }
                    mTextView.setText("" + weight);
                    break;
                case WEIGHT_STOP:
                    stopTest();
                    break;
            }
        }
    };

    /**
     * 开始
     */
    private void stopTest() {
        startView.setVisibility(View.GONE);
        stopView.setVisibility(View.VISIBLE);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                finish();
                break;
            case R.id.next_button:
                WelcomeActivity.getPerson().setWeight(Integer.valueOf(mTextView.getText().toString()));
                switch (bootTag) {
                    case WEIGHT_VIP_TEST:
                        WelcomeActivity.doVipTest(this);
                        break;
                    case WEIGHT_NEW_TEST:
                        WelcomeActivity.doTmpTest(this);
                        break;
                }
//                finish();
                break;
        }
    }
}
