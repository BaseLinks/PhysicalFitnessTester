package com.kangear.bodycompositionanalyzer;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.kangear.bodycompositionanalyzer.MusicService.SOUND_31_LOG_UP_TOUCH_ID_DONE;
import static com.kangear.bodycompositionanalyzer.MusicService.SOUND_32_LOG_UP_TOUCH_ID_FAIL;
import static com.kangear.bodycompositionanalyzer.MusicService.play;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.INVALID_FINGER_ID;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.REQUEST_CODE_TOUCHID;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.exitAsFail;

/**
 * 本页面不显示logo
 */
public class MemRegActivity2 extends Com2Activity {
    private static final String TAG = "MemRegActivity";
    private View logoView;
    private Button mFingerButton;
    private EditText mIdEditText;
    private EditText mHeightEditText;
    private EditText mAgeEditText;
    private Button mNextButton;
    private Person mPerson;
    private RadioButton mMaleRadio;
    private RadioButton mFeMaleRadio;
    private int mFingerId = INVALID_FINGER_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memreg);
        setView(false, getWindow().getDecorView(), null);
        logoView = findViewById(R.id.logo_imageview);
        logoView.setVisibility(View.GONE);
        mFingerButton = findViewById(R.id.finger_button);
        mIdEditText = findViewById(R.id.id_edittext);
        mHeightEditText = findViewById(R.id.height_edittext);
        mAgeEditText = findViewById(R.id.age_edittext);
        mNextButton = findViewById(R.id.keyboard_back_del_next).findViewById(R.id.kb_next_button);
        mMaleRadio = findViewById(R.id.male_radiobutton);
        mFeMaleRadio = findViewById(R.id.female_radiobutton);

        dissAllwithoutBackNext();
        mIdEditText.addTextChangedListener(mIdTextWatcher);
        mAgeEditText.addTextChangedListener(mAgeTextWatcher);
        mHeightEditText.addTextChangedListener(mHeightTextWatcher);

        mIdEditText.setText("");
        mAgeEditText.setText("");
        mHeightEditText.setText("");
        mPerson = new Person();

        // TODO: fingerId如何生成？ 0-1024范围，不能重复
        for (int i = 0; i < 1024; i++) {
            Person test = PersonBean.getInstance(this).queryByFingerId(i);
            if (test == null) {
                mFingerId = i;
                break;
            }
        }

        Log.i(TAG, "mFingerId: " + mFingerId);

        onContentChanged();
    }

    private TextWatcher mIdTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            try {
                int val = Integer.parseInt(s.toString());
                if(WelcomeActivity.checkId(val)) {
                    mIdEditText.setTextColor(Color.BLACK);
                } else {
                    mIdEditText.setTextColor(Color.RED);
                }
            } catch (NumberFormatException ex) {
                // Do something
            }
            onContentChanged();
        }
    };

    private TextWatcher mAgeTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            try {
                int val = Integer.parseInt(s.toString());
                if(WelcomeActivity.checkAge(val)) {
                    mAgeEditText.setTextColor(Color.BLACK);
                } else {
                    mAgeEditText.setTextColor(Color.RED);
                }
            } catch (NumberFormatException ex) {
                // Do something
            }
            onContentChanged();
        }
    };

    private TextWatcher mHeightTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            try {
                int val = Integer.parseInt(s.toString());
                if(WelcomeActivity.checkHeight(val)) {
                    mHeightEditText.setTextColor(Color.BLACK);
                } else {
                    mHeightEditText.setTextColor(Color.RED);
                }
            } catch (NumberFormatException ex) {
                // Do something
            }
            onContentChanged();
        }
    };

    private void dissAllwithoutBackNext() {
        int[] reses = {
                R.id.kb_0_button,
                R.id.kb_1_button,
                R.id.kb_2_button,
                R.id.kb_3_button,
                R.id.kb_4_button,
                R.id.kb_5_button,
                R.id.kb_6_button,
                R.id.kb_7_button,
                R.id.kb_8_button,
                R.id.kb_9_button,
                R.id.kb_dot_button,
                R.id.kb_delete_button,
                R.id.kb_softboard_button,
        };

        for (int res : reses) {
            findViewById(res).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.finger_button:
                Intent intent = new Intent(this, GetFingerActivity.class);
                startActivityForResult(intent, REQUEST_CODE_TOUCHID);
                break;
        }
    }

    // from the link above
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
            Toast.makeText(this, "keyboard visible", Toast.LENGTH_SHORT).show();
        } else if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
            Toast.makeText(this, "keyboard hidden", Toast.LENGTH_SHORT).show();
            hideSystemUI(getWindow().getDecorView());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_CANCELED:
                mFingerButton.setEnabled(true);
                play(this, SOUND_32_LOG_UP_TOUCH_ID_FAIL);
                break;
            case RESULT_OK:
                mFingerButton.setEnabled(false);
                play(this, SOUND_31_LOG_UP_TOUCH_ID_DONE);
                onContentChanged();
                break;
        }
    }



    @Override
    public void onBackButtonClick() {
        super.onBackButtonClick();
        exitAsFail(this);
    }

    @Override
    public void onNextButtonClick() {
        super.onNextButtonClick();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String gender = mFeMaleRadio.isChecked() ? Person.GENDER_FEMALE: Person.GENDER_MALE;
        mPerson.setGender(gender);
        try {
            mPerson.setFingerId(mFingerId);
            mPerson.setName(mIdEditText.getText().toString());
            mPerson.setAge(Integer.valueOf(mAgeEditText.getText().toString()));
            mPerson.setHeight(Integer.valueOf(mHeightEditText.getText().toString()));
            mPerson.setDate(dateFormat.format(date));
        } catch (Exception e) {
            Toast.makeText(this, "信息不能为空", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            boolean ret = TouchID.getInstance(this).saveFinger((short) mFingerId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "注册成功", Toast.LENGTH_LONG).show();
        PersonBean.getInstance(this).insert(mPerson);
        finish();
    }

    @Override
    public void onDeleteClick() {
        super.onDeleteClick();
        Toast.makeText(this, "删除按钮按下", Toast.LENGTH_SHORT).show();
    }

    public void onContentChanged() {
        boolean hefa = true;

        WatchDog.getInstance(getApplicationContext()).feed();

        Log.d(TAG, "onContentChanged");
        if (mFingerButton == null
                || mIdEditText == null
                || mHeightEditText == null
                || mAgeEditText == null
                || mNextButton == null) {
            Log.e(TAG, "onContentChanged");
            return;
        }

        // 指纹
        if (mFingerButton.isEnabled()) {
            //Log.e(TAG, "请指纹采集");
            hefa = false;
        }

        if (mIdEditText.getText().toString().equals("")) {
            //Log.e(TAG, "请输入ID");
            hefa = false;
        }

        if (mHeightEditText.getText().toString().equals("")) {
            //Log.e(TAG, "请输入身高");
            hefa = false;
        } else {
            int height = Integer.valueOf(mHeightEditText.getText().toString());
            if (!WelcomeActivity.checkHeight(height))
                hefa = false;
        }

        if (mAgeEditText.getText().toString().equals("")) {
            //Log.e(TAG, "请输入年龄");
            hefa = false;
        } else {
            int age = Integer.valueOf(mAgeEditText.getText().toString());
            if (!WelcomeActivity.checkAge(age))
                hefa = false;
        }

        mNextButton.setEnabled(hefa);
        Log.e(TAG, "setEnable: " + hefa);
    }

    @Override
    protected void onResume() {
        super.onResume();
        WelcomeActivity.hideSystemUI(getWindow().getDecorView());
    }

    /**
     * 判断Person数据库表，如果数据库表为空，那么Empty指纹
     */
    public static void checkMem(Activity activity) {
        // 如果是第一次启动，需要Clean
        PersonBean.getInstance(activity).check(activity);
    }
}
