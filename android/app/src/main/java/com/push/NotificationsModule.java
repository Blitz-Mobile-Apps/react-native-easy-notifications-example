package com.push;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONObject;

public class NotificationsModule extends ReactContextBaseJavaModule {
    public static String deviceId = null;
    public NotificationsModule(ReactApplicationContext reactContext) {
        super(reactContext);
        BroadcastReceiver geoLocationReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                NotificationsModule.this.sendMessage("onNotificationReceived");
            }
        };


        BroadcastReceiver notificationReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                NotificationsModule.this.sendMessage("notificationReceived");
            }
        };
        LocalBroadcastManager.getInstance(getReactApplicationContext()).registerReceiver(geoLocationReceiver, new IntentFilter("notificationTypeUpdate"));
        LocalBroadcastManager.getInstance(getReactApplicationContext()).registerReceiver(notificationReceiver, new IntentFilter("notificationReceived"));
    }
    @Override
    public String getName() {
        return "reactNativeEasyNotifications";
    }

    public void sendMessage(String name) {
        try {
            Log.d("NotEvent","name");
            getReactApplicationContext().getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(name, NotificationsService.EXTRA_PAYLOAD);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void registerForToken(Callback onRegister){
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( getCurrentActivity(),  new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                deviceId = instanceIdResult.getToken();
                onRegister.invoke(deviceId);
                Log.d("deviceId:  ",deviceId);
            }
        });
    }

    @ReactMethod
    public void getPayload(Callback getData){
        try{
            JSONObject obj = new JSONObject(NotificationsService.notificationData);
            if(NotificationsService.notificationData != null){
                getData.invoke(obj.toString());
                NotificationsService.clearPayload();
            }else{
                getData.invoke("No data received");
            }
        }catch (Exception e){
            getData.invoke(e.getMessage());
        }

    }
}
