package com.kangear.bodycompositionanalyzer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class AdDialogActivity extends AppCompatActivity {
    private static final String TAG = "AdDialogActivity";
    private EditText companyEditText;
    private EditText numberEditText;
    private boolean DEBUG = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_ad_text);

        companyEditText = findViewById(R.id.company_edittext);
        numberEditText = findViewById(R.id.number_edittext);

        Other o1 = OtherBean.getInstance(this).queryByName(Other.OTHER_NAME_COMPANY);
        Other o2 = OtherBean.getInstance(this).queryByName(Other.OTHER_NAME_NUMBER);
        String c = o1 == null ? "" : o1.getStrValue();
        String n = o2 == null ? "" : o2.getStrValue();
        Toast.makeText(this, "company: " + c + " number: " + n, Toast.LENGTH_LONG).show();
        companyEditText.setText(c);
        numberEditText.setText(n);
        companyEditText.setSelection(c.length());
        numberEditText.setSelection(n.length());

        setFinishOnTouchOutside(false);
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

    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.no_button:
//                setResult(RESULT_CANCELED, intent);
                break;
            case R.id.yes_button:
//                setResult(RESULT_OK, intent);
                OtherBean.getInstance(this).insert(new Other(Other.OTHER_NAME_COMPANY, companyEditText.getText().toString()));
                OtherBean.getInstance(this).insert(new Other(Other.OTHER_NAME_NUMBER, numberEditText.getText().toString()));

                if (DEBUG) {
                    Other o1 = OtherBean.getInstance(this).queryByName(Other.OTHER_NAME_COMPANY);
                    Other o2 = OtherBean.getInstance(this).queryByName(Other.OTHER_NAME_NUMBER);
                    String c = o1 == null ? "" : o1.getStrValue();
                    String n = o2 == null ? "" : o2.getStrValue();
                    Toast.makeText(this, "company: " + c + " number: " + n, Toast.LENGTH_LONG).show();
                }
                break;
        }

        finish();
    }

//    test() {
//        final AlertDialog alert = new AlertDialog.Builder(this).create();
//        LayoutInflater mInflater = LayoutInflater.from(this);
//        View adText = mInflater.inflate(R.layout.dialog_ad_text, null, false);
//        final EditText company = adText.findViewById(R.id.company_edittext);
//        final EditText number = adText.findViewById(R.id.number_edittext);
//
//        Other o1 = OtherBean.getInstance(mContext).queryByName(Other.OTHER_NAME_COMPANY);
//        Other o2 = OtherBean.getInstance(mContext).queryByName(Other.OTHER_NAME_NUMBER);
//        String c = o1 == null ? "null" : o1.getStrValue();
//        String n = o2 == null ? "null" : o2.getStrValue();
//        Toast.makeText(mContext, "company: " + c + " number: " + n, Toast.LENGTH_LONG).show();
//        company.setText(c);
//        number.setText(n);
//
//        View.OnClickListener mOnClickListener = new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                switch (view.getId()) {
//                    case R.id.yes_button:
//                        OtherBean.getInstance(mContext).insert(new Other(Other.OTHER_NAME_COMPANY, company.getText().toString()));
//                        OtherBean.getInstance(mContext).insert(new Other(Other.OTHER_NAME_NUMBER, number.getText().toString()));
//                        break;
//                    case R.id.no_button:
//                        break;
//                }
//
//                Other o1 = OtherBean.getInstance(mContext).queryByName(Other.OTHER_NAME_COMPANY);
//                Other o2 = OtherBean.getInstance(mContext).queryByName(Other.OTHER_NAME_NUMBER);
//                String c = o1 == null ? "null" : o1.getStrValue();
//                String n = o2 == null ? "null" : o2.getStrValue();
//                Toast.makeText(mContext, "company: " + c + " number: " + n, Toast.LENGTH_LONG).show();
//                alert.dismiss();
//            }
//        };
//        Button yes = adText.findViewById(R.id.yes_button);
//        yes.setOnClickListener(mOnClickListener);
//        Button no = adText.findViewById(R.id.no_button);
//        no.setOnClickListener(mOnClickListener);
//
//        alert.setView(adText);
//        alert.show();
//    }
}
