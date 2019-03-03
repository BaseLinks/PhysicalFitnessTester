package com.kangear.bodycompositionanalyzer.application;

import android.app.Application;
import android.util.Log;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.crashreport.CrashReport;

public class App extends Application {
    private static final String TAG = "Application";
    private static boolean isInit = false;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate");
        Beta.autoCheckUpgrade = false;
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(getApplicationContext());
        strategy.setAppReportDelay(20000);   //改为20s
        Bugly.init(getApplicationContext(), "cb731b8519", true, strategy);
    }

    public static boolean isInit() {
        return isInit;
    }

    public static void setInit(boolean init) {
        isInit = init;
    }
}
