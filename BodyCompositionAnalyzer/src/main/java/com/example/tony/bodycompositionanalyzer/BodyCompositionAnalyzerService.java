package com.example.tony.bodycompositionanalyzer;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.security.PublicKey;

/**
 * Created by tony on 16-7-11.
 */
public class BodyCompositionAnalyzerService extends Service {
    private static final String LOG_TAG = "BodyComAnalyzerService";
    private static BodyCompositionAnalyzer mBodyCompositionAnalyzer = null;

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
        Log.i(LOG_TAG, "onStartCommand");
        handleEvent(intent);
        return START_STICKY;
    }

    private void handleEvent(Intent intent) {
        Log.i(LOG_TAG, "handleEvent");
        if(intent != null) {
            int type = intent.getIntExtra(EVENT_TYPE, EVENT_TYPE_UNKNOW);
            int code = intent.getIntExtra(EVENT_CODE, EVENT_CODE_UNKOWN);

            Intent i = new Intent(ACTION_BROCAST);
            i.putExtra(EVENT_TYPE, type);
            i.putExtra(EVENT_CODE, code);

            if(type != EVENT_TYPE_UNKNOW && code != EVENT_CODE_UNKOWN) {
                switch (type) {
                    case EVENT_TYPE_PRINTER:
                        Log.i(LOG_TAG, "handleEvent Printer: " + code);
                        if(code == EVENT_CODE_PRINTER_OK) {
                            mBodyCompositionAnalyzer.handlePrinterAdd();
                        } else if(code == EVENT_CODE_PRINTER_NONE) {
                            mBodyCompositionAnalyzer.handlePrinterRemove();
                        }
                        LocalBroadcastManager.getInstance(this).sendBroadcast(i);
                        break;
                    case EVENT_TYPE_SERIAL:
                        Log.i(LOG_TAG, "handleEvent Serial: " + code);
                        LocalBroadcastManager.getInstance(this).sendBroadcast(i);
                        break;
                }
            }
        }
        Log.i(LOG_TAG, "handleEvent END");
    }

    public static void startActionAddPrinter(Context context) {
        Intent intent = new Intent(context, BodyCompositionAnalyzerService.class);
        intent.putExtra(BodyCompositionAnalyzerService.EVENT_TYPE, BodyCompositionAnalyzerService.EVENT_TYPE_PRINTER);
        intent.putExtra(BodyCompositionAnalyzerService.EVENT_CODE, BodyCompositionAnalyzerService.EVENT_CODE_PRINTER_OK);
        context.startService(intent);
    }

    public static void startActionNonePrinter(Context context) {
        Intent intent = new Intent(context, BodyCompositionAnalyzerService.class);
        intent.putExtra(BodyCompositionAnalyzerService.EVENT_TYPE, BodyCompositionAnalyzerService.EVENT_TYPE_PRINTER);
        intent.putExtra(BodyCompositionAnalyzerService.EVENT_CODE, BodyCompositionAnalyzerService.EVENT_CODE_PRINTER_NONE);
        context.startService(intent);
    }

    public static void startActionAddSerial(Context context) {
        Intent intent = new Intent(context, BodyCompositionAnalyzerService.class);
        intent.putExtra(BodyCompositionAnalyzerService.EVENT_TYPE, BodyCompositionAnalyzerService.EVENT_TYPE_SERIAL);
        intent.putExtra(BodyCompositionAnalyzerService.EVENT_CODE, BodyCompositionAnalyzerService.EVENT_CODE_SERIAL_OK);
        context.startService(intent);
    }

    public static void startActionNoneSerial(Context context) {
        Intent intent = new Intent(context, BodyCompositionAnalyzerService.class);
        intent.putExtra(BodyCompositionAnalyzerService.EVENT_TYPE, BodyCompositionAnalyzerService.EVENT_TYPE_SERIAL);
        intent.putExtra(BodyCompositionAnalyzerService.EVENT_CODE, BodyCompositionAnalyzerService.EVENT_CODE_SERIAL_NONE);
        context.startService(intent);
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
        mBodyCompositionAnalyzer.unInit();
    }
}
