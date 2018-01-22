package com.kangear.bodycompositionanalyzer;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;

import static com.kangear.bodycompositionanalyzer.WelcomeActivity.CONST_FINGER_ID;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.INVALID_FINGER_ID;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.exitAsFail;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.hideSystemUI;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.unkownError;

/**
 * 本页面不显示logo
 */
public class GetFingerActivity extends AppCompatActivity {
    private static final String TAG = "GetFingerActivity";
    private Context mContext;
    private static final int GET_FINGER_OK         = 1;
    private static final int PAGE_FAIL_EXIT        = 5;
    private static final int PAGE_DEVICE_UNCONNECT = 6;
    private static final int FINGER_ALREADY_EXIST  = 7;
    private static final int UNKOWN_ERROR          = 8;
    private static final int EXIT_OK               = 9;
    private ImageView mFingerImageView;
    private int mFingerId;
    private TextView mTextView;
    private Button mCancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getfinger);
        hideSystemUI(getWindow().getDecorView());
        mFingerImageView = findViewById(R.id.finger_imageview);
        mTextView = findViewById(R.id.textview);
        mCancelButton = findViewById(R.id.no_button);
        mContext = getApplicationContext();
    }

    /**
     * 线程
     */
    private Thread mThread = new Thread () {
        @Override
        public void run() {
            boolean ret;
            int fingerId;
            while(!isInterrupted()) {
                try {
                    ret = TouchID.getInstance(mContext).getFinger();
                    if (ret) {
                        fingerId = TouchID.getInstance(mContext).macheFinger();
                        if (fingerId != INVALID_FINGER_ID) {
                            mHandler.sendEmptyMessage(FINGER_ALREADY_EXIST);
                        } else {
                            mHandler.sendEmptyMessage(GET_FINGER_OK);
                            break;
                        }
                    }

                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        //e.printStackTrace();
                        // 这里必须break，否则会导致返回时仍然在获取指纹
                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    mHandler.sendEmptyMessage(PAGE_DEVICE_UNCONNECT);
                    break;
                }
            }
        }
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GET_FINGER_OK:
                    mCancelButton.setEnabled(false);
                    mFingerImageView.setBackgroundResource(R.drawable._80_finger_yellow);
                    mTextView.setTextColor(Color.YELLOW);
                    mTextView.setText("指纹识别成功");
                    sendEmptyMessageDelayed(EXIT_OK, 1000);
                    break;
                case PAGE_FAIL_EXIT:
                    exitAsFail(GetFingerActivity.this);
                    break;
                case PAGE_DEVICE_UNCONNECT:
                    Toast.makeText(GetFingerActivity.this, "指纹模块异常，请联系工作人员", Toast.LENGTH_LONG).show();
                    mHandler.sendEmptyMessageDelayed(PAGE_FAIL_EXIT, 2 * 1000);
                    break;
                case FINGER_ALREADY_EXIST:
                    mTextView.setTextColor(Color.RED);
                    mFingerImageView.setBackgroundResource(R.drawable._80_finger_red);
//                    Toast.makeText(mContext, "指纹已经存在", Toast.LENGTH_LONG).show();
                    mTextView.setText("指纹已存在,请使用新指纹");
                    break;
                case UNKOWN_ERROR:
                    Toast.makeText(mContext, "指纹模块：未知错误!", Toast.LENGTH_LONG).show();
                    break;
                case EXIT_OK:
                    setResult(RESULT_OK, new Intent());
                    finish();
                    break;
            }
        }
    };

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

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.no_button:
                Intent intent = new Intent(this, MemRegActivity.class);
                setResult(RESULT_CANCELED, intent);
                finish();
                break;
        }
    }
}
