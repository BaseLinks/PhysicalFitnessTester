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
        hideSystemUI(getWindow().getDecorView());
        mContext = this;

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
    }
}
