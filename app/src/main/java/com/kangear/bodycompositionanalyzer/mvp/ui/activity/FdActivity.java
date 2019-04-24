package com.kangear.bodycompositionanalyzer.mvp.ui.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.imgproc.Imgproc;
import org.opencv.samples.facedetect.DetectionBasedTracker;

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

import com.google.gson.Gson;
import com.kangear.bodycompositionanalyzer.R;

import cn.xsshome.taip.face.TAipFace;

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
