package com.example.tony.usbprinter;

import android.content.res.AssetManager;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    private static String LOG_TAG = "UsbPrinter";

    AssetManager mAssetManager = null;
    InputStream mInputStream = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 1. 读取资源文件
        mAssetManager = getAssets();
        try {
            mInputStream = mAssetManager.open("HelloWorld.r330");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 2. 写入打印机
        Printer mPrinter = new Printer(this);
        if(mPrinter.isConnected()) {
            mPrinter.write(mInputStream);
        }
    }
}
