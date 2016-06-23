package com.example.tony.usbprinter;

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
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    private UsbManager mManager;
    private UsbDevice mDevice;
    private UsbDeviceConnection mDeviceConnection;
    private UsbInterface mInterface;
    private PrinterDevice mPrinterDevice;
    private static String LOG_TAG = "UsbPrinter";
    private static final String ACTION_USB_PERMISSION =
            "com.android.example.USB_PERMISSION";

    AssetManager mAssetManager = null;
    InputStream mInputStream = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //读取资源文件
        mAssetManager = getAssets();
        try {
            mInputStream = mAssetManager.open("HelloWorld.r330");
        } catch (IOException e) {
            e.printStackTrace();
        }

        mManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        for(UsbDevice device : mManager.getDeviceList().values()) {
            UsbInterface intf = findPrinterInterface(device);
            if (setPrinterInterface(device, intf)) {
                break;
            }
        }

        // listen for new devices
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        filter.addAction(ACTION_USB_PERMISSION);
        registerReceiver(mUsbReceiver, filter);
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
                    mPrinterDevice = new PrinterDevice(this, mDeviceConnection, intf);
                    Log.i(LOG_TAG, "call start");
                    mPrinterDevice.start();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
