package com.kangear.bodycompositionanalyzer;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.annotation.ColorInt;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.yhao.floatwindow.FloatWindow;
import com.yhao.floatwindow.MoveType;
import com.yhao.floatwindow.PermissionListener;
import com.yhao.floatwindow.Screen;
import com.yhao.floatwindow.ViewStateListener;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * WatchDog
 */
public class WatchDog {
    private static final String TAG = "WatchDog";
    private static Context mContext = null;
    private static CountDownTimer mCountDownTimer = null;
    private static TextView textView;
    public static int TIMEOUT = 58; //default release timeout

    private volatile static WatchDog singleton = null;
    public WatchDog(Context context) {
        mContext = context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View myLayout = inflater.inflate(R.layout.float_count_down, null);
        textView = myLayout.findViewById(R.id.textView);
        textView.setTextColor(Color.BLUE);
        myLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.i(TAG, "onTouch");
                view.performClick();
                return false;
            }
        });

        // Debug
        if (BuildConfig.DEBUG) {
            TIMEOUT = 58;
        }

        FloatWindow
                .with(mContext)
                .setMoveType(MoveType.inactive)
                .setView(myLayout)
//                .setHeight(Screen.height, 1)
//                .setWidth(Screen.width, 1)
                .setWidth(50)                               //设置控件宽高
                .setHeight(30)
                .setX(Screen.width, 0.94f)                                   //设置控件初始位置
                .setY(Screen.height,0.0f)
                .setDesktopShow(true)                        //桌面显示
                .setViewStateListener(mViewStateListener)    //监听悬浮控件状态改变
                .setPermissionListener(mPermissionListener)  //监听权限申请结果
                .build();

        feed(Color.WHITE);
    }

    public void feed() {
        // cancel
        if (mCountDownTimer != null)
            mCountDownTimer.cancel();

        // restart
        mCountDownTimer = new CountDownTimer(TIMEOUT * 1000, 1 * 1000) {
            @Override
            public void onTick(long l) {
                textView.setText(String.valueOf((int)(l /1000)));
            }

            @Override
            public void onFinish() {
                // 最
                if (!isTopActivity()) {
                    Log.i(TAG, "不在TOP");
                    Intent intent = new Intent(mContext, WelcomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                } else {
                    Log.i(TAG, "已经在TOP");
                }
            }
        };

        mCountDownTimer.start();
    }

    public void feedSecond(int second) {
        // cancel
        if (mCountDownTimer != null)
            mCountDownTimer.cancel();

        // restart
        mCountDownTimer = new CountDownTimer(second * 1000, 1 * 1000) {
            @Override
            public void onTick(long l) {
                textView.setText(String.valueOf((int)(l /1000)));
            }

            @Override
            public void onFinish() {
                // 最
                if (!isTopActivity()) {
//                    Log.i(TAG, "不在TOP");
                    Intent intent = new Intent(mContext, WelcomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                } else {
//                    Log.i(TAG, "已经在TOP");
                }
            }
        };

        mCountDownTimer.start();
    }

    public void setVisible(boolean visible) {
        textView.setVisibility(!visible ? View.INVISIBLE : View.VISIBLE);
    }

    public void feed(@ColorInt int color) {
        feed();
    }

    public static TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            WatchDog.getInstance(mContext).feed();
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    public static boolean isTopActivity() {
        ActivityManager am = (ActivityManager)mContext.getSystemService(ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        return cn.getClassName().contains(WelcomeActivity.class.getName());
    }


    PermissionListener mPermissionListener = new PermissionListener() {
        @Override
        public void onSuccess() {

        }

        @Override
        public void onFail() {

        }
    };

    ViewStateListener mViewStateListener = new ViewStateListener() {
        @Override
        public void onPositionUpdate(int i, int i1) {

        }

        @Override
        public void onShow() {

        }

        @Override
        public void onHide() {

        }

        @Override
        public void onDismiss() {

        }

        @Override
        public void onMoveAnimStart() {

        }

        @Override
        public void onMoveAnimEnd() {

        }

        @Override
        public void onBackToDesktop() {

        }
    };


    // 请使用Application context
    public static WatchDog getInstance(Context context) {
        if (singleton == null) {
            synchronized (TouchID.class) {
                if (singleton == null) {
                    singleton = new WatchDog(context.getApplicationContext());
                }
            }
        }
        return singleton;
    }
}
