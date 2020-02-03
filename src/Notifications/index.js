import {
    NativeModules,
    NativeEventEmitter
} from "react-native"
const notificationModule = NativeModules.reactNativeEasyNotifications
const eventEmitter = new NativeEventEmitter(notificationModule);
export default  {
    getDeviceId: (callback) => {
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
    },
    getData:(c)=>{
        notificationModule.getIntent(c)
    },
    onMessageReceived: (callback) => {
        eventEmitter.addListener('notificationReceived', (event) => {
            console.log(event)
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
    },
    onNotificationTapped: (callback) => {
        eventEmitter.addListener('onNotificationTapped', (event) => {
            callback(event)
        })
    }
}