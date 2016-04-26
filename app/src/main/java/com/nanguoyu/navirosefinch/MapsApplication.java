package com.nanguoyu.navirosefinch;

import android.app.Application;
import android.content.Context;


import com.amap.api.navi.AMapNavi;
import com.nanguoyu.navirosefinch.module.TTSController;
import com.nanguoyu.navirosefinch.utils.Constants;
import com.nanguoyu.navirosefinch.utils.FileUtils;

import com.nanguoyu.navirosefinch.beans.BJCamera;
import com.nanguoyu.navirosefinch.dao.DaoMaster;
import com.nanguoyu.navirosefinch.dao.DaoSession;
import com.nanguoyu.navirosefinch.utils.JsonUtils;

import java.util.ArrayList;

public class MapsApplication extends Application {

    private static Context sContext;
    private static DaoMaster sDaoMaster;
    private static DaoSession sDaoSession;


    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        TTSController ttsController = TTSController.getInstance(this.getApplicationContext());
        ttsController.init();

        AMapNavi navi = AMapNavi.getInstance(sContext);

        if (!FileUtils.readBooleanFromSharedPreference(Constants.PreferenceKeys.KEY_INIT, false)){
            ArrayList<BJCamera> cameraBeans = JsonUtils.prasePaperCameras(FileUtils.readStringFromAsset(MapsApplication.getAppContext(), "beijing_paper.json"));
            for (BJCamera camera: cameraBeans){
                getDaoSession().insert(camera);
            }
            FileUtils.writeBooleanToSharedPreference(Constants.PreferenceKeys.KEY_INIT,true);
        }

    }

    public static Context getAppContext() {
        return sContext;
    }

    public static DaoMaster getDaoMaster() {
        if (sDaoMaster == null) {
            DaoMaster.OpenHelper helper = new DaoMaster.DevOpenHelper(sContext, "navirosefinch.db", null);
            sDaoMaster = new DaoMaster(helper.getWritableDatabase());
        }
        return sDaoMaster;
    }

    public static DaoSession getDaoSession() {
        if (sDaoSession == null) {
            sDaoSession = getDaoMaster().newSession();
        }
        return sDaoSession;
    }

}