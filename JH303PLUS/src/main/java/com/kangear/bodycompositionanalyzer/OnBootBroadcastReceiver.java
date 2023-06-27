package com.kangear.bodycompositionanalyzer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class OnBootBroadcastReceiver extends BroadcastReceiver {
    private static final String LOG_TAG = "OnBootBroadcastReceiver";

    public OnBootBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(LOG_TAG, "OnBootBroadcastReceiver # onReceive");
        /* 启动服务 */
        Intent i = new Intent(context, WelcomeActivity.class);
        i.setFlags(FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
