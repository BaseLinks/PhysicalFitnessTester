package com.example.tony.bodycompositionanalyzer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionGroupInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.print.PrintHelper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.List;

import android_serialport_api.ComBean;
import cn.trinea.android.common.util.PreferencesUtils;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = "PDFdemo";
    private static BodyCompositionAnalyzer mBodyCompositionAnalyzer = null;
    private static Intent mIntent;
    private static Context mContext;
    private CheckBox mDrawNegative;
    private CheckBox mDotPrintCheckBox;
    private TextView mPrinterTextView;
    private TextView mSerialTextView;
    private TextView mDescriptTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mContext = this;

        /** 启动服务 */
        mIntent = new Intent(this, BodyCompositionAnalyzerService.class);
        startService(mIntent);

        mDrawNegative = (CheckBox) findViewById(R.id.draw_negative_checkbox);
        mDotPrintCheckBox = (CheckBox) findViewById(R.id.dont_print_checkbox);
        mPrinterTextView = (TextView) findViewById(R.id.printer_textview);
        mSerialTextView = (TextView) findViewById(R.id.serial_textview);
        mDescriptTextView = (TextView) findViewById(R.id.des_textview);

        mPrinterTextView.setText(Printer.getInstance(mContext).isConnected() ? "已连接" : "未连接");
        mDescriptTextView.setText("版本号: " + getVersionName() + "\n" + "打印总分：" + getResources().getBoolean(R.bool.is_print_total_score));

        boolean isDrawNegative = PreferencesUtils.getBoolean(mContext, BodyCompositionAnalyzer.KEY_IS_DRAW_NEGATIVE);
        mDrawNegative.setChecked(isDrawNegative);
        mDrawNegative.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PreferencesUtils.putBoolean(mContext, BodyCompositionAnalyzer.KEY_IS_DRAW_NEGATIVE, isChecked);
            }
        });

        boolean dontPrint = PreferencesUtils.getBoolean(mContext, BodyCompositionAnalyzer.KEY_DO_NOT_PRINT);
        mDotPrintCheckBox.setChecked(dontPrint);
        mDotPrintCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PreferencesUtils.putBoolean(mContext, BodyCompositionAnalyzer.KEY_DO_NOT_PRINT, isChecked);
            }
        });

        LocalBroadcastManager.getInstance(this).registerReceiver(
                mBroadcastReceiver,
                new IntentFilter(BodyCompositionAnalyzerService.ACTION_BROCAST));
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
        if (intent != null) {
            int type = intent.getIntExtra(BodyCompositionAnalyzerService.EVENT_TYPE, BodyCompositionAnalyzerService.EVENT_TYPE_UNKNOW);
            int code = intent.getIntExtra(BodyCompositionAnalyzerService.EVENT_CODE, BodyCompositionAnalyzerService.EVENT_CODE_UNKOWN);
            if (type != BodyCompositionAnalyzerService.EVENT_TYPE_UNKNOW && code != BodyCompositionAnalyzerService.EVENT_CODE_UNKOWN) {
                String str;
                switch (type) {
                    case BodyCompositionAnalyzerService.EVENT_TYPE_PRINTER:
                        str = code == BodyCompositionAnalyzerService.EVENT_CODE_PRINTER_OK ? "已连接" : "未连接";
                        mPrinterTextView.setText(str);
                        Log.i(LOG_TAG, "handleEvent Printer: " + code);
                        break;
                    case BodyCompositionAnalyzerService.EVENT_TYPE_SERIAL:
                        Log.i(LOG_TAG, "handleEvent Serial: " + code);
                        str = code == BodyCompositionAnalyzerService.EVENT_CODE_SERIAL_OK ? "已连接" : "未连接";
                        mSerialTextView.setText(str);
                        break;
                }
            }
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            /** 读取测试数据 */
            case R.id.parse_helloworld_button: {
                MyIntentService.startActionTestData(this, "", "");
                break;
            }
            /** 解析数据 */
            case R.id.parse_button:
                MyIntentService.startActionParseData(this, "", "");
                break;
            case R.id.print_button:
                break;
            case R.id.get_printer_button:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
    }
}
