package com.kangear.bodycompositionanalyzer.mvp.ui.activity;

import android.app.Fragment;
import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kangear.bodycompositionanalyzer.R;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.xsshome.taip.face.TAipFace;

/**
 * A placeholder fragment containing a simple view.
 */
public class FaceIdActivityFragment2 extends Fragment {
    private static final String TAG = "FaceIdActivityFragment";
    private SurfaceView surfaceView;
    private TextView info;
    private SurfaceHolder mSurfaceHolder;
    private Context context;
    private Camera mCamera;
    private boolean mPreviewRunning;
    private ImageView mImageView;

    final static ExecutorService tpe = Executors.newSingleThreadExecutor();

    // 初始化一个TAipPtu
    TAipFace aipFace = new TAipFace("2114617797", "nyFobjtIqgIH1MXW");

    public FaceIdActivityFragment2() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View layout = inflater.inflate(R.layout.test_camera, container, false);
        surfaceView = (SurfaceView) layout.findViewById(R.id.camera);
        info = (TextView) layout.findViewById(R.id.cameraInfo);
        /*
         * mCamera = Camera.open(); if (mCamera != null) {
         */
        // surfaceView.setVisibility(View.VISIBLE);
        System.out.println(" no  null ");
        mSurfaceHolder = surfaceView.getHolder();
        mSurfaceHolder.addCallback(new MySurfaceHolder());
        // mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        // }

        return layout;
    }

    private class MySurfaceHolder implements SurfaceHolder.Callback {



        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {
            System.out.println(" ===================== ");

            /*
             * if (mPreviewRunning) { mCamera.stopPreview(); }
             */
            mCamera = Camera.open();
            if (mCamera != null) {
                Camera.Parameters params = mCamera.getParameters();
                params.setPictureFormat(PixelFormat.JPEG);
                params.setPreviewSize(800, 600);
                mCamera.setParameters(params);
                System.out.println(" ===================== 2");


                mCamera.setPreviewCallback(new Camera.PreviewCallback() {
                    @Override
                    public void onPreviewFrame(final byte[] bytes, Camera camera) {
                        Log.e(TAG, "onPreviewFrame: " + bytes.length);

                        tpe.submit(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String result = aipFace.detect(bytes);//人脸检测与分析
                                    Log.e(TAG, "result: " + result);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                    }
                });
            }

            try {
                mCamera.setPreviewDisplay(holder);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (mCamera == null) {
                return;
            }

            mCamera.startPreview();
            mPreviewRunning = true;
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            // TODO Auto-generated method stub

            // if (mCamera != null) {
            // try {
            // mCamera.unlock();
            // System.out.println("surfacecreated in");
            // mCamera.setPreviewDisplay(holder);
            // } catch (IOException e) {
            // e.printStackTrace();
            // mCamera.stopPreview();
            // mCamera.release();
            // mCamera = null;
            // }
            //
            // }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // TODO Auto-generated method stub
            if (mCamera != null) {
                mCamera.stopPreview();
                mPreviewRunning = false;
                mCamera.release();

            }
            mCamera = null;
        }

    }
}
