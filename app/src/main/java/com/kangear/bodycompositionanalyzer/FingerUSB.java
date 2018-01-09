package com.kangear.bodycompositionanalyzer;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.kangear.common.utils.ByteArrayUtils;

import java.io.IOException;

/**
 * Created by tony on 16-7-3.
 */
public class FingerUSB {
    private static final boolean DEBUG = true;
    private static final String TAG = "FingerUSB";
    private static final boolean USB_EVENT_DEBUG = false;

    private UsbDeviceConnection mDeviceConnection;
    private UsbEndpoint mEndpointOut;
    private UsbEndpoint mEndpointIn;
    private boolean standby = false;
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";

    private boolean init(UsbManager usbManager, UsbDevice device, UsbInterface intf) {
        //now follow line will NOT show: User has not given permission to device UsbDevice
        //add your operation code here
        if (device == null || intf == null) {
            return false;
        }

        mDeviceConnection = usbManager.openDevice(device);
        if (mDeviceConnection == null) {
            Log.i(TAG, "open failed");
            return false;
        }
        //　Log.i(TAG, "open succeeded");
        if (mDeviceConnection.claimInterface(intf, true)) {
            // Log.i(TAG, "claim interface succeeded");
        } else {
            Log.i(TAG, "claim interface failed");
            mDeviceConnection.close();
            return false;
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
            Log.e(TAG, "not all endpoints found");
            return false;
        }
        mEndpointOut = epOut;
        mEndpointIn = epIn;

        standby = true;
        return true;
    }

    /**
     * 写数据
     * @param arr
     * @return
     */
    public boolean send(Context context, byte[] arr) {
        if (!standby) {
            if (!open(context))
                return false;
        }

        if (mDeviceConnection == null || arr == null) {
            return false;
        }

        int length = mDeviceConnection.bulkTransfer(mEndpointOut, arr, arr.length, 5000);
        //Log.e(TAG, "send length: " + length);
        return length == arr.length;
    }

    /**
     * 读数据
     * @return
     */
    public boolean read(Context context, byte[] arr) {
        if (!standby) {
            if (!open(context)) {
                return false;
            }
        }

        if (mDeviceConnection == null || arr == null) {
            return false;
        }
        int length = mDeviceConnection.bulkTransfer(mEndpointIn, arr, arr.length, 5000);
        // Log.e(TAG, "read: " + length);
        return length > 0;
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
                            open(context.getApplicationContext());
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


    /**
     * 获取Usb权限
     * @param context
     * @param usbManager
     */
    private void tryGetUsbPermission(Context context, UsbManager usbManager, UsbDevice device, UsbInterface intf){
        // Log.i(TAG, "Printer#tryGetUsbPermission");
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

    private void afterGetUsbPermission(Context context, UsbManager usbManager, UsbDevice usbDevice,  UsbInterface intf){
        // Log.i(TAG, "Printer#afterGetUsbPermission");
        //call method to set up device communication
//        context.unregisterReceiver(mUsbPermissionActionReceiver);
        if(USB_EVENT_DEBUG) Log.d(TAG, String.valueOf("Got permission for usb device: " + usbDevice));
        if(USB_EVENT_DEBUG) Log.d(TAG, String.valueOf("Found USB device: VID=" + usbDevice.getVendorId() + " PID=" + usbDevice.getProductId()));
        if(USB_EVENT_DEBUG) Log.d(TAG, String.valueOf("Found USB device: VID=" + usbDevice.getVendorId() + " PID=" + usbDevice.getProductId()));

        boolean ret = init(usbManager, usbDevice, intf);
        if (ret) {
            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(WelcomeActivity.CONST_ACTION_TOUCHID_OK));
        }
    }

    /**
     * 该方法是处理热插拔事件的
     * @return
     */
    public boolean open(Context context) {
        /* 只处理检测到的第一个打印机，其它不进行处理 */
        UsbDevice device = null;
        UsbInterface intf = null;
        boolean hasPrinter = false;
        UsbManager mManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        if (mManager == null) {
            return false;
        }

        for(UsbDevice d : mManager.getDeviceList().values()) {
            //Log.d(TAG, "VID: " + String.format("%04X", d.getVendorId()) + " PID: " + String.format("%04X", d.getProductId()));

            if (d.getVendorId() != 0x2109 || d.getProductId() != 0x7638) {
                continue;
            }
            intf = findFingerInterface(d);
            if (intf != null) {
                //Log.i(TAG, "Found Finger interface " + intf);
                device = d;
                hasPrinter = true;
                break;
            }
        }

        //Log.e(TAG, "Has Finger: " + hasPrinter);

        if(!hasPrinter) {
            return false;
        }

        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        context.registerReceiver(mUsbPermissionActionReceiver, filter);
        tryGetUsbPermission(context, mManager, device, intf);
        return true;
    }

    // searches for an printer interface on the given USB device
    private static UsbInterface findFingerInterface(UsbDevice device) {
        //Log.d(TAG, "findFingerInterface " + device);
        int count = device.getInterfaceCount();
        for (int i = 0; i < count; i++) {
            UsbInterface intf = device.getInterface(i);
            if (intf.getInterfaceClass() == UsbConstants.USB_CLASS_MASS_STORAGE) {
                //Log.i(TAG, "findFingerInterface true!");
                return intf;
            }
        }
        return null;
    }

    public void getMaxLnu(Context context) {
        if (!standby) {
            if (!open(context)) {
                return;
            }
        }

        String str = "";

        synchronized (this) {
            if (mDeviceConnection != null) {
                // 接收的数据只有1个字节
                byte[] message = new byte[1];
                // 获取最大LUN命令的设置由USB Mass Storage的定义文档给出
                int result = mDeviceConnection.controlTransfer(0xA1, 0xFE, 0x00, 0x00, message, 1, 1000);
                if(result < 0) {
                    //Log.d(TAG,  "Get max lnu failed!");
                } else {
                    //Log.d(TAG, "Get max lnu succeeded!");
                    for(int i=0; i<message.length; i++) {
                        str += Integer.toString(message[i]&0x00FF);
                    }
                }
            }

            //Log.e(TAG, "MaxLnu: " + str);
        }
    }

    public void sendCommand() {
        byte[] cmd = new byte[] {
                (byte) 0x55, (byte) 0x53, (byte) 0x42, (byte) 0x43, // 固定值
                (byte) 0x30, (byte) 0x84, (byte) 0xf3, (byte) 0x03, // 自定义,与返回的CSW中的值是一样的
                (byte) 0x10, (byte) 0x00, (byte) 0x00, (byte) 0x00, // 传输数据长度为512字节
                (byte) 0x00, // 传入数据
                (byte) 0x00, // LNU为0,则设为0
                (byte) 0x0a, // 命令长度为1
                (byte) 0x86, (byte) 0x00, (byte) 0x00, (byte) 0x00, // READ FORMAT CAPACITIES,后面的0x00皆被忽略
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00
        };
        int result = mDeviceConnection.bulkTransfer(mEndpointOut, cmd, cmd.length, 1000);
        if(result < 0) {
            Log.d(TAG,  "Send command failed!");
        } else {
            //Log.d(TAG, "Send command succeeded!");
        }

        byte[] message = {(byte) 0xef, 0x01, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, 0x01, 0x00, 0x07, 0x13, 0x00, 0x00, 0x00, 0x00, 0x00, 0x1b};      //  需要足够的长度接收数据
//        byte[] message = TouchID.cmdPackage(TouchID.GR_GetImage, ARGE_NONE);
        result = mDeviceConnection.bulkTransfer(mEndpointOut, message, message.length, 1000);
        if(result < 0) {
            Log.d(TAG,  "Send message failed!");
        } else {
            //Log.d(TAG, "Send message succeeded!");
        }

        byte[] csw = new byte[13];
        result = mDeviceConnection.bulkTransfer(mEndpointIn, csw, csw.length, 1000);
        if(result < 0) {
            Log.d(TAG,  "Receive CSW failed!");
        } else {
            //Log.d(TAG, "Receive CSW succeeded!");
        }
        //Log.e(TAG, "" + ByteArrayUtils.bytesToHex(csw));
    }

    public void sendCommand3() {
        byte[] cmd = new byte[] {
                (byte) 0x55, (byte) 0x53, (byte) 0x42, (byte) 0x43, // 固定值
                (byte) 0x30, (byte) 0x84, (byte) 0xf3, (byte) 0x03, // 自定义,与返回的CSW中的值是一样的
                (byte) 0x0c, (byte) 0x00, (byte) 0x00, (byte) 0x00, // 传输数据长度为512字节
                (byte) 0x00, // 传入数据
                (byte) 0x00, // LNU为0,则设为0
                (byte) 0x0a, // 命令长度为1
                (byte) 0x86, (byte) 0x00, (byte) 0x00, (byte) 0x00, // READ FORMAT CAPACITIES,后面的0x00皆被忽略
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00
        };
        int result = mDeviceConnection.bulkTransfer(mEndpointOut, cmd, cmd.length, 1000);
        if(result < 0) {
            Log.d(TAG,  "Send command failed!");
        } else {
            //Log.d(TAG, "Send command succeeded!");
        }

//        byte[] message = {(byte) 0xef, 0x01, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, 0x01, 0x00, 0x07, 0x13, 0x00, 0x00, 0x00, 0x00, 0x00, 0x1b};      //  需要足够的长度接收数据
        byte[] message = TouchID.cmdPackage(TouchID.GR_GetImage, TouchID.ARGE_NONE);
        result = mDeviceConnection.bulkTransfer(mEndpointOut, message, message.length, 1000);
        if(result < 0) {
            Log.d(TAG,  "Send message failed!");
        } else {
            //Log.d(TAG, "Send message succeeded!");
        }

        byte[] csw = new byte[13];
        result = mDeviceConnection.bulkTransfer(mEndpointIn, csw, csw.length, 1000);
        if(result < 0) {
            Log.d(TAG,  "Receive CSW failed!");
        } else {
            //Log.d(TAG, "Receive CSW succeeded!");
        }
        //Log.e(TAG, "" + ByteArrayUtils.bytesToHex(csw));
    }

    public boolean send(byte[] arr) throws IOException {
        if (arr.length <=0 ) {
            return false;
        }

        byte[] cmd = new byte[] {
                (byte) 0x55, (byte) 0x53, (byte) 0x42, (byte) 0x43, // 固定值
                (byte) 0x60, (byte) 0xe4, (byte) 0x59, (byte) 0x02, // 自定义,与返回的CSW中的值是一样的
                (byte) arr.length, (byte) 0x00, (byte) 0x00, (byte) 0x00, // 传输数据长度为512字节
                (byte) 0x00, // 传入数据
                (byte) 0x00, // LNU为0,则设为0
                (byte) 0x0a, // 命令长度为1
                (byte) 0x86, (byte) 0x00, (byte) 0x00, (byte) 0x00, // READ FORMAT CAPACITIES,后面的0x00皆被忽略
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00
        };
        if (mDeviceConnection == null) {
            throw new IOException();
        }
        int result = mDeviceConnection.bulkTransfer(mEndpointOut, cmd, cmd.length, 1000);
        if(result < 0) {
            Log.d(TAG,  "Send command failed!");
            return false;
        } else {
            //Log.d(TAG, "Send command succeeded!");
        }

//        byte[] message = {(byte) 0xef, 0x01, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, 0x01, 0x00, 0x07, 0x13, 0x00, 0x00, 0x00, 0x00, 0x00, 0x1b};      //  需要足够的长度接收数据
        result = mDeviceConnection.bulkTransfer(mEndpointOut, arr, arr.length, 1000);
        if(result < 0) {
            Log.d(TAG,  "Send message failed!");
            return false;
        } else {
            //Log.d(TAG, "Send message succeeded!");
        }

        byte[] csw = new byte[13];
        result = mDeviceConnection.bulkTransfer(mEndpointIn, csw, csw.length, 1000);
        if(result < 0) {
            Log.d(TAG,  "Receive CSW failed!");
            return false;
        } else {
            //Log.d(TAG, "Receive CSW succeeded!");
        }
        //Log.e(TAG, "" + ByteArrayUtils.bytesToHex(csw));

        return true;
    }

    public byte[] read() {
        byte[] arr = new byte[0x40];
        byte[] cmd = new byte[] {
                (byte) 0x55, (byte) 0x53, (byte) 0x42, (byte) 0x43, // 固定值
                (byte) 0x60, (byte) 0xe4, (byte) 0x59, (byte) 0x02, // 自定义,与返回的CSW中的值是一样的
                (byte) 0x40, (byte) 0x00, (byte) 0x00, (byte) 0x00, // 传输数据长度为0x40字节
                (byte) 0x80, // 传入数据
                (byte) 0x00, // LNU为0,则设为0
                (byte) 0x0a, // 命令长度为1
                (byte) 0x85, (byte) 0x00, (byte) 0x00, (byte) 0x00, // READ FORMAT CAPACITIES,后面的0x00皆被忽略
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00
        };
        int result = mDeviceConnection.bulkTransfer(mEndpointOut, cmd, cmd.length, 1000);
        if(result < 0) {
            Log.d(TAG,  "Send command failed!");
        } else {
            //Log.d(TAG, "Send command succeeded!");
        }

//        byte[] message = {(byte) 0xef, 0x01, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, 0x01, 0x00, 0x07, 0x13, 0x00, 0x00, 0x00, 0x00, 0x00, 0x1b};      //  需要足够的长度接收数据
        result = mDeviceConnection.bulkTransfer(mEndpointIn, arr, arr.length, 1000);
        if(result < 0) {
            Log.d(TAG,  "Receive message failed!");
        } else {
            //Log.d(TAG, "Receive message succeeded!");
        }

        byte[] csw = new byte[13];
        result = mDeviceConnection.bulkTransfer(mEndpointIn, csw, csw.length, 1000);
        if(result < 0) {
            Log.d(TAG,  "Receive CSW failed!");
        } else {
            //Log.d(TAG, "Receive CSW succeeded!");
        }
        //Log.e(TAG, "Receive " + ByteArrayUtils.bytesToHex(arr));

        return arr;
    }

    public void sendCommand2() {
        String str = "";

        byte[] cmd = new byte[] {
                (byte) 0x55, (byte) 0x53, (byte) 0x42, (byte) 0x43, // 固定值
                (byte) 0x28, (byte) 0xe8, (byte) 0x3e, (byte) 0xfe, // 自定义,与返回的CSW中的值是一样的
                (byte) 0x00, (byte) 0x02, (byte) 0x00, (byte) 0x00, // 传输数据长度为512字节
                (byte) 0x80, // 传入数据
                (byte) 0x00, // LNU为0,则设为0
                (byte) 0x01, // 命令长度为1
                (byte) 0x23, (byte) 0x00, (byte) 0x00, (byte) 0x00, // READ FORMAT CAPACITIES,后面的0x00皆被忽略
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00
        };
        int result = mDeviceConnection.bulkTransfer(mEndpointOut, cmd, cmd.length, 1000);
        if(result < 0) {
            Log.d(TAG,  "Send command failed!");
            str += "Send command failed!\n";
        } else {
            //Log.d(TAG, "Send command succeeded!");
            str += "Send command succeeded!\n";
        }

        byte[] message = new byte[24];      //  需要足够的长度接收数据
        result = mDeviceConnection.bulkTransfer(mEndpointIn, message, message.length, 1000);
        if(result < 0) {
            Log.d(TAG,  "Receive message failed!");
            str += "Receive message failed!\n";
        } else {
            //Log.d(TAG, "Receive message succeeded!");
            str += "Receive message succeeded!\nFormat capacities : \n";
            for(int i=0; i<message.length; i++) {
                str += Integer.toHexString(message[i]&0x00FF) + " ";
            }
        }

        byte[] csw = new byte[13];
        result = mDeviceConnection.bulkTransfer(mEndpointIn, csw, csw.length, 1000);
        if(result < 0) {
            Log.d(TAG,  "Receive CSW failed!");
            str += "\nReceive CSW failed!";
        } else {
            //Log.d(TAG, "Receive CSW succeeded!");
            str += "\nReceive CSW succeeded!\nReceived CSW : ";
            for(int i=0; i<csw.length; i++) {
                str += Integer.toHexString(csw[i]&0x00FF) + " ";
            }
        }
        str += "\n";
        //Log.e(TAG, "" + str);
    }

    void sendAccessControlInLUN() {

    }
}

