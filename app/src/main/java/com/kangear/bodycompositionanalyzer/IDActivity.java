package com.kangear.bodycompositionanalyzer;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kangear.utils.InputFilterUtils;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class IDActivity extends Com2Activity {
    private EditText mEditText;
    private Button mDotButton;
    private Button mSoftwareBoardButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id);
        hideSystemUI(getWindow().getDecorView());
        mEditText = findViewById(R.id.edittext);
        setEditText(mEditText);
        mEditText.setClickable(false);
        mEditText.setEnabled(false);;
        mEditText.setText("");
        mNextButton = findViewById(R.id.kb_next_button);

        mSoftwareBoardButton = findViewById(R.id.kb_softboard_button);
        mDotButton = findViewById(R.id.kb_dot_button);

        mSoftwareBoardButton.setVisibility(View.VISIBLE);
        mDotButton.setVisibility(View.GONE);

        setEnable(false);
    }

    /**
     * check is can next
     * @return
     */
    @Override
    public boolean canNext() {
        return mEditText.getText().toString().length() > 0;
    }

    public void setEnable(boolean isEnable) {
        if (mNextButton != null) {
            mNextButton.setEnabled(isEnable);
            mNextButton.setClickable(isEnable);
            mNextButton.setFocusable(isEnable);
        }
    }
}
