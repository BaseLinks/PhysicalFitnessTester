package com.kangear.bodycompositionanalyzer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import static com.kangear.bodycompositionanalyzer.WelcomeActivity.CONST_FINGER_ID;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.INVALID_FINGER_ID;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.REQUEST_CODE_TOUCHID;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class HistoryActivity extends AppCompatActivity {
    private static final String TAG = "HistoryActivity";
    private static final int FIRST_PAGE_NUMBER = 1;
    private static final int LAST_PAGE_NUMBER  = 2;
    private Button mPreButton;
    private Button mNextButton;
    private View mFirstPage;
    private View mLastPage;
    private int WEIGHT_PROGRESS = 70;
    private int TIZHIFANG_PROGRESS = 35;
    private int GUGEJI_PROGRESS = 90;
    private int progress = 0;
    private Person mPerson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        hideSystemUI(getWindow().getDecorView());
        getWindow().setSoftInputMode(WindowManager.
                LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mPreButton = findViewById(R.id.previous_page_button);
        mNextButton = findViewById(R.id.next_page_button);
        mFirstPage = findViewById(R.id.result_first_page);
        mLastPage = findViewById(R.id.result_last_page);

//        mPerson = WelcomeActivity.getPerson();

//        ((EditText)findViewById(R.id.id_edittext)).setText(mPerson.getId());
//        ((EditText)findViewById(R.id.age_edittext)).setText(String.valueOf(mPerson.getAge()));
//        ((EditText)findViewById(R.id.height_edittext)).setText(String.valueOf(mPerson.getHeight()));
//        ((EditText)findViewById(R.id.gender_edittext)).setText(mPerson.getGender());

//        page(FIRST_PAGE_NUMBER);
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//            switch (msg.what){
//                case WEIGHT_ACTIVITY:
//                    progress ++;
//                    break;
//                case WEIGHT_STOP:
//                    stopTest();
//                    break;
//            }
        }
    };

    private void page(int page) {
        switch (page) {
            case FIRST_PAGE_NUMBER:
                mHandler.sendEmptyMessage(0);
                mLastPage.setVisibility(View.INVISIBLE);
                mPreButton.setVisibility(View.INVISIBLE);
                mNextButton.setVisibility(View.VISIBLE);
                mFirstPage.setVisibility(View.VISIBLE);
                break;
            case LAST_PAGE_NUMBER:
                mLastPage.setVisibility(View.VISIBLE);
                mPreButton.setVisibility(View.VISIBLE);
                mNextButton.setVisibility(View.INVISIBLE);
                mFirstPage.setVisibility(View.INVISIBLE);
                break;
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

    public void onClick(View v) {
        Intent intent = new Intent(this, WelcomeActivity.class);
        switch (v.getId()) {
            case R.id.back_button:
                setResult(RESULT_CANCELED, intent);
                finish();
                break;
            case R.id.next_page_button:
                page(LAST_PAGE_NUMBER);
                break;
            case R.id.previous_page_button:
                page(FIRST_PAGE_NUMBER);
                break;
            case R.id.vip_query_button:
                intent = new Intent(this, TouchIdActivity.class);
                startActivityForResult(intent, REQUEST_CODE_TOUCHID);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case REQUEST_CODE_TOUCHID:
                if (resultCode == RESULT_OK) {
                    // get finger id
                    // query all date
                    // TODO: show to user
                    Log.d(TAG, "intent: " + intent.getIntExtra(CONST_FINGER_ID, INVALID_FINGER_ID));
                } else {
                    Log.d(TAG, "fuck you");
                }
                break;
        }
    }
}
