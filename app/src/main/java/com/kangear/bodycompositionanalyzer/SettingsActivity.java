package com.kangear.bodycompositionanalyzer;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.gson.Gson;
import com.kangear.bodycompositionanalyzer.application.App;
import com.kangear.bodycompositionanalyzer.entry.PgyEntity;
import com.kangear.bodycompositionanalyzer.mvp.ui.activity.AboutActivity;
import com.kangear.qr.PrinterIntence;
import com.kangear.utils.QRCodeUtil;
import com.pgyer.pgyersdk.PgyerSDKManager;
import com.pgyer.pgyersdk.callback.CheckoutVersionCallBack;
import com.pgyer.pgyersdk.model.CheckSoftModel;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.crashreport.CrashReport;
//import com.xuexiang.xupdate.XUpdate;
//import com.xuexiang.xupdate.entity.UpdateEntity;
//import com.xuexiang.xupdate.listener.IUpdateParseCallback;
//import com.xuexiang.xupdate.proxy.IUpdateParser;

import org.json.JSONObject;

import java.io.File;
import java.nio.ByteBuffer;

import static com.kangear.bodycompositionanalyzer.BodyComposition.腰臀比;
import static com.kangear.bodycompositionanalyzer.BodyComposition.评分;
import static com.kangear.bodycompositionanalyzer.BodyComposition.身体年龄;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.CONST_FINGER_ID;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.DEFAULT_WEIGHT;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.FORMAT_WEIGHT;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.INVALID_FINGER_ID;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.REQUEST_CODE_TOUCHID;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.checkRadio;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.doVipTest;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.hideSystemUI;
import static com.kangear.bodycompositionanalyzer.application.App.startUploadData;
import static com.kangear.common.utils.ByteArrayUtils.bytesToHex;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SettingsActivity extends BaseActivity {
    private static final String TAG = "SettingsActivity";
    private AudioManager mAudioManager;
    private TextView mVolumeTextView;
    private int mMaxVolume;
    private Button mVolumeAdd;
    private Button mVolumeSub;
    private Context mContext;
    private TextView mRadioTextView;
    private EditText mRadioEditText;
    private Button mCalibrateRadioButton;
    private TextView mWeightTextView;
    private WifiManager mWifiManager;
    private ToggleButton mWiFiToggleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mWifiManager = (WifiManager) getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);

        mWiFiToggleButton = findViewById(R.id.wifi_ctrl_button);
        mWiFiToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                mWifiManager.setWifiEnabled(isChecked);
            }
        });

        mWiFiToggleButton.setChecked(mWifiManager.isWifiEnabled());

        mContext = this;
        mVolumeTextView = findViewById(R.id.volume_textview);
        mVolumeAdd = findViewById(R.id.volume_add);
        mVolumeSub = findViewById(R.id.volume_jian);
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        mRadioTextView = findViewById(R.id.radio_textview);
        mRadioEditText = findViewById(R.id.radio_edittext);
        mCalibrateRadioButton = findViewById(R.id.calibrate_radio_button);
        mWeightTextView = findViewById(R.id.weight_textview);
        mRadioTextView.setText("");
        mRadioEditText.setText("");
        mWeightTextView.setText("");
        mCalibrateRadioButton.setEnabled(false);
        mRadioEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    int val = Integer.parseInt(editable.toString());
                    if(WelcomeActivity.checkRadio(val)) {
                        mRadioEditText.setTextColor(Color.WHITE);
                        mCalibrateRadioButton.setEnabled(true);
                    } else {
                        mRadioEditText.setTextColor(Color.RED);
                        mCalibrateRadioButton.setEnabled(false);
                    }
                } catch (NumberFormatException ex) {
                    // Do something
                    mRadioEditText.setTextColor(Color.RED);
                    mCalibrateRadioButton.setEnabled(false);
                }

                WatchDog.getInstance(getApplicationContext()).feed();
            }
        });
    }

    private Bitmap createQr() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("sn", App.getSn());
            obj.put("action", "add_admin");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return QRCodeUtil.createQRCodeBitmap(obj.toString(), 450, 450);
    }

    public void onClick(View v) {
        Log.i(TAG, "onClick");
        switch (v.getId()) {
            case R.id.about_button:
                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.binding_admin:
                if (BuildConfig.FLAVOR_model.contains("jh303plus") || BuildConfig.FLAVOR_sub.contains("edu")) {
                    Toast.makeText(this, "暂未开通此服务", Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    ImageView image = new ImageView(this);
                    image.setImageBitmap(createQr());

                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(this).
                                    setMessage("微信小程序「体测318」扫此码添加数据即可添加成本设备管理员").
                                    setView(image);
                    builder.create().show();
                }
                break;
            case R.id.prev_page_button:
                findViewById(R.id.prev_pageview).setVisibility(View.VISIBLE);
                findViewById(R.id.next_pageview).setVisibility(View.INVISIBLE);
                break;
            case R.id.next_page_button:
                findViewById(R.id.prev_pageview).setVisibility(View.INVISIBLE);
                findViewById(R.id.next_pageview).setVisibility(View.VISIBLE);
                break;
            case R.id.upload_data:
                if (BuildConfig.FLAVOR_model.equals("jh303plus")) {
                    Toast.makeText(this, "暂未开通此服务", Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    startUploadData(this);
                }
                break;
            case R.id.read_radio_button:
                try {
                    Protocol.Radio radio = UartBca.getInstance(mContext).readTichengfen();
                    if (radio != null)
                        mRadioTextView.setText(String.valueOf(radio.getWeigthRatio()));
                    else
                        Toast.makeText(this, "读取系数失败", Toast.LENGTH_SHORT).show();
                } catch (Protocol.ProtocalExcption protocalExcption) {
                    protocalExcption.printStackTrace();
                    Toast.makeText(this, "读取系数失败", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.back2_button:
            case R.id.back_button:
                finish();
                break;
            case R.id.time_setting_button:
                startActivity(new Intent(this, TimeActivity.class));
                break;
            case R.id.volume_add:
                mAudioManager.setStreamVolume(
                        AudioManager.STREAM_MUSIC, mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC) + 1,
                        AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                updateVolume();
                break;
            case R.id.volume_jian:
                mAudioManager.setStreamVolume(
                        AudioManager.STREAM_MUSIC, mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC) - 1,
                        AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                updateVolume();
                break;
            case R.id.ad_text_setting:
                startActivity(new Intent(this, AdDialogActivity.class));
                break;
            case R.id.test_button:
                WelcomeActivity.startWeightTest(this, mWeightTextView, null);
                break;
            case R.id.calibrate_radio_button:
                try {
                    Protocol.Radio radio = new Protocol.Radio(Integer.valueOf(mRadioEditText.getText().toString()), 0x00);
                    UartBca.getInstance(mContext).writeTichengfen(radio);
                    Toast.makeText(this, "写入系数成功", Toast.LENGTH_SHORT).show();
                } catch (Protocol.ProtocalExcption protocalExcption) {
                    protocalExcption.printStackTrace();
                    Toast.makeText(this, "写入系数失败", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.update_apk_button:
//                Toast.makeText(this, "系统升级", Toast.LENGTH_SHORT).show();
//                if (BuildConfig.DEBUG)
//                    CrashReport.testJavaCrash();
//                Beta.checkUpgrade();
//                PgyerSDKManager.
                PgyerSDKManager.checkSoftwareUpdate(new CheckoutVersionCallBack() {
                    @Override
                    public void onSuccess(CheckSoftModel checkSoftModel) {
                        Log.e(TAG, "checkSoftModel: " + checkSoftModel);
                    }

                    @Override
                    public void onFail(String s) {
                    }
                });

//                String mUpdateUrl3 = "https://www.pgyer.com/apiv2/app/check?_api_key=7b0205ac1ee5a2d600f3b2205092c9ee&token=4fcc4ad01edc93c1a2d4dd8205eb8ea0&buildVersion=" + BuildConfig.VERSION_CODE;
//                XUpdate.newBuild(this)
//                        .updateUrl(mUpdateUrl3)
//                        .updateParser(new CustomUpdateParser()) //设置自定义的版本更新解析器
//                        .update();
                break;
            case R.id.wifi_button:
//                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
//                Intent intent = new Intent();
//                ComponentName comp = new ComponentName("com.farproc.wifi.connecter","com.farproc.wifi.connecter.TestWifiScan");
//                intent.setComponent(comp);
//                intent.setAction("android.intent.action.MAIN");
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(new Intent(this, WiFiActivity.class));
                break;
            case R.id.qingkong_finger:
                Toast.makeText(this, "清空指纹", Toast.LENGTH_SHORT).show();
                TouchID.getInstance(mContext).clearAll();
                PersonBean.getInstance(this).clearAll();
                break;
            case R.id.clear_record:
                Toast.makeText(this, "删除所有记录", Toast.LENGTH_SHORT).show();
                RecordBean.getInstance(this).clearAll();
                break;
            case R.id.report_url:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("上传服务器地址");
                Other o2 = OtherBean.getInstance(this).queryByName(Other.REPORT_URL);
                String url = o2 == null ? "" : o2.getStrValue();
                // Set up the input
                final EditText input = new EditText(this);
                input.setText(url);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_URI);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("保存", (dialog, which) -> {
                    String url2 = input.getText().toString();
                    OtherBean.getInstance(this).insert(new Other(Other.REPORT_URL, url2));
                });
                builder.setNegativeButton("取消", (dialog, which) -> dialog.cancel());

                builder.show();
                break;

        }
    }

//    public static class CustomUpdateParser implements IUpdateParser {
//        @Override
//        public UpdateEntity parseJson(String json) throws Exception {
//            Gson gson = new Gson();
//            PgyEntity pgy = gson.fromJson(json, PgyEntity.class);
//            if (pgy != null && pgy.getData() != null) {
//                return new UpdateEntity()
//                        .setHasUpdate(pgy.getData().isBuildHaveNewVersion())
//                        .setVersionCode(Integer.parseInt(pgy.getData().getBuildVersionNo()))
//                        .setVersionName(pgy.getData().getBuildName())
//                        .setUpdateContent(pgy.getData().getBuildUpdateDescription())
//                        .setDownloadUrl(pgy.getData().getDownloadURL())
//                        .setSize(Integer.parseInt(pgy.getData().getBuildFileSize()));
//            }
//            return null;
//        }
//
//        @Override
//        public void parseJson(String json, IUpdateParseCallback callback) throws Exception {
//
//        }
//
//        @Override
//        public boolean isAsyncParser() {
//            return false;
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case REQUEST_CODE_TOUCHID:
                if (resultCode == RESULT_OK) {
                    int fingerId = intent.getIntExtra(CONST_FINGER_ID, INVALID_FINGER_ID);
                    Person p = PersonBean.getInstance(this).queryByFingerId(fingerId);
                    if (p != null) {
                        WelcomeActivity.getRecord().setPerson(p);
                        doVipTest(this);
                        finish();
                    } else {
                        Log.i(TAG, "指纹识别异常");
                    }
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideSystemUI(getWindow().getDecorView());
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        updateVolume();
    }

    void updateVolume() {
        if (mAudioManager != null) {
            boolean add, sub;
            mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            int cur = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            int percent;
            if (cur == mMaxVolume) {
                percent = 100;
                // 禁用add
                add = false;
                sub = true;
            } else if (cur == 0) {
                percent = 0;
                // 禁用sub
                add = true;
                sub = false;
            } else {
                percent = cur * 100 / mMaxVolume;
                add = true;
                sub = true;
            }
            mVolumeAdd.setEnabled(add);
            mVolumeSub.setEnabled(sub);
            Log.i(TAG, "max: " + mMaxVolume + " cur: " + cur);
            mVolumeTextView.setText(String.valueOf(percent));
        }
    }
}
