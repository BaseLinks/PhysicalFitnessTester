package com.kangear.bodycompositionanalyzer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;

import com.kangear.common.utils.InputFilterUtils;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class HeightActivity extends Com2Activity {
    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_height);
        hideSystemUI(getWindow().getDecorView());
        mEditText = findViewById(R.id.edittext);
        mEditText.setClickable(false);
        mEditText.setEnabled(false);;

        mEditText.setText("");
        mEditText.setFilters(new InputFilter[]{ new InputFilterUtils.MinMax(1, 250)});
    }

    /**
     * check is can next
     * @return
     */
    @Override
    public boolean canNext(String str) {
        return str.length() > 0;
    }
}
