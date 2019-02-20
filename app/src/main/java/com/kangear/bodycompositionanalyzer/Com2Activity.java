package com.kangear.bodycompositionanalyzer;

import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
    private String regular;
    private Button mBackButton;
    private int mNumberLength = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setView(boolean hasDot, View v, final String regular) {
        this.hasDot = hasDot;
        this.mEditText = v.findViewById(R.id.tizhibi_edittext);
        this.mNextButton = v.findViewById(R.id.kb_next_button);
        this.regular = regular;
        mSoftwareBoardButton = v.findViewById(R.id.kb_softboard_button);
        mDotButton = v.findViewById(R.id.kb_dot_button);
        mBackButton = v.findViewById(R.id.kb_back_button);

        mDotButton.setVisibility(hasDot ? View.VISIBLE : View.GONE);
        mSoftwareBoardButton.setVisibility(!hasDot ? View.VISIBLE : View.GONE);

        if (mEditText != null) {
            mEditText.addTextChangedListener(new TextWatcher() {
                CharSequence before = "";
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (regular != null && !editable.toString().matches(regular) && !"".equals(editable.toString())) {
                        mEditText.setSelection(mEditText.getText().toString().length());
                    }

                    // 有输入，则将使能
                    setEnable(canNext(mEditText.getText().toString()));
                }
            });

            mEditText.setOnFocusChangeListener(new View.OnFocusChangeListener(){
                @Override
                public void onFocusChange(View v, boolean hasFocus){
                    Log.i(TAG, "hasFocus: " + hasFocus);
                    if (hasFocus){
                        //键盘弹起事件
                    }
                }
            });
        }
    }

    void setLongBackButton() {
        mBackButton.setBackgroundResource(R.drawable._50_kb_back_long);
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        params.height = 112;
        params.width = 350;
        params.startToStart = mNextButton.getId();
        params.endToEnd = mNextButton.getId();
        mBackButton.setLayoutParams(params);
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
        String tmp = "";
        if (mEditText != null) {
            tmp = mEditText.getText().toString();
        }
//        Toast.makeText(this, "haha: " + tmp, Toast.LENGTH_SHORT).show();
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
                onDeleteClick();
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
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(mEditText, InputMethodManager.SHOW_IMPLICIT);
                }
                break;
        }

        if (mEditText != null && tmp.length() <= mNumberLength) {
            mEditText.setText(tmp);
            // 将光标调到末尾
            // 使用tmp.length()是err
            mEditText.setSelection(mEditText.getText().toString().length());
        }
    }

    public void setNumberLength(int value) {
        mNumberLength = value;
    }

    @Override
    public void onDeleteClick() {

    }

//    boolean isFocus = false;

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
        MusicService.stop(this);
    }

    @Override
    public void onKeyBoardButtonClick() {
    }
}
