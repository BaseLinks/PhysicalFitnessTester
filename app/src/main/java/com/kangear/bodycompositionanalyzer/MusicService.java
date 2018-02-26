package com.kangear.bodycompositionanalyzer;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.IOException;

/**
 * Created by tony on 18-1-22.
 */

public class MusicService extends Service {
    private static final String TAG = "MusicService";

    public static final int SOUND_01_NEW_TEST             =  1;
    public static final int SOUND_02_VIP_TEST             =  2;
    public static final int SOUND_03_WEIGHT_DONE          =  3;
    public static final int SOUND_04_ID                   =  4;
    public static final int SOUND_05_AGE                  =  5;
    public static final int SOUND_06_GENDER               =  6;
    public static final int SOUND_07_HEIGHT               =  7;
    public static final int SOUND_08_TEST_START           =  8;
    public static final int SOUND_09_TEST_20              =  9;
    public static final int SOUND_10_TEST_100             = 10;
    public static final int SOUND_11_TEST_FAIL            = 11;
    public static final int SOUND_12_PRINT                = 12;
    public static final int SOUND_13_VIP_TOUCH_ID         = 13;
    public static final int SOUND_14_VIP_TOUCH_ID_DONE    = 14;
    public static final int SOUND_15_VIP_TOUCH_ID_FAIL    = 15;
    public static final int SOUND_30_LOG_UP               = 30;
    public static final int SOUND_31_LOG_UP_TOUCH_ID_DONE = 31;
    public static final int SOUND_32_LOG_UP_TOUCH_ID_FAIL = 32;
    public static final int SOUND_33_PRINT_FAIL           = 33;

    private static final String CONST_MUSIC_ID = "CONST_MUSIC_ID";
    private static final int INVALID_MUSIC_ID = -1;

    private MediaPlayer mMediaPlayer;
    private NoisyAudioStreamReceiver mNoisyAudioStreamReceiver;
    private IntentFilter intentFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");
        mMediaPlayer = new MediaPlayer();
        mNoisyAudioStreamReceiver = new NoisyAudioStreamReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(mNoisyAudioStreamReceiver, intentFilter);
    }

    public static void play(Context context, int number) {
        Intent intent = new Intent(context, MusicService.class);
        intent.putExtra(CONST_MUSIC_ID, number);
        context.startService(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand");
        if (intent == null)
            return START_STICKY;

        int number = intent.getIntExtra(CONST_MUSIC_ID, INVALID_MUSIC_ID);
        if (number == INVALID_MUSIC_ID) {
            return START_STICKY;
        }

        int resId = 0;
        switch(number) {
            case SOUND_01_NEW_TEST:
                resId = R.raw._01_new_test;
                break;
            case SOUND_02_VIP_TEST:
                resId = R.raw._02_vip_test;
                break;
            case SOUND_03_WEIGHT_DONE:
                resId = R.raw._03_weight_done;
                break;
            case SOUND_04_ID:
                resId = R.raw._04_id;
                break;
            case SOUND_05_AGE:
                resId = R.raw._05_age;
                break;
            case SOUND_06_GENDER:
                resId = R.raw._06_gender;
                break;
            case SOUND_07_HEIGHT:
                resId = R.raw._07_height;
                break;
            case SOUND_08_TEST_START:
                resId = R.raw._08_test_start;
                break;
            case SOUND_09_TEST_20:
                resId = R.raw._09_test_progress_20;
                break;
            case SOUND_10_TEST_100:
                resId = R.raw._10_test_progress_100;
                break;
            case SOUND_11_TEST_FAIL:
                resId = R.raw._11_test_fail;
                break;
            case SOUND_12_PRINT:
                resId = R.raw._12_print;
                break;
            case SOUND_13_VIP_TOUCH_ID:
                resId = R.raw._13_vip_touch_id;
                break;
            case SOUND_14_VIP_TOUCH_ID_DONE:
                resId = R.raw._14_vip_touch_id_done;
                break;
            case SOUND_15_VIP_TOUCH_ID_FAIL:
                resId = R.raw._15_vip_touch_id_fail;
                break;
            case SOUND_30_LOG_UP:
                resId = R.raw._30_log_up;
                break;
            case SOUND_31_LOG_UP_TOUCH_ID_DONE:
                resId = R.raw._31_log_up_touch_id_done;
                break;
            case SOUND_32_LOG_UP_TOUCH_ID_FAIL:
                resId = R.raw._32_log_up_touch_id_fail;
                break;
            case SOUND_33_PRINT_FAIL:
                resId = R.raw._33_print_fail;
                break;
        }

//        stop(this);
        String RES_PREFIX = "android.resource://com.kangear.bodycompositionanalyzer/";
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(this, Uri.parse(RES_PREFIX + resId));//设置文件路径
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                }
            });
            mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return START_STICKY;
    }

    /**
     * 停止当前声音
     */
    public static void stop(Context context) {
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(AudioManager.ACTION_AUDIO_BECOMING_NOISY));
    }

    private class NoisyAudioStreamReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intent.getAction())) {
                // Pause the playback
                Log.i(TAG, "ACTION_AUDIO_BECOMING_NOISY");
                mMediaPlayer.reset();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mNoisyAudioStreamReceiver);
    }
}
