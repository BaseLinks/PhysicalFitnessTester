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
    public static final String CONST_FINGER_ID = "CONST_FINGER_ID";
    public static final String CONST_PERSON = "PERSON";
    public static final String CONST_WEIGHT = "WEIGHT";
    public static final String CONST_ID     = "ID";
    public static final String CONST_HEIGHT = "HEIGHT";
    public static final String CONST_AGE    = "AGE";
    private static final int REQUEST_CODE_VIP_REGISTE = 1;
    private static final int REQUEST_CODE_VIP_TEST    = 2;
    private static final int REQUEST_CODE_NEW_TEST    = 3;
    private static final int REQUEST_CODE_ID          = 4;
    private static final int REQUEST_CODE_AGE         = 5;
    private static final int REQUEST_CODE_HEIGHT      = 6;
    private static final int REQUEST_CODE_TOUCHID     = 7;
    private static final String TAG = "WelcomeActivity";
    private TimeUtils mTimeUtils;
    private List<Person> mPersons = new ArrayList<>();
    private static Person mCurPersion;
    public static final int WEIGHT_INVALIDE          = -1;
    public static final int WEIGHT_NEW_TEST          = 1;
    public static final int WEIGHT_VIP_TEST          = 2;
    public static final String CONST_WEIGHT_TAG      = "CONST_WEIGHT_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        hideSystemUI(getWindow().getDecorView());
        mTimeUtils = new TimeUtils((TextView) findViewById(R.id.time_textview),
                (TextView)findViewById(R.id.date_textview));

        // 启动指纹
        TouchID.getInstance(this.getApplicationContext());
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
                Person p = new Person();
                p.setFingerId(0);
                p.setId("中国");
                intent = new Intent(this, MemRegActivity.class);
                intent.putExtra(CONST_PERSON, p.toJson());
                startActivityForResult(intent, REQUEST_CODE_VIP_REGISTE);
                break;
            case R.id.vip_test_imageview:
                startVipTest(this);
                break;
            case R.id.start_new_test_imageview:
                startTmpTest(this);
                break;
            case R.id.history_imageview:
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
        startTouchId(context);
    }

    /**
     * 1. ID
     * @param context
     */
    private static void startId(Context context) {
        context.startActivity(new Intent(context, IDActivity.class));
    }

    /**
     * 1. AGE
     * @param context
     */
    public static void startAge(Context context) {
        context.startActivity(new Intent(context, AgeActivity.class));
    }

    /**
     * 1. HEIGHT
     * @param context
     */
    public static void startHeight(Context context) {
        context.startActivity(new Intent(context, HeightActivity.class));
    }

    /**
     * 1. HEIGHT
     * @param context
     */
    public static void startTouchId(Context context) {
        context.startActivity(new Intent(context, TouchIdActivity.class));
    }

    /**
     * 1. TEST
     * @param context
     */
    public static void doTest(Context context) {
        context.startActivity(new Intent(context, TestActivity.class));
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
