package com.example.tony.bodycompositionanalyzer;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.net.Uri;
import android.os.PowerManager;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MyIntentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.example.tony.bodycompositionanalyzer.action.FOO";
    private static final String ACTION_BAZ = "com.example.tony.bodycompositionanalyzer.action.BAZ";
    private static final String ACTION_INIT = "com.example.tony.bodycompositionanalyzer.action.INIT";
    private static final String ACTION_UNINIT = "com.example.tony.bodycompositionanalyzer.action.UN_INIT";
    private static final String ACTION_TEST_DATA = "com.example.tony.bodycompositionanalyzer.action.TEST_DATA";
    private static final String ACTION_DATA_TO_PDF = "com.example.tony.bodycompositionanalyzer.action.DATA_TO_PDF";
    private static final String ACTION_PDF_TO_PRINTER = "com.example.tony.bodycompositionanalyzer.action.PDF_TO_PRINTER";
    private static final String ACTION_PDF_TO_OPEN = "com.example.tony.bodycompositionanalyzer.action.PDF_TO_OPEN";
    private static final String ACTION_PARSE_DATA = "com.example.tony.bodycompositionanalyzer.action.PARSE_DATA";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.example.tony.bodycompositionanalyzer.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.example.tony.bodycompositionanalyzer.extra.PARAM2";
    private static final String LOG_TAG = "MyIntentService";
    private static BodyCompositionAnalyzer mBodyCompositionAnalyzer = null;

    public MyIntentService() {
        super("MyIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action DataToPdf with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // 这个是一个测试入口，读取现有数据充当数据
    public static void startActionInit(Context context, String param1, String param2) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(ACTION_INIT);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action DataToPdf with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // 这个是一个测试入口，读取现有数据充当数据
    public static void startActionUnInit(Context context, String param1, String param2) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(ACTION_UNINIT);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action DataToPdf with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // 这个是一个测试入口，读取现有数据充当数据
    public static void startActionTestData(Context context, String param1, String param2) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(ACTION_TEST_DATA);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action DataToPdf with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // 这个是一个解析数据的入口
    public static void startActionParseData(Context context, String param1, String param2) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(ACTION_PARSE_DATA);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action DataToPdf with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionDataToPdf(Context context, String param1, String param2) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(ACTION_DATA_TO_PDF);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action DataToPdf with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionPdfToPrinter(Context context, String param1, String param2) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(ACTION_PDF_TO_PRINTER);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action DataToPdf with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionPdfToOpen(Context context, String param1, String param2) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(ACTION_PDF_TO_OPEN);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionFoo(param1, param2);
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            } else if (ACTION_INIT.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionInit(param1, param2);
            } else if (ACTION_UNINIT.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionUnInit(param1, param2);
            } else if (ACTION_TEST_DATA.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionTestData(param1, param2);
            } else if (ACTION_PARSE_DATA.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionParseData(param1, param2);
            } else if (ACTION_DATA_TO_PDF.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionDataToPdf(param1, param2);
            } else if (ACTION_PDF_TO_PRINTER.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionPdftoPrinter(param1, param2);
            } else if (ACTION_PDF_TO_OPEN.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionPdftoOpen(param1, param2);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        Log.i(LOG_TAG, "handleActionFoo");
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        Log.i(LOG_TAG, "handleActionBaz");
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action init in the provided background thread with the provided
     * parameters.
     */
    private void handleActionInit(String param1, String param2) {
        // 初始化
        mBodyCompositionAnalyzer = BodyCompositionAnalyzer.getInstance(this);
        mBodyCompositionAnalyzer.init();
    }

    /**
     * Handle action init in the provided background thread with the provided
     * parameters.
     */
    private void handleActionUnInit(String param1, String param2) {
        // 初始化
        mBodyCompositionAnalyzer = BodyCompositionAnalyzer.getInstance(this);
        mBodyCompositionAnalyzer.uninit();
    }

    /**
     * Handle action DataToPdf in the provided background thread with the provided
     * parameters.
     */
    private void handleActionTestData(String param1, String param2) {
        try {
            mBodyCompositionAnalyzer.test();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handle action DataToPdf in the provided background thread with the provided
     * parameters.
     */
    private void handleActionParseData(String param1, String param2) {
        BodyCompositionAnalyzer.parseData(mBodyCompositionAnalyzer.getFullData());
    }

    /**
     * Handle action DataToPdf in the provided background thread with the provided
     * parameters.
     */
    private void handleActionDataToPdf(String param1, String param2) {
        Log.i(LOG_TAG, "handleActionDataToPdf");
        mBodyCompositionAnalyzer.toPdf(mBodyCompositionAnalyzer.getBodyComposition());
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionPdftoPrinter(String param1, String param2) {
        Log.i(LOG_TAG, "handleActionPdftoPrinter");
        mBodyCompositionAnalyzer.toPdf(mBodyCompositionAnalyzer.getBodyComposition());
    }

    /**
     * Handle action PdftoPrinter in the provided background thread with the provided
     * parameters.
     */
    private void handleActionPdftoOpen(String param1, String param2) {
        Log.i(LOG_TAG, "handleActionPdftoOpen");
        startActivity(getPdfFileIntent(mBodyCompositionAnalyzer.getPdfPath()));
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
}
