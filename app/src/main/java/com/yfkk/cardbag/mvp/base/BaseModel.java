package com.yfkk.cardbag.mvp.base;

import android.content.Intent;
import android.os.Handler;
import android.util.Base64;

import com.yfkk.cardbag.MainApplication;
import com.yfkk.cardbag.cache.PersistentData;
import com.yfkk.cardbag.config.Config;
import com.yfkk.cardbag.config.UrlConfig;
import com.yfkk.cardbag.log.LogUtils;
import com.yfkk.cardbag.utils.ConcealUtils;
import com.yfkk.cardbag.utils.PhoneUtils;
import com.yfkk.cardbag.utils.SignUtils;
import com.yfkk.cardbag.utils.StringUtils;
import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

/**
 * 数据相关
 * <p>
 * Created by litao on 2018/1/16.
 */

public class BaseModel {

    protected static final int DEFAULT_TIMEOUT = 20;  // 超时时间
    protected Retrofit retrofit;
    protected OkHttpClient.Builder httpClientBuilder;
    protected Map<String, String> map = new HashMap<>();   // POST参数
    protected boolean addTailParameter; // 是否把Post参数放在url尾部

    private String requestUrl; // 最终请求地址


    public BaseModel() {
        Init(UrlConfig.HOST_URL);
    }

    public BaseModel(String url) {
        Init(url);
    }

    private void Init(String url) {
        httpClientBuilder = new OkHttpClient.Builder();
        // 利用拦截器进行日志/公参/参数加密等操作
        httpClientBuilder.addInterceptor(new LogInterceptor(map)).addInterceptor(addQueryParameterInterceptor).readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS).connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        if (!LogUtils.isDebug) {
            httpClientBuilder.proxy(Proxy.NO_PROXY); // 不使用代理
        }
        retrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create()) // 也可以自定义处理加解密
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(url)
                .build();
    }

    Interceptor addQueryParameterInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();
            Request request;

            String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
            // 公参添加在请求链接尾部
            HttpUrl.Builder tailBuilder = originalRequest.url().newBuilder();
            if (addTailParameter) {
                for (String key : map.keySet()) {
                    tailBuilder.addQueryParameter(key, map.get(key));
                }
            } else {
                tailBuilder.addQueryParameter("version_name", PhoneUtils.getVersionName(MainApplication.getInstance())) // 版本名
                        .addQueryParameter("version_code", PhoneUtils.getVersion(MainApplication.getInstance())) // 版本号
                        .addQueryParameter("deviceNo", PhoneUtils.getUniquePsuedoID()) // imei
                        .addQueryParameter("timestamp", timestamp)
                        .addQueryParameter("sign", getSign(new Gson().toJson(map), timestamp));// 签名
            }

            HttpUrl modifiedUrl = tailBuilder.build();
            requestUrl = modifiedUrl.toString();

            LogUtils.e("请求 : " + requestUrl);

            // Token放在header里面
            Request.Builder builder = originalRequest.newBuilder().url(modifiedUrl);
            if (PersistentData.getToken() != null) {
                builder = builder.addHeader("Authorization", PersistentData.getToken());
            }
            LogUtils.d("Token : " + PersistentData.getToken());
            builder = builder.addHeader("client", "android");

            // Body修改（如参数加密）
//            try {
//                if (requestUrl.startsWith(UrlConfig.HOST_URL) && originalRequest.body() != null && originalRequest.body().contentType().toString().contains("application/json")) {
//                    Buffer buffer = new Buffer();
//                    originalRequest.body().writeTo(buffer);
//                    String body = buffer.readString(originalRequest.body().contentType().charset());
//                    body = CryptoUtils.AES_Encrypt(body);
//                    RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=UTF-8"), body);
//                    builder.method(originalRequest.method(), requestBody);
//                }
//            } catch (Exception e) {
//
//            }
            request = builder.build();
            return chain.proceed(request);
        }
    };

    public String getSign(String jsonString, String timestamp) {
        SortedMap<Object, Object> mapSign = new TreeMap<Object, Object>();
        String tamp = String.valueOf(System.currentTimeMillis());
        if (tamp != null) {
            try {
                mapSign.put("jsonString", jsonString);
                mapSign.put("deviceNo", PhoneUtils.getUniquePsuedoID());
                mapSign.put("timestamp", timestamp);
                mapSign.put("sign_key", ConcealUtils.decrypt(new String(Base64.decode(Config.SIGN_KEY.getBytes(), Base64.DEFAULT))));
                new FileInputStream(tamp);
            } catch (FileNotFoundException e) {
            } catch (OutOfMemoryError oom) {
                try {
                    new FileInputStream(tamp);
                } catch (IOException e) {
                }
            } finally {
                if (tamp != null) {
                    try {
                        new FileInputStream(tamp);
                    } catch (IOException e) {
                    }
                }
            }
        }
        return SignUtils.createSign(mapSign);
    }


    public static <T> void invoke(BasePresenter.LifeSubscription lifeSubscription, Observable<T> observable, final Subscriber<T> subscriber, final RetryWhenInterface retryWhenInterface) {
        final Handler mainHandler = new Handler();
        Observable<T> mObservable = observable.subscribeOn(rx.schedulers.Schedulers.io()); // 网络请求异步
        if (retryWhenInterface != null) { // retryWhenInterface 异常后重试，一般用于启动页面后台获取数据的情况。并不适合用户手动操作的网络请求
            //总共重试1000次，重试间隔4秒，每次加1秒,最多24秒一次
            mObservable = mObservable.retryWhen(new Func1<Observable<? extends Throwable>, Observable<?>>() {
                int maxRetries = 1000;
                int retryDelayMillis = 4000;
                int retryCount = 0;

                @Override
                public Observable<?> call(Observable<? extends Throwable> attempts) {
                    return attempts
                            .flatMap(new Func1<Throwable, Observable<?>>() {
                                @Override
                                public Observable<?> call(final Throwable throwable) {
                                    if (++retryCount <= maxRetries) {
                                        // 主线程中回掉
                                        mainHandler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                LogUtils.e("网络请求失败了，再次请求：" + retryCount);
                                                retryWhenInterface.next(retryCount, throwable);
                                            }
                                        });
                                        return Observable.timer(retryDelayMillis + (retryCount > 20 ? 20 : retryCount) * 1000,
                                                TimeUnit.MILLISECONDS);
                                    }
                                    return Observable.error(throwable);
                                }
                            });
                }
            });
        }
        mObservable = mObservable.observeOn(AndroidSchedulers.mainThread()); // 下面的回掉方法用同步
        Subscription subscription = mObservable.subscribe(subscriber);
        lifeSubscription.bindSubscription(subscription);
    }

    public interface RetryWhenInterface {
        void next(int retryCount, Throwable e);
    }

    public class LogInterceptor implements Interceptor {
        private String content;
        private Map<String, String> map;

        public LogInterceptor(Map<String, String> map) {
            super();
            this.map = map;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            LogUtils.d("请求: " + request.toString() + " \r\n【参数】: " + new Gson().toJson(map));
            long t1 = System.nanoTime();
            Response response = null;
            try {
                response = chain.proceed(chain.request());
            } catch (Exception e1) {
                LogUtils.e("网络请求异常: " + e1);
            }
            okhttp3.MediaType mediaType = response.body().contentType();
            ResponseBody originalBody = response.body();
            if (null != originalBody) {
                content = originalBody.string();
            }
            LogUtils.e("【返回】: " + request.toString() + " \r\n【结果】: " + content);

            // 网络请求的数广播出去
//            if (PersistentData.getNetDebugMode()) {
//                Intent broadCastIntent = new Intent();
//                broadCastIntent.setAction(Config.BROADCAST_NET_REQUEST);
//                broadCastIntent.putExtra("url", requestUrl);
//                broadCastIntent.putExtra("body", new Gson().toJson(map));
//                if (!StringUtils.isEmpty(content) && content.length() > 1024 * 10) {
//                    broadCastIntent.putExtra("data", content.substring(0, 1024 * 10));
//                } else {
//                    broadCastIntent.putExtra("data", content);
//                }
//                MainApplication.getInstance().sendBroadcast(broadCastIntent);
//            }
            // body内容进行AES解密
//            content = CryptoUtils.AES_Decrypt(content);

            return response.newBuilder()
                    .body(ResponseBody.create(mediaType, content))
                    .build();
        }
    }
}
