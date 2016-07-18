package com.example.tony.bodycompositionanalyzer;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * Created by tony on 16-7-11.
 */
public class BodyCompositionAnalyzerService extends Service {
    private static final String LOG_TAG = "BodyComAnalyzerService";
    private static BodyCompositionAnalyzer mBodyCompositionAnalyzer = null;
    private PowerManager.WakeLock mWakeLock;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(LOG_TAG, "onCreate");
        // 控制一进入休眠
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        mWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "MyWakelockTag");
        if(!mWakeLock.isHeld())
            mWakeLock.acquire();
        mBodyCompositionAnalyzer = BodyCompositionAnalyzer.getInstance(this);
        /* 初始化：之前方式时间会比较长，系统已经抱怨了 */
        MyIntentService.startActionInit(this, "", "");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG, "onDestroy");
        /* 1. xxx */
        if(mWakeLock.isHeld()) {
            mWakeLock.release();
            mWakeLock = null;
        }
        /* 2. xxx */
        MyIntentService.startActionUnInit(this, "", "");
    }
}
