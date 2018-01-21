package com.kangear.bodycompositionanalyzer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static com.kangear.bodycompositionanalyzer.WelcomeActivity.CONST_FINGER_ID;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.DEFAULT_WEIGHT;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.FORMAT_WEIGHT;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.INVALID_FINGER_ID;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.REQUEST_CODE_TOUCHID;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.checkRadio;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.doVipTest;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.hideSystemUI;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SettingsActivity extends AppCompatActivity {
    private static final String TAG = "SettingsActivity";
    private View startView;
    private View stopView;
    private TextView mTextView;
    private static final int WEIGHT_ACTIVITY = 1;
    private static final int WEIGHT_STOP = 2;
    private int bootTag;
    private AudioManager mAudioManager;
    private TextView mVolumeTextView;
    private int mMaxVolume;
    private Button mVolumeAdd;
    private Button mVolumeSub;
    private Context mContext;
    private TextView mRadioTextView;
    private EditText mRadioEditText;
    private Button mCalibrateRadioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mContext = this;
        mVolumeTextView = findViewById(R.id.volume_textview);
        mVolumeAdd = findViewById(R.id.volume_add);
        mVolumeSub = findViewById(R.id.volume_jian);
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

//        startView = findViewById(R.id.weight_start);
//        stopView = findViewById(R.id.weight_stop);
//        mTextView = findViewById(R.id.weight_textview);
        mRadioTextView = findViewById(R.id.radio_textview);
        mRadioEditText = findViewById(R.id.radio_edittext);
        mCalibrateRadioButton = findViewById(R.id.calibrate_radio_button);
        mRadioTextView.setText("");
        mRadioEditText.setText("");
        mCalibrateRadioButton.setEnabled(false);
        mRadioEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    int val = Integer.parseInt(editable.toString());
                    if(WelcomeActivity.checkRadio(val)) {
                        mRadioEditText.setTextColor(Color.WHITE);
                        mCalibrateRadioButton.setEnabled(true);
                    } else {
                        mRadioEditText.setTextColor(Color.RED);
                        mCalibrateRadioButton.setEnabled(false);
                    }
                } catch (NumberFormatException ex) {
                    // Do something
                    mRadioEditText.setTextColor(Color.RED);
                    mCalibrateRadioButton.setEnabled(false);
                }
            }
        });
//
//        bootTag = getIntent().getIntExtra(WelcomeActivity.CONST_WEIGHT_TAG, WelcomeActivity.WEIGHT_INVALIDE);
//        startTest();
//        Log.i(TAG, "onCreate bootTag: " + bootTag);
    }

    /**
     * 开始
     */
    float weight = 0;
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
                        sleep(1);
                        mHandler.sendEmptyMessage(WEIGHT_ACTIVITY);
                        weight += 0.1;
                        if (weight >= DEFAULT_WEIGHT) {
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
                    mTextView.setText("" + String.format(FORMAT_WEIGHT, weight));
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
        Log.i(TAG, "onClick");
        switch (v.getId()) {
            case R.id.read_radio_button:
                try {
                    Protocol.Radio radio = UartBca.getInstance(mContext).readTichengfen();
                    if (radio != null)
                        mRadioTextView.setText(String.valueOf(radio.getWeigthRatio()));
                    else
                        Toast.makeText(this, "读取系数失败", Toast.LENGTH_SHORT).show();
                } catch (Protocol.ProtocalExcption protocalExcption) {
                    protocalExcption.printStackTrace();
                    Toast.makeText(this, "读取系数失败", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.back_button:
                finish();
                break;
            case R.id.time_setting_button:
                startActivity(new Intent(this, TimeActivity.class));
                break;
            case R.id.volume_add:
                mAudioManager.setStreamVolume(
                        AudioManager.STREAM_MUSIC, mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC) + 1,
                        AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                updateVolume();
                break;
            case R.id.volume_jian:
                mAudioManager.setStreamVolume(
                        AudioManager.STREAM_MUSIC, mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC) - 1,
                        AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                updateVolume();
                break;
            case R.id.ad_text_setting:
                startActivity(new Intent(this, AdDialogActivity.class));
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
                        doVipTest(this);
                        finish();
                    } else {
                        Log.i(TAG, "指纹识别异常");
                    }
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideSystemUI(getWindow().getDecorView());
        updateVolume();
    }

    void updateVolume() {
        if (mAudioManager != null) {
            boolean add, sub;
            mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            int cur = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            int percent;
            if (cur == mMaxVolume) {
                percent = 100;
                // 禁用add
                add = false;
                sub = true;
            } else if (cur == 0) {
                percent = 0;
                // 禁用sub
                add = true;
                sub = false;
            } else {
                percent = cur * 100 / mMaxVolume;
                add = true;
                sub = true;
            }
            mVolumeAdd.setEnabled(add);
            mVolumeSub.setEnabled(sub);
            Log.i(TAG, "max: " + mMaxVolume + " cur: " + cur);
            mVolumeTextView.setText(String.valueOf(percent));
        }
    }
}
