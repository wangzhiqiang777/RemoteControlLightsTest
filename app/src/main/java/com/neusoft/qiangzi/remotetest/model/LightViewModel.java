package com.neusoft.qiangzi.remotetest.model;

import android.os.RemoteException;

import com.google.gson.Gson;
import com.neusoft.qiangzi.remotetest.bean.LightBean;
import com.neusoft.qiangzi.socketservicedemo.ISocketBinder;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LightViewModel extends ViewModel {

    private static String DATA_HEADER_LIFHT_DATA = "DATA_LIGHT";
    private static String DATA_HEADER_LOCATION_DATA = "DATA_LOCATION";
    private static String DATA_HEADER_MUSIC_DATA = "DATA_MUSIC";
    private static String CMD_ENGIN_START = "CMD_ENGIN_START";
    private static String CMD_ENGIN_STOP = "CMD_ENGIN_STOP";

    private MutableLiveData<LightBean> mMutableLiveData = new MutableLiveData<>();
    private LightBean mLightBean = new LightBean();
    private ISocketBinder mBinder;

    public LightViewModel() {
        this.mMutableLiveData.setValue(mLightBean);
    }

    public LiveData<LightBean> getmMutableLiveData() {
        return mMutableLiveData;
    }

    public void setmBinder(ISocketBinder mBinder) {
        this.mBinder = mBinder;
    }

    public void setLightOn1(boolean b){
        mLightBean.setLightOn1(b);
        sendLightStateToRemote();
        mMutableLiveData.setValue(mLightBean);
    }

    public void setLightOn2(boolean b){
        mLightBean.setLightOn2(b);
        sendLightStateToRemote();
        mMutableLiveData.setValue(mLightBean);
    }

    public void setLightOn3(boolean b){
        mLightBean.setLightOn3(b);
        sendLightStateToRemote();
        mMutableLiveData.setValue(mLightBean);
    }
    public void setLightOn4(boolean b){
        mLightBean.setLightOn4(b);
        sendLightStateToRemote();
        mMutableLiveData.setValue(mLightBean);
    }
    public void setLightOn5(boolean b){
        mLightBean.setLightOn5(b);
        sendLightStateToRemote();
        mMutableLiveData.setValue(mLightBean);
    }

    private void sendLightStateToRemote() {
        Gson gson = new Gson();
        String json = gson.toJson(mLightBean, LightBean.class);
        try {
            mBinder.sendText(DATA_HEADER_LIFHT_DATA +"="+json);
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
        LightBean l = gson.fromJson(json, LightBean.class);
        mLightBean = l;
        mMutableLiveData.postValue(mLightBean);
    }
}
