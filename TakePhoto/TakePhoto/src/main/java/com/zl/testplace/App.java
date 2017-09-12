package com.zl.testplace;

import android.app.Application;
import android.content.Context;

/**
 * 
 * @author zl
 * @date 2017/9/12 15:30
 * @E-mail 764958658@qq.com
 */
public class App extends Application {

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
    }
}
