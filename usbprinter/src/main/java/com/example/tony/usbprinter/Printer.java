package com.example.tony.usbprinter;

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
import android.os.Build;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

/**
 * Created by tony on 16-7-3.
 */
public class Printer {
    private final Context mContext;
    private UsbManager mManager;
    private UsbDevice mDevice;
    private UsbDeviceConnection mDeviceConnection;
    private UsbInterface mInterface;
    private PrinterDevice mPrinterDevice;
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";

    private static final String LOG_TAG = "PrinterDevice";

    private UsbEndpoint mEndpointOut;
    private UsbEndpoint mEndpointIn;
    private String mSerial;
    private UsbInterface mUsbInterface = null;

    // pool of requests for the OUT endpoint
    private final LinkedList<UsbRequest> mOutRequestPool = new LinkedList<UsbRequest>();
    // pool of requests for the IN endpoint
    private final LinkedList<UsbRequest> mInRequestPool = new LinkedList<UsbRequest>();

    AssetManager mAssetManager = null;
    InputStream mInputStream = null;
    public Printer(Context context) {
        Log.i(LOG_TAG, "Printer");
        mContext = context;

        /* 只处理检测到的第一个打印机，其它不进行处理 */
        mManager = (UsbManager) mContext.getSystemService(Context.USB_SERVICE);
        for(UsbDevice device : mManager.getDeviceList().values()) {
            UsbInterface intf = findPrinterInterface(device);
            if (setPrinterInterface(device, intf)) {
                initPrinterDevice(mDeviceConnection, intf);
                mUsbInterface = intf;
                break;
            }
        }

        // listen for new devices
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        filter.addAction(ACTION_USB_PERMISSION);
        mContext.registerReceiver(mUsbReceiver, filter);

    }

    /**
     * 判断是否连接上打印机
     * @return
     */
    public boolean isConnected() {
        return mUsbInterface == null ? false : true;
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
    public String getModel() {
        String ret = "HP Deskjet 1112";
        byte[] b = new byte[1024];
        int length = mDeviceConnection.controlTransfer(
                UsbConstants.USB_DIR_IN | LIBUSB_RECIPIENT_INTERFACE | LIBUSB_REQUEST_TYPE_CLASS,
                USBLP_REQ_GET_ID,
                0,
                0,
                b,
                1024,
                5000);

        if(b != null) {
            ret = new String(b);
        }
        Log.i(LOG_TAG, " sf length: " + length + " " + b[0] + " " + b[1] + " " + b[2]);
        Log.i(LOG_TAG, "ret: " + new String(b));
        return ret;
    }

    private String parseDeviceid(byte[] in)  {
        String ret = "";

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
            } else if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbAccessory accessory = (UsbAccessory) intent.getParcelableExtra(UsbManager.EXTRA_ACCESSORY);

                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if(accessory != null){
                            //call method to set up accessory communication
                        }
                    }
                    else {
                        Log.d(LOG_TAG, "permission denied for accessory " + accessory);
                    }
                }
            }
        }
    };

    // searches for an adb interface on the given USB device
    static private UsbInterface findPrinterInterface(UsbDevice device) {
        Log.d(LOG_TAG, "findPrinterInterface " + device);
        int count = device.getInterfaceCount();
        for (int i = 0; i < count; i++) {
            UsbInterface intf = device.getInterface(i);
            if (intf.getInterfaceClass() == UsbConstants.USB_CLASS_PRINTER) {
                Log.i(LOG_TAG, "Printer");
                return intf;
            }
        }
        return null;
    }


    // Sets the current USB device and interface
    private boolean setPrinterInterface(UsbDevice device, UsbInterface intf) {
        if (mDeviceConnection != null) {
            if (mInterface != null) {
                mDeviceConnection.releaseInterface(mInterface);
                mInterface = null;
            }
            mDeviceConnection.close();
            mDevice = null;
            mDeviceConnection = null;
        }

        if (device != null && intf != null) {
            UsbDeviceConnection connection = mManager.openDevice(device);
            if (connection != null) {
                Log.i(LOG_TAG, "open succeeded");
                if (connection.claimInterface(intf, false)) {
                    Log.i(LOG_TAG, "claim interface succeeded");
                    mDevice = device;
                    mDeviceConnection = connection;
                    mInterface = intf;
//                    mPrinterDevice = new PrinterDevice(mContext, mDeviceConnection, intf);
//                    mPrinterDevice.write(mInputStream);
//                    Log.i(LOG_TAG, "call start");
//                    mPrinterDevice.start();
                    return true;
                } else {
                    Log.i(LOG_TAG, "claim interface failed");
                    connection.close();
                }
            } else {
                Log.i(LOG_TAG, "open failed");
            }
        }

        if (mDeviceConnection == null && mPrinterDevice != null) {
            //mPrinterDevice.stop();
            mPrinterDevice = null;
        }
        return false;
    }

    /**
     * 获取打印机型号等信息
     * @param connection UsbDeviceConnection
     * @param intf UsbInterface
     */
    private void initPrinterDevice(UsbDeviceConnection connection,
                         UsbInterface intf) {
        mDeviceConnection = connection;
        mSerial = connection.getSerial();

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
     * 写数据
     * @param in
     */
    public void write(InputStream in) {
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
                            bytesRead, 40);

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

                    Thread.sleep(1000);
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
}
