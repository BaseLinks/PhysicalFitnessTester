package com.kangear.bca.report;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.kangear.PrinterSdk.Printer;
import com.kangear.bca.BcaManager;
import com.kangear.bodycompositionanalyzer.R;

import cn.trinea.android.common.util.PreferencesUtils;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = "MainActivity";
    private Context mContext;
    private CheckBox mDrawNegative;
    private CheckBox mDotPrintCheckBox;
    private TextView mPrinterTextView;
    private TextView mSerialTextView;
    private TextView mDescriptTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        mContext = this;

        mDrawNegative = (CheckBox) findViewById(R.id.draw_negative_checkbox);
        mDotPrintCheckBox = (CheckBox) findViewById(R.id.dont_print_checkbox);
        mPrinterTextView = (TextView) findViewById(R.id.printer_textview);
        mSerialTextView = (TextView) findViewById(R.id.serial_textview);
        mDescriptTextView = (TextView) findViewById(R.id.des_textview);

        mPrinterTextView.setText(Printer.getInstance(mContext).isConnected() ? "已连接" : "未连接");
        mDescriptTextView.setText("版本号: " + getVersionName() + "\n" + "打印总分：" + getResources().getBoolean(R.bool.is_print_total_score));

        boolean isDrawNegative = PreferencesUtils.getBoolean(mContext, BcaManager.KEY_IS_DRAW_NEGATIVE);
        mDrawNegative.setChecked(isDrawNegative);
        mDrawNegative.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PreferencesUtils.putBoolean(mContext, BcaManager.KEY_IS_DRAW_NEGATIVE, isChecked);
            }
        });

        boolean dontPrint = PreferencesUtils.getBoolean(mContext, BcaManager.KEY_DO_NOT_PRINT);
        mDotPrintCheckBox.setChecked(dontPrint);
        mDotPrintCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PreferencesUtils.putBoolean(mContext, BcaManager.KEY_DO_NOT_PRINT, isChecked);
            }
        });

        IntentFilter itf = new IntentFilter();
        itf.addAction(Printer.ACTION_PRINER_ADD);
        itf.addAction(Printer.ACTION_PRINER_REMOVE);

        LocalBroadcastManager.getInstance(this).registerReceiver(
                mBroadcastReceiver,
                itf);


        int reportId = BcaManager.getInstance(this).getReportId();
        onRadioClick(findViewById(reportId));
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            handleEvent(intent);
        }
    };


    /**
     * 获取版本号
     *
     * @return
     */
    public String getVersionName() {
        String version = "unkown";
        try {
            // 获取packagemanager的实例
            PackageManager packageManager = getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = null;
            packInfo = packageManager.getPackageInfo(getPackageName(), 0);
            version = packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    private void handleEvent(Intent intent) {
        Log.i(LOG_TAG, "handleEvent");
        if (intent == null)
            return;

        String act = intent.getAction();
        if (act == null)
            return;

        switch (act) {
            case Printer.ACTION_PRINER_ADD:
                mPrinterTextView.setText("已连接");
                break;
            case Printer.ACTION_PRINER_REMOVE:
                mPrinterTextView.setText("未连接");
                break;
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            /* 解析数据 */
            case R.id.parse_button:
                BcaManager.getInstance(this).test();
                break;
            case R.id.print_button:
                break;
            case R.id.get_printer_button:
                break;
        }
    }

    public void onRadioClick(View v) {
        switch (v.getId()) {
            case R.id._20160610:
                findViewById(R.id._20160610).setBackgroundColor(Color.YELLOW);
                findViewById(R.id._20170610).setBackgroundColor(Color.LTGRAY);
                findViewById(R.id._20170504).setBackgroundColor(Color.LTGRAY);
                BcaManager.getInstance(this).setReportId(R.id._20160610);
                break;
            case R.id._20170610:
                findViewById(R.id._20160610).setBackgroundColor(Color.LTGRAY);
                findViewById(R.id._20170610).setBackgroundColor(Color.YELLOW);
                findViewById(R.id._20170504).setBackgroundColor(Color.LTGRAY);
                BcaManager.getInstance(this).setReportId(R.id._20170610);
                break;
            case R.id._20170504:
                findViewById(R.id._20160610).setBackgroundColor(Color.LTGRAY);
                findViewById(R.id._20170610).setBackgroundColor(Color.LTGRAY);
                findViewById(R.id._20170504).setBackgroundColor(Color.YELLOW);
                BcaManager.getInstance(this).setReportId(R.id._20170504);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
    }
}
