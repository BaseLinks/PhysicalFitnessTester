package com.kangear.bodycompositionanalyzer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import static com.kangear.bodycompositionanalyzer.WelcomeActivity.doSettings;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.startSettings;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class AdminPasswordDialogActivity extends AppCompatActivity {
    private static final String TAG = "AdDialogActivity";
    private EditText mPasswordEditText;
    private boolean DEBUG = true;
    private String mPasswd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_admin_passwd);

        mPasswordEditText = findViewById(R.id.passwd_edittext);
        mPasswordEditText.setText("");
        mPasswd = getResources().getString(R.string.admin_passwd);
        setFinishOnTouchOutside(false);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.no_button:
                finish();
                break;
            case R.id.yes_button:
                String passwd = mPasswordEditText.getText().toString();
                if (passwd.equals(mPasswd)) {
                    doSettings(this);
                    finish();
                } else
                    Toast.makeText(this, "密码错误，请重新输入", Toast.LENGTH_LONG).show();
                break;
        }
    }
}
