package com.wuchangi.searchforanswer.network;


import com.wuchangi.searchforanswer.bean.AnalysisResult;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by WuchangI on 2019/3/9.
 */

public interface ApiService {

    @Multipart
    @POST("handWrite")
    Observable<ResponseBody> getHandwritingResult(@Part MultipartBody.Part handwritingAnswer);

    @Multipart
    @POST("printedText")
    Observable<ResponseBody> getAnalysisResult(@Part MultipartBody.Part printedQuestion);

    @Multipart
    @POST("answerCart")
    Observable<ResponseBody> getAnswerSheetResult(@Part MultipartBody.Part answerSheet);
}
