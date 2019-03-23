package com.wuchangi.searchforanswer.network;

import android.widget.Toast;

import com.wuchangi.searchforanswer.base.BaseApplication;
import com.wuchangi.searchforanswer.constant.Constant;
import com.wuchangi.searchforanswer.utils.RetrofitUtils;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by WuchangI on 2019/3/9.
 */

public class HttpChannel {

    private static HttpChannel sHttpChannel;
    private ApiService mApiService;

    private HttpChannel() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(RetrofitUtils.getOkHttpClientWithLoggingInterceptor())
                .build();
        mApiService = retrofit.create(ApiService.class);
    }

    public static HttpChannel getInstance() {
        return sHttpChannel == null ? sHttpChannel = new HttpChannel() : sHttpChannel;
    }

    public ApiService getApiService(){
        return mApiService;
    }


    public void sendMessageToGetStringResponse(Observable<ResponseBody> observable, String appendUrl){
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    /**
                     * 成功接收到响应数据
                     * @param responseBody 响应数据
                     */
                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            ReceiveMessageManager.getInstance().dispatchAnswerMessage(responseBody.string(), appendUrl);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
