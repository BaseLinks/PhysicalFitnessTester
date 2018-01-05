package com.kangear.bodycompositionanalyzer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.kangear.common.utils.TimeUtils;

import org.xutils.DbManager;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class WelcomeActivity extends AppCompatActivity {
    public static final int MIN_HEIGHT = 1;
    public static final int MAX_HEIGHT = 250;
    public static final int MIN_AGE = 7;
    public static final int MAX_AGE = 99;
    public static final int INVALID_FINGER_ID = -1;
    public static final int INVALID_RECORD_ID = -1;
    public static final String CONST_FINGER_ID = "CONST_FINGER_ID";
    public static final String CONST_PERSON = "PERSON";
    public static final String CONST_WEIGHT = "WEIGHT";
    public static final String CONST_ID     = "ID";
    public static final String CONST_HEIGHT = "HEIGHT";
    public static final String CONST_AGE    = "AGE";
    public static final String CONST_RECORD_ID = "CONST_RECORD_ID";
    private static final int REQUEST_CODE_VIP_REGISTE = 1;
    private static final int REQUEST_CODE_VIP_TEST    = 2;
    private static final int REQUEST_CODE_NEW_TEST    = 3;
    private static final int REQUEST_CODE_ID          = 4;
    private static final int REQUEST_CODE_AGE         = 5;
    private static final int REQUEST_CODE_HEIGHT      = 6;
    public static final int REQUEST_CODE_TOUCHID      = 7;
    public static final int REQUEST_CODE_DELETE       = 8;
    private static final String TAG = "WelcomeActivity";
    private TimeUtils mTimeUtils;
    private List<Person> mPersons = new ArrayList<>();
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

    private static DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
            .setDbName("test2.db")
            // 不设置dbDir时, 默认存储在app的私有目录.
            .setDbDir(new File("/sdcard")) // "sdcard"的写法并非最佳实践, 这里为了简单, 先这样写了.
            .setDbVersion(2)
            .setDbOpenListener(new DbManager.DbOpenListener() {
                @Override
                public void onDbOpened(DbManager db) {
                    // 开启WAL, 对写入加速提升巨大
                    db.getDatabase().enableWriteAheadLogging();
                }
            })
            .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                @Override
                public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                    // TODO: ...
                    // db.addColumn(...);
                    // db.dropTable(...);
                    // ...
                    // or
                    // db.dropDb();
                }
            });

    private static DbManager mDb;

    public static DbManager getDB() {
        return mDb;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        mTimeUtils = new TimeUtils((TextView) findViewById(R.id.time_textview),
                (TextView)findViewById(R.id.date_textview));

        mDb = x.getDb(daoConfig);
        // 启动指纹
        TouchID.getInstance(this.getApplicationContext());

        // 判断Person数据库表，如果数据库表为空，那么Empty指纹
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

    @Override
    protected void onResume() {
        super.onResume();
        mTimeUtils.start();
        hideSystemUI(getWindow().getDecorView());
    }

    @Override
    protected void onPause() {
        super.onPause();
        mTimeUtils.stop();
    }

    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.vip_register_imageview:
                // TODO：获取一个id以及一个fingerId
                intent = new Intent(this, MemRegActivity.class);
                startActivityForResult(intent, REQUEST_CODE_VIP_REGISTE);
                break;
            case R.id.vip_test_imageview:
                startVipTest(this);
                break;
            case R.id.start_new_test_imageview:
                startTmpTest(this);
                break;
            case R.id.history_imageview:
                startHistory(this);
                break;
            case R.id.settings_imageview:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Log.e(TAG, "onActivityResult requestCode: " + requestCode + " resultCode: " + resultCode);
//        int weight;
//        switch (requestCode) {
//            case REQUEST_CODE_VIP_REGISTE:
//                if (resultCode == RESULT_OK) {
//                    mCurPersion = Person.fromJson(data.getStringExtra(CONST_PERSON));
//                    Log.d(TAG, "onActivityResult: person " + mCurPersion.toString());
//                    // 将Person写入数据库中
//                    mPersons.add(mCurPersion);
//                }
//                break;
//            case REQUEST_CODE_VIP_TEST: // 2.1. WEIGHT
//                if (resultCode == RESULT_OK) {
//                    weight = data.getIntExtra(CONST_WEIGHT, 0);
//                    Log.d(TAG, "onActivityResult: weight " + weight);
//                    mCurPersion = new Person();
//                    mCurPersion.setWeight(weight);
//
//                    Intent intent = new Intent(this, TouchIdActivity.class);
//                    startActivityForResult(intent, REQUEST_CODE_TOUCHID);
//                }
//                break;
//            case REQUEST_CODE_NEW_TEST: { // 1.1 WEIGHT
//                if (resultCode == RESULT_OK) {
//                    weight = data.getIntExtra(CONST_WEIGHT, 0);
//                    Log.d(TAG, "onActivityResult: weight " + weight);
//                    startId(this);
//                }
//                break;
//            }
//        }
    }

    public static Person getPerson() {
        if (mCurPersion == null) {
            mCurPersion = new Person();
        }
        return mCurPersion;
    }

    public static Record getRecord() {
        if (mCurRecord == null) {
            mCurRecord = new Record();
        }

        return mCurRecord;
    }


    private static void startTmpTest(Context context) {
        Intent intent = new Intent(context, WeightActivity.class);
        intent.putExtra(CONST_WEIGHT_TAG, WEIGHT_NEW_TEST);
        context.startActivity(intent);
    }

    private static void startVipTest(Context context) {
        Intent intent = new Intent(context, WeightActivity.class);
        intent.putExtra(CONST_WEIGHT_TAG, WEIGHT_VIP_TEST);
        context.startActivity(intent);
    }

    public static void doTmpTest(Context context) {
        startId(context);
    }

    public static void doVipTest(Context context) {
        doTest(context);
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
    }

    /**
     * 3. Gender
     * @param context
     */
    public static void startGender(Context context) {
        context.startActivity(new Intent(context, GenderActivity.class));
    }

    /**
     * 5. HEIGHT
     * @param context
     */
    public static void startHeight(Context context) {
        context.startActivity(new Intent(context, HeightActivity.class));
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
        actvity.startActivityForResult(new Intent(actvity, TouchIdActivity.class), REQUEST_CODE_TOUCHID);
    }

    /**
     * 11. History
     * @param context
     */
    public static void startHistory(Context context) {
        context.startActivity(new Intent(context, HistoryActivity.class));
    }

    public static void exitAsFail(Activity activity) {
        Intent intent = new Intent(activity, WelcomeActivity.class);
        activity.setResult(RESULT_CANCELED, intent);
        activity.finish();
    }

    public static void unkownError(Activity activity) {
        Toast.makeText(activity, "未知错误，请联系厂家", Toast.LENGTH_SHORT).show();
    }
}
