package com.example.tony.bodycompositionanalyzer;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.hardware.usb.UsbRequest;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import cn.trinea.android.common.util.FileUtils;
import cn.trinea.android.common.util.ShellUtils;

/**
 * Created by tony on 16-7-3.
 */
public class Printer {
    private static final String LOG_TAG = "Printer";
    private static final boolean USB_EVENT_DEBUG = false;
    private static PrinterModel mPrinterModel;
    private static Context mContext;
    private UsbManager mManager;
    private UsbDevice mDevice;
    private UsbDeviceConnection mDeviceConnection;
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";

    private UsbEndpoint mEndpointOut;
    private UsbEndpoint mEndpointIn;
    private String mSerial;
    private UsbInterface mUsbInterface = null;
    /* 相互传递的一个临时变量 */
    private UsbInterface mUsbInterfaceTmp = null;

    // pool of requests for the OUT endpoint
    private final LinkedList<UsbRequest> mOutRequestPool = new LinkedList<UsbRequest>();
    // pool of requests for the IN endpoint
    private final LinkedList<UsbRequest> mInRequestPool = new LinkedList<UsbRequest>();

    private final static String DEVICE_ID_CANON_IP2700    = "MFG:Canon;CMD:BJL,BJRaster3,BSCCe,IVEC,IVECPLI;SOJ:TXT01;MDL:iP2700 series;CLS:PRINTER;DES:Canon iP2700 series;";
    private final static String DEVICE_ID_HP_DESKJET_1112 = "MFG:HP;MDL:DeskJet 1110 series;CMD:PCL3GUI,PJL,Automatic,DW-PCL,DESKJET,DYN;CLS:PRINTER;DES:K7B87D;";
    private final static String DEVICE_ID_EPSON_R330      = "MFG:EPSON;CMD:ESCPL2,BDC,D4,D4PX;MDL:Epson Stylus Photo R330;CLS:PRINTER;DES:EPSON Epson Stylus Photo R330;CID:EpsonStd2;";

    public static final int PRINTER_STATE_GPIO = GPIO.GPIOB30;
    public static final int PRINTER_CONNECTED = GPIO.LOW;
    public static final int PRINTER_DISCONNECTED = GPIO.HIGH;

    /**
     * 单例模式: http://coolshell.cn/articles/265.html
     */
    private volatile static Printer singleton = null;
    public static Printer getInstance(Context context)   {
        if (singleton == null)  {
            synchronized (Printer.class) {
                if (singleton== null)  {
                    singleton= new Printer(context);
                }
            }
        }
        return singleton;
    }
    /**
     * 打印机型号类
     */
    public enum PrinterModel {
        /** Unkown model */
        UNKOWN_MODEL("unkown", "Unkonw model Printer", "NULL", "NULL"),
        /** Canon iP2780 */
        CANON_IP2780("bjc-MULTIPASS-MX420", "Canon iP2780 Printer", DEVICE_ID_CANON_IP2700, "HelloWorld.canonip2780"),
        /** HP Deskjet 1112 */
        HP_DESKJET_1112("hp-dj_5550", "HP Deskjet 1112 Printer", DEVICE_ID_HP_DESKJET_1112, "HelloWorld.hpdeskjet1112"),
        /** EPSON R330 */
        EPSON_330("", "Epson R330 Printer", DEVICE_ID_EPSON_R330, "HelloWorld.epsonr330");
        private final String des;
        private final String deviceId;
        private final String testFileName;
        private final String DeviceModel;

        PrinterModel(String gutenprintModel, String des, String deviceId, String name) {
            this.des = des;
            this.deviceId = deviceId;
            this.testFileName = name;
            this.DeviceModel = gutenprintModel;
        }
        public String getDes() {
            return des;
        }
        public String getDeviceId() {
            return deviceId;
        }

        public String getTestFileName() {
            return testFileName;
        }
    }

    /**
     * 根据DeviceId匹配打印机型号
     * @param deviceId
     * @return
     */
    public static PrinterModel matchPrinterModel(String deviceId) {
        if(deviceId != null) {
            for (PrinterModel pm : PrinterModel.values()) {
                if(deviceId.startsWith(pm.deviceId))
                    return pm;
            }
        }
        return PrinterModel.UNKOWN_MODEL;
    }

    private Printer(Context context) {
        Log.i(LOG_TAG, "Printer#Printer");
        mContext = context;

        // listen for new devices
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        context.registerReceiver(mUsbReceiver, filter);
    }

    public void initGpio() {
        /** 导出、输出、高低电平 */
        if(GPIO.getInstance(Printer.PRINTER_STATE_GPIO).activationPin()) {
            int ret = GPIO.getInstance(Printer.PRINTER_STATE_GPIO).initPin(GPIO.DIRECTION_OUT);
            if (ret < 0) {
                Log.e(LOG_TAG, "initPin " + Printer.PRINTER_STATE_GPIO + " fail code:" + ret);
            }
        } else {
            Log.e(LOG_TAG, "activationPin Gpio fail");
        }
    }

    public void uninitGpio() {
        /** 导出、输出、高低电平 */
        if(!GPIO.getInstance(Printer.PRINTER_STATE_GPIO).desactivationPin()) {
            Log.e(LOG_TAG, "uninit Gpio fail");
        }
        if (mContext != null) {
            mContext.unregisterReceiver(mUsbReceiver);
        }

    }

    public void init() {
        Log.i(LOG_TAG, "Printer#init");
        initGpio();

        /* 只处理检测到的第一个打印机，其它不进行处理 */
        boolean hasPrinter = false;
        mManager = (UsbManager) mContext.getSystemService(Context.USB_SERVICE);
        for(UsbDevice device : mManager.getDeviceList().values()) {
            mUsbInterfaceTmp = findPrinterInterface(device);
            if (mUsbInterfaceTmp != null) {
                Log.i(LOG_TAG, "Found Printer interface " + mUsbInterfaceTmp);
                if (setPrinterInterface(device, mUsbInterfaceTmp)) {
                    hasPrinter = true;
                    break;
                }
            }
        }

        if(hasPrinter == false) {
            // 没有连接打印机
            setPrinterInterface(null, null);
        }

        /** 将打印机状态告知服务 */
        Log.e(LOG_TAG, "Has Printer: " + hasPrinter);
    }

    public Context getContext() {
        return mContext;
    }

    /**
     * 反初始化
     */
    public void uninit() {
        Log.i(LOG_TAG, "Printer#uninit");
        if(getContext() != null) {
            setPrinterInterface(null, null);
            getContext().unregisterReceiver(mUsbReceiver);
        }

		/* gpio */
        uninitGpio();
    }

    /**
     * 判断是否连接上打印机
     * @return
     */
    public boolean isConnected() {
        return mPrinterModel == null ? false : true;
    }

    /**
     * USB Printer Requests
     */
    public static final int USBLP_REQ_GET_ID = 0x00;
    public static final int USBLP_REQ_GET_STATUS = 0x01;
    public static final int USBLP_REQ_RESET = 0x02;
    public static final int USBLP_REQ_HP_CHANNEL_CHANGE_REQUEST = 0x00;    /* HP Vendor-specific */

    public static final int USBLP_MINORS = 16;
    public static final int USBLP_MINOR_BASE = 0;

    public static final int USBLP_CTL_TIMEOUT = 5000;                    /* 5 seconds */

    public static final int USBLP_FIRST_PROTOCOL = 1;
    public static final int USBLP_LAST_PROTOCOL = 3;
    public static final int USBLP_MAX_PROTOCOLS = (USBLP_LAST_PROTOCOL + 1);

    /**
     * libusb
     */
    public static final int LIBUSB_RECIPIENT_INTERFACE = 0x01;
    public static final int LIBUSB_REQUEST_TYPE_CLASS = (0x01 << 5);

    /**
     * 获取打印机型号
     */
    public static PrinterModel getModel() {
        return mPrinterModel;
    }

    /**
     * 解析设备ID
     * @param deviceIdArray
     * @return
     */
    private String parseDeviceid(byte[] deviceIdArray) {
        Log.i(LOG_TAG, "Printer#parseDeviceid");
        String ret = "未连接打印机";
        String deviceIdStr = new String(deviceIdArray);
        Log.i(LOG_TAG, "DEVICE ID STR:" + deviceIdStr);
        if (deviceIdArray != null) {
            PrinterModel pm = matchPrinterModel(deviceIdStr);
            if (pm.equals(PrinterModel.UNKOWN_MODEL)) {
                Log.e(LOG_TAG, "未知型号打印机");
            }
            ret = pm.getDes();
            Log.e(LOG_TAG, "打印机型号: " + pm.getDes());
        }
        return ret;
    }

    BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
                UsbDevice device = (UsbDevice) intent
                        .getParcelableExtra(UsbManager.EXTRA_DEVICE);
                UsbInterface intf = findPrinterInterface(device);
                if (intf != null) {
                    Log.i(LOG_TAG, "Found Printer interface " + intf);
                    setPrinterInterface(device, intf);
                }
            } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                UsbDevice device = intent
                        .getParcelableExtra(UsbManager.EXTRA_DEVICE);
                String deviceName = device.getDeviceName();
                if (mDevice != null && mDevice.equals(deviceName)) {
                    Log.i(LOG_TAG, "printer interface removed");
                    setPrinterInterface(null, null);
                }
            }
        }
    };

    // searches for an printer interface on the given USB device
    static private UsbInterface findPrinterInterface(UsbDevice device) {
        Log.d(LOG_TAG, "findPrinterInterface " + device);
        int count = device.getInterfaceCount();
        for (int i = 0; i < count; i++) {
            UsbInterface intf = device.getInterface(i);
            if (intf.getInterfaceClass() == UsbConstants.USB_CLASS_PRINTER) {
                Log.i(LOG_TAG, "findPrinterInterface true!");
                return intf;
            }
        }
        return null;
    }

    /**
     * 获取Usb权限
     * @param context
     * @param usbManager
     */
    private void tryGetUsbPermission(Context context, UsbManager usbManager, UsbDevice device, UsbInterface intf){
        Log.i(LOG_TAG, "Printer#tryGetUsbPermission");
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
//        context.registerReceiver(mUsbPermissionActionReceiver, filter);

        PendingIntent mPermissionIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), 0);

        if(usbManager.hasPermission(device)){
            //if has already got permission, just goto connect it
            //that means: user has choose yes for your previously popup window asking for grant perssion for this usb device
            //and also choose option: not ask again
            afterGetUsbPermission(context, usbManager, device, intf);
        }else{
            //this line will let android popup window, ask user whether to allow this app to have permission to operate this usb device
            usbManager.requestPermission(device, mPermissionIntent);
        }
    }

    private final BroadcastReceiver mUsbPermissionActionReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice usbDevice = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        //user choose YES for your previously popup window asking for grant perssion for this usb device
                        if(null != usbDevice){
                            afterGetUsbPermission(context, mManager, usbDevice, mUsbInterfaceTmp);
                        }
                    }
                    else {
                        //user choose NO for your previously popup window asking for grant perssion for this usb device
                        Toast.makeText(context, String.valueOf("Permission denied for device" + usbDevice), Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    };

    private void afterGetUsbPermission(Context context, UsbManager usbManager, UsbDevice usbDevice,  UsbInterface intf){
        Log.i(LOG_TAG, "Printer#afterGetUsbPermission");
        //call method to set up device communication
//        context.unregisterReceiver(mUsbPermissionActionReceiver);
        if(USB_EVENT_DEBUG) Toast.makeText(context, String.valueOf("Got permission for usb device: " + usbDevice), Toast.LENGTH_LONG).show();
        if(USB_EVENT_DEBUG) Toast.makeText(context, String.valueOf("Found USB device: VID=" + usbDevice.getVendorId() + " PID=" + usbDevice.getProductId()), Toast.LENGTH_LONG).show();
        if(USB_EVENT_DEBUG) Toast.makeText(context, String.valueOf("Found USB device: VID=" + usbDevice.getVendorId() + " PID=" + usbDevice.getProductId()), Toast.LENGTH_LONG).show();
        doYourOpenUsbDevice(usbManager, usbDevice, intf);
    }

    /**
     * 成功打开usb端口，并连接上
     * @param usbManager
     * @param device
     * @param intf
     * 1. 打开指定USB端口
     * 2. 初始化打印机，获取型号等一般信息
     */
    private void doYourOpenUsbDevice(UsbManager usbManager, UsbDevice device, UsbInterface intf){
        Log.i(LOG_TAG, "Printer#doYourOpenUsbDevice");
        // 1. 打开指定USB端口
        intPrinterConnect(usbManager, device, intf);

        // 2. 初始化打印机端点等信息
        initPrinterDeviceEndpoint(mDeviceConnection, mUsbInterface);

        // 3. 更新打印机型号等信息
        updatePrinterModel(mDeviceConnection);
    }

    /**
     * 1. 初始化打印机连接信息
     * @param usbManager
     * @param device
     * @param intf
     */
    private void intPrinterConnect(UsbManager usbManager, UsbDevice device, UsbInterface intf) {
        Log.i(LOG_TAG, "Printer#intPrinterConnect");
        //now follow line will NOT show: User has not given permission to device UsbDevice
        //add your operation code here
        if (device != null && intf != null) {
            UsbDeviceConnection connection = usbManager.openDevice(device);
            if (connection != null) {
                Log.i(LOG_TAG, "open succeeded");
                if (connection.claimInterface(intf, false)) {
                    Log.i(LOG_TAG, "claim interface succeeded");
                    mDevice = device;
                    mDeviceConnection = connection;
                    mUsbInterface = intf;
                } else {
                    Log.i(LOG_TAG, "claim interface failed");
                    connection.close();
                }
            } else {
                Toast.makeText(mContext, "open failed", Toast.LENGTH_LONG).show();
                Log.i(LOG_TAG, "open failed");
            }
        }
    }

    // Sets the current USB device and interface

    /**
     * 该方法是处理热插拔事件的
     * @param device
     * @param intf
     * @return
     */
    private boolean setPrinterInterface(UsbDevice device, UsbInterface intf) {
        Log.i(LOG_TAG, "Printer#setPrinterInterface");
        boolean ret = false;
        if (mDeviceConnection != null) {
            if (mUsbInterface != null) {
                mDeviceConnection.releaseInterface(mUsbInterface);
                mUsbInterface = null;
            }
            mDeviceConnection.close();
            mDevice = null;
            mDeviceConnection = null;
            mPrinterModel = null;
        }

        if (device != null && intf != null) {
            tryGetUsbPermission(mContext, mManager, device, intf);
            ret = true;
        } else {
            // 处理未连接打印机情况
            handlePrinterRemove();
            ret = false;
        }
        return ret;
    }


    public static void handlePrinterAdd() {
        Log.i(LOG_TAG, "handlePrinterAdd");
        if (!GPIO.getInstance(Printer.PRINTER_STATE_GPIO).setState(Printer.PRINTER_CONNECTED)) {
            Log.e(LOG_TAG, "set gpio fail");
        }
    }

    public static void handlePrinterRemove() {
        Log.i(LOG_TAG, "handlePrinterRemove");
        if (!GPIO.getInstance(Printer.PRINTER_STATE_GPIO).setState(Printer.PRINTER_DISCONNECTED)) {
            Log.e(LOG_TAG, "set gpio fail");
        }
    }

    /**
     * 2. 初始化端点等信息
     * @param connection UsbDeviceConnection
     * @param intf UsbInterface
     */
    private void initPrinterDeviceEndpoint(UsbDeviceConnection connection,
                                   UsbInterface intf) {
        Log.i(LOG_TAG, "Printer#initPrinterDeviceEndpoint");
        mDeviceConnection = connection;
        mSerial = connection.getSerial();
        if (mDeviceConnection == null || mSerial == null) {
            return;
        }

        UsbEndpoint epOut = null;
        UsbEndpoint epIn = null;
        // look for our bulk endpoints
        for (int i = 0; i < intf.getEndpointCount(); i++) {
            UsbEndpoint ep = intf.getEndpoint(i);
            if (ep.getType() == UsbConstants.USB_ENDPOINT_XFER_BULK) {
                if (ep.getDirection() == UsbConstants.USB_DIR_OUT) {
                    epOut = ep;
                } else {
                    epIn = ep;
                }
            }
        }
        if (epOut == null || epIn == null) {
            throw new IllegalArgumentException("not all endpoints found");
        }
        mEndpointOut = epOut;
        mEndpointIn = epIn;
    }

    /**
     * 更新打印机型号等信息
     * @param connection
     */
    private static void updatePrinterModel(UsbDeviceConnection connection) {
        Log.i(LOG_TAG, "Printer#updatePrinterModel");
        PrinterModel ret = null;
        byte[] b = new byte[1024];
        int length = connection.controlTransfer(
                UsbConstants.USB_DIR_IN | LIBUSB_RECIPIENT_INTERFACE | LIBUSB_REQUEST_TYPE_CLASS,
                USBLP_REQ_GET_ID,
                0,
                0,
                b,
                1024,
                5000);

        /** 挑出有用信息 注：第一个字节为0，第二个字节为 { */
        byte[] deviceIdArray = Arrays.copyOfRange(b, 2, length);
        /** 解析设备ID */
        String deviceIdStr = new String(deviceIdArray);
        Log.i(LOG_TAG, "DEVICE ID STR:" + deviceIdStr);
        if (deviceIdArray != null) {
            PrinterModel pm = matchPrinterModel(deviceIdStr);
            ret = pm;
            Log.e(LOG_TAG, "打印机型号: " + pm.getDes());

            handlePrinterAdd();
        } else {
            handlePrinterRemove();
        }

        mPrinterModel = ret;
    }


    /**
     * 写数据
     * @param in
     */
    private void write(InputStream in) {
        Log.i(LOG_TAG, "Printer#write");
        Runnable r = new WaiterThread(in);
        new Thread(r).start();
    }

    /**
     * 写线程
     */
    private class WaiterThread implements Runnable {
        final InputStream inputStream;
        public WaiterThread(InputStream in) {
            inputStream = in;
        }
        @Override
        public void run() {
            BufferedInputStream bufferInputStream = null;

            try {
                final int MAX_USBFS_BUFFER_SIZE = 16384; //16KiB.
                //File pcl3gui = new File("/sdcard/HelloWorld.PCL3GUI");
                byte[] bytes = new byte[MAX_USBFS_BUFFER_SIZE];
                bufferInputStream = new BufferedInputStream(inputStream);
                int bytesRead = 0;
                int bytesWrite = 0;
                //从文件中按字节读取内容，到文件尾部时read方法将返回-1
                while ((bytesRead = bufferInputStream.read(bytes)) != -1) {

                    //将读取的字节转为字符串对象
                    //String chunk = new String(bytes, 0, bytesRead);
                    //Log.i("LOG_TAG", chunk);

                    bytesWrite = mDeviceConnection.bulkTransfer(mEndpointOut, bytes,
                            bytesRead, 30*000);

                    Log.i(LOG_TAG, "bytesRead:" + bytesRead + " bytesWrite:" + bytesWrite);

                    if(bytesWrite < 0) {
                        // 暂停此线程 排除故障 暂时做到这种程度，因为对Java多线程编程不是太熟悉。
                        // 打印被暂停时会进入到这个里面，可能引起的问题有：缺纸 卡纸等等。这个时候要有另外一个线程去获取Printer状态。
                        Log.e(LOG_TAG, "暂停此线程 排除故障!");
                        synchronized (this) {
                            wait();
                            notifyAll();
                        }
                    }

                    if(bytesWrite != bytesRead) {
                        Log.e(LOG_TAG, "bultTransfer error!");
                        break;
                    }

                    Thread.sleep(10);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if(bufferInputStream != null)
                        bufferInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 将PDF转换成打印机语言
     * @param rasterPath 目标文件路径
     * @param pdf 路径
     * @return 如果转换成功，返回true否则false
     */
    private static boolean covertPdfToEpson330(String rasterPath, String pdf) {
        Log.i(LOG_TAG, "covertPdfToEpson330 dsfdasdads");
        boolean ret = false;
        ShellUtils.CommandResult cr;
        if (ShellUtils.checkRootPermission()) {
            String cmd = "gs " +
                    "-sDEVICE=ijs " +
                    "-sIjsServer=ijsgutenprint " +
                    "-dIjsUseOutputFD " +
                    "-sDeviceManufacturer=Epson " +
                    "-sDeviceModel=escp2-r360 " +
                    "-sPAPERSIZE=a4 " +
                    "-dNOPAUSE " +
                    "-dSAFER " +
                    "-sOutputFile=" +
                    "\"" + rasterPath + "\" "  +
                    pdf + " " +
                    "-c quit";
            Log.e(LOG_TAG, "covertPdfToEpson330: str:" + cmd);
            cr = ShellUtils.execCommand(cmd, true);
            if(cr.result == 0) {
                cr = ShellUtils.execCommand("chmod 777 " + rasterPath, true);
                if(cr.result == 0)
                    ret = true;
            }
        } else {
            Log.e(LOG_TAG, "无ROOT权限，将打印测试数据");
            if(mContext != null) {
                InputStream in = null;
                try {
                    in = mContext.getResources().getAssets().open("HelloWorld.epsonr330");
                    if (in != null) {
                        ret = FileUtils.writeFile(rasterPath, in);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return ret;
    }

    /**
     * 将PDF转换成打印机语言
     * @param rasterPath 目标文件路径
     * @param pdf 路径
     * @return 如果转换成功，返回true否则false
     */
    private static boolean covertPdfToHp1112(String rasterPath, String pdf) {
        Log.i(LOG_TAG, "Printer # covertPdfToHp1112");
        boolean ret = false;
        ShellUtils.CommandResult cr;
        /* 有root权限才做如下事件 */
        if (ShellUtils.checkRootPermission()) {
            String cmd = "gs " +
                    "-dIjsUseOutputFD " +
                    "-sDEVICE=pcl3  " +
                    "-sSubdevice=unspec " +
                    "-sPJLLanguage=PCL3GUI " +
                    "-dOnlyCRD " +
                    "-r600 " +
                    "-sColourModel=CMYK " +
                    "-sPrintQuality=draft " +
                    "-sMedium=plain " +
                    "-sPAPERSIZE=a4 " +
                    "-dNOPAUSE " +
                    "-dSAFER " +
                    "-sOutputFile=" +
                    rasterPath +
                    " " +
                    pdf +
                    " -c quit";
            Log.i(LOG_TAG, "cmd: " + cmd);
            cr = ShellUtils.execCommand(cmd, true);
            if(cr.result == 0) {
                cr = ShellUtils.execCommand("chmod 777 " + rasterPath, true);
                if(cr.result == 0)
                    ret = true;
            }
        } else {
            Log.e(LOG_TAG, "无ROOT权限，将打印测试数据");
            if(mContext != null) {
                InputStream in = null;
                try {
                    in = mContext.getResources().getAssets().open("HelloWorld.PCL3GUI");
                    if (in != null) {
                        ret = FileUtils.writeFile(rasterPath, in);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return ret;
    }

    /**
     * 将PDF转换成打印机语言
     * @param rasterPath 目标文件路径
     * @param pdf 路径
     * @return 如果转换成功，返回true否则false
     */
    private static boolean covertPdfToPCL(String rasterPath, String pdf) {
        Log.i(LOG_TAG, "Printer # covertPdfToPCL");
        boolean ret = false;
        PrinterModel pm = getModel();
        if(pm.equals(PrinterModel.HP_DESKJET_1112)) {
            ret = covertPdfToHp1112(rasterPath, pdf);
        } else if (pm.equals(PrinterModel.EPSON_330)) {
        }
        return ret;
    }

    /**
     * print pdf file
     */
    public void printPdf(String rasterPath, String pdf) throws Exception {
        Log.i(LOG_TAG, "Printer # printPdf");
        boolean ret = false;
        if(isConnected()) {
//            Toast.makeText(mContext, getModel().getDes(), Toast.LENGTH_LONG).show();
        } else {
//            Toast.makeText(mContext, "打印机未连接", Toast.LENGTH_LONG).show();
            throw new Exception("打印机未连接");
        }

        // 2. cover pdf to PDL(Printer Des Language)
        ret = covertPdfToPCL(rasterPath, pdf);
        if(!ret) throw new Exception("转换PDF到打印机数据失败");

        // 3. write hp 1112 data to printer
        write(new FileInputStream(rasterPath));
    }
}

