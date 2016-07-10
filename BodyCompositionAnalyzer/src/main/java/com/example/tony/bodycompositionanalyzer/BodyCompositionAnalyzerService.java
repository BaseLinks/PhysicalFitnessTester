package com.example.tony.bodycompositionanalyzer;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * Created by tony on 16-7-11.
 */
public class BodyCompositionAnalyzerService extends Service {
    private static final boolean DEBUG = true;
    private static final String LOG_TAG = "BodyComAnalyzerService";
    private static BodyCompositionAnalyzer mBodyCompositionAnalyzer = null;
    public static final String EVENT_CODE = "event_code";
    public static final int EVENT_CODE_DATA_TO_PDF_OPEN  = 1;
    /* HelloWorld PDF输出到打印机 */
    public static final int EVENT_CODE_PDF_TO_PRINTER    = 2;
    /* HelloWorld raster data 直接输出到打印机 */
    public static final int EVENT_CODE_RASTER_TO_PRINTER = 3;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 创建BodyCompositionAnalyzer类
        mBodyCompositionAnalyzer = BodyCompositionAnalyzer.getInstance(this);

        // 初始化
        mBodyCompositionAnalyzer.init();
    }

    protected void onHandleIntent(Intent intent) {
        // 耗时任务代码
        final int envCode = intent.getIntExtra(EVENT_CODE, -1);
        if (DEBUG) Log.i(LOG_TAG, "handleStart " + " CODE: " + envCode);
        switch (envCode) {
            case EVENT_CODE_DATA_TO_PDF_OPEN:
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
            case EVENT_CODE_PDF_TO_PRINTER:
                try {
                    Printer.getInstance(this).printPdf("/system/usr/share/printer/test/HelloWorld.pdf");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case EVENT_CODE_RASTER_TO_PRINTER:
                break;
        }
    }

    //android获取一个用于打开PDF文件的intent
    public static Intent getPdfFileIntent(String param ) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/pdf");
        return intent;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
