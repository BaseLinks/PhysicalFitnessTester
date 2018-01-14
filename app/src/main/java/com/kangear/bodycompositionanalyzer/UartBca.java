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
    private static final int BYTE_BUFFER_ALLOCATE = 1024;
    private ByteBuffer mByteBuffer = ByteBuffer.allocate(BYTE_BUFFER_ALLOCATE);
    private volatile boolean hasData = false;
    private volatile long mLastTime;
    private volatile boolean hasHead = false;
    private volatile int curPackLength = 0;
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
            connectData(b);
        }
    }

    /**
     * 将数据连接
     * @param b
     */
    synchronized  void connectData(byte[] b) {
        // 获取目前长度
        mByteBuffer.put(b);
        int len = BYTE_BUFFER_ALLOCATE - mByteBuffer.remaining();
        Log.i(TAG, "connectData 1 len: " + len);

        // 获取head length 字节(3)
        if (len < 3) {
            return;

        }
        Log.i(TAG, "connectData 2");
        // 获取长度
        if (curPackLength == 0) {
            mByteBuffer.limit(mByteBuffer.remaining());
            mByteBuffer.rewind();
            byte[] headAndLength = new byte[len];
            mByteBuffer.get(headAndLength);
            if ((headAndLength[0] == (byte)0x55) && (headAndLength[1] == (byte)0xAA)) {
                curPackLength = (headAndLength[2] & 0xFF);
            } else {
                mByteBuffer.clear();
                return;
            }
            Log.i(TAG, "" + bytesToHex(headAndLength));
        }
        Log.i(TAG, "connectData 3 curPackLength: " + curPackLength);

        // 总长度不符合
        if (len < (curPackLength + 3)) {
            return;
        }

        Log.i(TAG, "connectData 4");
        mByteBuffer.limit(mByteBuffer.remaining());
        mByteBuffer.rewind();
        /* 提取 */
        Log.i(TAG, "connectData 5");
        mCache = new byte[curPackLength + 3];
        mByteBuffer.get(mCache);

        Log.i(TAG, "mCache: " + mCache.length + " : " + bytesToHex(mCache));

        Log.i(TAG, "connectData 6");
        /* 将byteBuffer清理 */
        mByteBuffer.clear();
        Log.i(TAG, "connectData 7");

        hasData = true;
        curPackLength = 0;
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
        while (!hasData){}
        hasData = false;
        Log.e(TAG, "recv: end =====================================================");
        return mCache;
    }
}
