import {
    NativeModules,
    NativeEventEmitter,
    Platform
} from "react-native"
const notificationModule = NativeModules.reactNativeEasyNotifications
var eventEmitter = null
if(notificationModule != null){
    eventEmitter = new NativeEventEmitter(notificationModule);
}
export default  {
    getDeviceId: (callback) => {
        if(Platform.OS === 'android'){
        if (notificationModule) {
            if (notificationModule.registerForToken) {
                notificationModule.registerForToken(deviceId => {
                    callback(deviceId)
                })
            } else {
                callback("Id not generated yet")
            }
        } else {
            callback("Id not generated yet")
        }
    }else{
        console.warn('getLastNotificationData is only available on android platform')
    }
    },
    getLastNotificationData:(callback,errorCallback)=>{
        if(Platform.OS === 'android'){
            notificationModule.getIntent(notification => {
                try{
                    if(typeof notification === 'string'){
                        let data = JSON.parse(notification)
                        callback(data)
                    }else{
                        throw "Invalid data provided";
                    }
                }catch(e){
                    errorCallback(e)
                }
            })
        }else{
            console.warn('getLastNotificationData is only available on android platform')
        }
    },
    onMessageReceived: (callback) => {
        if(Platform.OS === 'android'){
        eventEmitter.addListener('notificationReceived', (event) => {
            if(event){
                try{
                    let data = JSON.parse(event)
                    callback(data)
                }catch(e){
                    let d ={
                        message: "error in data parse"
                    }
                    callback(d)
                }
            }
        })
    }else{
        console.warn('getLastNotificationData is only available on android platform')
    }
    },
    onNotificationTapped: (callback) => {
        if(Platform.OS === 'android'){
        eventEmitter.addListener('onNotificationTapped', (event) => {
            callback(event)
        })
    }else{
        console.warn('getLastNotificationData is only available on android platform')
    }
    }
}