package com.kangear.bodycompositionanalyzer;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.app.Activity;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.TwoLineListItem;

import java.util.List;

public class WiFiActivity extends AppCompatActivity {
    private static final String TAG = "TestWifiScan";
    private WifiManager mWifiManager;
    private List<ScanResult> mScanResults;


    @SuppressLint("WifiManagerLeak")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);
//        super.getListView().setBackgroundColor(Color.BLACK);

        mWifiManager = (WifiManager)getSystemService(WIFI_SERVICE);

        ListView mListView = findViewById(R.id.listview);

//        mListView.setListAdapter(mListAdapter);
        mListView.setAdapter(mListAdapter);

        mListView.setOnItemClickListener(mItemOnClick);

        IntentFilter ifi = new IntentFilter();
        ifi.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
        ifi.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        ifi.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        ifi.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mB, ifi);
    }

    private BroadcastReceiver mB = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String act = intent.getAction();
            if (act == null)
                return;

            if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {// 监听wifi的打开与关闭，与wifi的连接无关
                int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
                Log.e("TAG", "wifiState:" + wifiState);
                switch (wifiState) {
                    case WifiManager.WIFI_STATE_DISABLED:
                        break;
                    case WifiManager.WIFI_STATE_DISABLING:
                        break;
                }
            }


            if (act.equals(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION)) {
                Log.e(TAG, "SUPPLICANT_STATE_CHANGED_ACTION: " + act);
                int linkWifiResult = intent.getIntExtra(WifiManager.EXTRA_SUPPLICANT_ERROR, 123);
                if (linkWifiResult == WifiManager.ERROR_AUTHENTICATING) {
                    Toast.makeText(getBaseContext(), "密码错误", Toast.LENGTH_LONG).show();
                }
            }

            // 监听wifi的连接状态即是否连上了一个有效无线路由
            if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
                Parcelable parcelableExtra = intent
                        .getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                if (null != parcelableExtra) {
                    // 获取联网状态的NetWorkInfo对象
                    NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
                    //获取的State对象则代表着连接成功与否等状态
                    NetworkInfo.State state = networkInfo.getState();
                    //判断网络是否已经连接
                    boolean isConnected = state == NetworkInfo.State.CONNECTED;
                    Log.d(TAG, "networkInfo: " + networkInfo.toString());
                    Log.e("TAG", "isConnected:" + isConnected);
                    if (isConnected) {
                        Toast.makeText(getBaseContext(), "WiFi已成功连接", Toast.LENGTH_LONG).show();
                    } else {

                    }
                }
            }

            // 监听网络连接，包括wifi和移动数据的打开和关闭,以及连接上可用的连接都会接到监听
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                //获取联网状态的NetworkInfo对象
                NetworkInfo info = intent
                        .getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
                if (info != null) {
                    //如果当前的网络连接成功并且网络连接可用
                    if (NetworkInfo.State.CONNECTED == info.getState() && info.isAvailable()) {
                        if (info.getType() == ConnectivityManager.TYPE_WIFI
                                || info.getType() == ConnectivityManager.TYPE_MOBILE) {
                            Log.i("TAG", getConnectionType(info.getType()) + "连上");
                        }
                    } else {
                        Log.i("TAG", getConnectionType(info.getType()) + "断开");
                    }
                }
            }
        }
    };

    public void onClick(View v) {
        finish();
    }

    private String getConnectionType(int type) {
        String connType = "";
        if (type == ConnectivityManager.TYPE_MOBILE) {
            connType = "3G网络数据";
        } else if (type == ConnectivityManager.TYPE_WIFI) {
            connType = "WIFI网络";
        }
        return connType;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mB);
    }

    @Override
    public void onResume() {
        super.onResume();
        final IntentFilter filter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(mReceiver, filter);
        mWifiManager.startScan();
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action.equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                mScanResults = mWifiManager.getScanResults();
                mListAdapter.notifyDataSetChanged();

                mWifiManager.startScan();
            }

        }
    };

    private BaseAdapter mListAdapter = new BaseAdapter() {

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null || !(convertView instanceof TwoLineListItem)) {
                convertView = View.inflate(getApplicationContext(),
                        android.R.layout.simple_list_item_2, null);
            }

            final ScanResult result = mScanResults.get(position);
            ((TwoLineListItem)convertView).getText1().setText(result.SSID);
            ((TwoLineListItem)convertView).getText2().setText(
                    String.format("%s  %d", result.BSSID, result.level)
            );
            return convertView;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public int getCount() {
            return mScanResults == null ? 0 : mScanResults.size();
        }
    };

    private AdapterView.OnItemClickListener mItemOnClick = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            final ScanResult result = mScanResults.get(position);
            launchWifiConnecter(WiFiActivity.this, result);
        }
    };

    /**
     * Try to launch Wifi Connecter with {@link #hostspot}. Prompt user to download if Wifi Connecter is not installed.
     * @param activity
     * @param hotspot
     */
    private static void launchWifiConnecter(final Activity activity, final ScanResult hotspot) {
        final Intent intent = new Intent("com.farproc.wifi.connecter.action.CONNECT_OR_EDIT");
        intent.putExtra("com.farproc.wifi.connecter.extra.HOTSPOT", hotspot);
        try {
            activity.startActivity(intent);
        } catch(ActivityNotFoundException e) {
            // Wifi Connecter Library is not installed.
            Toast.makeText(activity, "Wifi Connecter is not installed.", Toast.LENGTH_LONG).show();
            downloadWifiConnecter(activity);
        }
    }

    private static void downloadWifiConnecter(final Activity activity) {
        Intent downloadIntent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse("market://details?id=com.farproc.wifi.connecter"));
        try {
            activity.startActivity(downloadIntent);
            Toast.makeText(activity, "Please install this app.", Toast.LENGTH_LONG).show();
        } catch (ActivityNotFoundException e) {
            // Market app is not available in this device.
            // Show download page of this project.
            try {
                downloadIntent.setData(Uri.parse("http://code.google.com/p/android-wifi-connecter/downloads/list"));
                activity.startActivity(downloadIntent);
                Toast.makeText(activity, "Please download the apk and install it manully.", Toast.LENGTH_LONG).show();
            } catch  (ActivityNotFoundException e2) {
                // Even the Browser app is not available!!!!!
                // Show a error message!
                Toast.makeText(activity, "Fatel error! No web browser app in your device!!!", Toast.LENGTH_LONG).show();
            }
        }
    }
}
