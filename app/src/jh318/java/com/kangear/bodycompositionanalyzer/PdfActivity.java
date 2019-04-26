package com.kangear.bodycompositionanalyzer;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kangear.bodycompositionanalyzer.databinding.ActivityResultBinding;
import com.kangear.bodycompositionanalyzer.databinding.DialogPrintBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.kangear.bodycompositionanalyzer.BodyComposition.LEVEL_HIGH;
import static com.kangear.bodycompositionanalyzer.BodyComposition.LEVEL_LOW;
import static com.kangear.bodycompositionanalyzer.BodyComposition.LEVEL_NORMAL;
import static com.kangear.bodycompositionanalyzer.MusicService.SOUND_12_PRINT;
import static com.kangear.bodycompositionanalyzer.MusicService.SOUND_33_PRINT_FAIL;
import static com.kangear.bodycompositionanalyzer.ResultActivity.FLOAT_0_FORMAT;
import static com.kangear.bodycompositionanalyzer.ResultActivity.FLOAT_1_FORMAT;
import static com.kangear.bodycompositionanalyzer.ResultActivity.FLOAT_2_FORMAT;
import static com.kangear.bodycompositionanalyzer.ResultActivity.getGugejiScore;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.CONST_BITMAP;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.CONST_RECORD_ID;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.FORMAT_WEIGHT;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.PERSON_ID_ANONYMOUS;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.PERSON_ID_INVALID;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.RECORD_ID_INVALID;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.createPdfFromView;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.hideSystemUI;

/**
 * 入口: personId,heck recently 10 times history record by personId
 */
public class PdfActivity extends BaseActivity {
    private static final String TAG = "PdfActivity";
    private static final String JIBENXINXI_DATE_FORMAT  = "yyyy.MM.dd";
    private static final String JIBENXINXI_TIME_FORMAT  = "HH:mm";
    public static final String DATE_FORMAT_DATE  = "yy.MM.dd HH:mm";
    public static final String DATE_FORMAT_TIME  = "yy.MM.dd HH:mm:ss"; // FOR DEBUG
    public static String DATE_FORMAT  = DATE_FORMAT_DATE;
    private static float TICHENGFENFENXI_LESS_WIDTH = 78;
    private static float TICHENGFENFENXI_NOMAL_WIDTH = 40;
    private static float TICHENGFENFENXI_MORE_WIDTH = 90;
    private static float YINGYANGPINGGU_LESS_WIDTH = 25;
    private static float YINGYANGPINGGU_NOMAL_WIDTH = 25;
    private static float YINGYANGPINGGU_MORE_WIDTH = 25;

    public static String FLOAT_ZHIFANG_TIAOZHENGLIANG_FORMAT;
    public static String FLOAT_JIROU_TIAOZHENGLIANG_FORMAT;

    /** 使用自定义字体：宋体 */
    private static final int HANDLE_EVENT_PRE_FILL   = 0;
    private static final int HANDLE_EVENT_FILL       = 1;
    private static final int HANDLE_EVENT_PRINT      = 2;
    private static final int HANDLE_EVENT_PRINT_DONE = 3;
    private static final int HANDLE_EVENT_PRINT_FAIL = 4;

    private View mFailView;
    private View mPrintingView;

    public static void changeFonts(ViewGroup root, Activity act, Typeface typeface) {
        for (int i = 0; i < root.getChildCount(); i++) {
            View v = root.getChildAt(i);
            if (v instanceof TextView) {
                ((TextView) v).setTypeface(typeface);
            } else if (v instanceof Button) {
                ((Button) v).setTypeface(typeface);
            } else if (v instanceof EditText) {
                ((EditText) v).setTypeface(typeface);
            } else if (v instanceof ViewGroup) {
                changeFonts((ViewGroup) v, act, typeface);
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DialogPrintBinding binding = DataBindingUtil.setContentView(this, R.layout.dialog_print);
//        setContentView(R.layout.dialog_print);
        hideSystemUI(getWindow().getDecorView());
//        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        mContext = this;
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
//        Record mRecord = new Record(0, 0, "xiaoguo2", 343, 1, "xxxx", 323, 2222);
//        mRecord.setData(data2);
//        mRecord.getBodyComposition();
//
//        binding.setRecord(mRecord);

//        Pdf pdf = new Pdf(mRecord, findViewById(R.id.pdf), this, null);

//        LocalBroadcastManager.getInstance(mContext).registerReceiver(mBroadcastReceiver, mPrintDoneFilter);
//
//        Message msg = new Message();
//        msg.what = HANDLE_EVENT_PRINT;
//        msg.obj = pdf;
//        mHandler.sendMessageDelayed(msg, 1);
//
//        if (true)
//            return;

        Intent intent = getIntent();
        String path = intent.getStringExtra(CONST_BITMAP);
        if (path != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            BitmapDrawable ob = new BitmapDrawable(getResources(), bitmap);
            getWindow().getDecorView().setBackground(ob);
        }

        mFailView = findViewById(R.id.print_fail_view);
        mPrintingView = findViewById(R.id.printing_view);

        mFailView.setVisibility(View.GONE);
        mPrintingView.setVisibility(View.VISIBLE);

        if (BuildConfig.DEBUG) {
            DATE_FORMAT = DATE_FORMAT_TIME;
//            findViewById(R.id.pdf_view).setVisibility(View.VISIBLE);
        }

        int recordId = getIntent().getIntExtra(CONST_RECORD_ID, 0);
        if (recordId == RECORD_ID_INVALID) {
            Log.e(TAG, "recordId can not be RECORD_ID_INVALID");
            mHandler.sendEmptyMessage(HANDLE_EVENT_PRINT_FAIL);
            return;
        }

        Record curRecord = RecordBean.getInstance(this).query(recordId);
        if (curRecord == null) {
            Log.e(TAG, "curRecord can not be null");
            mHandler.sendEmptyMessage(HANDLE_EVENT_PRINT_FAIL);
            return;
        }

        if (curRecord.getBodyComposition() == null) {
            Log.e(TAG, "getBodyComposition can not be null");
            mHandler.sendEmptyMessage(HANDLE_EVENT_PRINT_FAIL);
            return;
        }

        int personId = curRecord.getPersonId();

        // specify an adapter (see also next example)
        Log.i(TAG, "PersonId: " + personId);
        List<Record> records = new ArrayList<>();
        if (personId == PERSON_ID_INVALID) {
            records.add(new Record(0, PERSON_ID_INVALID, "default", 25, 180, Person.GENDER_MALE, 70, 0));
            records.add(new Record(0, PERSON_ID_INVALID, "default", 25, 180, Person.GENDER_MALE, 70, 0));
            records.add(new Record(0, PERSON_ID_INVALID, "default", 25, 180, Person.GENDER_MALE, 70, 0));
            records.add(new Record(0, PERSON_ID_INVALID, "default", 25, 180, Person.GENDER_MALE, 70, 0));
            records.add(new Record(0, PERSON_ID_INVALID, "default", 25, 180, Person.GENDER_MALE, 70, 0));
            records.add(new Record(0, PERSON_ID_INVALID, "default", 25, 180, Person.GENDER_MALE, 70, 0));
            records.add(new Record(0, PERSON_ID_INVALID, "default", 25, 180, Person.GENDER_MALE, 70, 0));
            records.add(new Record(0, PERSON_ID_INVALID, "default", 25, 180, Person.GENDER_MALE, 70, 0));
            records.add(new Record(0, PERSON_ID_INVALID, "default", 25, 180, Person.GENDER_MALE, 70, 0));
            records.add(new Record(0, PERSON_ID_INVALID, "default", 25, 180, Person.GENDER_MALE, 70, 0));
        } else {
            if (personId == PERSON_ID_ANONYMOUS) {
                // 匿名没有历史记录 测试不打印历史纪录
//                records.add(curRecord);
            } else {
                records = RecordBean.getInstance(this).findRecentlyListById(personId, 10);
                Collections.reverse(records);
            }

        }
        Log.i(TAG, "mRecords: " + records.size());
        Toast.makeText(this, "历史记录数:"+records.size(), Toast.LENGTH_LONG).show();

        binding.setRecord(curRecord);

        Pdf pdf = new Pdf(curRecord, findViewById(R.id.pdf), this, records);

        Message msg = new Message();
        msg.what = HANDLE_EVENT_PRE_FILL;
        msg.obj = pdf;
        mHandler.sendMessageDelayed(msg, 1);

        LocalBroadcastManager.getInstance(mContext).registerReceiver(mBroadcastReceiver, mPrintDoneFilter);
    }

    private final IntentFilter mPrintDoneFilter = new IntentFilter(Printer.PRINT_DONE);
    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "mBroadcastReceiver");
            mHandler.sendEmptyMessage(HANDLE_EVENT_PRINT_DONE);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mBroadcastReceiver);
    }

    class Pdf {
        Record curRecord;
        View pdfView;
        Activity activity;
        List<Record> records;

        public Record getCurRecord() {
            return curRecord;
        }

        public View getPdfView() {
            return pdfView;
        }

        public Activity getActivity() {
            return activity;
        }

        public List<Record> getRecords() {
            return records;
        }

        public Pdf(Record curRecord, View pdfView, Activity activity, List<Record> records) {
            this.curRecord = curRecord;
            this.pdfView = pdfView;
            this.activity = activity;
            this.records = records;
        }

    }

    private Context mContext;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final Pdf pdf = (Pdf) msg.obj;

            switch (msg.what) {
                case HANDLE_EVENT_PRE_FILL:
                    Log.d(TAG, "HANDLE_EVENT_PRE_FILL: " + System.currentTimeMillis() / 1000);
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            for (Record r : pdf.getRecords()) {
                                r.getBodyComposition();
                            }
                            Message msg = new Message();
                            msg.what = HANDLE_EVENT_FILL;
                            msg.obj = pdf;
                            mHandler.sendMessageDelayed(msg, 1);
                        }
                    }.start();
                    break;
                case HANDLE_EVENT_FILL:
                    Log.d(TAG, "HANDLE_EVENT_FILL: " + System.currentTimeMillis() / 1000);
                    fillPdfView((Pdf) msg.obj);
                    Message m = new Message();
                    m.what = HANDLE_EVENT_PRINT;
                    m.obj = msg.obj;
                    mHandler.sendMessageDelayed(m, 1);
                    break;
                case HANDLE_EVENT_PRINT:
                    Log.d(TAG, "HANDLE_EVENT_PRINT:1 " + System.currentTimeMillis() / 1000);
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            try {
                                final String PDF_PATH = "/sdcard/test.pdf";
                                final String RASTER_PATH = "/sdcard/xerox3020.bin";
                                createPdfFromView(pdf.getPdfView(), PDF_PATH);
                                Log.d(TAG, "HANDLE_EVENT_PRINT:2 " + System.currentTimeMillis() / 1000);
                                Printer.getInstance(mContext).printPdf(RASTER_PATH, PDF_PATH);
                            } catch (Exception e) {
                                e.printStackTrace();
                                sendEmptyMessage(HANDLE_EVENT_PRINT_FAIL);
                            }
                        }
                    }.start();
                    break;
                case HANDLE_EVENT_PRINT_DONE:
                    MusicService.play(mContext, SOUND_12_PRINT);
                    Log.d(TAG, "HANDLE_EVENT_PRINT_DONE: " + System.currentTimeMillis() / 1000);
                    finish();
                    break;
                case HANDLE_EVENT_PRINT_FAIL:
                    Log.d(TAG, "HANDLE_EVENT_PRINT_FAIL: " + System.currentTimeMillis() / 1000);
                    mFailView.setVisibility(View.VISIBLE);
                    mPrintingView.setVisibility(View.GONE);
//                    MusicService.stop(mContext);
                    break;
            }
        }
    };

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.yes_button:
                finish();
                break;
        }
    }

    // 单个框
    private static void fillOne(View v, final BodyComposition.Third t, final int textviewId,
                 final boolean isNeedUnit, final String format) {
        String text = String.format(format, t.getCur());
        if (isNeedUnit)
            text += t.getUnit();
        ((TextView)v.findViewById(textviewId)).setText(text);
    }

    private static void fillRange(View pdfView, final BodyComposition.Third t, final int textviewId,
                 final boolean isNeedUnit, final String format) {
        String text = String.format(format, t.getMin()) + "-" + String.format(format, t.getMax());
        ((TextView)pdfView.findViewById(textviewId)).setText(text);
    }

    private static void fillCurAndRange(View pdfView, final BodyComposition.Third t, final int textviewId,
                         final boolean isNeedUnit, final String format) {
        String RANGE_FORMAT = "(%.1f-%.1f)";
        String cur = String.format("%.1f", t.getCur()) + t.getUnit();
        String text = cur + String.format(RANGE_FORMAT, t.getMin(), t.getMax());
        ((TextView)pdfView.findViewById(textviewId)).setText(text);
    }

    // 肥胖分析
    private static void fillCurAndRange(View pdfView, final BodyComposition.Third t, final int curId,
                         final int rangeId,
                         final boolean isNeedUnit, final String format) {
        fillOne(pdfView, t, curId, isNeedUnit, format);
        fillRange(pdfView, t, rangeId, isNeedUnit, format);
    }

    //
    private static void fillYelloMan(ResultActivity.YelloMan yelloMan, View ym) {
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


    private static void setProgressOfTichengfenfenxi(BodyComposition.Third t, View view) {
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

    private static void setProgressOfYingYangPingGu(BodyComposition.Third t, View view) {
        final int PECENT_MAX = 100;
        final float total = YINGYANGPINGGU_LESS_WIDTH + YINGYANGPINGGU_NOMAL_WIDTH + YINGYANGPINGGU_MORE_WIDTH;
        final float BILI = total / PECENT_MAX;
        final int progress = t.getProgress(YINGYANGPINGGU_LESS_WIDTH, YINGYANGPINGGU_NOMAL_WIDTH, YINGYANGPINGGU_MORE_WIDTH);
        final View progressView = view.findViewById(R.id.progress);
        progressView.setVisibility(View.VISIBLE);
        progressView.getLayoutParams().width = (int) ((PECENT_MAX - progress) * BILI);
        progressView.requestLayout();
    }


    private void fillPdfView(Pdf pdf) {
        if (pdf == null) {
            mHandler.sendEmptyMessage(HANDLE_EVENT_PRINT_FAIL);
            return;
        }

        Activity activity = pdf.getActivity();
        View pdfView = pdf.getPdfView();
        List<Record> mRecords = pdf.getRecords();
        Record curRecord = pdf.getCurRecord();

        if (activity == null || pdfView == null || mRecords == null || curRecord == null) {
            mHandler.sendEmptyMessage(HANDLE_EVENT_PRINT_FAIL);
            return;
        }

        BodyComposition bc = curRecord.getBodyComposition();
        if (bc == null) {
            Log.e(TAG, "BodyComposition can not be null");
            mHandler.sendEmptyMessage(HANDLE_EVENT_PRINT_FAIL);
            return;
        }

        Typeface typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/changchengchangsongti.ttf");
        changeFonts((ViewGroup)pdfView, activity, typeface);

        RecyclerView mRecyclerView = pdfView.findViewById(R.id.history_recyclerview);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        RecyclerView.Adapter mAdapter = new RecordPdfAdapter(mRecords);
        mRecyclerView.setAdapter(mAdapter);

        // Company and Number
        Other o1 = OtherBean.getInstance(activity).queryByName(Other.OTHER_NAME_COMPANY);
        Other o2 = OtherBean.getInstance(activity).queryByName(Other.OTHER_NAME_NUMBER);
        String c = o1 == null ? "" : o1.getStrValue();
        String n = o2 == null ? "" : o2.getStrValue();
        Toast.makeText(activity, "company: " + c + " number: " + n, Toast.LENGTH_LONG).show();
        ((TextView)pdfView.findViewById(R.id.company_textview)).setText("地址: "+ c + "  " + n);
//        ((TextView)pdfView.findViewById(R.id.number_textview)).setText(n);
//
//        // JiBenXinXi
//        // - ID, Date, Time, Gender
//        ((TextView)pdfView.findViewById(R.id.name_textview)).setText(curRecord.getName());
//        ((TextView)pdfView.findViewById(R.id.gender_textview)).setText(curRecord.getGender());
//        Date date = new Date(curRecord.getTime());
//        ((TextView)pdfView.findViewById(R.id.jibenxinxi_date_textview)).setText(new SimpleDateFormat(JIBENXINXI_DATE_FORMAT).format(date));
//        ((TextView)pdfView.findViewById(R.id.time_textview)).setText(new SimpleDateFormat(JIBENXINXI_TIME_FORMAT).format(date));
//
//        //
//        fillOne(pdfView, bc.身高, R.id.height_textview, true, FORMAT_WEIGHT);
//        fillOne(pdfView, bc.年龄, R.id.age_textview, true, FLOAT_0_FORMAT);
//        fillOne(pdfView, bc.评分, R.id.jibenxinxi_jiankangzhishu_textview, false, FLOAT_1_FORMAT);
//
//        // 身体成分分析
//        fillRange(pdfView, bc.体重, R.id.weight_range_textview, false, FLOAT_1_FORMAT);
//        fillRange(pdfView, bc.骨骼肌, R.id.gugeji_range_textview, false, FLOAT_1_FORMAT);
//        fillRange(pdfView, bc.体脂肪量, R.id.tizhifang_range_textview, false, FLOAT_1_FORMAT);
//        setProgressOfTichengfenfenxi(bc.体重, pdfView.findViewById(R.id.weight_progressbar));
//        setProgressOfTichengfenfenxi(bc.骨骼肌, pdfView.findViewById(R.id.gugeji_progressbar));
//        setProgressOfTichengfenfenxi(bc.体脂肪量, pdfView.findViewById(R.id.tizhifang_progressbar));
//
//        // -身体水分
//        fillCurAndRange(pdfView, bc.身体水分, R.id.shentishuifen_textview, true, FLOAT_1_FORMAT);
//        // -去脂体重
//        fillCurAndRange(pdfView, bc.去脂体重, R.id.quzhitizhong_textview, true, FLOAT_1_FORMAT);
//
//        // 营养评估
//        // -蛋白质 无机盐 总能耗
//        fillOne(pdfView, bc.蛋白质, R.id.danbaizhi_textview, true, FLOAT_1_FORMAT);
//        fillOne(pdfView, bc.无机盐, R.id.wujiyan_textview, true, FLOAT_1_FORMAT);
//        fillOne(pdfView, bc.总能耗, R.id.zongnenghao_textview, false, FLOAT_0_FORMAT);
//        setProgressOfYingYangPingGu(bc.蛋白质, pdfView.findViewById(R.id.danbaizhi_progressbar));
//        setProgressOfYingYangPingGu(bc.无机盐, pdfView.findViewById(R.id.wujiyan_progressbar));
//
//        // 肥胖分析
//        fillCurAndRange(pdfView, bc.BMI, R.id.bmi_textview, R.id.bmi_range_textview, true, FLOAT_1_FORMAT);
//        fillCurAndRange(pdfView, bc.体脂百分比, R.id.pdf_tizhibaifenbi_textview, R.id.tizhibaifenbi_range_textview, true, FLOAT_1_FORMAT);
//        fillCurAndRange(pdfView, bc.腰臀比, R.id.yaotunbi_textview, R.id.yaotunbi_range_textview, true, FLOAT_2_FORMAT);
//        fillOne(pdfView, bc.基础代谢, R.id.jichudaixie_textview, true, FLOAT_0_FORMAT);
//
//        // zonghepingjia
//        // -内脏面积
//        fillCurAndRange(pdfView, bc.内脏面积, R.id.neizangmianji_textview, true, FLOAT_1_FORMAT);
//
//        // -阶段
//        // - 阶段脂肪
//        fillYelloMan(
//                new ResultActivity.YelloMan(
//                        null,
//                        bc.左上脂肪量,
//                        bc.左下脂肪量,
//                        bc.右上脂肪量,
//                        bc.右下脂肪量,
//                        bc.躯干脂肪量
//                ),
//                pdfView.findViewById(R.id.zhifang_yellow_human)
//
//        );
//
//        // - 阶段肌肉
//        fillYelloMan(
//                new ResultActivity.YelloMan(
//                        null,
//                        bc.左上肢肌肉量,
//                        bc.左下肌肉量,
//                        bc.右上肢肌肉量,
//                        bc.右下肌肉量,
//                        bc.躯干肌肉量
//                ),
//                pdfView.findViewById(R.id.jirou_yellow_human)
//
//        );
//
//        // 综合评价
//        FLOAT_ZHIFANG_TIAOZHENGLIANG_FORMAT = "-" + FLOAT_1_FORMAT + bc.体脂肪量.getUnit();
//        FLOAT_JIROU_TIAOZHENGLIANG_FORMAT   = "+" + FLOAT_1_FORMAT + bc.骨骼肌.getUnit();
//        // -脂肪调整量
//        ((TextView)pdfView.findViewById(R.id.zhifangtiaozheng_textview)).setText(String.format(FLOAT_ZHIFANG_TIAOZHENGLIANG_FORMAT, bc.getZhifangAdjustment()));
//        // -肌肉调整量
//        ((TextView)pdfView.findViewById(R.id.jiroutiaozheng_textview)).setText(String.format(FLOAT_JIROU_TIAOZHENGLIANG_FORMAT, bc.getJirouAdjustment()));
//        // -健康指数(评分)
//        fillOne(pdfView, bc.评分, R.id.jiankangzhishu_textview, false, FLOAT_1_FORMAT);
//
//        // 生物电阻抗
//        // -5k
//        fillOne(pdfView, bc._5k电阻, R.id._5k_textview, true, FLOAT_0_FORMAT);
//        // -50k
//        fillOne(pdfView, bc._50k电阻, R.id._50k_textview, true, FLOAT_0_FORMAT);
//        // -250k
//        fillOne(pdfView, bc._250k电阻, R.id._250k_textview, true, FLOAT_0_FORMAT);
    }
}
