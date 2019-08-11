package com.kangear.bodycompositionanalyzer.track;

//import com.lalamove.huolala.app_common.entity.DriverAccountInfo;
//import com.lalamove.huolala.app_common.entity.HttpResult;
//import com.lalamove.huolala.app_common.entity.Meta2Data;
//import com.lalamove.huolala.app_common.entity.ReportInterval;
//import com.lalamove.huolala.app_common.entity.TargeItem;

import com.kangear.bodycompositionanalyzer.entry.HttpResult;
import com.kangear.bodycompositionanalyzer.entry.UploadResult;

import io.reactivex.Observable;
import okhttp3.MultipartBody;

public interface MainApiService {
    Observable<UploadResult> uploadReportImage(MultipartBody.Part filePart);
}