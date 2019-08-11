package com.kangear.bodycompositionanalyzer.application;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.kangear.bodycompositionanalyzer.BuildConfig;
import com.kangear.bodycompositionanalyzer.Record;
import com.kangear.bodycompositionanalyzer.mvp.ui.activity.UploadDataActivity;
import com.kangear.qr.PrinterIntence;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.crashreport.CrashReport;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class App extends Application {
    public static final String SERIAL_QR_SCAN = "/dev/ttyS3";
    public static final String XIAOPIAO_PRINTER = "/dev/ttyS2";
    private static final String TAG = "Application";
    private static boolean isInit = false;
    private static Record mCurRecord;
    private static String mSN;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate");
        Beta.autoCheckUpgrade = false;
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(getApplicationContext());
        strategy.setAppReportDelay(20000);   //改为20s
        Bugly.init(getApplicationContext(), BuildConfig.BUGLY_APPID, true, strategy);

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
