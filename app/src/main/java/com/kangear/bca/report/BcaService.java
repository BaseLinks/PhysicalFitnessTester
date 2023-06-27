package com.kangear.bca.report;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.kangear.bca.BcaManager;

/**
 * Created by tony on 16-7-11.
 */
public class BcaService extends Service {
    private static final String LOG_TAG = "BcaService";
    private static final String TAG = "BcaService";
    private BcaManager mBcaManager = null;

    public static final String ACTION_BROCAST = "com.example.tony.bodycompositionanalyzer.ACTION_BROCAST";

    public static final int EVENT_TYPE_UNKNOW = -1;
    public static final int EVENT_CODE_UNKOWN = -1;

    public static final String EVENT_TYPE = "EVENT_TYPE";
    public static final String EVENT_CODE = "EVENT_CODE";

    public static final int EVENT_TYPE_PRINTER = 1;
    public static final int EVENT_TYPE_SERIAL = 2;

    public static final int EVENT_CODE_PRINTER_OK = 11;
    public static final int EVENT_CODE_PRINTER_NONE = 12;

    public static final int EVENT_CODE_SERIAL_OK = 21;
    public static final int EVENT_CODE_SERIAL_NONE = 22;

    private PowerManager.WakeLock mWakeLock;
    private static Context mContext = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(LOG_TAG, "onCreate");
        mContext = this;
        // 控制一进入休眠
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        mWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "MyWakelockTag");
        if(!mWakeLock.isHeld())
            mWakeLock.acquire();

        // 启动
        mBcaManager = BcaManager.getInstance(this);
        // 启动Activity
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(LOG_TAG, "onStartCommand");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG, "BodyCompositionAnalyzerService#onDestroy");
        /* 1. xxx */
        if(mWakeLock.isHeld()) {
            mWakeLock.release();
            mWakeLock = null;
        }
        /* 2. xxx */
        mBcaManager.unInit();
    }
}
