package com.example.weborderingfood;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.webkit.JavascriptInterface;

public class WebAppInterface {
        Context mContext;

        public WebAppInterface(Context context) {
            mContext = context;
        }

        @JavascriptInterface
        public void onLoginSuccess() {
            // Đăng nhập thành công → chuyển sang HomeActivity
            Intent intent = new Intent(mContext, MainActivity.class);
            mContext.startActivity(intent);
            if (mContext instanceof Activity) {
                ((Activity) mContext).finish();
            }
        }
}
