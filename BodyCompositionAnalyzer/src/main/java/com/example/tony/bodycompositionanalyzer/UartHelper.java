package com.example.tony.bodycompositionanalyzer;

import android.content.Context;
import android.util.Log;
import android.widget.RelativeLayout;

import com.ftdi.j2xx.D2xxManager;
import com.ftdi.j2xx.FT_Device;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidParameterException;

import android_serialport_api.ComBean;
import android_serialport_api.MyFunc;
import android_serialport_api.SerialPort;

/**
 * Created by tony on 16-6-28.
 */
public abstract class UartHelper {
    private static final String LOG_TAG = "UartHelper";
    private SerialPort mSerialPort;
    // read data from USB
    private ReadThread mReadThread;
    private SendThread mSendThread;
    private String sPort;
    private int iBaudRate;
    private boolean _isOpen;
    private byte[] _bLoopData;
    private int iDelay;
    protected Context mContext;

    /**
     * 打开串口，设置参数
     * @throws SecurityException
     * @throws IOException
     * @throws InvalidParameterException
     */
    public void open() throws SecurityException, IOException, InvalidParameterException {
        boolean ret = false;
		/* port: 0 */
        portIndex = 0;

        // j2xx functions 获取当前设备列表
        createDeviceList();
        if(DevCount <= 0) {
            throw new InvalidParameterException("There is no any serial port can be open!");
        }

        // 打开串口
        ret = connectFunction();
        if (!ret) {
            throw new InvalidParameterException("connectFunction Fail");
        }
        // configure port
        // reset to UART mode for 232 devices

        ret = ftDev.setBitMode((byte) 0, D2xxManager.FT_BITMODE_RESET);
        if (!ret) {
            throw new InvalidParameterException("setBitMode Fail");
        }

        /* 数据位，停止位，校验，这里写死 8 1 None*/
        ftDev.setDataCharacteristics(D2xxManager.FT_DATA_BITS_8, D2xxManager.FT_STOP_BITS_1, D2xxManager.FT_PARITY_NONE);
        if (!ret) {
            throw new InvalidParameterException("setDataCharacteristics Fail");
        }

        /* 流控，这里写死 None */
        ftDev.setFlowControl(D2xxManager.FT_FLOW_NONE, XON, XOFF);
        if (!ret) {
            throw new InvalidParameterException("setFlowControl Fail");
        }

        /* 波特率 */
        ftDev.setBaudRate(iBaudRate);
        if (!ret) {
            throw new InvalidParameterException("setBaudRate Fail");
        }

        // 启动读数据线程
        this.mReadThread = new ReadThread();
        this.mReadThread.start();

        this.mSendThread = new SendThread();
        this.mSendThread.setSuspendFlag();
        this.mSendThread.start();
        this._isOpen = true;
    }

    /**
     * 关闭串口
     */
    public void close() {
        if (this.mReadThread != null) {
            this.mReadThread.interrupt();
        }

        if (this.mSerialPort != null) {
            this.mSerialPort.close();
            this.mSerialPort = null;
        }

        disconnectFunction();

        this._isOpen = false;
    }

    /**
     * 发送byteArray
     * @param bOutArray
     */
    public void send(byte[] bOutArray) {
        sendData(bOutArray.length, bOutArray);
    }

    /**
     * 发送16进制数
     * @return
     */
    public void sendHex(String sHex) {
        byte[] bOutArray = MyFunc.HexToByteArr(sHex);
        this.send(bOutArray);
    }

    /**
     * 发送文本
     * @return
     */
    public void sendTxt(String sTxt) {
        byte[] bOutArray = sTxt.getBytes();
        this.send(bOutArray);
    }

    /**
     * 获取波特率
     * @return
     */
    public int getBaudRate() {
        return this.iBaudRate;
    }

    /**
     * 设置波特率
     * @param iBaud
     * @return
     */
    public boolean setBaudRate(int iBaud) {
        if (this._isOpen) {
            return false;
        } else {
            this.iBaudRate = iBaud;
            return true;
        }
    }

    /**
     * 设置波特率
     * @param sBaud
     * @return
     */
    public boolean setBaudRate(String sBaud) {
        int iBaud = Integer.parseInt(sBaud);
        return this.setBaudRate(iBaud);
    }

    /**
     * TODO:暂未实现
     * @return
     */
    public String getPort() {
        return this.sPort;
    }

    /**
     * TODO:暂未实现
     * @param sPort
     * @return
     */
    public boolean setPort(String sPort) {
        if (this._isOpen) {
            return false;
        } else {
            this.sPort = sPort;
            return true;
        }
    }

    public boolean isOpen() {
        return this._isOpen;
    }

    /**
     * TODO:暂未实现
     */
    public byte[] getbLoopData() {
        return this._bLoopData;
    }

    /**
     * TODO:暂未实现
     */
    public void setbLoopData(byte[] bLoopData) {
        this._bLoopData = bLoopData;
    }

    /**
     * TODO:暂未实现
     */
    public void setTxtLoopData(String sTxt) {
        this._bLoopData = sTxt.getBytes();
    }

    /**
     * TODO:暂未实现
     */
    public void setHexLoopData(String sHex) {
        this._bLoopData = MyFunc.HexToByteArr(sHex);
    }

    /**
     * TODO:暂未实现
     */
    public int getiDelay() {
        return this.iDelay;
    }

    /**
     * TODO:暂未实现
     */
    public void setiDelay(int iDelay) {
        this.iDelay = iDelay;
    }

    /**
     * TODO:暂未实现
     */
    public void startSend() {
        if (this.mSendThread != null) {
            this.mSendThread.setResume();
        }

    }

    /**
     * TODO:暂未实现
     */
    public void stopSend() {
        if (this.mSendThread != null) {
            this.mSendThread.setSuspendFlag();
        }

    }

    /**
     * 设置Context对象实例，需要在子类的构造方法中调用
     * @param context
     */
    public void init(Context context) {
        this.mContext = context;

        try {
            ftD2xx = D2xxManager.getInstance(context);
        } catch (D2xxManager.D2xxException e) {
            Log.e("FTDI_HT", "getInstance fail!!");
        }

		/* allocate buffer */
        writeBuffer = new byte[512];
        readBuffer = new byte[UI_READ_BUFFER_SIZE];
        readBufferToChar = new char[UI_READ_BUFFER_SIZE];
        readDataBuffer = new byte[MAX_NUM_BYTES];
        actualNumBytes = 0;
    }

    /**
     * 循环读数据线程
     */
    class ReadThread extends Thread {
        final int USB_DATA_BUFFER = 8192;
        ReadThread() {
            this.setPriority(MAX_PRIORITY);
        }

        @Override
        public void run() {
            byte[] usbdata = new byte[USB_DATA_BUFFER];
            int readcount = 0;
            int iWriteIndex = 0;
            bReadTheadEnable = true;

            while (true == bReadTheadEnable) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                while (iTotalBytes > (MAX_NUM_BYTES - (USB_DATA_BUFFER + 1))) {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                readcount = ftDev.getQueueStatus();
                if (readcount > 0) {
                    if (readcount > USB_DATA_BUFFER) {
                        readcount = USB_DATA_BUFFER;
                    }
                    ftDev.read(usbdata, readcount);
                    onDataReceived(new ComBean("", usbdata, readcount));
                }
            }
            Log.e(LOG_TAG, "read thread terminate...");
        }
    }

    /**
     * 抽象方法，需要在子类中实现
     * @param var1
     */
    protected abstract void onDataReceived(ComBean var1);

    /**
     * 发送数据线程
     */
    private class SendThread extends Thread {
        public boolean suspendFlag;

        private SendThread() {
            this.suspendFlag = true;
        }

        public void run() {
            super.run();

            while (!this.isInterrupted()) {
                synchronized (this) {
                    while (this.suspendFlag) {
                        try {
                            this.wait();
                        } catch (InterruptedException var4) {
                            var4.printStackTrace();
                        }
                    }
                }

                send(getbLoopData());

                try {
                    Thread.sleep((long) iDelay);
                } catch (InterruptedException var3) {
                    var3.printStackTrace();
                }
            }

        }

        public void setSuspendFlag() {
            this.suspendFlag = true;
        }

        public synchronized void setResume() {
            this.suspendFlag = false;
            this.notify();
        }
    }

    // j2xx
    public static D2xxManager ftD2xx = null;
    FT_Device ftDev;
    int DevCount = -1;
    int currentPortIndex = -1;
    int portIndex = -1;

    enum DeviceStatus {
        DEV_NOT_CONNECT,
        DEV_NOT_CONFIG,
        DEV_CONFIG
    }
    final byte SOH = 1;    /* Start Of Header */
    final byte STX = 2;    /* Start Of Header 1K */
    final byte EOT = 4;    /* End Of Transmission */
    final byte ACK = 6;    /* ACKnowlege */
    final byte NAK = 0x15; /* Negative AcKnowlege */
    final byte CAN = 0x18; /* Cancel */
    final byte CHAR_C = 0x43; /* Character 'C' */
    final byte CHAR_G = 0x47; /* Character 'G' */

    final byte XON = 0x11;    /* Resume transmission */
    final byte XOFF = 0x13;    /* Pause transmission */
    final int MODE_GENERAL_UART = 0;

    int transferMode = MODE_GENERAL_UART;
    int tempTransferMode = MODE_GENERAL_UART;

    final int DATA_NONE = 0;
    final int DATA_ACK = 1;
    final int DATA_CHAR_C = 2;
    final int DATA_NAK = 3;

    // general data count
    int totalReceiveDataBytes = 0;
    int totalUpdateDataBytes = 0;

    long back_button_click_time;
    boolean bBackButtonClick = false;

    boolean bSendButtonClick = false;
    boolean bLogButtonClick = false;
    boolean bFormatHex = false;
    boolean bSendHexData = false;

    CharSequence contentCharSequence; // contain entire text content
    boolean bContentFormatHex = false;
    int contentFontSize = 12;
    boolean bWriteEcho = true;

    // show information message while send data by tapping "Write" button in hex content format
    int timesMessageHexFormatWriteData = 0;

    // note: when this values changed, need to check main.xml - android:id="@+id/ReadValues - android:maxLines="5000"
    final int TEXT_MAX_LINE = 1000;

    // variables
    final int UI_READ_BUFFER_SIZE = 10240; // Notes: 115K:1440B/100ms, 230k:2880B/100ms
    byte[] writeBuffer;
    byte[] readBuffer;
    char[] readBufferToChar;
    int actualNumBytes;

    int baudRate; /* baud rate */
    byte stopBit; /* 1:1stop bits, 2:2 stop bits */
    byte dataBit; /* 8:8bit, 7: 7bit */
    byte parity; /* 0: none, 1: odd, 2: even, 3: mark, 4: space */
    byte flowControl; /* 0:none, 1: CTS/RTS, 2:DTR/DSR, 3:XOFF/XON */
    boolean uart_configured = false;

    String uartSettings = "";

    //public static final int maxReadLength = 256;
    byte[] usbdata;
    char[] readDataToText;
    public int iavailable = 0;

    // file access//
    FileInputStream inputstream;
    FileOutputStream outputstream;

    FileWriter file_writer;
    FileReader file_reader;
    FileInputStream fis_open;
    FileOutputStream fos_save;
    BufferedOutputStream buf_save;
    boolean WriteFileThread_start = false;

    String fileNameInfo;
    String sFileName;
    int iFileSize = 0;
    int sendByteCount = 0;
    long start_time, end_time;
    long cal_time_1, cal_time_2;

    // data buffer
    byte[] writeDataBuffer;
    byte[] readDataBuffer; /* circular buffer */

    int iTotalBytes;
    int iReadIndex;

    final int MAX_NUM_BYTES = 65536;

    boolean bReadTheadEnable = false;


    /**
     * Called when the activity is first created.
     */
    public void onUartHelper(Context context) {
    }

    /**
     * TODO:偶尔识别不到串口设备，使用USB转TTL还好
     */
    public void createDeviceList() {
        int tempDevCount = ftD2xx.createDeviceInfoList(mContext);

        if (tempDevCount > 0) {
            if (DevCount != tempDevCount) {
                DevCount = tempDevCount;
            }
        } else {
            DevCount = -1;
            currentPortIndex = -1;
        }
    }

    public void disconnectFunction() {
        DevCount = -1;
        currentPortIndex = -1;
        bReadTheadEnable = false;
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (ftDev != null) {
            if (true == ftDev.isOpen()) {
                ftDev.close();
            }
        }
    }

    public boolean connectFunction() {
        boolean ret = false;
        if (portIndex + 1 > DevCount) {
            portIndex = 0;
        }

        if (currentPortIndex == portIndex
                && ftDev != null
                && true == ftDev.isOpen()) {
            //Toast.makeText(mContext,"Port("+portIndex+") is already opened.").show();
            return true;
        }

        if (true == bReadTheadEnable) {
            bReadTheadEnable = false;
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (null == ftDev) {
            ftDev = ftD2xx.openByIndex(mContext, portIndex);
        } else {
            ftDev = ftD2xx.openByIndex(mContext, portIndex);
        }
        uart_configured = false;

        if (ftDev == null) {
            Log.d(LOG_TAG,"Open port(" + portIndex + ") NG!");
            return false;
        }

        if (true == ftDev.isOpen()) {
            currentPortIndex = portIndex;
            //Toast.makeText(mContext, "open device port(" + portIndex + ") OK").show();
            ret = true;
            if (false == bReadTheadEnable) {
//                readThread = new ReadThread();
//                readThread.start();
            }
        } else {
            ret = false;
            Log.d(LOG_TAG,"Open port(" + portIndex + ") NG!");
        }
        return ret;
    }

    DeviceStatus checkDevice() {
        if (ftDev == null || false == ftDev.isOpen()) {
            Log.d(LOG_TAG, "Need to connect to cable.");
            return DeviceStatus.DEV_NOT_CONNECT;
        } else if (false == uart_configured) {
            //Log.d(LOG_TAG,"CHECK: uart_configured == false");
            Log.d(LOG_TAG,"Need to configure UART.");
            return DeviceStatus.DEV_NOT_CONFIG;
        }

        return DeviceStatus.DEV_CONFIG;

    }

    void sendData(int numBytes, byte[] buffer) {
        if (ftDev.isOpen() == false) {
            Log.e(LOG_TAG, "SendData: device not open");
            Log.w(LOG_TAG, "Device not open!");
            return;
        }

        if (numBytes > 0) {
            ftDev.write(buffer, numBytes);
        }
    }

    void sendData(byte buffer) {
        Log.e(LOG_TAG, "send buf:" + Integer.toHexString(buffer));
        byte tmpBuf[] = new byte[1];
        tmpBuf[0] = buffer;
        ftDev.write(tmpBuf, 1);
    }
    byte readData(int numBytes, byte[] buffer) {
        byte intstatus = 0x00; /* success by default */

		/* should be at least one byte to read */
        if ((numBytes < 1) || (0 == iTotalBytes)) {
            actualNumBytes = 0;
            intstatus = 0x01;
            return intstatus;
        }

        if (numBytes > iTotalBytes) {
            numBytes = iTotalBytes;
        }

		/* update the number of bytes available */
        iTotalBytes -= numBytes;
        actualNumBytes = numBytes;

		/* copy to the user buffer */
        for (int count = 0; count < numBytes; count++) {
            buffer[count] = readDataBuffer[iReadIndex];
            iReadIndex++;
            iReadIndex %= MAX_NUM_BYTES;
        }

        return intstatus;
    }

    int waitAck(int waitTime) {
        byte[] tmpdata = new byte[1];
        byte status;
        long time_1, time_2;
        time_1 = System.currentTimeMillis();
        Log.e(LOG_TAG, "waitAck...");

        do {
            if (false == bSendButtonClick)
                return DATA_NONE;

            if (iTotalBytes > 0) {
                status = readData(1, tmpdata);
                if (0x00 != status) {
                    Log.e(LOG_TAG, "waitAck - status error");
                }

                if (NAK == tmpdata[0]) {
                    Log.e(LOG_TAG, "get response - NAK");
                    return DATA_NAK;
                } else if (ACK == tmpdata[0]) {
                    Log.e(LOG_TAG, "get response - ACK");
                    return DATA_ACK;
                } else if (CHAR_C == tmpdata[0]) {
                    Log.e(LOG_TAG, "get response - CHAR_C");
                    return DATA_CHAR_C;
                } else {
                    Log.e(LOG_TAG, "get unexpected response :" + Integer.toHexString(tmpdata[0]));
                    time_2 = System.currentTimeMillis();
                }

            } else {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                time_2 = System.currentTimeMillis();
            }
        }
        while ((time_2 - time_1) < waitTime);

        Log.e(LOG_TAG, "DATA_NONE - timeout no response");
        return DATA_NONE;
    }

    // calculate CRC
    byte[] calCrc(byte[] buffer, int startPos, int count) {
        int crc = 0, i;
        byte[] crcHL = new byte[2];

        while (--count >= 0) {
            crc = crc ^ buffer[startPos++] << 8;
            for (i = 0; i < 8; ++i) {
                if ((crc & 0x8000) != 0) crc = crc << 1 ^ 0x1021;
                else crc = crc << 1;
            }
        }
        crc &= 0xFFFF;

        crcHL[0] = (byte) ((crc >> 8) & 0xFF);
        crcHL[1] = (byte) (crc & 0xFF);

        return crcHL;
    }

    void accumulateCrc(byte[] crcHL, byte[] buffer, int startPos, int count) {
        int crc = 0, i;
        short val = (short) (((crcHL[0] & 0xFF) << 8) | (crcHL[1] & 0xFF));
        crc = val;

        while (--count >= 0) {
            crc = crc ^ buffer[startPos++] << 8;
            for (i = 0; i < 8; ++i) {
                if ((crc & 0x8000) != 0) crc = crc << 1 ^ 0x1021;
                else crc = crc << 1;
            }
        }
        crc &= 0xFFFF;

        crcHL[0] = (byte) ((crc >> 8) & 0xFF);
        crcHL[1] = (byte) (crc & 0xFF);
    }

    void accumulateCrc(byte[] crcHL, byte buffer) {
        int crc = 0, i;

        short val = (short) (((crcHL[0] & 0xFF) << 8) | (crcHL[1] & 0xFF));

        crc = val;

        crc = crc ^ buffer << 8;
        for (i = 0; i < 8; ++i) {
            if ((crc & 0x8000) != 0) crc = crc << 1 ^ 0x1021;
            else crc = crc << 1;
        }

        crc &= 0xFFFF;

        crcHL[0] = (byte) ((crc >> 8) & 0xFF);
        crcHL[1] = (byte) (crc & 0xFF);
    }

    String hexToAscii(String s) throws IllegalArgumentException {
        int n = s.length();
        StringBuilder sb = new StringBuilder(n / 2);
        for (int i = 0; i < n; i += 2) {
            char a = s.charAt(i);
            char b = s.charAt(i + 1);
            sb.append((char) ((hexToInt(a) << 4) | hexToInt(b)));
        }
        return sb.toString();
    }

    static int hexToInt(char ch) {
        if ('a' <= ch && ch <= 'f') {
            return ch - 'a' + 10;
        }
        if ('A' <= ch && ch <= 'F') {
            return ch - 'A' + 10;
        }
        if ('0' <= ch && ch <= '9') {
            return ch - '0';
        }
        throw new IllegalArgumentException(String.valueOf(ch));
    }

    public static String bytesToHex(byte[] in) {
        final StringBuilder builder = new StringBuilder();
        for(byte b : in) {
            builder.append(String.format("%02x ", b));
        }
        return builder.toString();
    }
}
