package com.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class NotificationsModule extends ReactContextBaseJavaModule {
    public static String deviceId = null;
    public NotificationsModule(ReactApplicationContext reactContext) {
        super(reactContext);
        BroadcastReceiver geoLocationReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String json = intent.getStringExtra("data");


                sendMessage("notificationReceived",json);
            }
        };
        LocalBroadcastManager.getInstance(getReactApplicationContext()).registerReceiver(geoLocationReceiver, new IntentFilter("notificationReceived"));
    }
    @Override
    public String getName() {
        return "reactNativeEasyNotifications";
    }

    public void sendMessage(String name, String data) {
        try {

            Log.d("NotEvent","name");
            getReactApplicationContext().getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(name, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Map<String, String> bundleToMap(Bundle extras) {
        Map<String, String> map = new HashMap<String, String>();

        Set<String> ks = extras.keySet();
        Iterator<String> iterator = ks.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            map.put(key, extras.getString(key));
        }/*from   w ww .j  a  v  a 2s .c  o m*/
        return map;
    }
    @ReactMethod
    public void getIntent(Callback callback){
        Intent intent = getCurrentActivity().getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null){
            Map dataMap;
            dataMap = bundleToMap(extras);

            if(dataMap != null){
                JSONObject json = new JSONObject(dataMap);

                String str = json.toString();
                    callback.invoke(str);
            }

        }
    }
//        if(NotificationsService.message != null){

//            try {
//                data = new JSONObject(getCurrentActivity().getIntent().getStringExtra("notification")).toString();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//          Intent i =  getReactApplicationContext().getCurrentActivity().getIntent();
//            Log.d("INTENT_L",data);
//        }else{
//            Log.d("INTENT_L",":asdasdasdasdasdasdas");

//        }



    @ReactMethod
    public void registerForToken(Callback onRegister){
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( getCurrentActivity(),  new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                deviceId = instanceIdResult.getToken();
                onRegister.invoke(deviceId);
            }
        });
    }
}
