package co.hyperverge.hypersnapsample;

 import android.app.Application;


import co.hyperverge.hypersnapsdk.HyperSnapSDK;

import co.hyperverge.hypersnapsdk.objects.HyperSnapParams;


public class HyperSnapSampleApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        HyperSnapSDK.init(this, "12b169", "e431183393f7d9f44581", HyperSnapParams.Region.India);
    }

 }