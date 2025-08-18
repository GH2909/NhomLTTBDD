package com.example.weborderingfood;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.logging.HttpLoggingInterceptor;
import java.util.concurrent.TimeUnit;

public class ApiClient {

    private static Retrofit retrofit = null;
    private static OkHttpClient okHttpClient;
    // SỬA LẠI: Đảm bảo BASE_URL trỏ đến thư mục gốc của dự án web.
    private static final String BASE_URL = "http://10.0.2.2/ltweb/BTL/Webcoding-learning/";

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            // Cấu hình OkHttpClient để quản lý cookie
            okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .addInterceptor(interceptor)
                    .cookieJar(new PersistentCookieJar()) // THÊM DÒNG NÀY
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient) // SỬ DỤNG OKHTTP ĐÃ CẤU HÌNH
                    .build();
        }
        return retrofit;
    }
}
