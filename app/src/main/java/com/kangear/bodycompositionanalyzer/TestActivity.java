package com.kangear.bodycompositionanalyzer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class TestActivity extends AppCompatActivity {
    private static final String TAG = "TestActivity";
    private static final int FIRST_PAGE_NUMBER = 1;
    private static final int LAST_PAGE_NUMBER  = 2;
    private Button mBackButton;
    private Button mPreButton;
    private Button mNextButton;
    private Button mPrintButton;
    private View mFirstPage;
    private View mLastPage;
    private int WEIGHT_PROGRESS = 70;
    private int TIZHIFANG_PROGRESS = 35;
    private int GUGEJI_PROGRESS = 90;
    private ProgressBar mWeightProgressBar;
    private ProgressBar mGugejiProgressBar;
    private ProgressBar mTizhifangProgressBar;
    private int progress = 0;
    private ImageView mHumanProgress;
    private static final int PECENT_MAX = 100;
    private static final int HUMAN_HIGH = 360; // 360px
    private static final float BILI = HUMAN_HIGH / PECENT_MAX;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        hideSystemUI(getWindow().getDecorView());
        mTextView = findViewById(R.id.progress_textview);
        mHumanProgress = findViewById(R.id.human_frontgound_imageview);
//        mPreButton = findViewById(R.id.previous_page_button);
//        mNextButton = findViewById(R.id.next_page_button);
//        mFirstPage = findViewById(R.id.result_first_page);
//        mLastPage = findViewById(R.id.result_last_page);
//
//        mWeightProgressBar = findViewById(R.id.weight_progressbar);
//        mGugejiProgressBar = findViewById(R.id.gugeji_progressbar);
//        mTizhifangProgressBar = findViewById(R.id.tizhifang_progressbar);
//
//        page(FIRST_PAGE_NUMBER);
        mHandler.sendEmptyMessage(0);
    }

    private void setProgress2(int progress) {
        if (progress == 100) {
            mHumanProgress.setVisibility(View.INVISIBLE);
        } else {
            mHumanProgress.setVisibility(View.VISIBLE);
            mHumanProgress.getLayoutParams().height = (int) ((PECENT_MAX - progress) * BILI);
            mHumanProgress.requestLayout();
        }
        mTextView.setText("分析中..." + progress + "%");
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            setProgress2(progress);
            progress ++;
            if (progress <= 100) {
                sendEmptyMessageDelayed(0, 20);
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
