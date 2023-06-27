package com.kangear.bodycompositionanalyzer.tool;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.gson.Gson;
import com.kangear.bodycompositionanalyzer.application.App;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.InvalidParameterException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android_serialport_api.ComBean;
import android_serialport_api.SerialHelper;
import android_serialport_api.SerialPortFinder;

public class SerialDevice {
    private static final boolean DEBUG = true;
    private static final String LOG_TAG = "SerialDevice";
    /** */
    private final SerialHelper     serialCtrl       = new SerialControl();;
    private final SerialPortFinder serialPortFinder = new SerialPortFinder();
    private String                 serialPort       = null;

    /** 串口相关 */
    private static final String BAUDRATE_COIN = "115200";

    private static Context mContext;
    private Response mResponse = null;

    byte[] allByteArray = new byte[512];
    ByteBuffer buff = ByteBuffer.wrap(allByteArray);

    public void setOnResponse(Response response) {
        mResponse = response;
    }

    private class SerialControl extends SerialHelper {
        @Override
        protected void onDataReceived(final ComBean ComRecData) {
            handleRecData(ComRecData);
        }

        /** 处理接收到串口数据事件 */
        private void handleRecData(final ComBean ComRecData) {
            if (DEBUG) Log.i(LOG_TAG, "handleRecData:" + bytesToHex(ComRecData.bRec));
            buff.put(ComRecData.bRec);
            mCountDownTimer.cancel();
            mCountDownTimer.start();
        }
    }

    private CountDownTimer mCountDownTimer = new CountDownTimer(1000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
        }

        @Override
        public void onFinish() {
            if (mResponse != null) {
                byte[] a = new byte[buff.position()];
                buff.rewind();
                buff.get(a);
                mResponse.onResponse(a);
            }
            buff.clear();
        }
    };

    /**
     * 单例模式: http://coolshell.cn/articles/265.html
     */
    private volatile static SerialDevice singleton = null;
    public static SerialDevice getInstance(Context context)   {
        if (singleton== null)  {
            synchronized (SerialDevice.class) {
                if (singleton== null)  {
                    singleton= new SerialDevice(context);
                }
            }
        }
        return singleton;
    }

    private SerialDevice(Context context) {
        this.mContext = context;
        init();
    }

    public static String bytesToHex(byte[] in) {
        final StringBuilder builder = new StringBuilder();
        for(byte b : in) {
            builder.append(String.format("%02x ", b));
        }
        return builder.toString();
    }

    private boolean updatePort() {
        serialPort = App.SERIAL_QR_SCAN;
        return true;
    }

    /** 初始化投币器 */
    private boolean init() {
        boolean ret = false;
        /* 如果已经打开状态，强制关闭，以中断诸如循环发送状态 */
        /* TODO:如果强制关闭会导致下次读取时会有缓存硬币数量 */
        if (serialCtrl.isOpen()) {
            return true;
        }

        /* 1. 打开串口 */
        if (serialPort == null) {
            ret = updatePort();
            if (!ret) {
                if (DEBUG) Log.w(LOG_TAG, "UpdatePort failure!");
                return false;
            }
        }

        ret = serialCtrl.setPort(serialPort);
        if (!ret) {
            if (DEBUG) Log.w(LOG_TAG, "SetPort serialProt failure!");
            return false;
        }
        ret = serialCtrl.setBaudRate(BAUDRATE_COIN);
        if (!ret) {
            if (DEBUG) Log.w(LOG_TAG, "setBaudRate serialProt failure!");
            return false;
        }
        try {
            serialCtrl.open();
        } catch (SecurityException e) {
            if (DEBUG) Log.w(LOG_TAG, "打开串口失败:没有串口读/写权限!");
            return false;
        } catch (IOException e) {
            if (DEBUG) Log.w(LOG_TAG, "打开串口失败:未知错误!");
            return false;
        } catch (InvalidParameterException e) {
            if (DEBUG) Log.w(LOG_TAG, "打开串口失败:参数错误!");
            return false;
        }

        return true;
    }

    private static final String CODE_TEST = "0000(测试)";
    private String mCode = CODE_TEST;

    private static final String ACTION_STATE = "TicketPrinter.StateMachine....xxxx";
    private static final String TICKET_PRINTER_STATE = "sdfafasdfawe";
    public static final int STATE_POWEROFF = 1;

    /**
     * 唯一的一个方法向外界发打印机状态
     * @param state
     */
    public static synchronized void broadcastState(int state) {
        if (mContext != null) {
            Intent intent = new Intent(ACTION_STATE);
            intent.putExtra(TICKET_PRINTER_STATE, state);
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
        }
    }

    public static abstract class Response {
        public abstract void onResponse(byte[] content);
    }
}
