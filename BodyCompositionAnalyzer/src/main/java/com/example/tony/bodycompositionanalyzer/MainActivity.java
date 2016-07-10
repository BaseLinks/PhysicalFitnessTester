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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

//        PackageManager pm = getPackageManager();
//        List<ApplicationInfo> installedApps = pm.getInstalledApplications(0);
//
//        boolean isSystemApp = new AppUtil(this).isSystemApp(getPackageName());
//        if (isSystemApp) {
//            // System app - do something here
//            Log.e(LOG_TAG, "System app - do something here");
//        } else {
//            // User installed app?
//            Log.e(LOG_TAG, "User installed app");
//        }
//
//        isSystemApp = new AppUtil(this).isAppPreLoaded(getPackageName());
//        if (isSystemApp) {
//            // System app - do something here
//            Log.e(LOG_TAG, "isAppPreLoaded");
//        } else {
//            // User installed app?
//            Log.e(LOG_TAG, "NOT isAppPreLoaded");
//        }
//
        mBodyCompositionAnalyzer = BodyCompositionAnalyzer.getInstance(this);
        startService(new Intent(this, BodyCompositionAnalyzerService.class));

//        Intent service = new Intent(IPrintService.ACTION_PRINT);
//        service.setPackage("com.kangear.printspooler");
////		bindService(service, conn, BIND_AUTO_CREATE);
//        startService(service);
//
//        int intArray[] = null;
//        try {
//            intArray = getPackageManager().getPackageGids(getApplicationContext().getPackageName());
//        } catch (PackageManager.NameNotFoundException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//        if(intArray != null) {
//            for (int i : intArray) {
//                Log.i(LOG_TAG, "i:" + i);
//            }
//        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textview: {
                /* 创建PDF */
                String pdf = mBodyCompositionAnalyzer.toPdf(mBodyCompositionAnalyzer.getBodyComposition());
                /* 打开PDF */
                startActivity(getPdfFileIntent(pdf));
                break;
            }
            case R.id.parse_button:
                try {
                    mBodyCompositionAnalyzer.doIt(true);
                    /* 创建PDF */
                    String pdf = mBodyCompositionAnalyzer.toPdf(mBodyCompositionAnalyzer.getBodyComposition());
                    /* 打开PDF */
                    startActivity(getPdfFileIntent(pdf));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.print_button:
                try {
                    mBodyCompositionAnalyzer.doIt(false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.get_printer_button:
                break;
        }
    }

    /**
     * 打印需求规划
     * 不使用Android打印服务，但是使用Android USB host口
     * 串口有两种方式实现，一种基于FTDI厂家的实现，一种是传统串口方式
     */
    private void doPhotoPrint() {
        PrintHelper photoPrinter = new PrintHelper(this);
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.selection);
        photoPrinter.printBitmap("droids.jpg - test print", bitmap);
    }

    //android获取一个用于打开PDF文件的intent
    public static Intent getPdfFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/pdf");
        return intent;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
