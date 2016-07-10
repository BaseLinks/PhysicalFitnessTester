package com.example.tony.bodycompositionanalyzer;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by tony on 16-7-11.
 */
public class BodyCompositionAnalyzerService extends Service {
    private static final boolean DEBUG = true;
    private static final String LOG_TAG = "BodyComAnalyzerService";
    private static BodyCompositionAnalyzer mBodyCompositionAnalyzer = null;
    public static final String EVENT_CODE = "event_code";
    public static final int EVENT_CODE_DATA_TO_PDF_OPEN = 1;
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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handleStart(intent, startId);
        return START_NOT_STICKY;
    }

    /**
     * onStart & onStartCommand 集合
     * @param intent
     * @param startId
     */
    private void handleStart(final Intent intent, int startId) {
        final int envCode = intent.getIntExtra(EVENT_CODE, -1);
        if (DEBUG) Log.i(LOG_TAG, "handleStart " + " CODE: " + envCode);
        switch (envCode) {
            case EVENT_CODE_DATA_TO_PDF_OPEN:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
