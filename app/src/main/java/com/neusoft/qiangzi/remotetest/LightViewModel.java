package com.neusoft.qiangzi.remotetest;

import android.os.RemoteException;

import com.google.gson.Gson;
import com.neusoft.qiangzi.socketservicedemo.ISocketBinder;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LightViewModel extends ViewModel {

    private MutableLiveData<Light> mutableLiveData = new MutableLiveData<>();
    private Light light = new Light();
    private ISocketBinder binder;

    public LightViewModel() {
        this.mutableLiveData.setValue(light);
    }

    public LiveData<Light> getMutableLiveData() {
        return mutableLiveData;
    }

    public void setBinder(ISocketBinder binder) {
        this.binder = binder;
    }

    public void setLightOn1(boolean b){
        light.setLightOn1(b);
        sendLightStateToRemote();
        mutableLiveData.setValue(light);
    }

    public void setLightOn2(boolean b){
        light.setLightOn2(b);
        sendLightStateToRemote();
        mutableLiveData.setValue(light);
    }

    public void setLightOn3(boolean b){
        light.setLightOn3(b);
        sendLightStateToRemote();
        mutableLiveData.setValue(light);
    }
    public void setLightOn4(boolean b){
        light.setLightOn4(b);
        sendLightStateToRemote();
        mutableLiveData.setValue(light);
    }
    public void setLightOn5(boolean b){
        light.setLightOn5(b);
        sendLightStateToRemote();
        mutableLiveData.setValue(light);
    }

    private void sendLightStateToRemote() {
        Gson gson = new Gson();
        String json = gson.toJson(light, Light.class);
        try {
            binder.sendText("LIGHT_DATA="+json);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    public void receivedRemoteData(String data){
//        if(data.equals("SW1_ON")){
//            light.setLightOn1(true);
//        }else if(data.equals("SW1_OFF")){
//            light.setLightOn1(false);
//        } else if (data.equals("SW2_ON")) {
//            light.setLightOn2(true);
//        } else if (data.equals("SW2_OFF")) {
//            light.setLightOn2(false);
//        }
        if(!data.startsWith("LIGHT_DATA"))return;
        String json = data.substring(data.indexOf("=")+1);
        Gson gson = new Gson();
        Light l = gson.fromJson(json, Light.class);
        light = l;
        mutableLiveData.postValue(light);
    }
}
