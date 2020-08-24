package com.neusoft.qiangzi.remotetest.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import com.neusoft.qiangzi.remotetest.bean.LightBean;
import com.neusoft.qiangzi.remotetest.model.LightViewModel;
import com.neusoft.qiangzi.remotetest.R;
import com.neusoft.qiangzi.socketservicedemo.IOnSocketReceivedListener;
import com.neusoft.qiangzi.socketservicedemo.ISocketBinder;

public class MainActivity extends AppCompatActivity implements ServiceConnection, CompoundButton.OnCheckedChangeListener {

    ISocketBinder binder;
    Switch aSwitch1;
    Switch aSwitch2;
    Switch aSwitch3;
    Switch aSwitch4;
    Switch aSwitch5;
    ImageView imageViewRed;
    ImageView imageViewYellow;
    ImageView imageViewGreen;
    ImageView imageViewBlue;
    ImageView imageViewPurple;

    LightViewModel viewModel;

    IOnSocketReceivedListener receivedListener = new IOnSocketReceivedListener.Stub() {
        @Override
        public void onReceived(String data) throws RemoteException {
            viewModel.receivedRemoteData(data);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this).get(LightViewModel.class);
        viewModel.getmMutableLiveData().observe(this, new Observer<LightBean>() {
            @Override
            public void onChanged(LightBean lightBean) {
                aSwitch1.setChecked(lightBean.isLightOn1());
                aSwitch2.setChecked(lightBean.isLightOn2());
                aSwitch3.setChecked(lightBean.isLightOn3());
                aSwitch4.setChecked(lightBean.isLightOn4());
                aSwitch5.setChecked(lightBean.isLightOn5());
                if(lightBean.isLightOn1()) imageViewRed.setImageResource(R.drawable.ic_light_red);
                else imageViewRed.setImageResource(R.drawable.ic_light_black);
                if(lightBean.isLightOn2()) imageViewYellow.setImageResource(R.drawable.ic_light_yellow);
                else imageViewYellow.setImageResource(R.drawable.ic_light_black);
                if(lightBean.isLightOn3()) imageViewGreen.setImageResource(R.drawable.ic_light_green);
                else imageViewGreen.setImageResource(R.drawable.ic_light_black);
                if(lightBean.isLightOn4()) imageViewBlue.setImageResource(R.drawable.ic_light_blue);
                else imageViewBlue.setImageResource(R.drawable.ic_light_black);
                if(lightBean.isLightOn5()) imageViewPurple.setImageResource(R.drawable.ic_light_purple);
                else imageViewPurple.setImageResource(R.drawable.ic_light_black);
            }
        });

        aSwitch1 = findViewById(R.id.switch1);
        aSwitch2 = findViewById(R.id.switch2);
        aSwitch3 = findViewById(R.id.switch3);
        aSwitch4 = findViewById(R.id.switch4);
        aSwitch5 = findViewById(R.id.switch5);
        imageViewRed = findViewById(R.id.imageView1);
        imageViewYellow = findViewById(R.id.imageView2);
        imageViewGreen = findViewById(R.id.imageView3);
        imageViewBlue = findViewById(R.id.imageView4);
        imageViewPurple = findViewById(R.id.imageView5);

        aSwitch1.setOnCheckedChangeListener(this);
        aSwitch2.setOnCheckedChangeListener(this);
        aSwitch3.setOnCheckedChangeListener(this);
        aSwitch4.setOnCheckedChangeListener(this);
        aSwitch5.setOnCheckedChangeListener(this);

//        imageView1.setVisibility(View.INVISIBLE);
//        imageView2.setVisibility(View.INVISIBLE);
//        imageView3.setVisibility(View.INVISIBLE);
//        imageView4.setVisibility(View.INVISIBLE);
//        imageView5.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent i = new Intent();
        i.setComponent(new ComponentName("com.neusoft.qiangzi.socketservicedemo",
                "com.neusoft.qiangzi.socketservicedemo.SocketService"));
        bindService(i, this, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(binder!=null){
            try {
                binder.unregisterListener(receivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        unbindService(this);
    }

    private static final String TAG = "MainActivity";
    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        Log.d(TAG, "onServiceConnected: is called.");
        binder = ISocketBinder.Stub.asInterface(iBinder);
        viewModel.setmBinder(binder);
        try {
            binder.registerListener(receivedListener);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if(!compoundButton.isPressed())return;
        switch (compoundButton.getId()){
            case R.id.switch1:
                viewModel.setLightOn1(b);
                break;
            case R.id.switch2:
                viewModel.setLightOn2(b);
                break;
            case R.id.switch3:
                viewModel.setLightOn3(b);
                break;
            case R.id.switch4:
                viewModel.setLightOn4(b);
                break;
            case R.id.switch5:
                viewModel.setLightOn5(b);
                break;
        }
    }
}