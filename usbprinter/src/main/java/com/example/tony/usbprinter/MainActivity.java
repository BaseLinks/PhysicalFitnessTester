package com.example.tony.usbprinter;

import android.content.res.AssetManager;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    private static String LOG_TAG = "UsbPrinter";

    private Printer mPrinter;

    AssetManager mAssetManager = null;
    TextView mPrinterModelTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mPrinterModelTextView = (TextView) findViewById(R.id.model_textview);

        // 2. 获取打印机型号
        mPrinter = new Printer(this);
        if(mPrinter.isConnected()) {
            mPrinterModelTextView.setText(mPrinter.getModel2().getDes());
        }
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.test_button:
                if(mPrinter.isConnected()) {
                    // 1. 读取资源文件
                    mAssetManager = getAssets();
                    InputStream mInputStream = null;
                    try {
                        mInputStream = mAssetManager.open(mPrinter.getModel2().getTestFileName());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // 2. 写入数据
                    mPrinter.write(mInputStream);
                }
                break;
        }
    }
}
