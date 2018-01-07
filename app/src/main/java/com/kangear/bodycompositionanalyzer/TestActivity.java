package com.kangear.bodycompositionanalyzer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.xutils.ex.DbException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.kangear.bodycompositionanalyzer.WelcomeActivity.CONST_RECORD_ID;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.DEFAULT_GUGEJI;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.DEFAULT_JICHUDAIXIELIANG;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.DEFAULT_SHENTIZHILIANGZHISHU;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.DEFAULT_TIZHIBAIFENBI;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.DEFAULT_TIZHIFANG;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.FORMAT_WEIGHT;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 1. 显示「测试准备」界面，发送测试指令，收到从机回复的标识后，等待(N_SEC)后显示界面
 * 2. DEFAULT_TEST_COST_TIME(32)秒进度条走到DEFAULT_TEST_PROGRESS(95)，收到数据并解析完毕，
 * 进度条DEFAULT_TEST_PROGRESS_MAX(100)，显示[返回，详情，打印]等按钮，2秒体重等依次进度条走到对应位置。
 */
public class TestActivity extends AppCompatActivity {
    private static final String TAG = "TestActivity";
    private static final int DEFAULT_TEST_COST_TIME         = 1 * 1000;
    private static final int DEFAULT_TEST_PROGRESS_MAX      = 100;
    private static final int DEFAULT_TEST_PROGRESS          = DEFAULT_TEST_PROGRESS_MAX; //95;
    private static final int DEFAULT_TEST_SEC_PROGRESS_TIME = 2 * 1000;
    private static final int PROGRESS_STEP_TIME             = DEFAULT_TEST_COST_TIME / DEFAULT_TEST_PROGRESS;
    private static final String TEXT_EMPTY = "";

    private ProgressBar mWeightProgressBar;
    private ProgressBar mGugejiProgressBar;
    private ProgressBar mTizhifangProgressBar;
    private int progress = 0;
    private ImageView mHumanProgress;
    private static final int PECENT_MAX = 100;
    private static final int HUMAN_HIGH = 360; // 360px
    private static final float BILI = HUMAN_HIGH / PECENT_MAX;
    private TextView mTextView;
    private View mButtonsView;
    private View mProgressView;
    private static final int LOW            = 1;
    private static final int NORMAL         = 2;
    private static final int HIGH           = 3;
    private double weightProgress           = 67.4;
    private double gugejiProgress           = 36.3;
    private double tizhifangProgress        = 46.1;
    private int shentizhiliangzhishuLevel   = LOW;
    private double shentizhiliangzhishu     = 30.1;
    private int tizhibaifenbiLevel          = HIGH;
    private double tizhibaifenbi            = 32.7;
    private double jichudaixieliang         = 1787;

    private static final int SHOW_CLEAN                     =  7;
    private static final int SHOW_WAIT                      =  8;
    private static final int SHOW_TEST                      =  9;
    private static final int SHOW_TEST_DONE                 = 10;
    private static final int SHOW_WEIGHT_DONE               = 11;
    private static final int SHOW_GUGEJI_DONE               = 12;
    private static final int SHOW_TIZHIFANG_DONE            = 13;
    private static final int SHOW_SHENTIZHILIANGZHISHU_DONE = 14;
    private static final int SHOW_TIZHIFANG_BAIFENGBI_DONE  = 15;
    private static final int SHOW_JICHUDAIXIELIANG_DONE     = 16;
    private TextView mWeightTextView;
    private TextView mGugejiTextView;
    private TextView mTizhifangTextView;
    private TextView mJichudaixieliangTextView;
    private TextView mShentizhiliangzhishuTextView;
    private TextView mTizhibaifenbiTextView;
    private RadioGroup mTizhibaifenbiRadioGroup;
    private RadioGroup mShentizhiliangzhishuRadioGroup;
    private View mWaitView;
    private View mTestView;
    private TextView mHeadGenderTextView;
    private TextView mHeadIdTextView;
    private TextView mHeadWeightTextView;
    private TextView mHeadHeightTextView;
    private TextView mHeadAgeTextView;

    private Record mRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        mRecord = WelcomeActivity.getRecord();

        hideSystemUI(getWindow().getDecorView());
        mTextView                 = findViewById(R.id.progress_textview);
        mHumanProgress            = findViewById(R.id.human_frontgound_imageview);
        mButtonsView              = findViewById(R.id.buttons_constrainlayout);
        mProgressView             = findViewById(R.id.progress_constrainlayout);

        mWeightProgressBar        = findViewById(R.id.weight_progressbar);
        mWeightTextView           = findViewById(R.id.weight_result_textview);
        mGugejiProgressBar        = findViewById(R.id.gugeji_progressbar);
        mGugejiTextView           = findViewById(R.id.gugeji_result_textview);
        mTizhifangProgressBar     = findViewById(R.id.tizhifang_progressbar);
        mTizhifangTextView        = findViewById(R.id.tizhifang_result_textview);

        mShentizhiliangzhishuTextView          = findViewById(R.id.shentizhiliangzhishu_result_textview);
        mTizhibaifenbiTextView                 = findViewById(R.id.tizhibaifenbi_result_textview);
        mJichudaixieliangTextView              = findViewById(R.id.jichudaixie_result_textview);

        mShentizhiliangzhishuRadioGroup        = findViewById(R.id.shentizhiliangzhishu_radiogroup);
        mTizhibaifenbiRadioGroup               = findViewById(R.id.tizhibaifenbi_radiogroup);

        mWaitView = findViewById(R.id.test_first_page);
        mTestView = findViewById(R.id.test_last_page);

        mHeadGenderTextView = findViewById(R.id.gender_textview);
        mHeadIdTextView     = findViewById(R.id.id_textview);
        mHeadWeightTextView = findViewById(R.id.weight_textview);
        mHeadHeightTextView = findViewById(R.id.height_textview);
        mHeadAgeTextView    = findViewById(R.id.age_textview);

//        page(FIRST_PAGE_NUMBER);
        mHandler.sendEmptyMessage(SHOW_CLEAN);

        Record record = WelcomeActivity.getRecord();
        Log.e(TAG, "" + record.toString());
        mHeadIdTextView.setText(record.getName());
        mHeadGenderTextView.setText(record.getGender());
        mHeadAgeTextView.setText(String.valueOf(record.getAge()));
        mHeadWeightTextView.setText(String.valueOf(record.getWeight()));
        mHeadHeightTextView.setText(String.valueOf(record.getHeight()));

        mWeightTextView.setText(String.valueOf(record.getWeight()));
        weightProgress         = record.getWeight();
        gugejiProgress         = DEFAULT_GUGEJI;
        tizhifangProgress      = DEFAULT_TIZHIFANG;
        shentizhiliangzhishu   = DEFAULT_SHENTIZHILIANGZHISHU;
        tizhibaifenbi          = DEFAULT_TIZHIBAIFENBI;
        jichudaixieliang       = DEFAULT_JICHUDAIXIELIANG;

    }

    private void setProgress2(int progress) {
        if (progress == DEFAULT_TEST_PROGRESS_MAX) {
            mHumanProgress.setVisibility(View.INVISIBLE);
            // 测试完成
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            mRecord.setDate(dateFormat.format(new Date()));
            mRecord.setPersonId((int) System.currentTimeMillis());
            RecordBean.getInstance(this).insert(mRecord);
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
            switch (msg.what) {
                case SHOW_CLEAN: // show weight
                    mWeightProgressBar.setProgress(0);
                    mGugejiProgressBar.setProgress(0);
                    mTizhifangProgressBar.setProgress(0);

                    mWeightTextView.setText(TEXT_EMPTY);
                    mGugejiTextView.setText(TEXT_EMPTY);
                    mTizhifangTextView.setText(TEXT_EMPTY);

                    mShentizhiliangzhishuTextView.setText(TEXT_EMPTY);
                    mTizhibaifenbiTextView.setText(TEXT_EMPTY);
                    mJichudaixieliangTextView.setText(TEXT_EMPTY);

//                    mShentizhiliangzhishuHighRadioButton.setChecked(false);
//                    mShentizhiliangzhishuNormalRadioButton.setChecked(false);
//                    mShentizhiliangzhishuLowRadioButton.setChecked(false);
//
//                    mTizhibaifenbiHighRadioButton.setChecked(false);
//                    mTizhibaifenbiNormalRadioButton.setChecked(false);
//                    mTizhibaifenbiLowRadioButton.setChecked(false);
                    mHandler.sendEmptyMessageDelayed(SHOW_WAIT, 1 * 1000);
                    // test
                    break;
                case SHOW_WAIT: // 等待测试 界面
                    mWaitView.setVisibility(View.VISIBLE);
                    mTestView.setVisibility(View.GONE);
                    mHandler.sendEmptyMessageDelayed(SHOW_TEST, 3 * 1000);
                    break;
                case SHOW_TEST: // 测试中界面 界面
                    mWaitView.setVisibility(View.GONE);
                    mTestView.setVisibility(View.VISIBLE);
                    setProgress2(progress);
                    progress ++;
                    if (progress <= DEFAULT_TEST_PROGRESS_MAX) {
                        sendEmptyMessageDelayed(msg.what, PROGRESS_STEP_TIME);
                    } else {
                        progress = 0;
                        sendEmptyMessageDelayed(SHOW_TEST_DONE, PROGRESS_STEP_TIME);
                    }
                    break;
                case SHOW_TEST_DONE: // show weight
                    update(weightProgress, mWeightProgressBar, msg.what, SHOW_WEIGHT_DONE, mWeightTextView);
                    break;
                case SHOW_WEIGHT_DONE: // show gugeji
                    update(gugejiProgress, mGugejiProgressBar, msg.what, SHOW_GUGEJI_DONE, mGugejiTextView);
                    break;
                case SHOW_GUGEJI_DONE: // show tizhifang
                    update(tizhifangProgress, mTizhifangProgressBar, msg.what, SHOW_TIZHIFANG_DONE, mTizhifangTextView);
                    break;
                case SHOW_TIZHIFANG_DONE: // show 身体质量指数
                    updateFeipangchengfen(shentizhiliangzhishu,
                            1,
                            mShentizhiliangzhishuRadioGroup,
                            getShentizhiliangzhishuRadioButtonResId(shentizhiliangzhishuLevel),
                            msg.what,
                            SHOW_SHENTIZHILIANGZHISHU_DONE,
                            mShentizhiliangzhishuTextView);
                    break;
                case SHOW_SHENTIZHILIANGZHISHU_DONE: // show 体脂百分比
                    updateFeipangchengfen(tizhibaifenbi,
                            1,
                            mTizhibaifenbiRadioGroup,
                            getTizhibaifenbiRadioButtonResId(tizhibaifenbiLevel),
                            msg.what,
                            SHOW_TIZHIFANG_BAIFENGBI_DONE,
                            mTizhibaifenbiTextView);
                    break;
                case SHOW_TIZHIFANG_BAIFENGBI_DONE: // show 体脂百分比
                    mJichudaixieliangTextView.startAnimation(AnimationUtils.loadAnimation(TestActivity.this, R.anim.test_textview));
                    mJichudaixieliangTextView.setText(String.valueOf((int)jichudaixieliang));
                    mHandler.sendEmptyMessageDelayed(SHOW_JICHUDAIXIELIANG_DONE, 2 * 1000);
                    break;

                case SHOW_JICHUDAIXIELIANG_DONE:
                    // 显示buttons
                    mButtonsView.setVisibility(View.VISIBLE);
                    mProgressView.setVisibility(View.GONE);
                    break;
            }
        }
    };

    private int getShentizhiliangzhishuRadioButtonResId(int level) {
        int ret = R.id.shentizhiliangzhishu_low_radiobutton;
        switch (level) {
            case LOW:
                ret = R.id.shentizhiliangzhishu_low_radiobutton;
                break;
            case NORMAL:
                ret = R.id.shentizhiliangzhishu_normal_radiobutton;
                break;
            case HIGH:
                ret = R.id.shentizhiliangzhishu_high_radiobutton;
                break;
        }
        return ret;
    }

    private int getTizhibaifenbiRadioButtonResId(int level) {
        int ret = R.id.tizhibaifenbi_low_radiobutton;
        switch (level) {
            case LOW:
                ret = R.id.tizhibaifenbi_low_radiobutton;
                break;
            case NORMAL:
                ret = R.id.tizhibaifenbi_normal_radiobutton;
                break;
            case HIGH:
                ret = R.id.tizhibaifenbi_high_radiobutton;
                break;
        }
        return ret;
    }

    private void update(double max, ProgressBar pb, int curWhat, int nextWhat, TextView tv) {
        progress ++;
        if (progress <= max) {
            pb.setProgress(progress);
            mHandler.sendEmptyMessageDelayed(curWhat, 20);
        } else {
            tv.setText(String.format(FORMAT_WEIGHT, max));
            tv.startAnimation(AnimationUtils.loadAnimation(this, R.anim.test_textview));
            progress = 0;
            mHandler.sendEmptyMessageDelayed(nextWhat, 1 * 1000);
        }
    }

    private void updateFeipangchengfen(double value, double max, RadioGroup rg, int rb, int curWhat, int nextWhat, TextView tv) {
        progress ++;
        if (progress <= max) {
            tv.startAnimation(AnimationUtils.loadAnimation(this, R.anim.test_textview));
            tv.setText(String.format(FORMAT_WEIGHT, value));
            mHandler.sendEmptyMessageDelayed(curWhat, 50);
        } else {
            if (rg != null)
                rg.check(rb);
            progress = 0;
            mHandler.sendEmptyMessageDelayed(nextWhat, 1 * 1000);
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
        Intent intent;
        switch (v.getId()) {
            case R.id.back_button:
                finish();
                break;
            case R.id.detail_button:
                intent = new Intent(this, ResultActivity.class);
                intent.putExtra(CONST_RECORD_ID, mRecord.getId());
                startActivity(intent);
                break;
            case R.id.print_button:
                Toast.makeText(this, "打印机未连接", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
