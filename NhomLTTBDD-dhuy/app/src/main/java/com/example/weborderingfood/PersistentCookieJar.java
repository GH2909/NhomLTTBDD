package com.example.weborderingfood;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

public class PersistentCookieJar implements CookieJar {

    private final ConcurrentHashMap<String, List<Cookie>> cookies = new ConcurrentHashMap<>();

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        this.cookies.put(url.host(), cookies);
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies = this.cookies.get(url.host());
        return cookies != null ? cookies : new ArrayList<Cookie>();
    }
}