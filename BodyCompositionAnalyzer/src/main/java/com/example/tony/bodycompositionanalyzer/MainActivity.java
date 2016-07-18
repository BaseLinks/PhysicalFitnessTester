package com.example.tony.bodycompositionanalyzer;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionGroupInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.print.PrintHelper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
        stopService(mIntent);
    }
}
