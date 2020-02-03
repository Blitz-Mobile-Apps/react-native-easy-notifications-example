package com.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
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

    @ReactMethod
    public void getIntent(Callback callback){
        if(NotificationsService.message != null){
            String data = "";
            data = new JSONObject(NotificationsService.message.getData()).toString();
//          Intent i =  getReactApplicationContext().getCurrentActivity().getIntent();
            Log.d("INTENT_L",data);
        }else{
            Log.d("INTENT_L",NotificationsService.EXTRA_PAYLOAD);

        }

    }

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
