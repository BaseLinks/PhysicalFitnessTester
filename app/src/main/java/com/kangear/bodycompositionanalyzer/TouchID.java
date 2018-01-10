package com.kangear.bodycompositionanalyzer;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

import static com.kangear.bodycompositionanalyzer.WelcomeActivity.INVALID_FINGER_ID;
import static com.kangear.common.utils.ByteArrayUtils.bytesToHex;

/**
 * Created by tony on 17-12-23.
 */

public class TouchID {
    private static final String TAG = "TouchID";
    /**
     * 从传感器上读入图像存于图像缓冲区
     */
    public static final byte[] GR_GetImage         = {0x01};
    /**
     * 根据原始图像生成指纹特征存于 CharBuffer1 或 CharBuffer2
     */
    public static final byte[] GR_GenChar          = {0x02};
    /**
     * 精确比对 CharBuffer1 与 CharBuffer2 中的特征文件
     */
    public static final byte[] GR_Match            = {0x03};
    /**
     * 以 CharBuffer1 或 CharBuffer2 中的特征文件搜索整个或部分指纹库
     */
    public static final byte[] GR_Search           = {0x04};
    /**
     * 将 CharBuffer1 与 CharBuffer2 中的特征文件合并生成模板存于
     CharBuffer1 与 CharBuffer2
     */
    public static final byte[] GR_RegModel         = {0x05};
    /**
     * 将特征缓冲区中的文件储存到 flash 指纹库中
     */
    public static final byte[] GR_StoreChar        = {0x06};
    /**
     * 从 flash 指纹库中读取一个模板到特征缓冲区
     */
    public static final byte[] GR_LoadChar         = {0x07};
    /**
     * 将特征缓冲区中的文件上传给上位机
     */
    public static final byte[] GR_UpChar           = {0x08};
    /**
     * 从上位机下载一个特征文件到特征缓冲区
     */
    public static final byte[] GR_DownChar         = {0x09};
    /**
     * 上传原始图像
     */
    public static final byte[] GR_UpImage          = {0x0A};
    /**
     * 下载原始图像
     */
    public static final byte[] GR_DownImage        = {0x0B};
    /**
     * 删除 flash 指纹库中的一个特征文件
     */
    public static final byte[] GR_DeletChar        = {0x0C};
    /**
     * 清空 flash 指纹库
     */
    public static final byte[] GR_Empty            = {0x0D};
    /**
     * 设置系统参数
     */
    public static final byte[] GR_WriteReg         = {0x0E};
    /**
     * 读系统基本参数
     */
    public static final byte[] GR_ReadSysPara      = {0x0F};
    /**
     * 注册模板
     */
    public static final byte[] GR_Enroll           = {0x10};
    /**
     * 验证指纹
     */
    public static final byte[] GR_Identify         = {0x11};
    /**
     * 设置设备握手口令
     */
    public static final byte[] GR_SetPwd           = {0x12};
    /**
     * 验证设备握手口令
     */
    public static final byte[] GR_VfyPwd           = {0x13};
    /**
     * 采样随机数
     */
    public static final byte[] GR_GetRandomCode    = {0x14};
    /**
     * 设置模块地址
     */
    public static final byte[] GR_SetAddr          = {0x15};
    /**
     * 通讯端口（UART/USB）开关控制
     */
    public static final byte[] GR_Port_Control     = {0x17};
    // ? 0x16
    /**
     * 写记事本
     */
    public static final byte[] GR_WriteNotepad     = {0x18};
    /**
     * 读记事本
     */
    public static final byte[] GR_ReadNotepad      = {0x19};
    /**
     * 高速搜索 FLASH
     */
    public static final byte[] GR_HighSpeedSearch  = {0x1B};
    /**
     * 生成二值化指纹图像
     */
    public static final byte[] GR_GenBinImage      = {0x1C};
    /**
     * 读有效模板个数
     */
    public static final byte[] GR_ValidTempleteNum = {0x1D};

    public static final byte[] ARGE_NONE = {};


    /**
     * 包头
     */
    private static final byte[] PACKET_HEAD   = {(byte) 0xEF, 0x01};
    /**
     * 模块地址
     */
    private static final byte[] MODULE_ADDR   = {(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};
    /**
     * 包标识
     */
    private static final byte[] PACKET_TAG_SEND = {0x01};

    /**
     * 包长度=包长度至校验和（指令、参数或数据）的总字节数，
     * 包含校验和，但不包含包长度本身的字节数。
     */
    private static final byte PACKET_LENGTH = 0;

    /**
     * 校验和是从包标识至校验和之间所有字节之和，超出 2 字节的进位忽略。
     */
    private static byte[] CHECK_SUM = {0x00, 0x00};
    private static final byte CHECK_SUM_LENGTH = 2;

    private static int BYTE_BUFFER_ALLOCATE = 1024;

    /**
     * 和校验
     * @param arr
     * @return
     */
    public static int sum(byte[] arr, int start, int end) {
        int sum = 0;
        if (arr != null && arr.length >= end) {
            for (int i = start; i < end; i++)
                sum += arr[i];
        }
        return sum;
    }

    public static long getUnsignedInt(int x) {
        return x & (-1L >>> 32);
    }

    /**
     * 和校验
     * @param arr
     * @return
     */
    public static byte[] sumAsArray(byte[] arr, int start, int end) {
        byte[] arr2 = new byte[2];
        int sum = 0;
        if (arr != null && arr.length >= end) {
            for (int i = start; i < end; i++)
                sum += (arr[i] & 0xFF);
        }
        arr2[0] = (byte)((sum >> 8) & 0xFF);
        arr2[1] = (byte)(sum & 0xFF);
        return arr2;
    }

    /**
     * @param cmd 指令码
     * @return
     */
    public static byte[] cmdPackage(byte cmd[], byte[] arg) {
        // 计算包长度
        int length = cmd.length + arg.length + CHECK_SUM.length; // cmd'length(1) + arg'length

        byte[] byteArray = null;
        ByteBuffer target = ByteBuffer.allocate(BYTE_BUFFER_ALLOCATE);
        target.clear();
        /* 1. 包头 */
        target.put(PACKET_HEAD);

        /* 2. 模块地址 */
        target.put(MODULE_ADDR);

        /* 3. 包标识 从这里开始SUM */
        int startSum = PACKET_HEAD.length + MODULE_ADDR.length + PACKET_TAG_SEND.length - 1;
        target.put(PACKET_TAG_SEND);

        /* 4. 包长度 */
        target.putShort((short) length);

        /* 5. 指令码 */
        target.put(cmd);

        /* 6. 参数 */
        target.put(arg);

        /* 7. SUM */
        int sum = sum(target.array(), startSum, target.array().length);
        target.putShort((short) sum);

        target.limit(target.remaining());
        target.rewind();
		/* 提取 */
        byteArray = new byte[BYTE_BUFFER_ALLOCATE - target.remaining()];
        target.get(byteArray);
		/* 将byteBuffer清理 */
        target.clear();
        return byteArray;
    }

    /**
     * @param response response
     * @return data
     */
    public static byte[] parsePackage(byte response[]) {
        byte[] b;
        byte[] ret;
        int tmpInt;
        int end = 0;
        int start = 0;
        int length = 0;

        byte[] RESPONSE_PACK_HEAD    = {(byte) 0xEF, 0x01};
        byte[] RESPONSE_MODULE_ADDR  = {(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};
        byte[] RESPONSE_PRCK_TAG     = {0x07};

        int RESPONSE_PACK_HEAD_START    = 0; // LENGTH 2
        int RESPONSE_MODULE_ADDR_START  = 2; // LENGTH 4
        int RESPONSE_PACK_TAG_START     = 6; // LENGTH 1
        int RESPONSE_PACK_LENGTH_START  = 7; // LENGTH 2

        int RESPONSE_PACK_HEAD_LENGTH    = RESPONSE_PACK_HEAD.length;   // LENGTH 2
        int RESPONSE_MODULE_ADDR_LENGTH  = RESPONSE_MODULE_ADDR.length; // LENGTH 4
        int RESPONSE_PACK_TAG_LENGTH     = RESPONSE_PRCK_TAG.length;    // LENGTH 1
        int RESPONSE_PACK_LENGTH_LENGTH  = 2; // LENGTH 2
        int RESPONSE_SUM_LENGTH          = 2; // LENGTH 2

        // 1. Package Head check
        start = RESPONSE_PACK_HEAD_START;
        end = start + RESPONSE_PACK_HEAD_LENGTH;
        if (response.length < end)
            return null;
        b = Arrays.copyOfRange(response, start, end);
        if (!Arrays.equals(b, RESPONSE_PACK_HEAD)) {
            Log.e(TAG, "parsePackage: RESPONSE_PACK_HEAD error");
            return null;
        }

        // 2. Module addr
        start = RESPONSE_MODULE_ADDR_START;
        end = start + RESPONSE_MODULE_ADDR_LENGTH;
        if (response.length < end) {
            Log.e(TAG, "parsePackage: response.length error");
            return null;
        }
        b = Arrays.copyOfRange(response, start, end);
        if (!Arrays.equals(b, RESPONSE_MODULE_ADDR)) {
            Log.e(TAG, "parsePackage: RESPONSE_MODULE_ADDR error");
            return null;
        }

        // 3. Package tag
        start = RESPONSE_PACK_TAG_START;
        end = start + RESPONSE_PACK_TAG_LENGTH;
        if (response.length < end) {
            Log.e(TAG, "parsePackage: response.length error");
            return null;
        }
        b = Arrays.copyOfRange(response, start, end);
        if (!Arrays.equals(b, RESPONSE_PRCK_TAG)) {
            Log.e(TAG, "parsePackage: RESPONSE_PRCK_TAG error");
            return null;
        }

        // 6. Package Length
        start = RESPONSE_PACK_LENGTH_START;
        end = start + RESPONSE_PACK_LENGTH_LENGTH;
        if (response.length < end) {
            Log.e(TAG, "parsePackage: response.length error");
            return null;
        }
        b = Arrays.copyOfRange(response, start, end);
        length = ByteBuffer.wrap(b).order(ByteOrder.BIG_ENDIAN).getShort() & 0xFFFF;


        // 7. Package Data
        start = RESPONSE_PACK_LENGTH_START + RESPONSE_PACK_LENGTH_LENGTH;
        end = start + (length - RESPONSE_SUM_LENGTH);
        if (response.length < end) {
            Log.e(TAG, "parsePackage: response.length error");
            return null;
        }
        ret = Arrays.copyOfRange(response, start, end);

        // 8. Package SUM
        int startSum = RESPONSE_PACK_TAG_START;
        int endSum = startSum + RESPONSE_PACK_TAG_LENGTH + RESPONSE_PACK_LENGTH_LENGTH + (length - RESPONSE_SUM_LENGTH) ;
        start = RESPONSE_PACK_LENGTH_START + RESPONSE_PACK_LENGTH_LENGTH + (length - RESPONSE_SUM_LENGTH);
        end = start + RESPONSE_SUM_LENGTH;
        if (response.length < end) {
            Log.e(TAG, "parsePackage: response.length error");
            return null;
        }
        b = Arrays.copyOfRange(response, start, end);
        byte[] sum = sumAsArray(response, startSum, endSum);
        if (!Arrays.equals(b, sum)) {
            Log.e(TAG, "parsePackage: RESPONSE_SUM error read: " + bytesToHex(b) + " but cal: " + bytesToHex(sum) + "( " + startSum + "-" + endSum + " )");
            return null;
        }

        //Log.e(TAG, "parsePackage: " + bytesToHex(ret));
        return ret;
    }


    private boolean register(int fingerId) throws IOException {
        // TODO: 这里等待DeviceConnection被打开
        return insert((short) fingerId);
    }

    private static final byte CharBuffer1 = 0x01;
    private static final byte[] RESULT_OK = {0x00};
    private static final byte start = 0;
    private static final byte stop  = 100;

    /**
     * 验证Touch ID
     * TODO: 返回fingerid 如果不存在就返回INVALID_FINGER_ID
     * @return
     */
    public int mache() throws IOException {
        FingerUSB fu = mFingerUsb;
        byte[] b;
        int fingerId = INVALID_FINGER_ID;
        boolean ret;
        // 1. GR_GetImage
        do {
            ret = fu.send(TouchID.cmdPackage(TouchID.GR_GetImage, ARGE_NONE));
            if (!ret) {
                return fingerId;
            }

            b = TouchID.parsePackage(fu.read());
            ret = ((b != null) && Arrays.equals(b, RESULT_OK));

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (!ret);
        Log.e(TAG, "Finger: " + bytesToHex(b));

        // 2. GR_GenChar
        ret = fu.send(TouchID.cmdPackage(TouchID.GR_GenChar, new byte[]{CharBuffer1}));
        if (!ret) {
            return fingerId;
        }
        b = TouchID.parsePackage(fu.read());
        if (b == null) {
            return fingerId;
        }
        Log.e(TAG, "Finger: " + bytesToHex(b));

        // 3. 搜索指纹 GR_Search
        ret = fu.send(TouchID.cmdPackage(TouchID.GR_Search, new byte[]{CharBuffer1, 0x00, 0x00, 0x00, 0x64}));
        if (!ret) {
            return fingerId;
        }
        b = TouchID.parsePackage(fu.read());
        int code = 0;
        int PageID = 0;
        int MatchScore = 0;
        if (b!=null) Log.e(TAG, "Finger: GR_Search " + bytesToHex(b));
        if (b == null || !Arrays.equals(Arrays.copyOfRange(b, 0, 1), RESULT_OK)) {
            Log.e(TAG, "TouchID.parsePackage(fu.read()) == null or mach fail");
            return fingerId;
        }

        code = b[0];
        PageID = ByteBuffer.wrap(Arrays.copyOfRange(b, 1, 3)).order(ByteOrder.BIG_ENDIAN).getShort() & 0xFFFF;
        MatchScore = ByteBuffer.wrap(Arrays.copyOfRange(b, 3, 5)).order(ByteOrder.BIG_ENDIAN).getShort() & 0xFFFF;
        Log.e(TAG, "Finger: " + bytesToHex(b) + " code: " + code + " PageID: " + PageID + " MatchScore: " + MatchScore);
        return PageID;
    }


    private volatile static TouchID singleton = null;
    private final FingerUSB mFingerUsb;
    private Context mContext;

    public TouchID(Context context) {
        mContext = context;
        mFingerUsb = new FingerUSB();
        mFingerUsb.open(mContext);
    }
    // 请使用Application context
    public static TouchID getInstance(Context context) {
        if (singleton == null) {
            synchronized (TouchID.class) {
                if (singleton == null) {
                    singleton = new TouchID(context);
                }
            }
        }
        return singleton;
    }

    /**
     * 增
     * TODO: 判断本地是否已经有该指纹了，目前版本暂不添加此功能了
     * TODO: 返回主键FingerId
     */
    private boolean insert(short pageId) throws IOException {
        FingerUSB fu = mFingerUsb;
        byte[] b;
        boolean ret;
        // 1. GR_GetImage
        ret = fu.send(TouchID.cmdPackage(TouchID.GR_GetImage, ARGE_NONE));
        if (!ret) {
            return false;
        }

        b = TouchID.parsePackage(fu.read());
        ret = ((b != null) && Arrays.equals(b, RESULT_OK));
        if (!ret) {
            return false;
        }
        //Log.e(TAG, "Finger: " + bytesToHex(b));

        // 2. GR_GenChar
        ret = fu.send(TouchID.cmdPackage(TouchID.GR_GenChar, new byte[]{CharBuffer1}));
        if (!ret) {
            return false;
        }
        b = TouchID.parsePackage(fu.read());
        if (b == null) {
            return false;
        }
        //Log.e(TAG, "Finger: " + bytesToHex(b));

        // 3. GR_StoreChar
        ret = fu.send(TouchID.cmdPackage(TouchID.GR_StoreChar, new byte[]{CharBuffer1, (byte)((pageId & 0xFFFF) >> 8), (byte)(pageId & 0xFFFF)}));
        if (!ret) {
            return false;
        }
        b = TouchID.parsePackage(fu.read());
        if (b == null || !Arrays.equals(b, RESULT_OK)) {
            return false;
        }
        //Log.e(TAG, "Finger: " + bytesToHex(b));

        return true;
    }

    /**
     * 获取一个指纹
     * @return
     * @throws IOException
     */
    public boolean getFinger() throws IOException {
        FingerUSB fu = mFingerUsb;
        byte[] b;
        boolean ret;
        // 1. GR_GetImage
        ret = fu.send(TouchID.cmdPackage(TouchID.GR_GetImage, ARGE_NONE));
        if (!ret) {
            return false;
        }

        b = TouchID.parsePackage(fu.read());
        ret = ((b != null) && Arrays.equals(b, RESULT_OK));
        if (!ret) {
            return false;
        }
        //Log.e(TAG, "Finger: " + bytesToHex(b));

        // 2. GR_GenChar
        ret = fu.send(TouchID.cmdPackage(TouchID.GR_GenChar, new byte[]{CharBuffer1}));
        if (!ret) {
            return false;
        }
        b = TouchID.parsePackage(fu.read());
        if (b == null) {
            return false;
        }
        // TODO: fixme 判断成功方法不太对
        return true;
    }

    /**
     * 验证Touch ID，如果存在，则返回fingerId,否则返回INVALID
     * @return
     */
    public int macheFinger() throws IOException {
        FingerUSB fu = mFingerUsb;
        byte[] b;
        int fingerId = INVALID_FINGER_ID;
        boolean ret;
        // 1. 搜索指纹 GR_Search
        ret = fu.send(TouchID.cmdPackage(TouchID.GR_Search, new byte[]{CharBuffer1, 0x00, 0x00, 0x00, 0x64}));
        if (!ret) {
            return fingerId;
        }
        b = TouchID.parsePackage(fu.read());
        int code = 0;
        int PageID = 0;
        int MatchScore = 0;
        if (b!=null) Log.e(TAG, "Finger: GR_Search " + bytesToHex(b));
        if (b == null || !Arrays.equals(Arrays.copyOfRange(b, 0, 1), RESULT_OK)) {
            Log.e(TAG, "TouchID.parsePackage(fu.read()) == null or mach fail");
            return fingerId;
        }

        code = b[0];
        PageID = ByteBuffer.wrap(Arrays.copyOfRange(b, 1, 3)).order(ByteOrder.BIG_ENDIAN).getShort() & 0xFFFF;
        MatchScore = ByteBuffer.wrap(Arrays.copyOfRange(b, 3, 5)).order(ByteOrder.BIG_ENDIAN).getShort() & 0xFFFF;
        Log.e(TAG, "Finger: " + bytesToHex(b) + " code: " + code + " PageID: " + PageID + " MatchScore: " + MatchScore);
        return PageID;
    }

    /**
     * 增
     * TODO: 判断本地是否已经有该指纹了，目前版本暂不添加此功能了
     * TODO: 返回主键FingerId
     */
    public boolean saveFinger(short pageId) throws IOException {
        FingerUSB fu = mFingerUsb;
        byte[] b;
        boolean ret;
        // 1. GR_StoreChar
        ret = fu.send(TouchID.cmdPackage(TouchID.GR_StoreChar, new byte[]{CharBuffer1, (byte)((pageId & 0xFFFF) >> 8), (byte)(pageId & 0xFFFF)}));
        if (!ret) {
            return false;
        }
        b = TouchID.parsePackage(fu.read());
        return (b != null && Arrays.equals(b, RESULT_OK));
    }


    /**
     * 删
     */
    void delete() {
    }

    /**
     * 改
     */
    void update() {
    }

    /**
     * 查
     */
    void query() {
    }

    /**
     * clear all
     */
    public boolean clearAll() {
        Log.i(TAG, "clearAll");
        FingerUSB fu = mFingerUsb;
        byte[] b;
        boolean ret = false;
        // 1. GR_Empty
        try {
            ret = fu.send(TouchID.cmdPackage(TouchID.GR_Empty, ARGE_NONE));
            if (!ret) {
                return false;
            }

            b = TouchID.parsePackage(fu.read());
            ret = ((b != null) && Arrays.equals(b, RESULT_OK));
            if (b != null)
                Log.e(TAG, "GR_Empty: " + bytesToHex(b));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ret;
    }
}
