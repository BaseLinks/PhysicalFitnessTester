package com.example.tony.bodycompositionanalyzer;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by tony on 16-7-11.
 */
public class BodyCompositionAnalyzerService extends Service {
    private static BodyCompositionAnalyzer mBodyCompositionAnalyzer = null;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 创建BodyCompositionAnalyzer类
        mBodyCompositionAnalyzer = new BodyCompositionAnalyzer(this);

        // 初始化
        mBodyCompositionAnalyzer.init();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
