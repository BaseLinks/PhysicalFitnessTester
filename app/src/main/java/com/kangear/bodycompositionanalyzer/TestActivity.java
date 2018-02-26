package com.kangear.bodycompositionanalyzer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kangear.common.utils.ByteArrayUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.kangear.bodycompositionanalyzer.Protocol.MSG_STATE_DONE;
import static com.kangear.bodycompositionanalyzer.Protocol.MSG_STATE_TESTING_1;
import static com.kangear.bodycompositionanalyzer.Protocol.MSG_STATE_TESTING_2;
import static com.kangear.bodycompositionanalyzer.Protocol.MSG_STATE_TESTING_3;
import static com.kangear.bodycompositionanalyzer.Protocol.MSG_STATE_TESTING_4;
import static com.kangear.bodycompositionanalyzer.Protocol.MSG_STATE_WAIT;
import static com.kangear.bodycompositionanalyzer.Protocol.PROTOCAL_GENDER_FEMALE;
import static com.kangear.bodycompositionanalyzer.Protocol.PROTOCAL_GENDER_MALE;
import static com.kangear.bodycompositionanalyzer.Person.GENDER_MALE;
import static com.kangear.bodycompositionanalyzer.ResultActivity.FLOAT_0_FORMAT;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.CONST_RECORD_ID;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.FORMAT_WEIGHT;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.HANDLE_EVENT_TICHENGFEN_ERROR;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.HANDLE_EVENT_UPDATE_TICHENGFEN_PROGRESS;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.PERSON_ID_ANONYMOUS;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.PERSON_ID_INVALID;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.RECORD_ID_ANONYMOUS;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.hideSystemUI;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.startPdf;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.startTichengfenTest;

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
    private static final int LOW            = BodyComposition.LEVEL_LOW;
    private static final int NORMAL         = BodyComposition.LEVEL_NORMAL;
    private static final int HIGH           = BodyComposition.LEVEL_HIGH;
    private int shentizhiliangzhishuLevel   = LOW;
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
    private Context mContext;
    private BodyComposition mBodyComposition;

    // |_(低于)_|_(正常)_|_(超过)_| from UI
    private static final float 低于_WIDTH_PX = 110;
    private static final float 正常_WIDTH_PX = 110;
    private static final float 超过_WIDTH_PX = 140;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mContext = this;

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
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            float curValue;
            int progress;
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

                    mHandler.sendEmptyMessage(SHOW_WAIT);
                    // test
                    break;
                case SHOW_WAIT: // 等待测试 界面
                    mWaitView.setVisibility(View.VISIBLE);
                    mTestView.setVisibility(View.GONE);
//                    mHandler.sendEmptyMessageDelayed(SHOW_TEST, 3 * 1000);
                    byte gender = (mRecord.getGender().equals(GENDER_MALE)) ? PROTOCAL_GENDER_MALE : PROTOCAL_GENDER_FEMALE;
                    byte age = (byte)(mRecord.getAge() & 0xFF);
                    short height = (short) (((int)mRecord.getHeight() & 0xFFFF) * 10);
                    short weight = (short) (((int)mRecord.getWeight() & 0xFFFF) * 10);
                    startTichengfenTest((Activity) mContext, mHandler, gender, age, height, weight);
                    break;
                case SHOW_TEST: // 测试中界面 界面
                    mWaitView.setVisibility(View.GONE);
                    mTestView.setVisibility(View.VISIBLE);
//                    setProgress2(progress);
//                    progress ++;
//                    if (progress <= DEFAULT_TEST_PROGRESS_MAX) {
//                        sendEmptyMessageDelayed(msg.what, PROGRESS_STEP_TIME);
//                    } else {
//                        progress = 0;
//                        sendEmptyMessageDelayed(SHOW_TEST_DONE, PROGRESS_STEP_TIME);
//                    }
                    break;

                case SHOW_TEST_DONE: // show weight
                    mBodyComposition = mRecord.getBodyComposition();
                    progress = mBodyComposition.体重.getProgress(低于_WIDTH_PX, 正常_WIDTH_PX, 超过_WIDTH_PX);
                    curValue = mBodyComposition.体重.getCur();
                    update(progress, curValue, mWeightProgressBar, msg.what, SHOW_WEIGHT_DONE, mWeightTextView);
                    break;


                case SHOW_WEIGHT_DONE: // show gugeji
                    curValue = mBodyComposition.骨骼肌.getCur();
                    progress = mBodyComposition.骨骼肌.getProgress(低于_WIDTH_PX, 正常_WIDTH_PX, 超过_WIDTH_PX);
                    update(progress, curValue, mGugejiProgressBar, msg.what, SHOW_GUGEJI_DONE, mGugejiTextView);
                    break;


                case SHOW_GUGEJI_DONE: // show tizhifang
                    curValue = mBodyComposition.体脂肪量.getCur();
                    progress = mBodyComposition.体脂肪量.getProgress(低于_WIDTH_PX, 正常_WIDTH_PX, 超过_WIDTH_PX);
                    update(progress, curValue, mTizhifangProgressBar, msg.what, SHOW_TIZHIFANG_DONE, mTizhifangTextView);
                    break;


                case SHOW_TIZHIFANG_DONE: // show 身体质量指数(BMI)
                    curValue = mBodyComposition.BMI.getCur();
                    progress = mBodyComposition.BMI.getLevel();
                    updateFeipangchengfen(curValue,
                            1,
                            mShentizhiliangzhishuRadioGroup,
                            getShentizhiliangzhishuRadioButtonResId(progress),
                            msg.what,
                            SHOW_SHENTIZHILIANGZHISHU_DONE,
                            mShentizhiliangzhishuTextView);
                    break;


                case SHOW_SHENTIZHILIANGZHISHU_DONE: // show 体脂百分比
                    curValue = mBodyComposition.体脂百分比.getCur();
                    progress = mBodyComposition.体脂百分比.getLevel();
                    updateFeipangchengfen(curValue,
                            1,
                            mTizhibaifenbiRadioGroup,
                            getTizhibaifenbiRadioButtonResId(progress),
                            msg.what,
                            SHOW_TIZHIFANG_BAIFENGBI_DONE,
                            mTizhibaifenbiTextView);
                    break;

                case SHOW_TIZHIFANG_BAIFENGBI_DONE: // show 基础代谢量
                    curValue = mBodyComposition.基础代谢.getCur();
                    mJichudaixieliangTextView.startAnimation(AnimationUtils.loadAnimation(TestActivity.this, R.anim.test_textview));
                    mJichudaixieliangTextView.setText(String.format(FLOAT_0_FORMAT, curValue));
                    mHandler.sendEmptyMessageDelayed(SHOW_JICHUDAIXIELIANG_DONE, 2 * 1000);
                    break;

                case SHOW_JICHUDAIXIELIANG_DONE:
                    // 显示buttons
                    mButtonsView.setVisibility(View.VISIBLE);
                    mProgressView.setVisibility(View.GONE);
                    break;

                case HANDLE_EVENT_UPDATE_TICHENGFEN_PROGRESS:
                    progress = msg.arg1;
                    mTextView.setText("分析中..." + progress + "%");
                    switch (progress) {
                        case 20:
                            mWaitView.setVisibility(View.GONE);
                            mTestView.setVisibility(View.VISIBLE);
                            mHumanProgress.setVisibility(View.VISIBLE);
                            mHumanProgress.getLayoutParams().height = (int) ((PECENT_MAX - progress) * BILI);
                            mHumanProgress.requestLayout();
                            break;
                        case 100:
                            mHumanProgress.setVisibility(View.INVISIBLE);
                            // 测试完成
                            if (mRecord.getPersonId() != PERSON_ID_INVALID) {
                                if (mRecord.getPersonId() != PERSON_ID_ANONYMOUS) {
                                    RecordBean.getInstance(mContext).insert(mRecord);
                                    Toast.makeText(mContext, "会员测试保存成功", Toast.LENGTH_LONG).show();
                                } else {
                                    mRecord.setId(RECORD_ID_ANONYMOUS);
                                    RecordBean.getInstance(mContext).update(mRecord);
                                    Toast.makeText(mContext, "临时测试保存成功", Toast.LENGTH_LONG).show();
                                }
                            }
                            mHandler.sendEmptyMessageDelayed(SHOW_TEST_DONE, PROGRESS_STEP_TIME);
                            break;
                        default:
                            mHumanProgress.setVisibility(View.VISIBLE);
                            mHumanProgress.getLayoutParams().height = (int) ((PECENT_MAX - progress) * BILI);
                            mHumanProgress.requestLayout();
                            break;
                    }
                    break;
                case HANDLE_EVENT_TICHENGFEN_ERROR:
                    finish();
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

    private void update(double maxProgress, double curValule, ProgressBar pb, int curWhat, int nextWhat, TextView tv) {
        progress ++;
        if (progress <= maxProgress) {
            pb.setProgress(progress);
            mHandler.sendEmptyMessageDelayed(curWhat, 10);
        } else {
            tv.setText(String.format(FORMAT_WEIGHT, curValule));
            tv.startAnimation(AnimationUtils.loadAnimation(this, R.anim.test_textview));
            progress = 0;
            mHandler.sendEmptyMessageDelayed(nextWhat, 10);
        }
    }

    private void updateFeipangchengfen(double value, double max, RadioGroup rg, int rb, int curWhat, int nextWhat, TextView tv) {
        progress ++;
        if (progress <= max) {
            tv.startAnimation(AnimationUtils.loadAnimation(this, R.anim.test_textview));
            tv.setText(String.format(FORMAT_WEIGHT, value));
            mHandler.sendEmptyMessageDelayed(curWhat, 25);
        } else {
            if (rg != null)
                rg.check(rb);
            progress = 0;
            mHandler.sendEmptyMessageDelayed(nextWhat, 10);
        }
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
                startPdf(this, mRecord.getId());
                break;
        }
    }
}
