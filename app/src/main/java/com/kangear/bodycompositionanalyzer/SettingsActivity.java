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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.crashreport.CrashReport;

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
    private AudioManager mAudioManager;
    private TextView mVolumeTextView;
    private int mMaxVolume;
    private Button mVolumeAdd;
    private Button mVolumeSub;
    private Context mContext;
    private TextView mRadioTextView;
    private EditText mRadioEditText;
    private Button mCalibrateRadioButton;
    private TextView mWeightTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mContext = this;
        mVolumeTextView = findViewById(R.id.volume_textview);
        mVolumeAdd = findViewById(R.id.volume_add);
        mVolumeSub = findViewById(R.id.volume_jian);
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        mRadioTextView = findViewById(R.id.radio_textview);
        mRadioEditText = findViewById(R.id.radio_edittext);
        mCalibrateRadioButton = findViewById(R.id.calibrate_radio_button);
        mWeightTextView = findViewById(R.id.weight_textview);
        mRadioTextView.setText("");
        mRadioEditText.setText("");
        mWeightTextView.setText("");
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
            case R.id.test_button:
                WelcomeActivity.startWeightTest(this, mWeightTextView, null);
                break;
            case R.id.calibrate_radio_button:
                try {
                    Protocol.Radio radio = new Protocol.Radio(Integer.valueOf(mRadioEditText.getText().toString()), 0x00);
                    UartBca.getInstance(mContext).writeTichengfen(radio);
                    Toast.makeText(this, "写入系数成功", Toast.LENGTH_SHORT).show();
                } catch (Protocol.ProtocalExcption protocalExcption) {
                    protocalExcption.printStackTrace();
                    Toast.makeText(this, "写入系数失败", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.update_apk_button:
//                Toast.makeText(this, "系统升级", Toast.LENGTH_SHORT).show();
//                CrashReport.testJavaCrash();
                Beta.checkUpgrade();
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
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
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
