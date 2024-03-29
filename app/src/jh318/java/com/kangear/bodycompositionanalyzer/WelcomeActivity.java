package com.kangear.bodycompositionanalyzer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.pdf.PdfDocument;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.os.Process;
import android.os.SystemClock;
import android.print.PrintAttributes;
import android.support.annotation.ColorInt;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kangear.bodycompositionanalyzer.application.App;
import com.kangear.bodycompositionanalyzer.entry.SchoopiaStudent;
import com.kangear.bodycompositionanalyzer.tool.SerialDevice;
import com.kangear.common.utils.ByteArrayUtils;
import com.kangear.common.utils.TimeUtils;
import com.kangear.qr.PrinterIntence;
import com.yhao.floatwindow.FloatWindow;
import com.yhao.floatwindow.MoveType;
import com.yhao.floatwindow.PermissionListener;
import com.yhao.floatwindow.Screen;
import com.yhao.floatwindow.ViewStateListener;

import org.w3c.dom.Text;
import org.xutils.DbManager;
import org.xutils.x;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeoutException;

import static com.kangear.bodycompositionanalyzer.BcaService.installApks;
import static com.kangear.bodycompositionanalyzer.BcaService.installBootAnimation;
import static com.kangear.bodycompositionanalyzer.BcaService.installBusybox;
import static com.kangear.bodycompositionanalyzer.BcaService.installPrinterDriver;
import static com.kangear.bodycompositionanalyzer.BodyComposition.tixingfenxiString;
import static com.kangear.bodycompositionanalyzer.MusicService.SOUND_01_NEW_TEST;
import static com.kangear.bodycompositionanalyzer.MusicService.SOUND_02_VIP_TEST;
import static com.kangear.bodycompositionanalyzer.MusicService.SOUND_03_WEIGHT_DONE;
import static com.kangear.bodycompositionanalyzer.MusicService.SOUND_04_ID;
import static com.kangear.bodycompositionanalyzer.MusicService.SOUND_05_AGE;
import static com.kangear.bodycompositionanalyzer.MusicService.SOUND_06_GENDER;
import static com.kangear.bodycompositionanalyzer.MusicService.SOUND_07_HEIGHT;
import static com.kangear.bodycompositionanalyzer.MusicService.SOUND_08_TEST_START;
import static com.kangear.bodycompositionanalyzer.MusicService.SOUND_09_TEST_20;
import static com.kangear.bodycompositionanalyzer.MusicService.SOUND_10_TEST_100;
import static com.kangear.bodycompositionanalyzer.MusicService.SOUND_11_TEST_FAIL;
import static com.kangear.bodycompositionanalyzer.MusicService.SOUND_12_PRINT;
import static com.kangear.bodycompositionanalyzer.MusicService.SOUND_13_VIP_TOUCH_ID;
import static com.kangear.bodycompositionanalyzer.MusicService.SOUND_30_LOG_UP;
import static com.kangear.bodycompositionanalyzer.Person.GENDER_MALE;
import static com.kangear.bodycompositionanalyzer.Protocol.MSG_STATE_DONE;
import static com.kangear.bodycompositionanalyzer.Protocol.MSG_STATE_TESTING_1;
import static com.kangear.bodycompositionanalyzer.Protocol.MSG_STATE_TESTING_2;
import static com.kangear.bodycompositionanalyzer.Protocol.MSG_STATE_TESTING_3;
import static com.kangear.bodycompositionanalyzer.Protocol.MSG_STATE_TESTING_4;
import static com.kangear.bodycompositionanalyzer.Protocol.MSG_STATE_WAIT;
import static com.kangear.bodycompositionanalyzer.Protocol.PROTOCAL_GENDER_FEMALE;
import static com.kangear.bodycompositionanalyzer.Protocol.PROTOCAL_GENDER_MALE;
import static com.kangear.qr.PrinterIntence.getJiaoHao;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class WelcomeActivity extends BaseActivity {
    private static final String TAG = "WelcomeActivity";
    public static final int INVALID_FINGER_ID = -1;
    public static final int INVALID_RECORD_ID = -1;
    public static final String CONST_FINGER_ID = "CONST_FINGER_ID";
    public static final String CONST_RECORD_ID = "CONST_RECORD_ID";
    public static final String CONST_PERSON_ID = "CONST_PERSON_ID";
    private static final int REQUEST_CODE_VIP_REGISTE = 1;
    public static final int REQUEST_CODE_TOUCHID      = 7;
    public static final int REQUEST_CODE_DELETE       = 8;
    public static final int PERSON_ID_INVALID         = -1;
    public static final int PERSON_ID_ANONYMOUS       = 0; // for tmp test
    public static final int RECORD_ID_INVALID         = -1; // for tmp test
    public static final int RECORD_ID_ANONYMOUS       = 1; // for tmp test

    public static final int HANDLE_EVENT_INIT                       = 96;
    public static final int HANDLE_EVENT_AUTO_TEST_START            = 97;
    public static final int HANDLE_EVENT_AUTO_TEST_DONE             = 98;
    public static final int HANDLE_EVENT_AUTO_TEST_ERROR            = 99;
    public static final int HANDLE_EVENT_WEIGHT_STOP                = 100;
    public static final int HANDLE_EVENT_UPDATE_TICHENGFEN_PROGRESS = 101;
    public static final int HANDLE_EVENT_WEIGHT_ERROR               = 102;
    public static final int HANDLE_EVENT_TICHENGFEN_ERROR           = 103;
    public static final String CONST_BITMAP = "BITMAP";

    private TimeUtils mTimeUtils;
    private static Person mCurPersion;
    private static Record mCurRecord;
    public static final int WEIGHT_INVALIDE          = -1;
    public static final int WEIGHT_NEW_TEST          = 1;
    public static final int WEIGHT_VIP_TEST          = 2;
    public static final String CONST_WEIGHT_TAG      = "CONST_WEIGHT_TAG";

    public static final float DEFAULT_WEIGHT = (float) 60.4;
    public static final float DEFAULT_GUGEJI = (float) 36.3;
    public static final float DEFAULT_TIZHIFANG = (float)46.1;
    public static final float DEFAULT_SHENTIZHILIANGZHISHU = (float) 30.1;
    public static final float DEFAULT_TIZHIBAIFENBI = (float)32.7;
    public static final int DEFAULT_JICHUDAIXIELIANG = 1787;

    public static final String FORMAT_WEIGHT = "%.1f";

    private static DbManager mDb;

    public static DbManager getDB() {
        return mDb;
    }

    public static final String CONST_ACTION_TOUCHID_OK = "CONST_ACTION_TOUCHID_OK";

    private static int mAgeMin;
    private static int mAgeMax;
    private static int mHeightMax;
    private static int mHeightMin;
    private static int mWeightMax;
    private static int mWeightMin;
    private static int mGenderMin;
    private static int mGenderMax;
    private static int mRadioMin;
    private static int mRadioMax;
    private static Context mContext;
    private ProgressDialog mSelfCheckProgressDialog;
    //创建一个SoundPool对象
    private ImageView mLogoImageView;
    private ImageView mWifiImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        mContext = this;

        preInit();

        if (!App.isInit()) {
            App.setInit(true);
            mHandler.sendEmptyMessageDelayed(HANDLE_EVENT_AUTO_TEST_START, 1);
        } else {
            // second boot
            mHandler.sendEmptyMessageDelayed(HANDLE_EVENT_AUTO_TEST_DONE, 1);
        }


        ImageView imageView = new ImageView(getApplicationContext());
        imageView.setImageResource(R.drawable._01_logo_color);

        IntentFilter ifi = new IntentFilter();
        ifi.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
        ifi.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        ifi.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        ifi.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mB, ifi);

        WatchDog.getInstance(this).feed(Color.WHITE);

        addVersionNameView();

        Log.e(TAG, "" + tixingfenxiString[1][2]);
    }

    private void addVersionNameView() {
        ((TextView)findViewById(R.id.version_textview)).setText(getAppVersionName(getApplicationContext()));
//        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
//        View myLayout = inflater.inflate(R.layout.version_name, null);
//        TextView textView = myLayout.findViewById(R.id.textView);
//        textView.setText(getAppVersionName(getApplicationContext()));
//
//        FloatWindow
//                .with(getApplicationContext())
//                .setMoveType(MoveType.inactive)
//                .setView(myLayout)
////                .setHeight(Screen.height, 1)
////                .setWidth(Screen.width, 1)
//                .setWidth(100)                               //设置控件宽高
//                .setHeight(20)
//                .setX(Screen.width, 0.00f)                                   //设置控件初始位置
//                .setY(Screen.height,1f)
//                .setDesktopShow(true)                        //桌面显示
//                .setTag("version")
//                .build();
    }


    /**
     * 返回当前程序版本名
     */
    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }

    /**
     * 判断性别是否合法
     * @param gender
     * @return
     */
    public static boolean checkGender(int gender) {
        return gender == mGenderMax || gender == mGenderMin;
    }

    /**
     * 判断年龄是否合法
     * @param age
     * @return
     */
    public static boolean checkAge(int age) {
        return ((age >= mAgeMin) && (age <= mAgeMax));
    }

    /**
     * 判断身高是否合法
     * @param val
     * @return
     */
    public static boolean checkId(int val) {
        return true;
    }

    /**
     * 判断身高是否合法
     * @param height cm
     * @return
     */
    public static boolean checkHeight(int height) {
        // 转换成mm
        height = height * 10;
        return height >= mHeightMin && height <= mHeightMax;
    }

    /**
     * 判断体重是否合法
     * @param weight kg
     * @return
     */
    public static boolean checkWeight(int weight) {
        // 转换成mm
        weight = weight * 10;
        return weight >= mWeightMin && weight <= mWeightMax;
    }

    /**
     * 判断体重是否合法
     * @param value kg
     * @return
     */
    public static boolean checkRadio(int value) {
        // 转换成mm
        return value >= mRadioMin && value <= mRadioMax;
    }


    // This snippet hides the system bars.
    public static void hideSystemUI(View v) {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        v.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    /*隐藏虚拟按键*/
    public static boolean hideNavigation(Context context){
        boolean ishide;
        try
        {
            String command;
            command = "LD_LIBRARY_PATH=/vendor/lib:/system/lib service call activity 42 s16 com.android.systemui";
            java.lang.Process proc = Runtime.getRuntime().exec(new String[] { "su", "-c", command });
            proc.waitFor();
            ishide = true;
        }
        catch(Exception ex)
        {
            Toast.makeText(context.getApplicationContext(), ex.getMessage(),
                    Toast.LENGTH_LONG).show();
            ishide = false;
        }
        return ishide;
    }

//    隐藏导航栏和状态栏:
    //A83T
    public static String ACTION_HIDE_NAV_BAR = "com.xrd.action.hide_nav_bar";
    public static String ACTION_SHOW_NAV_BAR = "com.xrd.action.show_nav_bar";

    public static String ACTION_SHOW_STATUS_BAR = "com.xrd.action.show_status_bar";
    public static String ACTION_HIDE_STATUS_BAR = "com.xrd.action.hide_status_bar";
    //A33:
//    public static String ACTION_HIDE_NAV_BAR = "gzsd.hclydao.action.hide_nav_bar";
//    public static String ACTION_SHOW_NAV_BAR = "gzsd.hclydao.action.show_nav_bar";
//
//    public static String ACTION_SHOW_STATUS_BAR = "gzys.hclydao.action.show_status_bar";
//    public static String ACTION_HIDE_STATUS_BAR = "gzys.hclydao.action.hide_status_bar";

    /*显示虚拟按键*/
    public boolean showNavigation(Context context){
        boolean isshow = true;
        try {
            Intent intent = new Intent(ACTION_SHOW_NAV_BAR);
            Intent intent1 = new Intent(ACTION_SHOW_STATUS_BAR);
            sendBroadcast(intent);
            sendBroadcast(intent1);
        }
        catch (Exception e){
            isshow = false;
            e.printStackTrace();
        }
        return isshow;
    }

    /*显示虚拟按键*/
    public static boolean showNavigation2(Context context){
        boolean isshow;
        try
        {
            String command;
            command = "LD_LIBRARY_PATH=/vendor/lib:/system/lib am startservice -n com.android.systemui/.SystemUIService";
            java.lang.Process proc = Runtime.getRuntime().exec(new String[] { "su", "-c", command });
            proc.waitFor();
            isshow = true;
        }
        catch (Exception e){
            isshow = false;
            e.printStackTrace();
        }
        return isshow;
    }

    /**
     * 开始
     */
    public static void startWeightTest(final Activity activity,
                                       final TextView weightTextView,
                                       final Handler handler) {
        weightTextView.setText("0.0");

        if (mTichengfen != null && mTichengfen.isAlive()) {
            mTichengfen.interrupt();
            mTichengfen = null;
        }

        // star phread
        mWeight = new Thread() {
            @Override
            public void run() {
                boolean ret = false;
                float weight = 0;
                try {
                    // 倒计时，有启动指令下发的（体成分指令，体重指令），下位机，等待查询指令的，58秒倒计时退出时需要给下位机发停止命令，下位机回初始状态。
                    // 针对上述问题，解决方案是在测试体重时先发一个终止指令
                    ret = UartBca.getInstance(activity).stopWeight();
                    ret = UartBca.getInstance(activity).startWeight();
                } catch (Protocol.ProtocalExcption protocalExcption) {
                    protocalExcption.printStackTrace();
                    ret = false;
                } finally {
                    if (!ret) {
                        activity.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(activity, "体重测试开始失败，请重新测试", Toast.LENGTH_LONG).show();
                            }
                        });

                        MusicService.play(mContext, SOUND_11_TEST_FAIL);
                        if (handler != null) {
                            handler.sendEmptyMessage(HANDLE_EVENT_WEIGHT_ERROR);
                        }
                        return;
                    } else {
                        activity.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(activity, "体重测试开始成功", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }


                while(true) {
                    try {
                        sleep(500);
                        final Protocol.QueryResult qr = UartBca.getInstance(activity).qeuryWeight();
                        if (qr == null) {
                            continue;
                        }
                        byte state = qr.getState();
                        Protocol.parseErrorState(state);
                        weight = qr.getShortFromData() / (float)10;
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                weightTextView.setText(String.format(FORMAT_WEIGHT, qr.getShortFromData() / (float)10));
                            }
                        });
                        Log.d(TAG, "weight: " + weight);
                        if (state == Protocol.MSG_STATE_DONE) {
                            MusicService.play(mContext, SOUND_03_WEIGHT_DONE);
                            if (handler != null)
                                handler.sendEmptyMessage(HANDLE_EVENT_WEIGHT_STOP);
                            break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        MusicService.play(mContext, SOUND_11_TEST_FAIL);
                        if (handler != null) {
                            handler.sendEmptyMessage(HANDLE_EVENT_WEIGHT_ERROR);
                        }
                    }
                }

                Log.e(TAG, "startWeightTest: end...");
            }
        };
        mWeight.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWifiImageView.setVisibility(isWifiConnected() ? View.VISIBLE : View.INVISIBLE);
        mTimeUtils.start();
        hideSystemUI(getWindow().getDecorView());
        WatchDog.getInstance(getBaseContext()).setVisible(false);

        if (mTichengfen != null && mTichengfen.isAlive()) {
            mTichengfen.interrupt();
            mTichengfen = null;
        }

        if (mWeight != null && mWeight.isAlive()) {
            mWeight.interrupt();
            mWeight = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mTimeUtils.stop();
        WatchDog.getInstance(getBaseContext()).setVisible(true);
    }

    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.vip_register_imageview:
                // TODO：获取一个id以及一个fingerId
                MusicService.play(mContext, SOUND_30_LOG_UP);
                intent = new Intent(this, MemRegActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivityForResult(intent, REQUEST_CODE_VIP_REGISTE);
                break;
            case R.id.vip_test_imageview:
                // 318 会员测试
                getRecord().setTime(System.currentTimeMillis());
                startVipTest(this);
                break;
            case R.id.start_new_test_imageview:
                getRecord().setTime(System.currentTimeMillis());
                startTmpTest(this);
                break;
            case R.id.history_imageview:
                startHistory(this);
                break;
            case R.id.settings_imageview:
//                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
//                alertDialog.setTitle("Alert");
//                alertDialog.setMessage("Alert message to be shown");
//                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        });
//                alertDialog.show();
                startSettings(this);
//                onClick2(v);
                break;
        }
    }

    public static Person getPerson() {
        if (mCurPersion == null) {
            mCurPersion = new Person();
        }
        return mCurPersion;
    }

    public static Record getRecord() {
//        if (mCurRecord == null) {
//            mCurRecord = new Record();
//        }

        return App.getRecord();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLE_EVENT_AUTO_TEST_START:
                    mAgeMin = getResources().getInteger(R.integer.age_min);
                    mAgeMax = getResources().getInteger(R.integer.age_max);
                    mHeightMin = getResources().getInteger(R.integer.height_min);
                    mHeightMax = getResources().getInteger(R.integer.height_max);
                    mWeightMin = getResources().getInteger(R.integer.weight_min);
                    mWeightMax = getResources().getInteger(R.integer.weight_max);
                    mGenderMin = getResources().getInteger(R.integer.gender_min);
                    mGenderMax = getResources().getInteger(R.integer.gender_max);
                    mRadioMin = getResources().getInteger(R.integer.radio_min);
                    mRadioMax = getResources().getInteger(R.integer.radio_max);

                    mSelfCheckProgressDialog.show();
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            selfCheck(mContext);
                        }
                    }.start();
                    break;
                case HANDLE_EVENT_AUTO_TEST_DONE:
                    mSelfCheckProgressDialog.dismiss();
                    // 启动指纹
                    TouchID.getInstance(mContext.getApplicationContext());
                    UartBca.getInstance(mContext);
                    break;
                case HANDLE_EVENT_AUTO_TEST_ERROR:
                    mSelfCheckProgressDialog.dismiss();
                    startErrorDialog(mContext);
                    break;
            }
        }
    };


    /**
     * 初始化
     */
    private void preInit() {

        // hide edu
        findViewById(R.id.textView73).setVisibility(BuildConfig.FLAVOR_sub.contains("edu") ? View.VISIBLE : View.INVISIBLE);

        if (!BuildConfig.DEBUG)
            hideNavigation(this);

        mWifiImageView = findViewById(R.id.imageView12);
        mWifiImageView.setVisibility(isWifiConnected() ? View.VISIBLE : View.INVISIBLE);

        mLogoImageView = findViewById(R.id.logo_imageview);
        mLogoImageView.setOnClickListener(new View.OnClickListener() {
            long[] mHits = new long[3];
            @Override
            public void onClick(View arg0) {
                System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
                mHits[mHits.length - 1] = SystemClock.uptimeMillis();
                if (mHits[0] >= (SystemClock.uptimeMillis() - 1000)) {
                    Arrays.fill(mHits, 0);
                    showNavigation(mContext);
                    finishAffinity();
                    Process.killProcess(Process.myPid());
                    Toast.makeText(mContext, "虚拟按键已启用", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mSelfCheckProgressDialog = new ProgressDialog(mContext, ProgressDialog.THEME_HOLO_DARK);
        mSelfCheckProgressDialog.setTitle("开机自检");
        mSelfCheckProgressDialog.setMessage("自检请稍候");
        mTimeUtils = new TimeUtils((TextView) findViewById(R.id.time_textview), (TextView)findViewById(R.id.date_textview));

//        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
//                .setDbName("test2.db")
//                // 不设置dbDir时, 默认存储在app的私有目录.
//                .setDbDir(getFilesDir()) // "sdcard"的写法并非最佳实践, 这里为了简单, 先这样写了.
//                .setDbVersion(2)
//                .setDbOpenListener(new DbManager.DbOpenListener() {
//                    @Override
//                    public void onDbOpened(DbManager db) {
//                        // 开启WAL, 对写入加速提升巨大
//                        db.getDatabase().enableWriteAheadLogging();
//                    }
//                })
//                .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
//                    @Override
//                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
//                        // TODO: ...
//                        // db.addColumn(...);
//                        // db.dropTable(...);
//                        // ...
//                        // or
//                        // db.dropDb();
//                    }
//                });
//        mDb = x.getDb(daoConfig);

        mDb = App.getDB();

        // id 1 for ANONYMOUS
        Record tmp = RecordBean.getInstance(mContext).query(RECORD_ID_ANONYMOUS);
        if (tmp == null) {
            tmp = new Record();
            tmp.setId(RECORD_ID_ANONYMOUS);
            tmp.setPersonId(PERSON_ID_ANONYMOUS);
            RecordBean.getInstance(mContext).insert(tmp);
        }

        TouchID.getInstance(mContext);
        // 判断Person数据库表，如果数据库表为空，那么Empty指纹
        MemRegActivity.checkMem(this);
        Printer.getInstance(this).init();
        // 2. device connect
        UartBca.getInstance(mContext);
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

                    mWifiImageView.setVisibility(isWifiConnected() ? View.VISIBLE : View.INVISIBLE);
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

    public boolean isWifiConnected() {

        WifiManager mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
        int ipAddress = wifiInfo == null ? 0 : wifiInfo.getIpAddress();
        if (mWifiManager.isWifiEnabled() && ipAddress != 0) {
            return true;
        }
        return false;

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

    /**
     * 自检查
     * @param context
     */
    private void selfCheck(final Context context) {
        if (BuildConfig.DEBUG) {
            try {
                Printer.getInstance(this).init();

                installBusybox(mContext);
                installPrinterDriver(mContext);
                installBootAnimation(mContext);
                installApks(mContext);
                //installNotoFonts(mContext);

                // 判断Person数据库表，如果数据库表为空，那么Empty指纹
                MemRegActivity.checkMem(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
            mHandler.sendEmptyMessage(HANDLE_EVENT_AUTO_TEST_DONE);
            return;
        }

        // 1. Finger module
        boolean ret = false;
        Message msg = new Message();

        try {
            installBusybox(mContext);
            installPrinterDriver(mContext);
            installBootAnimation(mContext);
            installApks(mContext);
            //installNotoFonts(mContext);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ret = TouchID.getInstance(mContext).selfCheck();
        if (!ret) {
            msg.obj = "指纹模块异常";
            msg.what = HANDLE_EVENT_AUTO_TEST_ERROR;
            mHandler.sendMessage(msg);
            return;
        }

        // 判断Person数据库表，如果数据库表为空，那么Empty指纹
        MemRegActivity.checkMem(this);

        Printer.getInstance(this).init();


        // 2. device connect
        ret = UartBca.getInstance(mContext).selfCheck();
        if (!ret) {
            String ms = "体成分分析仪异常";
            msg.obj = ms;
            Log.i(TAG, ms);
            msg.what = HANDLE_EVENT_AUTO_TEST_ERROR;
            mHandler.sendMessage(msg);
            return;
        }

        // 3. database
        // 4. other
        mHandler.sendEmptyMessage(HANDLE_EVENT_AUTO_TEST_DONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            TouchID.getInstance(this).unInit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        unregisterReceiver(mB);
    }

    // pre test
    private static void startPreTest() {
        // 时间
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        getRecord().setDate(dateFormat.format(new Date()));
        getRecord().setTime(System.currentTimeMillis());
    }

    private static void startTmpTest(Context context) {
        startPreTest();
        // for tmp
        getRecord().setPersonId(PERSON_ID_ANONYMOUS);
        Intent intent = new Intent(context, WeightActivity.class);
        intent.putExtra(CONST_WEIGHT_TAG, WEIGHT_NEW_TEST);
        startActivityWithoutHistory(intent);
        MusicService.play(mContext, SOUND_01_NEW_TEST);
    }

    private static void startVipTest(Context context) {
        startPreTest();
        MusicService.play(mContext, SOUND_02_VIP_TEST);
        Intent intent = new Intent(context, WeightActivity.class);
        intent.putExtra(CONST_WEIGHT_TAG, WEIGHT_VIP_TEST);
        startActivityWithoutHistory(intent);
    }

    // 临时测试需要将personId设置成INVALID
    public static void doTmpTest(Context context) {
        startId(context);
        MusicService.play(mContext, SOUND_04_ID);
    }

    public static void doVipTest(Context context) {
        doTest(context);
    }

    /**
     * 1. ID
     * @param context
     */
    public static void startWelcome(Context context) {
        context.startActivity(new Intent(context, WelcomeActivity.class));
    }


    /**
     * 1. ID
     * @param context
     */
    private static void startId(Context context) {
        context.startActivity(new Intent(context, IDActivity.class));
    }

    /**
     * 2. AGE
     * @param context
     */
    public static void startAge(Context context) {
        context.startActivity(new Intent(context, AgeActivity.class));
        MusicService.play(mContext, SOUND_05_AGE);
    }

    /**
     * 3. Gender
     * @param context
     */
    public static void startGender(Context context) {
        context.startActivity(new Intent(context, GenderActivity.class));
        MusicService.play(mContext, SOUND_06_GENDER);
    }

    /**
     * 5. HEIGHT
     * @param context
     */
    public static void startHeight(Context context) {
        context.startActivity(new Intent(context, HeightActivity.class));
        MusicService.play(mContext, SOUND_07_HEIGHT);
    }

    /**
     * 8. TEST
     * @param context
     */
    public static void doTest(Context context) {
        context.startActivity(new Intent(context, TestActivity.class));
    }

    /**
     * 10. HEIGHT
     * @param actvity
     */
    public static void startTouchId(Activity actvity) {
        MusicService.play(mContext, SOUND_13_VIP_TOUCH_ID);
        actvity.startActivityForResult(new Intent(actvity, TouchIdActivity.class), REQUEST_CODE_TOUCHID);
    }


    /**
     * 11. Settings
     * @param actvity
     */
    public static void startSettings(Activity actvity) {
        startActivityWithoutHistory(new Intent(actvity, AdminPasswordDialogActivity.class));
    }

    /**
     * 11. Settings
     * @param actvity
     */
    public static void doSettings(Activity actvity) {
        actvity.startActivity(new Intent(actvity, SettingsActivity.class));
    }


    /**
     * 12. Pdf
     * @param actvity
     */
    public static void startPdf(Activity actvity, int recordId, String path) {
        Intent intent = new Intent(actvity, PdfActivity.class);
        intent.putExtra(CONST_RECORD_ID, recordId);
        intent.putExtra(CONST_BITMAP, path);
        actvity.startActivity(intent);
    }

    /**
     * 11. History
     * @param context
     */
    public static void startHistory(Context context) {
        startActivityWithoutHistory(new Intent(context, HistoryActivity.class));
    }

    private static void startActivityWithoutHistory(Intent intent) {
        if (mContext == null) {
            return;
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mContext.startActivity(intent);
    }

    /**
     * 系统错误
     * @param context
     */
    public static void startErrorDialog(Context context) {
        context.startActivity(new Intent(context, DeviceErrorDialogActivity.class));
    }

    public static void exitAsFail(Activity activity) {
        MusicService.stop(activity);
        Intent intent = new Intent(activity, WelcomeActivity.class);
        activity.setResult(RESULT_CANCELED, intent);
        activity.finish();
    }

    public static void unkownError(Activity activity) {
        Toast.makeText(activity, "未知错误，请联系厂家", Toast.LENGTH_SHORT).show();
    }

    private static Thread mTichengfen;
    private static Thread mWeight;

    /**
     * 开始
     */
    public static void startTichengfenTest(final Activity activity,
                                           final Handler handler,
                                           Record mRecord) {
        final long startTime = System.currentTimeMillis();
        final byte gender = (mRecord.getGender().equals(GENDER_MALE)) ? PROTOCAL_GENDER_MALE : PROTOCAL_GENDER_FEMALE;
        final byte age = (byte)((int)(mRecord.getAge() * (float)Math.pow(10, BodyComposition.年龄.dot)) & 0xFF);
        final short height = (short) (((int)(mRecord.getHeight() * (float)Math.pow(10, BodyComposition.身高.dot)) & 0xFFFF));
        final short weight = (short) (((int)(mRecord.getWeight() * (float)Math.pow(10, BodyComposition.体重.dot)) & 0xFFFF));

        Log.i(TAG,  "startTichengfenTest: gender: " + gender + " age: " + age + " height: " + height + " weight: " + weight);

        // star phread
        MusicService.play(mContext, SOUND_08_TEST_START);
        mTichengfen = new Thread() {
            @Override
            public void run() {
                boolean ret = false;
                try {
                    ret = UartBca.getInstance(activity).startTichengfen(gender, age, height, weight);
                } catch (Protocol.ProtocalExcption protocalExcption) {
                    protocalExcption.printStackTrace();
                    ret = false;
                } finally {
                    if (!ret) {
                        activity.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(activity, "体成分测试开始失败，请重新测试", Toast.LENGTH_LONG).show();
                                MusicService.play(mContext, SOUND_11_TEST_FAIL);
                                if (handler != null) {
                                    handler.sendEmptyMessage(HANDLE_EVENT_TICHENGFEN_ERROR);
                                }
                            }
                        });
                        return;
                    } else {
                        activity.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(activity, "体成分测试开始成功", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }

                boolean isRun = true;
                boolean isFirst20 = true;
                while(isRun) {
                    try {
//                        if ((System.currentTimeMillis() - startTime) / 1000 > (WatchDog.TIMEOUT - 10)) {
//                            // timeout
//                            throw new TimeoutException("一直未响应");
//                        }
                        sleep(20);
                        UartBca.QueryResult qr = UartBca.getInstance(activity).qeuryTichengfen();
                        if (qr == null) {
                            continue;
                        }
                        Message msg = new Message();
                        switch (qr.getState()) {
                            case MSG_STATE_WAIT:
                                // 进度条走到0%
                                break;
                            case MSG_STATE_TESTING_1:
                                // 进度条走到20%
//                                handler.sendEmptyMessage(SHOW_TEST);
//                                setProgress2(20);
                                msg.what = HANDLE_EVENT_UPDATE_TICHENGFEN_PROGRESS;
                                msg.arg1 = 20;
                                handler.sendMessage(msg);
                                if (isFirst20) {
                                    isFirst20 = false;
                                    MusicService.play(mContext, SOUND_09_TEST_20);
                                }
                                break;
                            case MSG_STATE_TESTING_2:
                                // 进度条走到40%
                                msg.what = HANDLE_EVENT_UPDATE_TICHENGFEN_PROGRESS;
                                msg.arg1 = 40;
                                handler.sendMessage(msg);
                                break;
                            case MSG_STATE_TESTING_3:
                                // 进度条走到60%
                                msg.what = HANDLE_EVENT_UPDATE_TICHENGFEN_PROGRESS;
                                msg.arg1 = 60;
                                handler.sendMessage(msg);
                                break;
                            case MSG_STATE_TESTING_4:
                                // 进度条走到80%
                                msg.what = HANDLE_EVENT_UPDATE_TICHENGFEN_PROGRESS;
                                msg.arg1 = 80;
                                handler.sendMessage(msg);
                                break;
                            case MSG_STATE_DONE:
                                // 进度条走到100%
                                Log.d(TAG, "ALLDATA: " + ByteArrayUtils.bytesToHex(qr.getData()));
                                BodyComposition bc = new BodyComposition(qr.getData());
                                getRecord().setBodyComposition(bc);
                                getRecord().setData(qr.getData());
                                msg.what = HANDLE_EVENT_UPDATE_TICHENGFEN_PROGRESS;
                                msg.arg1 = 100;
                                handler.sendMessage(msg);
                                isRun = false;
                                MusicService.play(mContext, SOUND_10_TEST_100);
                                break;
                            default:
                                Protocol.parseErrorState(qr.getState());
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        MusicService.play(mContext, SOUND_11_TEST_FAIL);
                        if (handler != null) {
                            handler.sendEmptyMessage(HANDLE_EVENT_TICHENGFEN_ERROR);
                        }
                        activity.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(activity, R.string.error_tip, Toast.LENGTH_LONG).show();
                            }
                        });
                        break;
                    }
                }

                Log.e(TAG, "startTichengfenTest: end...");
            }
        };
        mTichengfen.start();
    }

    public static void createPdfFromView(View content, final String pdfPath) {
        // create a new document
        PdfDocument document = new PdfDocument();
        Log.d(TAG, "FUCK0: " + System.currentTimeMillis() / 1000);
        // crate a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(
                PrintAttributes.MediaSize.ISO_A4.getWidthMils() * 72 / 1000,
                PrintAttributes.MediaSize.ISO_A4.getHeightMils() * 72 / 1000, 1)
                .create();

        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);

        // draw something on the page
//        LayoutInflater li = LayoutInflater.from(getApplicationContext());
//        View content = li.inflate(R.layout.activity_welcome, null);
        Canvas canvas = page.getCanvas();
        Log.i(TAG, "canvas: " + (canvas == null ? "null" : "!null"));
        Log.d(TAG, "FUCK1: " + System.currentTimeMillis() / 1000);
        content.draw(canvas);
        Log.d(TAG, "FUCK2: " + System.currentTimeMillis() / 1000);

        // finish the page
        document.finishPage(page);
        Log.d(TAG, "FUCK3: " + System.currentTimeMillis() / 1000);
        // add more pages
        // write the document content
        FileOutputStream os = null;
        try {
            Log.i(TAG, "String:" + pdfPath);
            os = new FileOutputStream(pdfPath);
            Log.d(TAG, "FUCK4: " + System.currentTimeMillis() / 1000);
            document.writeTo(os);
            Log.d(TAG, "FUCK5: " + System.currentTimeMillis() / 1000);
            os.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            // close the document
            document.close();
        }
        Log.d(TAG, "FUCK6: " + System.currentTimeMillis() / 1000);
    }
}
