package com.wuchangi.searchforanswer.network;


import com.wuchangi.searchforanswer.constant.Constant;

import java.io.File;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by WuchangI on 2019/3/9.
 */

public class SendMessageManager {

    private static SendMessageManager sSendMessageManager;
    private HttpChannel mHttpChannel;
    private ApiService mApiService;

    private SendMessageManager() {
        mHttpChannel = HttpChannel.getInstance();
        mApiService = mHttpChannel.getApiService();
    }

    public static SendMessageManager getInstance() {
        return sSendMessageManager == null ? sSendMessageManager = new SendMessageManager() : sSendMessageManager;
    }


    public void getHandwritingResult(File file) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), file);
        MultipartBody.Part picture = MultipartBody.Part.createFormData("the_file", "answer.jpg", requestBody);
        Observable<ResponseBody> observable = mApiService.getHandwritingResult(picture);
        mHttpChannel.sendMessageToGetStringResponse(observable, Constant.GET_HANDWRITING_RESULT);
    }


    public void getAnalysisResult(File file) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), file);
        MultipartBody.Part picture = MultipartBody.Part.createFormData("image", "question.jpg", requestBody);
        Observable<ResponseBody> observable = mApiService.getAnalysisResult(picture);
        mHttpChannel.sendMessageToGetStringResponse(observable, Constant.GET_ANALYSIS_RESULT);
    }
}
