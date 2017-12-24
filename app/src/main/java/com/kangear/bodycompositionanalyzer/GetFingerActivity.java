package com.kangear.bodycompositionanalyzer;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 本页面不显示logo
 */
public class GetFingerActivity extends Com2Activity {
    private static final String TAG = "GetFingerActivity";
    private TextView mTextView;
    private View logoView;
    private Context mContext;
    private static final int GET_FINGER_OK = 1;
    private ImageView mFingerImageView;
    private Button mNextButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getfinger);
        hideSystemUI(getWindow().getDecorView());
        logoView = findViewById(R.id.logo_imageview);
        logoView.setVisibility(View.GONE);
        mFingerImageView = findViewById(R.id.finger_imageview);
        mNextButton = findViewById(R.id.kb_next_button);
        dissAllwithoutBackNext();
        mContext = getApplicationContext();
        mNextButton.setEnabled(false);
    }

    /**
     * 线程
     */
    private Thread mThread = new Thread () {
        @Override
        public void run() {
            boolean ret;
            while(!isInterrupted()) {
                ret = TouchID.getInstance(mContext).register(0);
                if (ret) {
                    mHandler.sendEmptyMessage(GET_FINGER_OK);
                    break;
                }

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private void dissAllwithoutBackNext() {
        int[] reses = {
                R.id.kb_0_button,
                R.id.kb_1_button,
                R.id.kb_2_button,
                R.id.kb_3_button,
                R.id.kb_4_button,
                R.id.kb_5_button,
                R.id.kb_6_button,
                R.id.kb_7_button,
                R.id.kb_8_button,
                R.id.kb_9_button,
                R.id.kb_dot_button,
                R.id.kb_softboard_button,
        };

        for (int res : reses) {
            findViewById(res).setVisibility(View.GONE);
        }
    }

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
            switch (msg.what){
                case GET_FINGER_OK:
                    mFingerImageView.setBackgroundResource(R.drawable._20_finger_ok);
                    mNextButton.setEnabled(true);
                    break;
            }
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
        intent.putExtra("FINGER_ID", 1);
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

    @Override
    protected void onResume() {
        super.onResume();
        if (mThread != null)
            mThread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mThread.interrupt();
    }
}
