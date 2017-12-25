package com.kangear.bodycompositionanalyzer;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static com.kangear.bodycompositionanalyzer.WelcomeActivity.CONST_FINGER_ID;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.INVALID_FINGER_ID;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.exitAsFail;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.unkownError;

/**
 * 本页面不显示logo
 */
public class TouchIdActivity extends Com2Activity {
    private static final String TAG = "TouchIdActivity";
    private Context mContext;
    private static final int GET_FINGER_OK = 1;
    private ImageView mFingerImageView;
    private static final int PAGE_NORMAL       = 1;
    private static final int PAGE_SUCCESS      = 2;
    private static final int PAGE_FAIL         = 3;
    private static final int PAGE_SUCCESS_EXIT = 4;
    private static final int PAGE_FAIL_EXIT    = 5;
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

//    private int tryTime = 3;
    private void page(int page) {
        switch (page) {
            case PAGE_NORMAL:
                mFingerImageView.setBackgroundResource(R.drawable._80_finger_white);
                mThread.start();
                // 开始
                break;
            case PAGE_SUCCESS:
                mFingerImageView.setBackgroundResource(R.drawable._80_finger_yellow);
                // 停止
                // TODO: send 1 miao finish
                mHandler.sendEmptyMessageDelayed(PAGE_SUCCESS_EXIT, 1 * 1000);
                break;
            case PAGE_FAIL:
//                if (tryTime <= 0) {
                    mThread.interrupt();
                    mFingerImageView.setBackgroundResource(R.drawable._80_finger_red);
                    // 停止
                    // TODO: send 1 miao finish
                    mHandler.sendEmptyMessageDelayed(PAGE_FAIL_EXIT, 1 * 1000);
//                } else {
//                    Toast.makeText(this, "识别失败，请再试", Toast.LENGTH_LONG).show();
//                    mThread.interrupt();
//                    mHandler.sendEmptyMessageDelayed(PAGE_NORMAL, 2000);
//                    tryTime--;
//                }
                break;
            case PAGE_SUCCESS_EXIT:
                Intent intent = new Intent(this, WelcomeActivity.class);
                int fingerId = 0;
                intent.putExtra(WelcomeActivity.CONST_FINGER_ID, fingerId);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case PAGE_FAIL_EXIT:
                exitAsFail(this);
                break;
        }
    }

    /**
     * 线程
     */
    private Thread mThread = new Thread () {
        @Override
        public void run() {
            while(!isInterrupted()) {
                mFingerId = TouchID.getInstance(mContext).mache();
                if (mFingerId != INVALID_FINGER_ID) {
                    mHandler.sendEmptyMessage(PAGE_SUCCESS);
                    break;
                } else {
                    mHandler.sendEmptyMessage(PAGE_FAIL);
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    // This snippet hides the system bars.
    public static void hideSystemUI(View v) {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        v.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
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
        //Toast.makeText(this, "返回按钮按下", Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, "下一项按钮按下", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MemRegActivity.class);
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    @Override
    public void onNextButtonClick() {
        super.onNextButtonClick();
        //Toast.makeText(this, "下一项按钮按下", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MemRegActivity.class);
        intent.putExtra("FINGER_ID", mFingerId);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onDeleteClick() {
        super.onDeleteClick();
        //Toast.makeText(this, "删除按钮按下", Toast.LENGTH_SHORT).show();

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
