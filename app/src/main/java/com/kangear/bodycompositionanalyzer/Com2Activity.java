package com.kangear.bodycompositionanalyzer;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.kangear.utils.TimeUtils;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Com2Activity extends AppCompatActivity implements iCom2 {
    private boolean hasDot = true;
    private EditText mEditText;
    Button mNextButton;
    private String TAG = "Com2Activity";
    private Button mDotButton;
    private Button mSoftwareBoardButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setView(boolean hasDot, View v) {
        this.hasDot = hasDot;
        this.mEditText = v.findViewById(R.id.edittext);
        this.mNextButton = v.findViewById(R.id.kb_next_button);
        mSoftwareBoardButton = v.findViewById(R.id.kb_softboard_button);
        mDotButton = v.findViewById(R.id.kb_dot_button);

        mDotButton.setVisibility(hasDot ? View.VISIBLE : View.GONE);
        mSoftwareBoardButton.setVisibility(!hasDot ? View.VISIBLE : View.GONE);

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // 有输入，则将使能
                setEnable(canNext(mEditText.getText().toString()));
            }
        });
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
     * @param v
     */
    public void onClick(View v) {
        // Toast.makeText(this, "haha", Toast.LENGTH_SHORT).show();
        if (mEditText == null) {
            return;
        }

        String tmp = mEditText.getText().toString();
        switch (v.getId()) {
            case R.id.kb_0_button:
                tmp += "0";
                break;
            case R.id.kb_1_button:
                tmp += "1";
                break;
            case R.id.kb_2_button:
                tmp += "2";
                break;
            case R.id.kb_3_button:
                tmp += "3";
                break;
            case R.id.kb_4_button:
                tmp += "4";
                break;
            case R.id.kb_5_button:
                tmp += "5";
                break;
            case R.id.kb_6_button:
                tmp += "6";
                break;
            case R.id.kb_7_button:
                tmp += "7";
                break;
            case R.id.kb_8_button:
                tmp += "8";
                break;
            case R.id.kb_9_button:
                tmp += "9";
                break;
            case R.id.kb_dot_button:
                tmp += ".";
                break;
            case R.id.kb_delete_button:
                if (tmp.length() > 0)
                    tmp = tmp.substring(0, tmp.length() - 1);
                break;
            case R.id.kb_next_button:
                onNextButtonClick();
                break;
            case R.id.kb_back_button:
                onBackButtonClick();
                break;
            case R.id.kb_softboard_button:
                // 启动软键盘
                if (mEditText.requestFocus()) {
                    mEditText.setText("");
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(mEditText, InputMethodManager.SHOW_IMPLICIT);
                    isFocus = true;
                }
                break;
        }

        if (tmp.length() <= 4 && !isFocus)
            mEditText.setText(tmp);
    }

    boolean isFocus = false;

    public boolean canNext(String str) {
        return true;
    }

    /**
     * 使能禁用
     * @param isEnable 是否使能
     */
    public void setEnable(boolean isEnable) {
        if (mNextButton != null) {
            mNextButton.setEnabled(isEnable);
            mNextButton.setClickable(isEnable);
            mNextButton.setFocusable(isEnable);
        }
    }

//    /**
//     * @param editText
//     */
//    public void setEditText(EditText editText) {
//        mEditText = editText;
//    }
//
//    public void setNextButton(Button nextButton) {
//        mNextButton = nextButton;
//    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onNextButtonClick() {
    }

    @Override
    public void onBackButtonClick() {
    }

    @Override
    public void onKeyBoardButtonClick() {
    }
}
