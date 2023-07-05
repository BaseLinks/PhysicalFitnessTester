package com.kangear.bodycompositionanalyzer.application;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.kangear.bodycompositionanalyzer.BuildConfig;
import com.kangear.bodycompositionanalyzer.Record;
import com.kangear.bodycompositionanalyzer.mvp.ui.activity.UploadDataActivity;
import com.kangear.qr.PrinterIntence;
//import com.kangear.utils.OKHttpUpdateHttpService;
import com.pgyer.pgyersdk.PgyerSDKManager;
import com.pgyersdk.crash.PgyCrashManager;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.crashreport.CrashReport;
//import com.xuexiang.xupdate.XUpdate;
//import com.xuexiang.xupdate.entity.UpdateError;
//import com.xuexiang.xupdate.listener.OnUpdateFailureListener;
//import com.xuexiang.xupdate.utils.UpdateUtils;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import cn.trinea.android.common.util.ToastUtils;

//import static com.xuexiang.xupdate.entity.UpdateError.ERROR.CHECK_NO_NEW_VERSION;

public class App extends Application {
    public static final String SERIAL_QR_SCAN = "/dev/ttyS3";
    public static final String XIAOPIAO_PRINTER = "/dev/ttyS3"; //ttyS3
    private static final String TAG = "Application";
    private static boolean isInit = false;
    private static Record mCurRecord;
    private static String mSN;
    /**
     *  初始化蒲公英SDK
     * @param application
     */
    private static void initPgyerSDK(App application){
//        new PgyerSDKManager.Init()
//                .setContext(application) //设置上下问对象
//                .start();
        PgyCrashManager.register();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate");
        Beta.autoCheckUpgrade = false;
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(getApplicationContext());
        strategy.setAppReportDelay(20000);   //改为20s
        Bugly.init(getApplicationContext(), BuildConfig.BUGLY_APPID, true, strategy);
        //在attachBaseContext方法中调用初始化sdk
        initPgyerSDK(this);

//        XUpdate.get()
//                .debug(true)
//                .isWifiOnly(true)                                               //默认设置只在wifi下检查版本更新
//                .isGet(true)                                                    //默认设置使用get请求检查版本
//                .isAutoMode(false)                                              //默认设置非自动模式，可根据具体使用配置
//                .param("versionCode", UpdateUtils.getVersionCode(this))         //设置默认公共请求参数
//                .param("appKey", getPackageName())
//                .setOnUpdateFailureListener(new OnUpdateFailureListener() {     //设置版本更新出错的监听
//                    @Override
//                    public void onFailure(UpdateError error) {
//                        if (error.getCode() != CHECK_NO_NEW_VERSION) {          //对不同错误进行处理
////                            ToastUtils.show(this, error.toString());
//                        }
//                    }
//                })
//                .supportSilentInstall(true)                                     //设置是否支持静默安装，默认是true
//                .setIUpdateHttpService(new OKHttpUpdateHttpService())           //这个必须设置！实现网络请求功能。
//                .init(this);                                                    //这个必须初始化
//


        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
                .setDbName("test2.db")
                // 不设置dbDir时, 默认存储在app的私有目录.
                .setDbDir(getFilesDir()) // "sdcard"的写法并非最佳实践, 这里为了简单, 先这样写了.
                .setDbVersion(4)
                .setDbOpenListener(db -> {
                    // 开启WAL, 对写入加速提升巨大
                    db.getDatabase().enableWriteAheadLogging();
                })
                .setDbUpgradeListener((db, oldVersion, newVersion) -> {
                    // TODO: ...
                    switch (oldVersion) {
                        case 2:
                            switch (newVersion) {
                                case 4:
                                    try {
                                        db.addColumn(Record.class, "img");
                                    } catch (DbException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                            }
                            break;
                    }
                    // db.addColumn(...);
                    // db.dropTable(...);
                    // ...
                    // or
                    // db.dropDb();
                });
        mDb = x.getDb(daoConfig);

        mSN = id2();
    }

    public static String getSn() {
        return mSN;
    }

    private static DbManager mDb;

    public static DbManager getDB() {
        return mDb;
    }

    public static Record getRecord() {
        if (mCurRecord == null) {
            mCurRecord = new Record();
        }

        return mCurRecord;
    }

    /**
     * 确保为32位
     * @return
     */
    public String id2() {
        String id = "123456";
        /* 如果使能调试设备ID，那么直接返回调试设备ID */
        try {
            FileReader fileReader;
            fileReader = new FileReader(new File("/proc/cpuinfo"));
            BufferedReader br = new BufferedReader(fileReader);
            String line = null;
            // if no more lines the readLine() returns null
            while ((line = br.readLine()) != null) {
                // reading lines until the end of the file
                if (line.indexOf("Serial") >= 0) {
                    if (line.split(":").length == 2) {
                        String tmp = line.split(":")[1].trim();
                        id = tmp;
                    }
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            try {
                FileReader fileReader;
                fileReader = new FileReader(new File(
                        "/sys/class/net/eth0/address"));
                BufferedReader br = new BufferedReader(fileReader);
                String line = br.readLine();
                line = line.replace(":", "");
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < (32 - line.length()); i++)
                    sb.append("0");
                sb.append(line);
                id = sb.toString();
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return id;
    }

    /**
     * 2. AGE
     * @param context
     */
    public static void startUploadData(Context context) {
        context.startActivity(new Intent(context, UploadDataActivity.class));
    }

    public static boolean isInit() {
        return isInit;
    }

    public static void setInit(boolean init) {
        isInit = init;
    }
}
