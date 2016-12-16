package com.xpjun.newcashbook.Application;

import android.app.Application;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.update.BmobUpdateAgent;

/**
 * Created by U-nookia on 2016/8/19.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this, "f9f75358d8518bb2c8a3e6497c6bf699");
    }
}
