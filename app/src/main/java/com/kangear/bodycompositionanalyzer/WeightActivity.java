package com.kangear.bodycompositionanalyzer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import static com.kangear.bodycompositionanalyzer.WelcomeActivity.CONST_FINGER_ID;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.DEFAULT_WEIGHT;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.FORMAT_WEIGHT;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.HANDLE_EVENT_WEIGHT_ERROR;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.INVALID_FINGER_ID;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.REQUEST_CODE_TOUCHID;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.WEIGHT_NEW_TEST;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.HANDLE_EVENT_WEIGHT_STOP;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.WEIGHT_VIP_TEST;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.doVipTest;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.hideSystemUI;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.startAge;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.startTouchId;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.startWelcome;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class WeightActivity extends BaseActivity {
    private static final String TAG = "WeightActivity";
    private View startView;
    private View stopView;
    private TextView mTextView;
    private static final int WEIGHT_ACTIVITY = 1;
    private int bootTag;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight);
        mContext = this;
        hideSystemUI(getWindow().getDecorView());
        startView = findViewById(R.id.weight_start);
        stopView = findViewById(R.id.weight_stop);
        mTextView = findViewById(R.id.weight_textview);

        bootTag = getIntent().getIntExtra(WelcomeActivity.CONST_WEIGHT_TAG, WelcomeActivity.WEIGHT_INVALIDE);
        switch (bootTag) {
            case WEIGHT_VIP_TEST:
//                startTouchId(this);
//                getSoundPool().play(mMusicId.get(1),1,1, 0, 0, 1);
                break;
            case WEIGHT_NEW_TEST:
//                WelcomeActivity.doTmpTest(this);
                break;
        }

        startView.setVisibility(View.VISIBLE);
        stopView.setVisibility(View.GONE);

        WelcomeActivity.startWeightTest(this, mTextView, mHandler);
        Log.i(TAG, "onCreate bootTag: " + bootTag);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case HANDLE_EVENT_WEIGHT_STOP:
                    stopTest();
                    break;
                case HANDLE_EVENT_WEIGHT_ERROR:
                    startWelcome(mContext);
                    finish();
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
                MusicService.stop(this);
                finish();
                break;
            case R.id.next_button:
                WelcomeActivity.getRecord().setWeight(Float.valueOf(mTextView.getText().toString()));
                switch (bootTag) {
                    case WEIGHT_VIP_TEST:
                        startTouchId(this);
                        break;
                    case WEIGHT_NEW_TEST:
                        WelcomeActivity.doTmpTest(this);
                        break;
                }
//                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case REQUEST_CODE_TOUCHID:
                if (resultCode == RESULT_OK) {
                    int fingerId = intent.getIntExtra(CONST_FINGER_ID, INVALID_FINGER_ID);
                    Person p = PersonBean.getInstance(this).queryByFingerId(fingerId);
                    if (p != null) {
                        WelcomeActivity.getRecord().setPerson(p);
                        WelcomeActivity.getRecord().setPersonId(p.getId());
                        doVipTest(this);
                        finish();
                    } else {
                        Log.i(TAG, "会员识别异常");
                        Toast.makeText(this, "会员识别异常", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
}
