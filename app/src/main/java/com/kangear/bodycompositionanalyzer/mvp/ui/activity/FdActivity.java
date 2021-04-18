package com.kangear.bodycompositionanalyzer.mvp.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.aip.face.AipFace;
import com.google.gson.Gson;
import com.kangear.bodycompositionanalyzer.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.samples.facedetect.DetectionBasedTracker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import cn.xsshome.taip.face.TAipFace;
import cn.xsshome.taip.util.Base64Util;
import cn.xsshome.taip.util.FileUtil;

import static com.kangear.bodycompositionanalyzer.WelcomeActivity.CONST_FINGER_ID;

public class FdActivity extends Activity implements CvCameraViewListener2 {

    public static final String ZHUCE = "zhuce.....";

    private static final String    TAG                 = "OCVSample::Activity";
    private static final Scalar    FACE_RECT_COLOR     = new Scalar(0, 255, 0, 255);
    public static final int        JAVA_DETECTOR       = 0;
    public static final int        NATIVE_DETECTOR     = 1;

    private MenuItem               mItemFace50;
    private MenuItem               mItemFace40;
    private MenuItem               mItemFace30;
    private MenuItem               mItemFace20;
    private MenuItem               mItemType;

    private Mat                    mRgba;
    private Mat                    mGray;
    private File                   mCascadeFile;
    private CascadeClassifier      mJavaDetector;
    private DetectionBasedTracker mNativeDetector;

    private int                    mDetectorType       = JAVA_DETECTOR;
    private String[]               mDetectorName;

    private float                  mRelativeFaceSize   = 0.2f;
    private int                    mAbsoluteFaceSize   = 0;

    private CameraBridgeViewBase   mOpenCvCameraView;

    private final String FACEID_GROUP_ID = "group20180513";
    private static final String PATH = "/sdcard/fff.jpg";

    //设置APPID/AK/SK
    public static final String APP_ID = "15948914";
    public static final String API_KEY = "1LqlAMRRDIoChzN7aQZtQoBR";
    public static final String SECRET_KEY = "I2T44Wq6lV8fZdAeNmgyNDGeRwsGGtGG";

    private BaseLoaderCallback  mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");

                    // Load native library after(!) OpenCV initialization
                    System.loadLibrary("opencv_java3");
                    System.loadLibrary("detection_based_tracker");

                    try {
                        // load cascade file from application resources
                        InputStream is = getResources().openRawResource(R.raw.lbpcascade_frontalface);
                        File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
                        mCascadeFile = new File(cascadeDir, "lbpcascade_frontalface.xml");
                        FileOutputStream os = new FileOutputStream(mCascadeFile);

                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = is.read(buffer)) != -1) {
                            os.write(buffer, 0, bytesRead);
                        }
                        is.close();
                        os.close();

                        mJavaDetector = new CascadeClassifier(mCascadeFile.getAbsolutePath());
                        if (mJavaDetector.empty()) {
                            Log.e(TAG, "Failed to load cascade classifier");
                            mJavaDetector = null;
                        } else
                            Log.i(TAG, "Loaded cascade classifier from " + mCascadeFile.getAbsolutePath());

                        mNativeDetector = new DetectionBasedTracker(mCascadeFile.getAbsolutePath(), 0);

                        cascadeDir.delete();

                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e(TAG, "Failed to load cascade. Exception thrown: " + e);
                    }

                    mOpenCvCameraView.enableView();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };
    private boolean zhuce = false;

    public FdActivity() {
        mDetectorName = new String[2];
        mDetectorName[JAVA_DETECTOR] = "Java";
        mDetectorName[NATIVE_DETECTOR] = "Native (tracking)";

        Log.i(TAG, "Instantiated new " + this.getClass());
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Intent intent = getIntent();
        zhuce = intent.getBooleanExtra(ZHUCE, false);

        setContentView(R.layout.face_detect_surface_view);

        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.fd_activity_surface_view);
        mOpenCvCameraView.setVisibility(CameraBridgeViewBase.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        mOpenCvCameraView.disableView();
    }

    public void onCameraViewStarted(int width, int height) {
        mGray = new Mat();
        mRgba = new Mat();
    }

    public void onCameraViewStopped() {
        mGray.release();
        mRgba.release();
    }

    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {

        mRgba = inputFrame.rgba();
        mGray = inputFrame.gray();

        if (mAbsoluteFaceSize == 0) {
            int height = mGray.rows();
            if (Math.round(height * mRelativeFaceSize) > 0) {
                mAbsoluteFaceSize = Math.round(height * mRelativeFaceSize);
            }
            mNativeDetector.setMinFaceSize(mAbsoluteFaceSize);
        }

        MatOfRect faces = new MatOfRect();

        if (mDetectorType == JAVA_DETECTOR) {
            if (mJavaDetector != null)
                mJavaDetector.detectMultiScale(mGray, faces, 1.1, 2, 2, // TODO: objdetect.CV_HAAR_SCALE_IMAGE
                        new Size(mAbsoluteFaceSize, mAbsoluteFaceSize), new Size());
        }
        else if (mDetectorType == NATIVE_DETECTOR) {
            if (mNativeDetector != null)
                mNativeDetector.detect(mGray, faces);
        }
        else {
            Log.e(TAG, "Detection method is not selected!");
        }

        Rect[] facesArray = faces.toArray();
        for (int i = 0; i < facesArray.length; i++)
            Imgproc.rectangle(mRgba, facesArray[i].tl(), facesArray[i].br(), FACE_RECT_COLOR, 3);


        handleFace(facesArray, inputFrame);
        return mRgba;
    }

    int state = 0;
    // 初始化一个TAipPtu
    TAipFace aipFace = new TAipFace("2114617797", "nyFobjtIqgIH1MXW");
    // 初始化一个AipFace
    AipFace client = new AipFace(APP_ID, API_KEY, SECRET_KEY);

    void handleFace(Rect[] facesArray, CvCameraViewFrame inputFrame) {
        if (this.state != 1) {
            int faceNumber = facesArray.length;
            if (faceNumber == 1) {
                this.state = 1;
                Mat mRgb = new Mat();
                Imgproc.cvtColor(inputFrame.rgba(), mRgb, 3);
                Imgcodecs.imwrite("/sdcard/fff.jpg", mRgb);
                Log.i("xxx", "照片已存储！！！");
                new Thread(new Runnable() {
                    public void run() {
                        netWork1();
                    }
                }).start();
            }
        }
    }

    public FdActivity.baiduSearchEntity baiduSearch(AipFace client, String personId, String imgPath) throws IOException, JSONException {
        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("max_face_num", "1");
        options.put("match_threshold", "70");
        options.put("quality_control", "NORMAL");
        options.put("liveness_control", "LOW");
        options.put("user_id", personId);
        options.put("max_user_num", "1");

        byte[] image = FileUtil.readFileByBytes(imgPath);
        String base64Content = Base64Util.encode(image);
        String imageType = "BASE64";
        String groupIdList = FACEID_GROUP_ID;

        // 人脸搜索
        JSONObject res = client.search(base64Content, imageType, groupIdList, options);
        Gson gson = new Gson();
        FdActivity.baiduSearchEntity ret = gson.fromJson(res.toString(), FdActivity.baiduSearchEntity.class);
        System.out.println(res.toString(2));
        return ret;
    }

    final static class BaiduMultiSearch {
        /**
         * error_code : 0
         * error_msg : SUCCESS
         * log_id : 240483475
         * timestamp : 1535533440
         * cached : 0
         * result : {"face_num":2,"face_list":[{"face_token":"6fe19a6ee0c4233db9b5bba4dc2b9233","location":{"left":31.95568085,"top":120.3764267,"width":87,"height":85,"rotation":-5},"user_list":[{"group_id":"group1","user_id":"5abd24fd062e49bfa906b257ec40d284","user_info":"userinfo1","score":69.85684967041},{"group_id":"group1","user_id":"2abf89cffb31473a9948268fde9e1c3f","user_info":"userinfo2","score":66.586112976074}]},{"face_token":"fde61e9c074f48cf2bbb319e42634f41","location":{"left":219.4467773,"top":104.7486954,"width":81,"height":77,"rotation":3},"user_list":[{"group_id":"group1","user_id":"088717532b094c3990755e91250adf7d","user_info":"userinfo","score":65.154159545898}]}]}
         */

        private int error_code;
        private String error_msg;
        private long log_id;
        private int timestamp;
        private int cached;
        private ResultBean result;

        public int getError_code() {
            return error_code;
        }

        public void setError_code(int error_code) {
            this.error_code = error_code;
        }

        public String getError_msg() {
            return error_msg;
        }

        public void setError_msg(String error_msg) {
            this.error_msg = error_msg;
        }

        public long getLog_id() {
            return log_id;
        }

        public void setLog_id(long log_id) {
            this.log_id = log_id;
        }

        public int getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(int timestamp) {
            this.timestamp = timestamp;
        }

        public int getCached() {
            return cached;
        }

        public void setCached(int cached) {
            this.cached = cached;
        }

        public ResultBean getResult() {
            return result;
        }

        public void setResult(ResultBean result) {
            this.result = result;
        }

        public static class ResultBean {
            /**
             * face_num : 2
             * face_list : [{"face_token":"6fe19a6ee0c4233db9b5bba4dc2b9233","location":{"left":31.95568085,"top":120.3764267,"width":87,"height":85,"rotation":-5},"user_list":[{"group_id":"group1","user_id":"5abd24fd062e49bfa906b257ec40d284","user_info":"userinfo1","score":69.85684967041},{"group_id":"group1","user_id":"2abf89cffb31473a9948268fde9e1c3f","user_info":"userinfo2","score":66.586112976074}]},{"face_token":"fde61e9c074f48cf2bbb319e42634f41","location":{"left":219.4467773,"top":104.7486954,"width":81,"height":77,"rotation":3},"user_list":[{"group_id":"group1","user_id":"088717532b094c3990755e91250adf7d","user_info":"userinfo","score":65.154159545898}]}]
             */

            private int face_num;
            private List<FaceListBean> face_list;

            public int getFace_num() {
                return face_num;
            }

            public void setFace_num(int face_num) {
                this.face_num = face_num;
            }

            public List<FaceListBean> getFace_list() {
                return face_list;
            }

            public void setFace_list(List<FaceListBean> face_list) {
                this.face_list = face_list;
            }

            public static class FaceListBean {
                /**
                 * face_token : 6fe19a6ee0c4233db9b5bba4dc2b9233
                 * location : {"left":31.95568085,"top":120.3764267,"width":87,"height":85,"rotation":-5}
                 * user_list : [{"group_id":"group1","user_id":"5abd24fd062e49bfa906b257ec40d284","user_info":"userinfo1","score":69.85684967041},{"group_id":"group1","user_id":"2abf89cffb31473a9948268fde9e1c3f","user_info":"userinfo2","score":66.586112976074}]
                 */

                private String face_token;
                private LocationBean location;
                private List<UserListBean> user_list;

                public String getFace_token() {
                    return face_token;
                }

                public void setFace_token(String face_token) {
                    this.face_token = face_token;
                }

                public LocationBean getLocation() {
                    return location;
                }

                public void setLocation(LocationBean location) {
                    this.location = location;
                }

                public List<UserListBean> getUser_list() {
                    return user_list;
                }

                public void setUser_list(List<UserListBean> user_list) {
                    this.user_list = user_list;
                }

                public static class LocationBean {
                    /**
                     * left : 31.95568085
                     * top : 120.3764267
                     * width : 87
                     * height : 85
                     * rotation : -5
                     */

                    private double left;
                    private double top;
                    private int width;
                    private int height;
                    private int rotation;

                    public double getLeft() {
                        return left;
                    }

                    public void setLeft(double left) {
                        this.left = left;
                    }

                    public double getTop() {
                        return top;
                    }

                    public void setTop(double top) {
                        this.top = top;
                    }

                    public int getWidth() {
                        return width;
                    }

                    public void setWidth(int width) {
                        this.width = width;
                    }

                    public int getHeight() {
                        return height;
                    }

                    public void setHeight(int height) {
                        this.height = height;
                    }

                    public int getRotation() {
                        return rotation;
                    }

                    public void setRotation(int rotation) {
                        this.rotation = rotation;
                    }
                }

                public static class UserListBean {
                    /**
                     * group_id : group1
                     * user_id : 5abd24fd062e49bfa906b257ec40d284
                     * user_info : userinfo1
                     * score : 69.85684967041
                     */

                    private String group_id;
                    private String user_id;
                    private String user_info;
                    private double score;

                    public String getGroup_id() {
                        return group_id;
                    }

                    public void setGroup_id(String group_id) {
                        this.group_id = group_id;
                    }

                    public String getUser_id() {
                        return user_id;
                    }

                    public void setUser_id(String user_id) {
                        this.user_id = user_id;
                    }

                    public String getUser_info() {
                        return user_info;
                    }

                    public void setUser_info(String user_info) {
                        this.user_info = user_info;
                    }

                    public double getScore() {
                        return score;
                    }

                    public void setScore(double score) {
                        this.score = score;
                    }
                }
            }
        }
    }

    public FdActivity.BaiduMultiSearch baiduMultiSearch(AipFace client, String imgPath) throws IOException, JSONException {
        Log.e(TAG, "baiduMultiSearch start");
        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("max_face_num", "1");
        options.put("match_threshold", "80");
        options.put("quality_control", "NORMAL");
        options.put("liveness_control", "LOW");
        options.put("max_user_num", "1");

        byte[] image = FileUtil.readFileByBytes(imgPath);
        String base64Content = Base64Util.encode(image);
        String imageType = "BASE64";
        String groupIdList = FACEID_GROUP_ID;

        // 人脸搜索
        JSONObject res = client.multiSearch(base64Content, imageType, groupIdList, options);
        Gson gson = new Gson();
        Log.e(TAG, " result: " + res.toString());
        Log.e(TAG, "baiduMultiSearch start 2");
        FdActivity.BaiduMultiSearch ret = gson.fromJson(res.toString(), FdActivity.BaiduMultiSearch.class);
        Log.e(TAG, "baiduMultiSearch end");
        return ret;
    }

    public boolean baiduAddUser(AipFace client, String personId, String imgPath) throws IOException, JSONException {
        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("user_info", "user's info");
        options.put("quality_control", "NORMAL");
        options.put("liveness_control", "LOW");
        options.put("action_type", "REPLACE");

        byte[] image = FileUtil.readFileByBytes(imgPath);
        String base64Content = Base64Util.encode(image);
        JSONObject res = client.addUser(base64Content, "BASE64", FACEID_GROUP_ID, personId, options);
        Log.e(TAG, "result1: " + res.toString(2));
        return res.getInt("error_code") == 0;
    }

    public void baiduDelUser(AipFace client, String personId) throws JSONException {
        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();

        String userId = personId;
        String groupId = FACEID_GROUP_ID;

        // 删除用户
        JSONObject res = client.deleteUser(userId, groupId, options);
        System.out.println(res.toString(2));

    }

    /**
     * sech or add new one
     */
    private void netWork2() {
        File file = new File(PATH);
        Gson gson = new Gson();
        if (file.exists()) {
            try {

                String result;
                // zhuce
                if (zhuce) {
                    String personId = String.valueOf(System.currentTimeMillis() / 1000);
                    result = aipFace.faceNewperson(PATH, FACEID_GROUP_ID, personId, String.valueOf(System.currentTimeMillis()));//个体创建
                    NewPerson newPerson = gson.fromJson(result, NewPerson.class);
                    final String perceId = newPerson.getData().getPerson_id();
                    result = aipFace.faceIdentify(PATH, FACEID_GROUP_ID, 1);//人脸识别
                    FaceReg faceReg = gson.fromJson(result, FaceReg.class);
                    List<FaceReg.DataBean.CandidatesBean> candidatesBeans = faceReg.getData().getCandidates();
                    if (candidatesBeans.size() > 0 &&  faceReg.getData().getCandidates().get(0).getPerson_id().equals(perceId)) {
                        Log.e(TAG, "注册成功: " + perceId);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ((ImageView)findViewById(R.id.faceid_imageview)).setImageResource(R.drawable._80_faceid_green);
                                ((TextView) findViewById(R.id.cameraInfo)).setText(R.string.face_id_ok);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent();
                                        intent.putExtra(CONST_FINGER_ID, Integer.valueOf(perceId));
                                        setResult(RESULT_OK, intent);
                                        finish();
                                    }
                                }, 1 * 1000);
                            }
                        });
                    } else {
                        aipFace.faceDelperson(personId);//个体创建
                        state = 0;
                    }
                } else {
                    result = aipFace.faceIdentify(PATH, FACEID_GROUP_ID, 9);//人脸识别
                    FaceReg faceReg = gson.fromJson(result, FaceReg.class);
                    if (faceReg.getRet() == 0) {
                        List<FaceReg.DataBean.CandidatesBean> candidatesBeans = faceReg.getData().getCandidates();
                        if (candidatesBeans.size() > 0 && candidatesBeans.get(0).getConfidence() > 90) {
                            final String perceId = candidatesBeans.get(0).getPerson_id();
                            Log.e(TAG, "识别成功: " + candidatesBeans.get(0).getPerson_id());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ((ImageView) findViewById(R.id.faceid_imageview)).setImageResource(R.drawable._80_faceid_green);
                                    ((TextView) findViewById(R.id.cameraInfo)).setText(R.string.face_id_ok);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent();
                                            intent.putExtra(CONST_FINGER_ID, Integer.valueOf(perceId));
                                            setResult(RESULT_OK, intent);
                                            finish();
                                        }
                                    }, 1 * 1000);
                                }
                            });
                        } else {
                            fail();
                        }
                    } else {
                        fail();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static class baiduSearchEntity {

        /**
         * face_token : fid
         * user_list : [{"group_id":"test1","user_id":"u333333","user_info":"Test User","score":99.3}]
         */

        private String face_token;
        private List<UserListBean> user_list;

        public String getFace_token() {
            return face_token;
        }

        public void setFace_token(String face_token) {
            this.face_token = face_token;
        }

        public List<UserListBean> getUser_list() {
            return user_list;
        }

        public void setUser_list(List<UserListBean> user_list) {
            this.user_list = user_list;
        }

        public static class UserListBean {
            /**
             * group_id : test1
             * user_id : u333333
             * user_info : Test User
             * score : 99.3
             */

            private String group_id;
            private String user_id;
            private String user_info;
            private double score;

            public String getGroup_id() {
                return group_id;
            }

            public void setGroup_id(String group_id) {
                this.group_id = group_id;
            }

            public String getUser_id() {
                return user_id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public String getUser_info() {
                return user_info;
            }

            public void setUser_info(String user_info) {
                this.user_info = user_info;
            }

            public double getScore() {
                return score;
            }

            public void setScore(double score) {
                this.score = score;
            }
        }
    }

    /**
     * sech or add new one
     */
    private void netWork1() {
        File file = new File(PATH);
        Gson gson = new Gson();
        if (file.exists()) {
            try {
                String result;
                // zhuce
                if (zhuce) {
                    String personId = String.valueOf(System.currentTimeMillis() / 1000);
                    boolean ret = baiduAddUser(client, personId, PATH);
                    if (!ret) {
                        state = 0;
                        return;
                    }
                    Log.e(TAG, "result2: baiduAddUser 成功");
                    FdActivity.BaiduMultiSearch bs = baiduMultiSearch(client, PATH);
//                    Log.e(TAG, "result2: " + result);
//                    FaceReg faceReg = gson.fromJson(result, FaceReg.class);
//                    List<FaceReg.DataBean.CandidatesBean> candidatesBeans = faceReg.getData().getCandidates();
                    if (bs.error_code == 0
                            && bs.getResult().face_num > 0
                            && bs.getResult().getFace_list().get(0).getUser_list().size() > 0
                            && bs.getResult().getFace_list().get(0).getUser_list().get(0).getUser_id().equals(personId)) {
                        Log.e(TAG, "注册成功: " + personId);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ((ImageView)findViewById(R.id.faceid_imageview)).setImageResource(R.drawable._80_faceid_green);
                                ((TextView) findViewById(R.id.cameraInfo)).setText(R.string.face_id_ok);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent();
                                        intent.putExtra(CONST_FINGER_ID, Integer.valueOf(personId));
                                        setResult(RESULT_OK, intent);
                                        finish();
                                    }
                                }, 1 * 1000);
                            }
                        });
                    } else {
                        Log.e(TAG, "个体创建: " + personId);
                        //aipFace.faceDelperson(personId);//个体创建
                        baiduDelUser(client, personId);
                        state = 0;
                    }
                } else {
                    FdActivity.BaiduMultiSearch bs = baiduMultiSearch(client, PATH);
//                    Log.e(TAG, "result2: " + result);
//                    FaceReg faceReg = gson.fromJson(result, FaceReg.class);
//                    List<FaceReg.DataBean.CandidatesBean> candidatesBeans = faceReg.getData().getCandidates();
                    if (bs.error_code == 0
                            && bs.getResult().face_num > 0
                            && bs.getResult().getFace_list().get(0).getUser_list().size() > 0
                            && bs.getResult().getFace_list().get(0).getUser_list().get(0).getScore() > 80) {
                        final String personId = bs.getResult().getFace_list().get(0).getUser_list().get(0).getUser_id();
                        Log.e(TAG, "识别成功: " + personId);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ((ImageView) findViewById(R.id.faceid_imageview)).setImageResource(R.drawable._80_faceid_green);
                                ((TextView) findViewById(R.id.cameraInfo)).setText(R.string.face_id_ok);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent();
                                        intent.putExtra(CONST_FINGER_ID, Integer.valueOf(personId));
                                        setResult(RESULT_OK, intent);
                                        finish();
                                    }
                                }, 1 * 1000);
                            }
                        });
                    } else {
                        fail();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, e.getLocalizedMessage());
            }
        }
    }

    private void fail() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((TextView) findViewById(R.id.cameraInfo)).setText(R.string.face_id_fail);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        state = 0;
                        ((TextView) findViewById(R.id.cameraInfo)).setText(R.string.face_id_info);
                    }
                }, 1 * 1000);
            }
        });
        Log.e(TAG, "识别失败");

        times--;
        if (times < 0) {
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    private int times = 3;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG, "called onCreateOptionsMenu");
        mItemFace50 = menu.add("Face size 50%");
        mItemFace40 = menu.add("Face size 40%");
        mItemFace30 = menu.add("Face size 30%");
        mItemFace20 = menu.add("Face size 20%");
        mItemType   = menu.add(mDetectorName[mDetectorType]);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "called onOptionsItemSelected; selected item: " + item);
        if (item == mItemFace50)
            setMinFaceSize(0.5f);
        else if (item == mItemFace40)
            setMinFaceSize(0.4f);
        else if (item == mItemFace30)
            setMinFaceSize(0.3f);
        else if (item == mItemFace20)
            setMinFaceSize(0.2f);
        else if (item == mItemType) {
            int tmpDetectorType = (mDetectorType + 1) % mDetectorName.length;
            item.setTitle(mDetectorName[tmpDetectorType]);
            setDetectorType(tmpDetectorType);
        }
        return true;
    }

    private void setMinFaceSize(float faceSize) {
        mRelativeFaceSize = faceSize;
        mAbsoluteFaceSize = 0;
    }

    private void setDetectorType(int type) {
        if (mDetectorType != type) {
            mDetectorType = type;

            if (type == NATIVE_DETECTOR) {
                Log.i(TAG, "Detection Based Tracker enabled");
                mNativeDetector.start();
            } else {
                Log.i(TAG, "Cascade detector enabled");
                mNativeDetector.stop();
            }
        }
    }
}