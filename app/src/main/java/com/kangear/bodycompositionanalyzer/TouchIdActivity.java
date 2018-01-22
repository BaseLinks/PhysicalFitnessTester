package com.kangear.bodycompositionanalyzer;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;

import static com.kangear.bodycompositionanalyzer.MusicService.SOUND_14_VIP_TOUCH_ID_DONE;
import static com.kangear.bodycompositionanalyzer.MusicService.SOUND_15_VIP_TOUCH_ID_FAIL;
import static com.kangear.bodycompositionanalyzer.MusicService.play;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.CONST_FINGER_ID;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.INVALID_FINGER_ID;

/**
 * 本页面不显示logo
 */
public class TouchIdActivity extends Com2Activity {
    private static final String TAG = "TouchIdActivity";
    private Context mContext;
    private static final int GET_FINGER_OK = 1;
    private ImageView mFingerImageView;
    private static final int PAGE_NORMAL           = 1;
    private static final int PAGE_SUCCESS          = 2;
    private static final int PAGE_FAIL             = 3;
    private static final int PAGE_SUCCESS_EXIT     = 4;
    private static final int PAGE_FAIL_EXIT        = 5;
    private static final int PAGE_DEVICE_UNCONNECT = 6;
    private int mFingerId = INVALID_FINGER_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touchid);
        hideSystemUI(getWindow().getDecorView());
        mFingerImageView = findViewById(R.id.finger_imageview);
        mContext = getApplicationContext();
        page(PAGE_NORMAL);
    }

    private int tryTime = 3;
    private DownloadFilesTask mTask;
    private void page(int page) {
        switch (page) {
            case PAGE_NORMAL:
                mFingerImageView.setBackgroundResource(R.drawable._80_finger_white);
                mTask = new DownloadFilesTask();
                mTask.execute(null, null, null);
                // 开始
                break;
            case PAGE_SUCCESS:
                mFingerImageView.setBackgroundResource(R.drawable._80_finger_yellow);
                // 停止
                // TODO: send 1 miao finish
                play(this, SOUND_14_VIP_TOUCH_ID_DONE);
                mHandler.sendEmptyMessageDelayed(PAGE_SUCCESS_EXIT, 2 * 1000);
                break;
            case PAGE_FAIL:
                mFingerImageView.setBackgroundResource(R.drawable._80_finger_red);
                tryTime--;
                if (tryTime <= 0) {
                    Toast.makeText(this, "识别失败", Toast.LENGTH_LONG).show();
                    mHandler.sendEmptyMessageDelayed(PAGE_FAIL_EXIT, 5 * 1000);
                } else {
                    play(this, SOUND_15_VIP_TOUCH_ID_FAIL);
                    Toast.makeText(this, "识别失败，请再试", Toast.LENGTH_LONG).show();
                    mHandler.sendEmptyMessageDelayed(PAGE_NORMAL, 2000);
                }
                break;
            case PAGE_SUCCESS_EXIT:
                Intent intent = new Intent();
                intent.putExtra(CONST_FINGER_ID, mFingerId);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case PAGE_FAIL_EXIT:
                Intent intent2 = new Intent();
                intent2.putExtra(CONST_FINGER_ID, mFingerId);
                setResult(RESULT_CANCELED, intent2);
                finish();
                break;
            case PAGE_DEVICE_UNCONNECT:
                Toast.makeText(this, "指纹模块异常，请联系工作人员", Toast.LENGTH_LONG).show();
                mHandler.sendEmptyMessageDelayed(PAGE_FAIL_EXIT, 2 * 1000);
                break;
        }
    }

    private class DownloadFilesTask extends AsyncTask<URL, Integer, Integer> {
        protected Integer doInBackground(URL... urls) {
            try {
                mFingerId = TouchID.getInstance(mContext).mache();
                if (mFingerId != INVALID_FINGER_ID) {
                    mHandler.sendEmptyMessage(PAGE_SUCCESS);
                } else {
                    mHandler.sendEmptyMessage(PAGE_FAIL);
                }
            } catch (IOException e) {
                // e.printStackTrace();
                // 未连接
                mHandler.sendEmptyMessage(PAGE_DEVICE_UNCONNECT);
            }
            Log.e(TAG, "mFingerId: " + mFingerId);
            return mFingerId;
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(int result) {
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            page(msg.what);
        }
    };

    @Override
    public void onBackButtonClick() {
        super.onBackButtonClick();
    }

    @Override
    public void onNextButtonClick() {
        super.onNextButtonClick();
    }

    @Override
    public void onDeleteClick() {
        super.onDeleteClick();
    }

    // from the link above
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
            Toast.makeText(this, "keyboard visible", Toast.LENGTH_SHORT).show();
        } else if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
            Toast.makeText(this, "keyboard hidden", Toast.LENGTH_SHORT).show();
            hideSystemUI(getWindow().getDecorView());
        }
    }
}
