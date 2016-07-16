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
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.List;

import android_serialport_api.ComBean;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = "PDFdemo";
    private static BodyCompositionAnalyzer mBodyCompositionAnalyzer = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /** 启动服务 */
        startService(new Intent(this, BodyCompositionAnalyzerService.class));
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
}
