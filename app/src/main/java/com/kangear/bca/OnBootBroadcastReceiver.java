package com.kangear.bca;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.kangear.bca.report.BcaService;

public class OnBootBroadcastReceiver extends BroadcastReceiver {
    private static final String LOG_TAG = "OnBootBroadcastReceiver";

    public OnBootBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(LOG_TAG, "OnBootBroadcastReceiver # onReceive");
        /* 启动服务 */
        context.startService(new Intent(context, BcaService.class));
    }
}
