package com.kangear.bodycompositionanalyzer;

import android.content.Intent;
import android.content.res.Configuration;
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

/**
 * 本页面不显示logo
 */
public class MemRegActivity extends Com2Activity {
    private static final String TAG = "MemRegActivity";
    private TextView mTextView;
    private static final int WEIGHT_ACTIVITY = 1;
    private static final int WEIGHT_STOP = 2;
    private View logoView;
    private Button mFingerButton;
    private EditText mIdEditText;
    private EditText mHeightEditText;
    private EditText mAgeEditText;
    private Button mNextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memreg);
        hideSystemUI(getWindow().getDecorView());
        mTextView = findViewById(R.id.weight_textview);
        logoView = findViewById(R.id.logo_imageview);
        logoView.setVisibility(View.GONE);
        mFingerButton = findViewById(R.id.finger_button);
        mIdEditText = findViewById(R.id.id_edittext);
        mHeightEditText = findViewById(R.id.height_edittext);
        mAgeEditText = findViewById(R.id.age_edittext);
        mNextButton = findViewById(R.id.kb_next_button);

        dissAllwithoutBackNext();
        mIdEditText.addTextChangedListener(mTextWatcher);
        mAgeEditText.addTextChangedListener(mTextWatcher);
        mHeightEditText.addTextChangedListener(mTextWatcher);

        mIdEditText.setText("");
        mAgeEditText.setText("");
        mHeightEditText.setText("");
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
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

                        if (weight > 10) {
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
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.back_button:
                Toast.makeText(this, "返回按钮按下", Toast.LENGTH_SHORT).show();
                break;
            case R.id.next_button:
                Toast.makeText(this, "下一项按钮按下", Toast.LENGTH_SHORT).show();
                break;
            case R.id.finger_button:
                Intent intent = new Intent(this, GetFingerActivity.class);
                startActivityForResult(intent, 0);
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
        Log.e(TAG, "onActivityResult requestCode: " + resultCode);
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
        finish();
    }

    @Override
    public void onNextButtonClick() {
        super.onNextButtonClick();
    }

    @Override
    public void onDeleteClick() {
        super.onDeleteClick();
        Toast.makeText(this, "删除按钮按下", Toast.LENGTH_SHORT).show();
    }

    public void onContentChanged() {
        boolean hefa = true;

        Log.e(TAG, "onContentChanged");
        if (mFingerButton == null
                || mIdEditText == null
                || mHeightEditText == null
                || mAgeEditText == null
                || mNextButton == null) {
            return;
        }

        // 指纹
        if (mFingerButton.isEnabled()) {
            Log.e(TAG, "请指纹采集");
            hefa = false;
        }

        if (mIdEditText.getText().toString().equals("")) {
            Log.e(TAG, "请输入ID");
            hefa = false;
        }

        if (mHeightEditText.getText().toString().equals("")) {
            Log.e(TAG, "请输入身高");
            hefa = false;
        }

        if (mAgeEditText.getText().toString().equals("")) {
            Log.e(TAG, "请输入年龄");
            hefa = false;
        }

        mNextButton.setEnabled(hefa);
    }
}
