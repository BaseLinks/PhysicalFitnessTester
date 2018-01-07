package com.kangear.bodycompositionanalyzer;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.xutils.db.Selector;
import org.xutils.ex.DbException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.kangear.bodycompositionanalyzer.WelcomeActivity.CONST_FINGER_ID;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.INVALID_FINGER_ID;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.MAX_AGE;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.MAX_HEIGHT;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.MIN_AGE;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.MIN_HEIGHT;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.REQUEST_CODE_TOUCHID;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.exitAsFail;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.unkownError;

/**
 * 本页面不显示logo
 */
public class MemRegActivity extends Com2Activity {
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
        logoView = findViewById(R.id.logo_imageview);
        logoView.setVisibility(View.GONE);
        mFingerButton = findViewById(R.id.finger_button);
        mIdEditText = findViewById(R.id.id_edittext);
        mHeightEditText = findViewById(R.id.height_edittext);
        mAgeEditText = findViewById(R.id.age_edittext);
        mNextButton = findViewById(R.id.kb_next_button);
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
            try {
                Person test = WelcomeActivity.getDB().selector(Person.class).where("fingerId", "=", i).findFirst();
                if (test == null) {
                    mFingerId = i;
                    break;
                }
            } catch (DbException e) {
                e.printStackTrace();
            }
        }

        Log.i(TAG, "mFingerId: " + mFingerId);
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
                if(val >= MIN_AGE && val <= MAX_AGE) {
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
                if(val >= MIN_HEIGHT && val <= MAX_HEIGHT) {
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
                intent.putExtra(CONST_FINGER_ID, mFingerId);
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
                break;
            case RESULT_OK:
                mFingerButton.setEnabled(false);
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
        mPerson.setFingerId(mFingerId);
        mPerson.setName(mIdEditText.getText().toString());
        mPerson.setAge(Integer.valueOf(mAgeEditText.getText().toString()));
        mPerson.setHeight(Integer.valueOf(mHeightEditText.getText().toString()));
        mPerson.setDate(dateFormat.format(date));
        Toast.makeText(this, "注册成功", Toast.LENGTH_LONG).show();
        try {
            WelcomeActivity.getDB().save(mPerson);
        } catch (DbException e) {
            e.printStackTrace();
            Log.e(TAG, "DB().save error");
        }
        finish();
    }

    @Override
    public void onDeleteClick() {
        super.onDeleteClick();
        Toast.makeText(this, "删除按钮按下", Toast.LENGTH_SHORT).show();
    }

    public void onContentChanged() {
        boolean hefa = true;

        //onActivityResultLog.e(TAG, "onContentChanged");
        if (mFingerButton == null
                || mIdEditText == null
                || mHeightEditText == null
                || mAgeEditText == null
                || mNextButton == null) {
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
            if (height < MIN_HEIGHT || height > MAX_HEIGHT)
                hefa = false;
        }

        if (mAgeEditText.getText().toString().equals("")) {
            //Log.e(TAG, "请输入年龄");
            hefa = false;
        } else {
            int age = Integer.valueOf(mAgeEditText.getText().toString());
            if (age < MIN_AGE || age > MAX_AGE)
                hefa = false;
        }
        mNextButton.setEnabled(hefa);
    }

    @Override
    protected void onResume() {
        super.onResume();
        WelcomeActivity.hideSystemUI(getWindow().getDecorView());
    }

    /**
     * 判断Person数据库表，如果数据库表为空，那么Empty指纹
     */
    public static void checkMem(Context context) {
        PersonBean.getInstance(context).check();
    }
}
