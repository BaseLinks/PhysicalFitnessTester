package com.kangear.bodycompositionanalyzer.mvp.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.singledateandtimepicker.dialog.DoubleDateAndTimePickerDialog;
import com.google.gson.Gson;
import com.kangear.bodycompositionanalyzer.BaseActivity;
import com.kangear.bodycompositionanalyzer.Other;
import com.kangear.bodycompositionanalyzer.OtherBean;
import com.kangear.bodycompositionanalyzer.R;
import com.kangear.bodycompositionanalyzer.Record;
import com.kangear.bodycompositionanalyzer.application.App;
import com.kangear.bodycompositionanalyzer.entry.SchoopiaRecord;
import com.kangear.bodycompositionanalyzer.tool.UploadImage;

import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static com.kangear.bodycompositionanalyzer.Record.DB_COL_TIME;

public class UploadDataActivity extends BaseActivity {
    private static final String TAG = "Main2Activity";
    private static final int UPDATE_PROGRESS = 1;
    private static final int DATA_EMPTY      = 2;
    DoubleDateAndTimePickerDialog.Builder doubleBuilder;
    ProgressBar mProgressBar = null;

    private DatePicker mStartDatePicker;
    private DatePicker mEndDatePicker;

    SimpleDateFormat simpleDateFormat;
    SimpleDateFormat QUERY_DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    List<Long> mDates = new ArrayList<>();
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploaddata);

        mStartDatePicker = findViewById(R.id.start_datepicker);
        mEndDatePicker = findViewById(R.id.end_datepicker);

        this.simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd(EEE) HH:mm:ss", Locale.getDefault());
        mProgressBar = findViewById(R.id.progressBar2);
        mProgressBar.setProgress(0);

        Other o2 = OtherBean.getInstance(this).queryByName(Other.REPORT_URL);
        url = o2 == null ? "" : o2.getStrValue();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // 根据what的值处理不同操作
                case UPDATE_PROGRESS:
                    mProgressBar.setMax(100);
                    mProgressBar.setProgress(msg.arg1);
                    if (msg.arg1 >= 100) {
                        Toast.makeText(getBaseContext(), "数据上传完成", Toast.LENGTH_SHORT).show();
                    } else if (msg.arg1 < 0) {
                        Toast.makeText(getBaseContext(), "数据上传失败，请检测网络后重试", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case DATA_EMPTY:
                    Toast.makeText(getBaseContext(), "所选日期范围内无数据", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private long getTimeFromDatePicker(DatePicker dp) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(dp.getYear(), dp.getMonth(), dp.getDayOfMonth(),0, 0, 0);
        return calendar.getTimeInMillis();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                finish();
                break;
//            case R.id.start_button:
//                doubleClicked((TextView)findViewById(R.id.start_textview), (TextView)findViewById(R.id.end_textview));
//                break;
            case R.id.upload_button:

                mDates.clear();
                mDates.add(getTimeFromDatePicker(mStartDatePicker));
                mDates.add(getTimeFromDatePicker(mEndDatePicker) + 86399000);

                if (mDates.size() != 2) {
                    Toast.makeText(this, "请选择时间", Toast.LENGTH_LONG).show();
                    break;
                }
                Log.e(TAG, "r2: start " + simpleDateFormat.format(mDates.get(0)) + " end: " + simpleDateFormat.format(mDates.get(1)));

                final List<Record> rList = findRecentlyListById(mDates);
                Log.e(TAG, "r2: " + rList);
                if (rList.size() == 0) {
                    mHandler.sendEmptyMessage(DATA_EMPTY);
                    return;
                }

                new Thread() {
                    @Override
                    public void run() {
                        super.run();

                        for (int i=0; i<rList.size(); i++) {
                            Record r = rList.get(i);
//                            r.getBodyComposition();
//                            SchoopiaRecord sr = SchoopiaRecord.toHere(r);
//                            Gson gson = new Gson();
//                            String json = gson.toJson(sr);
//                            Log.e(TAG, json);
                            Message msg = new Message();
                            msg.what = UPDATE_PROGRESS;
                            msg.arg1 = (i+1) * 100 / rList.size();

                            try {

                                UploadImage.doSomething(null, r, url);
                            } catch (Exception e) {
                                e.printStackTrace();
//                                msg.arg1 = -1;
                            }

                            mHandler.sendMessage(msg);
                            if (msg.arg1 < 0) {
                                break;
                            }
                        }
                    }
                }.start();

                break;
        }
    }

    List<Record> findRecentlyListById(List<Long> dates) {
        List<Record> list = null;
        try {
            list = App.getDB().selector(Record.class)
                    .orderBy(DB_COL_TIME, false) // 这里不倒序
                    .where(WhereBuilder.b(DB_COL_TIME, "BETWEEN", dates))
                    .findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void doubleClicked(final TextView startTextView, final TextView endTextView) {
        final Date now = new Date();
        final Calendar calendarMin = Calendar.getInstance();
        final Calendar calendarMax = Calendar.getInstance();

        calendarMin.setTime(new Date(now.getTime() - TimeUnit.DAYS.toMillis(150))); // Set min now
        calendarMax.setTime(now); // Set max now + 150 days

        final Date minDate = calendarMin.getTime();
        final Date maxDate = calendarMax.getTime();

        doubleBuilder = new DoubleDateAndTimePickerDialog.Builder(this)
                //.bottomSheet()
                //.curved()

//                .backgroundColor(Color.BLACK)
//                .mainColor(Color.GREEN)
                .minutesStep(15)
                .mustBeOnFuture()

                .minDateRange(minDate)
                .maxDateRange(maxDate)

                .secondDateAfterFirst(true)

                //.defaultDate(now)
                .tab0Date(now)
                .tab1Date(new Date(now.getTime() + TimeUnit.HOURS.toMillis(1)))

                .title("起始时间")

                .tab0Text("开始")
                .tab1Text("结束")
                .listener(new DoubleDateAndTimePickerDialog.Listener() {
                    @Override
                    public void onDateSelected(List<Date> dates) {
                        final StringBuilder stringBuilder = new StringBuilder();
                        mDates.clear();
                        for (Date date : dates) {
                            stringBuilder.append(simpleDateFormat.format(date)).append("\n");
                            mDates.add(date.getTime());
                        }
                        startTextView.setText(simpleDateFormat.format(dates.get(0)));
                        endTextView.setText(simpleDateFormat.format(dates.get(1)));
                        Log.e(TAG, "STR: " + stringBuilder.toString());
//                        doubleText.setText(stringBuilder.toString());
                    }
                });
        doubleBuilder.display();
    }
}
