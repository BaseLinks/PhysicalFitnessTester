package com.kangear.bodycompositionanalyzer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.kangear.bodycompositionanalyzer.ResultActivity.FLOAT_0_FORMAT;
import static com.kangear.bodycompositionanalyzer.ResultActivity.FLOAT_1_FORMAT;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.CONST_PERSON_ID;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.FORMAT_WEIGHT;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.PERSON_ID_INVALID;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.hideSystemUI;

/**
 * 入口: personId,heck recently 10 times history record by personId
 */
public class PdfActivity extends AppCompatActivity {
    private String TAG = "PdfActivity";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private int personId;
    private static final String JIBENXINXI_DATE_FORMAT  = "yyyy.MM.dd";
    private static final String JIBENXINXI_TIME_FORMAT  = "hh:mm";
//    public static final String DATE_FORMAT  = "yy.MM.dd";
    public static final String DATE_FORMAT  = "hh.mm.ss";
    private static float TICHENGFENFENXI_LESS_WIDTH = 78;
    private static float TICHENGFENFENXI_NOMAL_WIDTH = 40;
    private static float TICHENGFENFENXI_MORE_WIDTH = 90;
    private static float YINGYANGPINGGU_LESS_WIDTH = 25;
    private static float YINGYANGPINGGU_NOMAL_WIDTH = 25;
    private static float YINGYANGPINGGU_MORE_WIDTH = 25;

    public String FLOAT_ZHIFANG_TIAOZHENGLIANG_FORMAT;
    public String FLOAT_JIROU_TIAOZHENGLIANG_FORMAT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdf_20180115);
        hideSystemUI(getWindow().getDecorView());

        personId = getIntent().getIntExtra(CONST_PERSON_ID, PERSON_ID_INVALID);

        mRecyclerView = findViewById(R.id.history_recyclerview);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        Log.i(TAG, "PersonId: " + personId);
        List<Record> mRecords = new ArrayList<>();
        if (personId == PERSON_ID_INVALID) {
            mRecords.add(new Record(0, PERSON_ID_INVALID, "default", 25, 180, Person.GENDER_MALE, 70, 0));
            mRecords.add(new Record(0, PERSON_ID_INVALID, "default", 25, 180, Person.GENDER_MALE, 70, 0));
            mRecords.add(new Record(0, PERSON_ID_INVALID, "default", 25, 180, Person.GENDER_MALE, 70, 0));
            mRecords.add(new Record(0, PERSON_ID_INVALID, "default", 25, 180, Person.GENDER_MALE, 70, 0));
            mRecords.add(new Record(0, PERSON_ID_INVALID, "default", 25, 180, Person.GENDER_MALE, 70, 0));
            mRecords.add(new Record(0, PERSON_ID_INVALID, "default", 25, 180, Person.GENDER_MALE, 70, 0));
            mRecords.add(new Record(0, PERSON_ID_INVALID, "default", 25, 180, Person.GENDER_MALE, 70, 0));
            mRecords.add(new Record(0, PERSON_ID_INVALID, "default", 25, 180, Person.GENDER_MALE, 70, 0));
            mRecords.add(new Record(0, PERSON_ID_INVALID, "default", 25, 180, Person.GENDER_MALE, 70, 0));
            mRecords.add(new Record(0, PERSON_ID_INVALID, "default", 25, 180, Person.GENDER_MALE, 70, 0));
        } else {
            mRecords = RecordBean.getInstance(this).findRecentlyListById(personId, 10);
            Collections.reverse(mRecords);
            // parse BodyComposition
            for (Record record : mRecords) {
                byte[] data = record.getData();
                if (data != null) {
                    record.setBodyComposition(new BodyComposition(data));
                }
            }
        }
        Log.i(TAG, "mRecords: " + mRecords.size());
        Toast.makeText(this, "历史记录数:"+mRecords.size(), Toast.LENGTH_LONG).show();
        mAdapter = new RecordPdfAdapter(mRecords);
        mRecyclerView.setAdapter(mAdapter);

        Record record = WelcomeActivity.getRecord();
        BodyComposition bc = record.getBodyComposition();

        // Company and Number
        Other o1 = OtherBean.getInstance(this).queryByName(Other.OTHER_NAME_COMPANY);
        Other o2 = OtherBean.getInstance(this).queryByName(Other.OTHER_NAME_NUMBER);
        String c = o1 == null ? "" : o1.getStrValue();
        String n = o2 == null ? "" : o2.getStrValue();
        Toast.makeText(this, "company: " + c + " number: " + n, Toast.LENGTH_LONG).show();
        ((TextView)findViewById(R.id.company_textview)).setText(c);
        ((TextView)findViewById(R.id.number_textview)).setText(n);

        // JiBenXinXi
        // - ID, Date, Time, Gender
        ((TextView)findViewById(R.id.name_textview)).setText(record.getName());
        ((TextView)findViewById(R.id.gender_textview)).setText(record.getGender());
        Date date = new Date(record.getTime());
        ((TextView)findViewById(R.id.jibenxinxi_date_textview)).setText(new SimpleDateFormat(JIBENXINXI_DATE_FORMAT).format(date));
        ((TextView)findViewById(R.id.time_textview)).setText(new SimpleDateFormat(JIBENXINXI_TIME_FORMAT).format(date));

        //
        fillOne(bc.身高, R.id.height_textview, true, FORMAT_WEIGHT);
        fillOne(bc.年龄, R.id.age_textview, true, FLOAT_0_FORMAT);
        fillOne(bc.评分, R.id.jibenxinxi_jiankangzhishu_textview, false, FLOAT_1_FORMAT);

        // 身体成分分析
        fillRange(bc.体重, R.id.weight_range_textview, false, FLOAT_1_FORMAT);
        fillRange(bc.骨骼肌, R.id.gugeji_range_textview, false, FLOAT_1_FORMAT);
        fillRange(bc.体脂肪量, R.id.tizhifang_range_textview, false, FLOAT_1_FORMAT);
        setProgressOfTichengfenfenxi(bc.体重, findViewById(R.id.weight_progressbar));
        setProgressOfTichengfenfenxi(bc.骨骼肌, findViewById(R.id.gugeji_progressbar));
        setProgressOfTichengfenfenxi(bc.体脂肪量, findViewById(R.id.tizhifang_progressbar));

        // -身体水分
        fillCurAndRange(bc.身体水分, R.id.shentishuifen_textview, true, FLOAT_1_FORMAT);
        // -去脂体重
        fillCurAndRange(bc.去脂体重, R.id.quzhitizhong_textview, true, FLOAT_1_FORMAT);

        // 营养评估
        // -蛋白质 无机盐 总能耗
        fillOne(bc.蛋白质, R.id.danbaizhi_textview, true, FLOAT_1_FORMAT);
        fillOne(bc.无机盐, R.id.wujiyan_textview, true, FLOAT_1_FORMAT);
        fillOne(bc.总能耗, R.id.zongnenghao_textview, true, FLOAT_0_FORMAT);
        setProgressOfYingYangPingGu(bc.蛋白质, findViewById(R.id.danbaizhi_progressbar));
        setProgressOfYingYangPingGu(bc.无机盐, findViewById(R.id.wujiyan_progressbar));

        // 肥胖分析
        fillCurAndRange(bc.BMI, R.id.bmi_textview, R.id.bmi_range_textview, true, FLOAT_1_FORMAT);
        fillCurAndRange(bc.体脂百分比, R.id.tizhibaifenbi_textview, R.id.tizhibaifenbi_range_textview, true, FLOAT_1_FORMAT);
        fillCurAndRange(bc.腰臀比, R.id.yaotunbi_textview, R.id.yaotunbi_range_textview, true, FLOAT_1_FORMAT);
        fillOne(bc.基础代谢, R.id.jichudaixie_textview, true, FLOAT_0_FORMAT);

        // zonghepingjia
        // -内脏面积
        fillCurAndRange(bc.内脏面积, R.id.neizangmianji_textview, true, FLOAT_1_FORMAT);

        // -阶段
        // - 阶段脂肪
        fillYelloMan(
                new ResultActivity.YelloMan(
                        null,
                        bc.左上脂肪量,
                        bc.左下脂肪量,
                        bc.右上脂肪量,
                        bc.右下脂肪量,
                        bc.躯干脂肪量
                ),
                findViewById(R.id.zhifang_yellow_human)

        );

        // - 阶段肌肉
        fillYelloMan(
                new ResultActivity.YelloMan(
                        null,
                        bc.左上肢肌肉量,
                        bc.左下肌肉量,
                        bc.右上肢肌肉量,
                        bc.右下肌肉量,
                        bc.躯干肌肉量
                ),
                findViewById(R.id.jirou_yellow_human)

        );

        // 综合评价
        FLOAT_ZHIFANG_TIAOZHENGLIANG_FORMAT = "-" + FLOAT_1_FORMAT + bc.体脂肪量.getUnit();
        FLOAT_JIROU_TIAOZHENGLIANG_FORMAT   = "+" + FLOAT_1_FORMAT + bc.骨骼肌.getUnit();
        // -脂肪调整量
        ((TextView)findViewById(R.id.zhifangtiaozheng_textview)).setText(String.format(FLOAT_ZHIFANG_TIAOZHENGLIANG_FORMAT, bc.getZhifangAdjustment()));
        // -肌肉调整量
        ((TextView)findViewById(R.id.jiroutiaozheng_textview)).setText(String.format(FLOAT_JIROU_TIAOZHENGLIANG_FORMAT, bc.getJirouAdjustment()));
        // -健康指数(评分)
        fillOne(bc.评分, R.id.jiankangzhishu_textview, false, FLOAT_1_FORMAT);

        // 生物电阻抗
        // -5k
        fillOne(bc._5k电阻, R.id._5k_textview, true, FLOAT_0_FORMAT);
        // -50k
        fillOne(bc._50k电阻, R.id._50k_textview, true, FLOAT_0_FORMAT);
        // -250k
        fillOne(bc._250k电阻, R.id._250k_textview, true, FLOAT_0_FORMAT);
    }

    // 单个框
    void fillOne(final BodyComposition.Third t, final int textviewId,
                 final boolean isNeedUnit, final String format) {
        String text = String.format(format, t.getCur());
        if (isNeedUnit)
            text += t.getUnit();
        ((TextView)findViewById(textviewId)).setText(text);
    }

    void fillRange(final BodyComposition.Third t, final int textviewId,
                 final boolean isNeedUnit, final String format) {
        String text = String.format(format, t.getMin()) + "-" + String.format(format, t.getMax());
        ((TextView)findViewById(textviewId)).setText(text);
    }

    void fillCurAndRange(final BodyComposition.Third t, final int textviewId,
                         final boolean isNeedUnit, final String format) {
        String RANGE_FORMAT = "(%.1f-%.1f)";
        String cur = String.format("%.1f", t.getCur()) + t.getUnit();
        String text = cur + String.format(RANGE_FORMAT, t.getMin(), t.getMax());
        ((TextView)findViewById(textviewId)).setText(text);
    }

    // 肥胖分析
    void fillCurAndRange(final BodyComposition.Third t, final int curId,
                         final int rangeId,
                         final boolean isNeedUnit, final String format) {
        fillOne(t, curId, isNeedUnit, format);
        fillRange(t, rangeId, isNeedUnit, format);
    }

    //
    private void fillYelloMan(ResultActivity.YelloMan yelloMan, View ym) {
        if (yelloMan == null || ym == null)
            return;

        ((TextView) ym.findViewById(R.id.la_level_textview)).setText(yelloMan.getLeftArmLevel());
        ((TextView) ym.findViewById(R.id.ll_level_textview)).setText(yelloMan.getLeftLegeLevel());
        ((TextView) ym.findViewById(R.id.qugan_level_textview)).setText(yelloMan.getQuganLevel());
        ((TextView) ym.findViewById(R.id.ra_level_textview)).setText(yelloMan.getRightArmLevel());
        ((TextView) ym.findViewById(R.id.rl_level_textview)).setText(yelloMan.getRightLegeLevel());

        ((TextView) ym.findViewById(R.id.la_textview)).setText(yelloMan.getLeftArm());
        ((TextView) ym.findViewById(R.id.ll_textview)).setText(yelloMan.getLeftLege());
        ((TextView) ym.findViewById(R.id.qugan_textview)).setText(yelloMan.getQugan());
        ((TextView) ym.findViewById(R.id.ra_textview)).setText(yelloMan.getRightArm());
        ((TextView) ym.findViewById(R.id.rl_textview)).setText(yelloMan.getRightLege());
    }

    private void fillYingYangPingGu(int curId, int progressid, BodyComposition.Third t) {
        TextView etCur = findViewById(curId);
        ProgressBar pb = findViewById(progressid);
        etCur.setText(String.format(FLOAT_1_FORMAT, t.getCur()) + t.getUnit());
        pb.setProgress(t.getProgress(YINGYANGPINGGU_LESS_WIDTH, YINGYANGPINGGU_NOMAL_WIDTH, YINGYANGPINGGU_MORE_WIDTH));
    }


    private void setProgressOfTichengfenfenxi(BodyComposition.Third t, View view) {
        final int PECENT_MAX = 100;
        final int HUMAN_HIGH = 208; // 208px
        final float TWO_GE = (float) (20.8 * 2);
        final float BILI = HUMAN_HIGH / PECENT_MAX;
        final int progress = t.getProgress(TICHENGFENFENXI_LESS_WIDTH, TICHENGFENFENXI_NOMAL_WIDTH, TICHENGFENFENXI_MORE_WIDTH);
        final ImageView progressView = view.findViewById(R.id.shentichengfenfenxi_frontgound_imageview);
        final TextView textView = view.findViewById(R.id.progress_textview);
        progressView.setVisibility(View.VISIBLE);
        progressView.getLayoutParams().width = (int) (((PECENT_MAX - progress) * BILI) + TWO_GE);
        progressView.requestLayout();
        textView.setText(String.format(FORMAT_WEIGHT, t.getCur()) + t.getUnit());
    }

    private void setProgressOfYingYangPingGu(BodyComposition.Third t, View view) {
        final int PECENT_MAX = 100;
        final float total = YINGYANGPINGGU_LESS_WIDTH + YINGYANGPINGGU_NOMAL_WIDTH + YINGYANGPINGGU_MORE_WIDTH;
        final float BILI = total / PECENT_MAX;
        final int progress = t.getProgress(YINGYANGPINGGU_LESS_WIDTH, YINGYANGPINGGU_NOMAL_WIDTH, YINGYANGPINGGU_MORE_WIDTH);
        final View progressView = view.findViewById(R.id.progress);
        progressView.setVisibility(View.VISIBLE);
        progressView.getLayoutParams().width = (int) ((PECENT_MAX - progress) * BILI);
        progressView.requestLayout();
    }
}
