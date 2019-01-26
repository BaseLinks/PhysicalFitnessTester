package com.kangear.bodycompositionanalyzer.application;

import android.app.Application;
import android.util.Log;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.crashreport.CrashReport;

public class App extends Application {
    private static final String TAG = "Application";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate");
        Beta.autoCheckUpgrade = false;
        Bugly.init(getApplicationContext(), "cb731b8519", true);
    }
}
