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
import android.widget.TextView;
import android.widget.Toast;

import org.xutils.ex.DbException;

import static com.kangear.bodycompositionanalyzer.WelcomeActivity.CONST_RECORD_ID;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.INVALID_FINGER_ID;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.INVALID_RECORD_ID;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ResultActivity extends AppCompatActivity {
    private static final String TAG = "ResultActivity";
    private static final int FIRST_PAGE_NUMBER = 1;
    private static final int LAST_PAGE_NUMBER  = 2;
    private Button mPreButton;
    private Button mNextButton;
    private View mFirstPage;
    private View mLastPage;
    private int WEIGHT_PROGRESS = 70;
    private int TIZHIFANG_PROGRESS = 35;
    private int GUGEJI_PROGRESS = 90;
    private ProgressBar mWeightProgressBar;
    private ProgressBar mGugejiProgressBar;
    private ProgressBar mTizhifangProgressBar;
    private int progress = 0;
    private Person mPerson;
    private Record mRecord;
    private Record DEFAULT_RECORD = new Record(new Person("测试值", Person.GENDER_MALE, 0), "2018-01-01");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        hideSystemUI(getWindow().getDecorView());
        getWindow().setSoftInputMode(WindowManager.
                LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mPreButton = findViewById(R.id.previous_page_button);
        mNextButton = findViewById(R.id.next_page_button);
        mFirstPage = findViewById(R.id.result_first_page);
        mLastPage = findViewById(R.id.result_last_page);

        mWeightProgressBar = findViewById(R.id.weight_progressbar);
        mGugejiProgressBar = findViewById(R.id.gugeji_progressbar);
        mTizhifangProgressBar = findViewById(R.id.tizhifang_progressbar);

        int recordId = getIntent().getIntExtra(CONST_RECORD_ID, INVALID_RECORD_ID);
        try {
            mRecord = WelcomeActivity.getDB().selector(Record.class).where("id", "=", recordId).findFirst();
        } catch (DbException e) {
            e.printStackTrace();
        }

        if (mRecord == null) {
            Log.e(TAG, "mRecord == null recordId: " + recordId);
            mRecord = DEFAULT_RECORD;
        }
//        mPerson = mRecord.getPerson();
        try {
            mPerson = WelcomeActivity.getDB().selector(Person.class).where("id", "=", mRecord.getPersonId()).findFirst();
        } catch (DbException e) {
            e.printStackTrace();
        }

        if (mPerson == null) {
            Log.e(TAG, "mPerson == null PersonId: " + mRecord.getPersonId());
        } else {
            ((EditText)findViewById(R.id.id_edittext)).setText(mPerson.getId());
            ((EditText)findViewById(R.id.age_edittext)).setText(String.valueOf(mPerson.getAge()));
            ((EditText)findViewById(R.id.height_edittext)).setText(String.valueOf(mPerson.getHeight()));
            ((EditText)findViewById(R.id.gender_edittext)).setText(mPerson.getGender());

            page(FIRST_PAGE_NUMBER);
        }
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


            mWeightProgressBar.setProgress(progress < WEIGHT_PROGRESS ? progress : WEIGHT_PROGRESS);
            mGugejiProgressBar.setProgress(progress < GUGEJI_PROGRESS ? progress : GUGEJI_PROGRESS);
            mTizhifangProgressBar.setProgress(progress < TIZHIFANG_PROGRESS ? progress : TIZHIFANG_PROGRESS);
            progress ++;
            if (progress <= 100) {
                sendEmptyMessageDelayed(0, 20);
            } else {
                progress = 0;
            }
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
            case R.id.print_button:
                Toast.makeText(this, "打印机未连接", Toast.LENGTH_LONG).show();
                break;
        }
    }
}
