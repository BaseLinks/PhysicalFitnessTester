package com.kangear.bodycompositionanalyzer;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.InvalidParameterException;
import java.util.Arrays;

import android_serialport_api.ComBean;
import android_serialport_api.SerialHelper;

import static com.kangear.common.utils.ByteArrayUtils.bytesToHex;

/**
 * Created by tony on 18-1-14.
 */

public class UartBca extends Protocol {
    private static final boolean DEBUG = true;
    private static final String LOG_TAG = "UartBca";

    /** 串口相关 */
    private static final int BAUDRATE = 115200;
    private static final String DEV_NODE = "/dev/ttyS0";
    private static final String TAG = LOG_TAG;
    private final SerialHelper serialCtrl = new SerialControl();

    /**
     * 单例模式: http://coolshell.cn/articles/265.html
     */
    private volatile static UartBca singleton = null;
    private final Context mContext;
    private volatile boolean hasData = false;
    private volatile byte[] mCache;

    public static UartBca getInstance(Context context)   {
        if (singleton== null)  {
            synchronized (UartBca.class) {
                if (singleton== null)  {
                    singleton= new UartBca(context.getApplicationContext());
                }
            }
        }
        return singleton;
    }

    private UartBca(Context context) {
        this.mContext = context;
        if (BuildConfig.DEBUG) {
            // do something for a debug build
            FAKE_DATA = true;
        } else {
            FAKE_DATA = false;
        }
        initMachine();
    }

    private class SerialControl extends SerialHelper {
        @Override
        protected void onDataReceived(final ComBean ComRecData) {
            handleRecData(ComRecData);
        }

        /** 处理接收到串口数据事件 */
        private void handleRecData(final ComBean ComRecData) {
            byte[] b = ComRecData.bRec;
            if (DEBUG) Log.i(LOG_TAG, "STX:" + b.length + " : " + bytesToHex(b));
            mCache = connectData(b);
            if (mCache != null)
                hasData = true;
        }
    }


    /** 处理错误 */
    @SuppressWarnings("unused")
    private static void handleError(double d, byte[] cache, byte[] in) {
        if (DEBUG) Log.w(LOG_TAG, "parseCoin    in: " + d + " (" + in.length + ") "
                + Arrays.toString(in));
        if (DEBUG) Log.w(LOG_TAG, "parseCoin cache: " + d + " (" + cache.length + ") "
                + Arrays.toString(cache));
    }

    private String errMsg = "未知错误";

    /** 初始化 */
    private boolean initMachine() {
        boolean ret = false;
		/* 如果已经打开状态，强制关闭，以中断诸如循环发送状态 */
		/* TODO:如果强制关闭会导致下次读取时会有缓存硬币数量 */
        if (serialCtrl.isOpen()) {
            try {
                serialCtrl.stopSend();
                Thread.sleep(1000);
                serialCtrl.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return false;
            }
        }

        ret = serialCtrl.setPort(DEV_NODE);
        if (!ret) {
            if (DEBUG) Log.w(LOG_TAG, "SetPort serialProt failure!");
            return false;
        }
        ret = serialCtrl.setBaudRate(BAUDRATE);
        if (!ret) {
            if (DEBUG) Log.w(LOG_TAG, "setBaudRate serialProt failure!");
            errMsg = "setBaudRate serialProt failure!";
            return false;
        }
        try {
            serialCtrl.open();
        } catch (SecurityException e) {
            if (DEBUG) Log.w(LOG_TAG, "打开串口失败:没有串口读/写权限!");
            errMsg = "打开串口失败:没有串口读/写权限!";
            return false;
        } catch (IOException e) {
            if (DEBUG) Log.w(LOG_TAG, "打开串口失败:未知错误!");
            errMsg = "打开串口失败:未知错误!";
            return false;
        } catch (InvalidParameterException e) {
            if (DEBUG) Log.w(LOG_TAG, "打开串口失败:参数错误!");
            errMsg = "打开串口失败:参数错误!";
            return false;
        }

        return true;
    }

    @Override
    public boolean send(byte[] buf, int timeout) {
        Log.i(TAG, "send");
        serialCtrl.send(buf);
        return true;
    }
    @Override
    public byte[] recv(int timeout) {
        Log.e(TAG, "recv: start =========================================== ");
        while (!hasData){
            if (Thread.currentThread().isInterrupted()||timeout<=1) {

                break;
            }
        }
        hasData = false;
        Log.e(TAG, "recv: end =====================================================");
        return mCache;
    }
}
