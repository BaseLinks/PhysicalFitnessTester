package com.kangear.bodycompositionanalyzer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.constraint.Guideline;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kangear.bodycompositionanalyzer.databinding.ActivityResultBinding;
import com.kangear.utils.QRCodeUtil;

import java.nio.ByteBuffer;
import java.util.Arrays;

import static com.kangear.bodycompositionanalyzer.BodyComposition.BMI;
import static com.kangear.bodycompositionanalyzer.BodyComposition.LEVEL_HIGH;
import static com.kangear.bodycompositionanalyzer.BodyComposition.LEVEL_LOW;
import static com.kangear.bodycompositionanalyzer.BodyComposition.LEVEL_NORMAL;
import static com.kangear.bodycompositionanalyzer.BodyComposition.体脂百分比;
import static com.kangear.bodycompositionanalyzer.BodyComposition.体脂肪量;
import static com.kangear.bodycompositionanalyzer.BodyComposition.体重;
import static com.kangear.bodycompositionanalyzer.BodyComposition.内脏面积;
import static com.kangear.bodycompositionanalyzer.BodyComposition.右下脂肪量;
import static com.kangear.bodycompositionanalyzer.BodyComposition.基础代谢;
import static com.kangear.bodycompositionanalyzer.BodyComposition.左上肢肌肉量;
import static com.kangear.bodycompositionanalyzer.BodyComposition.性别;
import static com.kangear.bodycompositionanalyzer.BodyComposition.总能耗;
import static com.kangear.bodycompositionanalyzer.BodyComposition.无机盐;
import static com.kangear.bodycompositionanalyzer.BodyComposition.腰臀比;
import static com.kangear.bodycompositionanalyzer.BodyComposition.评分;
import static com.kangear.bodycompositionanalyzer.BodyComposition.身体年龄;
import static com.kangear.bodycompositionanalyzer.BodyComposition.骨骼肌;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.CONST_RECORD_ID;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.FORMAT_WEIGHT;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.INVALID_RECORD_ID;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.PERSON_ID_INVALID;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.startPdf;
import static com.kangear.common.utils.ByteArrayUtils.bytesToHex;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ResultActivity extends AppCompatActivity {
    private static final String TAG = "ResultActivity";
    private static final int FIRST_PAGE_NUMBER = 1;
    private static final int LAST_PAGE_NUMBER  = 2;
    private static final Record DEFAULT_RECORD = new Record(0, PERSON_ID_INVALID, "default", 25, 180, Person.GENDER_MALE, 70, 0);
    private Button mPreButton;
    private Button mNextButton;
    private View mFirstPage;
    private View mLastPage;
    private int WEIGHT_PROGRESS = 70;
    private int TIZHIFANG_PROGRESS = 35;
    private int GUGEJI_PROGRESS = 90;
    private int progress = 0;
    private BodyComposition mBodyComposition;
    public static final int LESS_LEVEL_WIDTH = 149;
    public static final int NOMAL_LEVEL_WIDTH = 78;
    public static final int MORE_LEVEL_WIDTH  = 255 - 55;

    public static final String FLOAT_2_FORMAT                      = "%.2f";
    public static final String FLOAT_1_FORMAT                      = "%.1f";
    public static final String FLOAT_0_FORMAT                      = "%.0f";
    public String FLOAT_ZHIFANG_TIAOZHENGLIANG_FORMAT;
    public String FLOAT_JIROU_TIAOZHENGLIANG_FORMAT;

    private static final int YINGYANGPINGGU_LESS_LEVEL_WIDTH = 74 + 2;
    private static final int YINGYANGPINGGU_NOMAL_LEVEL_WIDTH = 70 + 2;
    private static final int YINGYANGPINGGU_MORE_LEVEL_WIDTH = 72 + 2;

    private Record mRecord = null;

    @BindingAdapter({"android:layout_height"})
    public static void setLayoutHeight(View view, int height) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = height;
        view.setLayoutParams(layoutParams);
    }

//    @BindingAdapter({"app:layout_constraintWidth_percent"})
//    public static void setConstraintWidth_percent(ConstraintLayout view, float percent) {
//        ConstraintLayout constraintLayout = (ConstraintLayout) view.getParent();
//        ConstraintSet constraintSet = new ConstraintSet();
//        constraintSet.clone(constraintLayout);
//        constraintSet.setGuidelinePercent();
//        constraintLayout.setConstraintSet(constraintSet);
//    }

    @BindingAdapter({"layout_constraintWidth_percent"})
    public static void setLayoutConstraintWidthPercent(ConstraintLayout view, float percent) {
//        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams)view.getLayoutParams();
//        layoutParams.matchConstraintPercentWidth = 0.5f;
//        view.setLayoutParams(layoutParams);
    }

    @BindingAdapter("layout_constraintGuide_percent")
    public static void setLayoutConstraintGuideBegin(Guideline guideline, float percent) {
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) guideline.getLayoutParams();
        params.guidePercent = percent;
        guideline.setLayoutParams(params);
    }

    private static void setProgressOfTichengfenfenxi(BodyComposition.Third t, View view) {
        final int PECENT_MAX = 100;
        final int HUMAN_HIGH = LESS_LEVEL_WIDTH + NOMAL_LEVEL_WIDTH + MORE_LEVEL_WIDTH; // 208px
        final float TWO_GE = (float) (50.8 * 2);
        final float BILI = HUMAN_HIGH / PECENT_MAX;
        final int progress = t.getProgress(LESS_LEVEL_WIDTH, NOMAL_LEVEL_WIDTH, MORE_LEVEL_WIDTH);
        final ImageView progressView = view.findViewById(R.id.shentichengfenfenxi_frontgound_imageview);
        final TextView textView = view.findViewById(R.id.progress_textview);
        progressView.setVisibility(View.VISIBLE);
        progressView.getLayoutParams().width = (int) (((PECENT_MAX - progress) * BILI) + TWO_GE);
        progressView.requestLayout();
        textView.setText(String.format(FORMAT_WEIGHT, t.getCur()) + t.getUnit());
    }

    public static float getGugejiScore(final BodyComposition bc) {
        float score = (float) (bc.骨骼肌.getProgress(0, NOMAL_LEVEL_WIDTH, MORE_LEVEL_WIDTH) * 0.2);
        if (bc.骨骼肌.getLevel() == LEVEL_LOW)
            score = 0;
        Log.i(TAG, "骨骼肌 占的分数:  " + score);
        return score;
    }

    /**
     * 这里的Record入口应该是Record对象，因为像临时测试是不存数据库，读数据库并不适合
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityResultBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_result);
//        setContentView(R.layout.activity_result);
        hideSystemUI(getWindow().getDecorView());
        getWindow().setSoftInputMode(WindowManager.
                LayoutParams.SOFT_INPUT_STATE_HIDDEN);

//        byte[] data2 = {
//                /*0x55, (byte)0xAA, (byte)0xCD, 0x02, 0x31, 0x00, */0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
//                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
//                0x00, 0x00, 0x01, 0x28, (byte)0xA4, 0x06, 0x20, 0x03, (byte)0x9D, 0x1A, 0x7D, 0x16,
//                0x50, 0x00, 0x74, 0x0B, 0x48, 0x0B, 0x65, 0x01, (byte)0xF9, 0x09, 0x78, 0x09,
//                0x13, 0x15, 0x53, 0x13, 0x00, 0x00, 0x00, 0x00, 0x00, 0x7B, 0x02, 0x16,
//                0x02,(byte) 0xB5, 0x02, 0x33, 0x02, (byte)0xFC, 0x01, 0x1B, 0x02, (byte)0xED, 0x00, 0x5F,
//                0x00, 0x7F, 0x00, 0x10, 0x02, (byte)0xD5, 0x01, (byte)0xF5, 0x01, 0x2F, 0x01, 0x0A,
//                0x01, 0x4A, 0x01, (byte)0x99, 0x01, 0x6D, 0x01, (byte)0x84, 0x01, 0x77, 0x00, 0x64,
//                0x00, 0x71, 0x00, 0x23, 0x24, 0x26, 0x00, (byte)0x81, 0x00, (byte)0x8F, 0x00, (byte)0xA3,
//                0x00, 0x18, 0x01, (byte)0xF5, 0x00, 0x09, 0x01, 0x23, 0x1F, 0x21, 0x24, 0x1F,
//                0x21, 0x19, 0x01, (byte)0xEA, 0x00, (byte)0xF9, 0x00, 0x56, 0x00, 0x56, 0x00, 0x5C,
//                0x00, 0x5A, 0x00, 0x56, 0x00, 0x5C, 0x00, 0x0F, 0x06, 0x08, 0x0E, 0x06,
//                0x08, (byte)0x8C, 0x00, 0x2F, 0x00, 0x3F, 0x00, 0x23, 0x11, 0x17, 0x21, 0x11,
//                0x17, (byte)0xEB, 0x04, (byte)0x84, 0x03, 0x4C, 0x04, 0x14, 0x01, (byte)0xB9, 0x00, (byte)0xF0,
//                0x00, 0x28, 0x01, (byte)0x96, 0x00, (byte)0xC8, 0x00, 0x40, 0x04, 0x3E, 0x04, 0x5D,
//                0x04, (byte)0xB1, 0x03, 0x5D, 0x50, 0x5A, 0x1F, 0x1E, 0x23, 0x03, 0x04, (byte)0xF0,
//                0x04, (byte)0xA4, 0x02, 0x2B, (byte)0xA5, (byte)0x80, 0x7E, (byte)0x80, 0x2B, (byte)0x80, 0x0F, 0x07,
//                (byte)0xDC, 0x0A, (byte)0x81, (byte)0xA8
//        };
//
//        mRecord = new Record(0, 0, "xiaoguo2", 343, 1, "xxxx", 323, 2222);
//        mRecord.setData(data2);
//        mRecord.getBodyComposition();

        mPreButton = findViewById(R.id.previous_page_button);
        mNextButton = findViewById(R.id.next_page_button);
        mFirstPage = findViewById(R.id.result_first_page);
        mLastPage = findViewById(R.id.result_last_page);

        page(FIRST_PAGE_NUMBER);

        int recordId = getIntent().getIntExtra(CONST_RECORD_ID, INVALID_RECORD_ID);
        if (recordId != INVALID_RECORD_ID) {
            mRecord = RecordBean.getInstance(this).query(recordId);
        }

        if (mRecord == null) {
            return;
        }
        mBodyComposition = mRecord.getBodyComposition();

        binding.setRecord(mRecord);
        if (true)
            return;

        FLOAT_ZHIFANG_TIAOZHENGLIANG_FORMAT = "-" + FLOAT_1_FORMAT + mBodyComposition.体脂肪量.getUnit();
        FLOAT_JIROU_TIAOZHENGLIANG_FORMAT   = "+" + FLOAT_1_FORMAT + mBodyComposition.骨骼肌.getUnit();

        if (mRecord == null) {
            mRecord = DEFAULT_RECORD;
            Toast.makeText(this, "获取记录异常", Toast.LENGTH_SHORT).show();
        }

        // -Page 1
        // --基本信息
        ((EditText)findViewById(R.id.id_edittext)).setText(mRecord.getName());
        ((EditText)findViewById(R.id.age_edittext)).setText(String.valueOf(mRecord.getAge()) + mBodyComposition.年龄.getUnit());
        ((EditText)findViewById(R.id.height_edittext)).setText(String.valueOf(mRecord.getHeight()) + mBodyComposition.身高.getUnit());
        ((EditText)findViewById(R.id.gender_edittext)).setText(mRecord.getGender() + mBodyComposition.性别.getUnit());

        // --综合评价
        // ---脂肪调整量
        ((EditText)findViewById(R.id.zhifangtiaozheng_edittext)).setText(String.format(FLOAT_ZHIFANG_TIAOZHENGLIANG_FORMAT, mBodyComposition.getZhifangAdjustment()));

        // ---肌肉调整量
        ((EditText)findViewById(R.id.jiroutiaozheng_edittext)).setText(String.format(FLOAT_JIROU_TIAOZHENGLIANG_FORMAT, mBodyComposition.getJirouAdjustment()));

        // ---内脏面积
        fillOne(R.id.neizangmianji_edittext, mBodyComposition.内脏面积, FLOAT_1_FORMAT);

        // ---健康指数(评分)
        fillOne(R.id.jiankangzhishu_edittext, mBodyComposition.评分, FLOAT_1_FORMAT);

        // ---体重 骨骼肌 体脂肪量
//        mWeightProgressBar.setProgress(mBodyComposition.体重.getProgress(LESS_LEVEL_WIDTH, NOMAL_LEVEL_WIDTH, MORE_LEVEL_WIDTH));
//        mGugejiProgressBar.setProgress(mBodyComposition.骨骼肌.getProgress(LESS_LEVEL_WIDTH, NOMAL_LEVEL_WIDTH, MORE_LEVEL_WIDTH));
//        mTizhifangProgressBar.setProgress(mBodyComposition.体脂肪量.getProgress(LESS_LEVEL_WIDTH, NOMAL_LEVEL_WIDTH, MORE_LEVEL_WIDTH));
//        fillProgress(mWeightProgressBar, mBodyComposition.体重);
        setProgressOfTichengfenfenxi(mBodyComposition.体重, findViewById(R.id.weight_progressbar));
        setProgressOfTichengfenfenxi(mBodyComposition.骨骼肌, findViewById(R.id.gugeji_progressbar));
        setProgressOfTichengfenfenxi(mBodyComposition.体脂肪量, findViewById(R.id.tizhifang_progressbar));

        // ---身体水分
        fillOne(R.id.shentishuifen_edittext, mBodyComposition.身体水分, FLOAT_1_FORMAT);

        // ---去脂体重
        fillOne(R.id.quzhitizhong_edittext, mBodyComposition.去脂体重, FLOAT_1_FORMAT);

        // -Page 2
        // --肥胖分析
        // ---BMI 体脂百分比 腰臀比 基础代谢
        fillTwo(R.id.bmi_edittext, R.id.bmi_normal_edittext, mBodyComposition.BMI, FLOAT_1_FORMAT);
        fillTwo(R.id.tizhibi_edittext, R.id.tizhibi_normal_edittext, mBodyComposition.体脂百分比, FLOAT_1_FORMAT);
        fillTwo(R.id.yaotunbi_edittext, R.id.yaotunbi_normal_edittext, mBodyComposition.腰臀比, FLOAT_2_FORMAT);
        fillOne(R.id.jichudaixie_edittext, mBodyComposition.基础代谢, FLOAT_0_FORMAT);

        // -- 蛋白质 无机盐
        fillYingYangPingGu(
                R.id.danbaizhi_zhengchang_edittext,
                R.id.danbaizhi_progressbar,
                mBodyComposition.蛋白质);
        fillYingYangPingGu(
                R.id.wujiyan_zhengchang_edittext,
                R.id.wujiyan_progressbar,
                mBodyComposition.无机盐);
        // -- 总能耗
        ((EditText)findViewById(R.id.zongnenghao_edittext)).setText(
                String.format(FLOAT_0_FORMAT + mBodyComposition.总能耗.getUnit(),
                        mBodyComposition.总能耗.getCur()));

        // -- 阶段脂肪
        fillYelloMan(
                new YelloMan(
                        "阶段脂肪",
                        mBodyComposition.左上脂肪量,
                        mBodyComposition.左下脂肪量,
                        mBodyComposition.右上脂肪量,
                        mBodyComposition.右下脂肪量,
                        mBodyComposition.躯干脂肪量
                ),
                findViewById(R.id.zhifang_yellow_human)

        );

        // -- 阶段肌肉
        fillYelloMan(
                new YelloMan(
                        "阶段肌肉",
                        mBodyComposition.左上肢肌肉量,
                        mBodyComposition.左下肌肉量,
                        mBodyComposition.右上肢肌肉量,
                        mBodyComposition.右下肌肉量,
                        mBodyComposition.躯干肌肉量
                ),
                findViewById(R.id.jirou_yellow_human)

        );

        page(FIRST_PAGE_NUMBER);
    }

    public static class YelloMan {
        String leftArmLevel;
        String leftArm;
        String leftLegeLevel;
        String leftLege;

        String rightArmLevel;
        String rightArm;
        String rightLegeLevel;
        String rightLege;

        String quganLevel;
        String qugan;

        String title;

        public YelloMan(String title,
                        BodyComposition.Third la,
                        BodyComposition.Third ll,
                        BodyComposition.Third ra,
                        BodyComposition.Third rl,
                        BodyComposition.Third qg) {
            this.title = title;
            this.leftArmLevel = la.getLevelAsChinese();
            this.leftArm = String.format(FLOAT_1_FORMAT + la.getUnit(), la.getCur());
            this.leftLegeLevel = ll.getLevelAsChinese();
            this.leftLege = String.format(FLOAT_1_FORMAT + ll.getUnit(), ll.getCur());
            this.rightArmLevel = ra.getLevelAsChinese();
            this.rightArm = String.format(FLOAT_1_FORMAT + ra.getUnit(), ra.getCur());
            this.rightLegeLevel = rl.getLevelAsChinese();
            this.rightLege = String.format(FLOAT_1_FORMAT + rl.getUnit(), rl.getCur());
            this.quganLevel = qg.getLevelAsChinese();
            this.qugan = String.format(FLOAT_1_FORMAT + qg.getUnit(), qg.getCur());
        }

        public String getLeftArmLevel() {
            return leftArmLevel;
        }

        public String getLeftArm() {
            return leftArm;
        }

        public String getLeftLegeLevel() {
            return leftLegeLevel;
        }

        public String getLeftLege() {
            return leftLege;
        }

        public String getRightArmLevel() {
            return rightArmLevel;
        }

        public String getRightArm() {
            return rightArm;
        }

        public String getRightLegeLevel() {
            return rightLegeLevel;
        }

        public String getRightLege() {
            return rightLege;
        }

        public String getQuganLevel() {
            return quganLevel;
        }

        public String getQugan() {
            return qugan;
        }

        public String getTitle() {
            return title;
        }
    }

    public static void fillYelloMan(YelloMan yelloMan, View ym) {
        if (yelloMan == null || ym == null)
            return;

        if (yelloMan.getTitle() != null)
            ((TextView) ym.findViewById(R.id.yellowhuman_title_textview)).setText(yelloMan.getTitle());

        ((EditText) ym.findViewById(R.id.la_level_edittext)).setText(yelloMan.getLeftArmLevel());
        ((EditText) ym.findViewById(R.id.ll_level_edittext)).setText(yelloMan.getLeftLegeLevel());
        ((EditText) ym.findViewById(R.id.qugan_level_edittext)).setText(yelloMan.getQuganLevel());
        ((EditText) ym.findViewById(R.id.ra_level_edittext)).setText(yelloMan.getRightArmLevel());
        ((EditText) ym.findViewById(R.id.rl_level_edittext)).setText(yelloMan.getRightLegeLevel());

        ((EditText) ym.findViewById(R.id.la_edittext)).setText(yelloMan.getLeftArm());
        ((EditText) ym.findViewById(R.id.ll_edittext)).setText(yelloMan.getLeftLege());
        ((EditText) ym.findViewById(R.id.qugan_edittext)).setText(yelloMan.getQugan());
        ((EditText) ym.findViewById(R.id.ra_edittext)).setText(yelloMan.getRightArm());
        ((EditText) ym.findViewById(R.id.rl_edittext)).setText(yelloMan.getRightLege());
    }


    private void fillOne(int curId, BodyComposition.Third t, String format) {
        EditText et = findViewById(curId);
        et.setText(String.format(format + t.getUnit(), t.getCur()));
    }

    private void fillTwo(int curId, int norId, BodyComposition.Third t, String format) {
        EditText et = findViewById(curId);
        EditText et2 = findViewById(norId);
        et.setText(String.format(format, t.getCur()) + t.getUnit());
        et2.setText(String.format(format,t.getMin()) + "-" + String.format(format, t.getMax()));
    }

    private void fillYingYangPingGu(int curId, int progressid, BodyComposition.Third t) {
//        EditText etMin = findViewById(minId);
        EditText etCur = findViewById(curId);
//        EditText etMax = findViewById(maxId);
        ProgressBar pb = findViewById(progressid);

        String valueStr = String.format(FLOAT_1_FORMAT, t.getCur()) + t.getUnit();
        String minStr = "";
        String curStr = "";
        String maxStr = "";

        int level = t.getLevel();
        switch (level) {
            case BodyComposition.LEVEL_LOW:
                minStr = valueStr;
                break;
            case BodyComposition.LEVEL_NORMAL:
                curStr = valueStr;
                break;
            case BodyComposition.LEVEL_HIGH:
                maxStr = valueStr;
                break;
        }

//        etMin.setText(minStr);
        etCur.setText(valueStr);
//        etMax.setText(maxStr);
        pb.setProgress(t.getProgress(YINGYANGPINGGU_LESS_LEVEL_WIDTH, YINGYANGPINGGU_NOMAL_LEVEL_WIDTH, YINGYANGPINGGU_MORE_LEVEL_WIDTH));
    }

    private void page(int page) {
        switch (page) {
            case FIRST_PAGE_NUMBER:
//                mHandler.sendEmptyMessage(0);
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

    private Bitmap createQr() {
        byte[] qrData = mRecord.getData().clone();

        int BYTE_BUFFER_ALLOCATE = 1024;
        ByteBuffer target = ByteBuffer.allocate(1024);
        target.put(Arrays.copyOfRange(qrData, 性别.getCurStart(), 体重.getCurStart() + 体重.getLength()));
        target.put(Arrays.copyOfRange(qrData, 体重.getMinStart(), 体重.getMaxStart() + 体重.getLength()));
        target.put(Arrays.copyOfRange(qrData, 体脂肪量.getCurStart(), 体脂肪量.getMaxStart() + 体脂肪量.getLength()));
        target.put(Arrays.copyOfRange(qrData, 骨骼肌.getCurStart(), 无机盐.getMaxStart() + 无机盐.getLength()));
        target.put(Arrays.copyOfRange(qrData, 左上肢肌肉量.getCurStart(), 右下脂肪量.getMaxStart() + 右下脂肪量.getLength()));
        target.put(Arrays.copyOfRange(qrData, BMI.getCurStart(), 体脂百分比.getMaxStart() + 体脂百分比.getLength()));
        target.put(Arrays.copyOfRange(qrData, 腰臀比.getCurStart(), 腰臀比.getMaxStart() + 腰臀比.getLength()));
        target.put(Arrays.copyOfRange(qrData, 内脏面积.getCurStart(), 内脏面积.getCurStart() + 内脏面积.getLength()));
        target.put(Protocol.getDataFromShort((int) 评分.getCur()));
        target.put((byte) 身体年龄.getCur()); // 身体年龄
        Log.i(TAG, "身体年龄: " + 身体年龄.getCur());
        target.put((byte) 0x00); //肌肉调整符号
        target.put(Protocol.getDataFromShort((int) (mBodyComposition.getJirouAdjustment() * 10.0)));
        target.put((byte) 0x01); //脂肪调整符号
        target.put(Protocol.getDataFromShort((int) (mBodyComposition.getZhifangAdjustment() * 10.0)));
        target.put(Protocol.getDataFromShort((int) (基础代谢.getCur())));
        target.put(Arrays.copyOfRange(qrData, 总能耗.getCurStart(), 总能耗.getCurStart() + 总能耗.getLength()));
        target.limit(target.remaining());
        target.rewind();
        /* 提取 */
        byte[] byteArray = new byte[BYTE_BUFFER_ALLOCATE - target.remaining()];
        target.get(byteArray);
        /* 将byteBuffer清理 */
        target.clear();

        Log.i(TAG, bytesToHex(byteArray));

        // 加密 -0xAA
        for (int i=0; i<byteArray.length; i++) {
            byteArray[i] -= (byte)0xAA;
        }

        String qrcontent = bytesToHexWithoutSpace(byteArray);
        Log.i(TAG, qrcontent);

        return QRCodeUtil.createQRCodeBitmap(qrcontent, 480, 480);
    }

    public static String bytesToHexWithoutSpace(byte[] in) {
        StringBuilder builder = new StringBuilder();
        byte[] var2 = in;
        int var3 = in.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            byte b = var2[var4];
            builder.append(String.format("%02X", b));
        }

        return builder.toString();
    }

    public void onClick(View v) {
        Intent intent = new Intent(this, WelcomeActivity.class);
        switch (v.getId()) {
            case R.id.back_button:
                setResult(RESULT_CANCELED, intent);
                finish();
                // 返回到体重
//                WelcomeActivity.startWelcome(this);
                break;
            case R.id.next_page_button:
                page(LAST_PAGE_NUMBER);
                break;
            case R.id.previous_page_button:
                page(FIRST_PAGE_NUMBER);
                break;
            case R.id.qr_button:
                Toast.makeText(this, "打印二维码", Toast.LENGTH_SHORT).show();
                ImageView image = new ImageView(this);
                image.setImageBitmap(createQr());
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(this).
                                setMessage("微信扫一扫收藏本次测试结果").
                                setView(image);
                builder.create().show();
                break;
            case R.id.print_button:
                if (mRecord != null)
                    startPdf(this, mRecord.getId());
                break;
        }
    }
}
