package com.example.tony.bodycompositionanalyzer;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.example.tony.bodycompositionanalyzer.BodyCompositionAnalyzer;
import com.example.tony.bodycompositionanalyzer.BodyCompositionAnalyzerService;
import com.example.tony.bodycompositionanalyzer.Printer;

import java.io.File;

/**
 * Created by tony on 16-7-15.
 */
public class TaskService extends IntentService {
    private static final boolean DEBUG = true;
    private static final String LOG_TAG = "TaskService";
    private static BodyCompositionAnalyzer mBodyCompositionAnalyzer = null;
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for dejava.lang.Stringbugging.
     */
    public TaskService(String name) {
        super(name);
        Log.i(LOG_TAG, "TaskService");
        mBodyCompositionAnalyzer = BodyCompositionAnalyzer.getInstance(this);
    }

    public TaskService() {
        this("TaskService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // 耗时任务代码
        final int envCode = intent.getIntExtra(BodyCompositionAnalyzerService.EVENT_CODE, -1);
        if (DEBUG) Log.i(LOG_TAG, "TaskService onHandleIntent " + " CODE: " + envCode);
        switch (envCode) {
            case BodyCompositionAnalyzerService.EVENT_CODE_DATA_TO_PDF:
                // 1. 提取数据，并生成PDF
                mBodyCompositionAnalyzer.toPdf(mBodyCompositionAnalyzer.getBodyComposition());
                break;
            case BodyCompositionAnalyzerService.EVENT_CODE_PDF_TO_PRINTER:
//                // 2. 将PDF进行打印
//                try {
//                    Printer.getInstance(this).printPdf(
//                            mBodyCompositionAnalyzer.getRasterPath(),
//                            mBodyCompositionAnalyzer.getPdfPath());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                /* 打开PDF */
                startActivity(getPdfFileIntent(mBodyCompositionAnalyzer.getPdfPath()));
                break;
            case BodyCompositionAnalyzerService.EVENT_CODE_DATA_TO_PDF_OPEN:
                try {
                    /* 打开PDF */
                    startActivity(getPdfFileIntent(mBodyCompositionAnalyzer.getPdfPath()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case BodyCompositionAnalyzerService.EVENT_CODE_TESTPDF_TO_PRINTER:
                // n. 将HelloWorld PDF进行打印
                try {
                    Printer.getInstance(this).printPdf(
                            mBodyCompositionAnalyzer.getRasterPath(),
                            "/system/usr/share/printer/test/HelloWorld.pdf");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case BodyCompositionAnalyzerService.EVENT_CODE_RASTER_TO_PRINTER:
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
}
