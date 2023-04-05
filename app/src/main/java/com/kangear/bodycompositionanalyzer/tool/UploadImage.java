package com.kangear.bodycompositionanalyzer.tool;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Handler;
import android.print.PrintAttributes;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.kangear.bodycompositionanalyzer.BuildConfig;
import com.kangear.bodycompositionanalyzer.R;
import com.kangear.bodycompositionanalyzer.Record;
import com.kangear.bodycompositionanalyzer.RecordBean;
import com.kangear.bodycompositionanalyzer.entry.HttpResult;
import com.kangear.bodycompositionanalyzer.entry.MasterFitEntity;
import com.kangear.bodycompositionanalyzer.entry.SchoopiaRecord;
import com.kangear.bodycompositionanalyzer.entry.UploadResult;
import com.kangear.bodycompositionanalyzer.track.MainApiService;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSource;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import static com.kangear.bodycompositionanalyzer.WelcomeActivity.createPdfFromView;

public class UploadImage implements MainApiService {
    public static final String TAG = "UploadImage";

    /**
     * 单例模式: http://coolshell.cn/articles/265.html
     */
    private volatile static UploadImage singleton = null;
    public static UploadImage getInstance()   {
        if (singleton == null)  {
            synchronized (UploadImage.class) {
                if (singleton== null)  {
                    singleton= new UploadImage();
                }
            }
        }
        return singleton;
    }

    private static void uploadReport(Record record) {
        // 上报报告
        SchoopiaRecord sr = SchoopiaRecord.toHere(record);
        Gson gson = new Gson();
        String json = gson.toJson(sr);
//        Log.e(TAG, json);
        SchoopiaRecord.sendPost(json);
    }

    private static void uploadReportMasterfit(Record record) {
        // 上报报告
        MasterFitEntity sr = MasterFitEntity.toHere(record);
        Gson gson = new Gson();
        String json = gson.toJson(sr);
//        Log.e(TAG, json);
        SchoopiaRecord.sendPost(json);
    }

    // Do something
    // 318 NO: uploadReportImg
    // 318 EDU: uploadReport
    public static void doSomething(Activity act, Record record) {
        if (BuildConfig.FLAVOR_model.equals("jh318")) {
            if (BuildConfig.FLAVOR_sub.equals("normal")) {
                if (act != null) {
                    uploadReportImg(act, record);
                } else {
                    uploadReport(record);
                }
            } else if (BuildConfig.FLAVOR_sub.contains("edu")) {
                uploadReportMasterfit(record);
            }
        } else {
            // do not report upload
        }
    }

    public static void createPdfFromView(View content, final OutputStream os) {
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
        try {
            document.writeTo(os);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            // close the document
            document.close();
        }
    }

    public static void uploadReportImg(Activity act, Record record) {
        // 上报图片
        View v = act.findViewById(R.id.pdfpicture_view);
        if (v == null) {
            return;
        }

        v.setVisibility(View.INVISIBLE);

        if (record.getImg() != null) {
            return;
        }
        new Handler().postDelayed(() -> {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            createPdfFromView(v, stream);
            byte[] byteArray = stream.toByteArray();
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", "xxx.jpg", RequestBody.create(MediaType.parse("application/pdf"), byteArray));
            UploadImage.getInstance().uploadReportImage(filePart)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<UploadResult>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onNext(UploadResult uploadResultHttpResult) {
                            String id = uploadResultHttpResult.getData().getId();
                            Log.e(TAG, "上传成功: " + id);
                            record.setImg(id);
                            RecordBean.getInstance(act.getApplicationContext()).update(record);
                            uploadReport(record);
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onComplete() {
                            Log.e(TAG, "onComplete");
                        }
                    });
        }, 1000);
    }

    private static Bitmap getCacheBitmapFromView(View view) {
        final boolean drawingCacheEnabled = true;
        view.setDrawingCacheEnabled(drawingCacheEnabled);
        view.buildDrawingCache(drawingCacheEnabled);
        final Bitmap drawingCache = view.getDrawingCache();
        Bitmap bitmap;
        if (drawingCache != null) {
            bitmap = Bitmap.createBitmap(drawingCache);
            view.setDrawingCacheEnabled(false);
        } else {
            bitmap = null;
        }
        return bitmap;
    }

    /**
     * Upload Image
     * @param bm
     * @return
     */
    public static void uploadImage(final Bitmap bm, final String UPLOAD_URL) {
            try {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("file", "card_" + System.currentTimeMillis() + ".jpg", RequestBody.create(MediaType.parse("image/jpeg"), byteArray))
                        .build();

                Request request = new Request.Builder().url(UPLOAD_URL).post(requestBody).build();

                OkHttpClient client = new OkHttpClient();
                Response response = client.newCall(request).execute();
                System.out.println("response.body().string(): " + response.body().string());
            } catch (Exception e) {
                // Log.e(TAG, "Other Error: " + e.getLocalizedMessage())0;
                e.printStackTrace();
            }
    }

    @Override
    public Observable<UploadResult> uploadReportImage(final MultipartBody.Part filePart) {
        return new Observable<UploadResult>() {
            @Override
            protected void subscribeActual(Observer<? super UploadResult> observer) {
                try {
                    RequestBody requestBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addPart(filePart)
                            .build();
                    Request request = new Request.Builder().url("https://328s.cn/v1/318/reportimg").post(requestBody).build();
                    OkHttpClient client = new OkHttpClient();
                    Response response = client.newCall(request).execute();
                    String result =  response.body().string();
                    System.out.println("response.body().string(): " + result);
                    Gson gson = new Gson();
                    UploadResult ui = gson.fromJson(result, UploadResult.class);
                    observer.onNext(ui);
                    observer.onComplete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
