package com.kangear.utils;

import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.widget.TextView;

public class TimeUtils {
    private TextView mTvTime;
    private TextView mTvDate;
    private boolean isStart;

    public TimeUtils(TextView tvTime, TextView tvDate) {
        mTvTime = tvTime;
        mTvDate = tvDate;

        TimeThread thread = new TimeThread();
        thread.start();
    }

    private class TimeThread extends Thread {
        @Override
        public void run() {
            do {
                try {
                    Thread.sleep(1000);
                    if (isStart) {
                        Message msg = new Message();
                        msg.what = 1;
                        mHandler.sendMessage(msg);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }
    }

    public void start() {
        isStart = true;
    }

    public void stop() {
        isStart = false;
    }

    //在主线程里面处理消息并更新UI界面
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    long sysTime = System.currentTimeMillis();
                    CharSequence sysTimeStr = DateFormat.format("hh:mm:ss", sysTime);
                    CharSequence sysDateStr = DateFormat.format("yyyy-MM-dd", sysTime);
                    mTvTime.setText(sysTimeStr); //更新时间
                    mTvDate.setText(sysDateStr);
                break;
            default:
                break;
            }
        }
    };
}
