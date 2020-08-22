package com.neusoft.qiangzi.remotetest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import com.neusoft.qiangzi.socketservicedemo.IOnSocketReceivedListener;
import com.neusoft.qiangzi.socketservicedemo.ISocketBinder;

public class MainActivity extends AppCompatActivity implements ServiceConnection, CompoundButton.OnCheckedChangeListener {

    ISocketBinder binder;
    Switch aSwitch1;
    Switch aSwitch2;
    Switch aSwitch3;
    Switch aSwitch4;
    Switch aSwitch5;
    ImageView imageView1;
    ImageView imageView2;
    ImageView imageView3;
    ImageView imageView4;
    ImageView imageView5;

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
        viewModel.getMutableLiveData().observe(this, new Observer<Light>() {
            @Override
            public void onChanged(Light light) {
                aSwitch1.setChecked(light.isLightOn1());
                aSwitch2.setChecked(light.isLightOn2());
                aSwitch3.setChecked(light.isLightOn3());
                aSwitch4.setChecked(light.isLightOn4());
                aSwitch5.setChecked(light.isLightOn5());
                if(light.isLightOn1()) imageView1.setImageResource(R.drawable.ic_light_red);
                else imageView1.setImageResource(R.drawable.ic_light_black);
                if(light.isLightOn2()) imageView2.setImageResource(R.drawable.ic_light_yellow);
                else imageView2.setImageResource(R.drawable.ic_light_black);
                if(light.isLightOn3()) imageView3.setImageResource(R.drawable.ic_light_green);
                else imageView3.setImageResource(R.drawable.ic_light_black);
                if(light.isLightOn4()) imageView4.setImageResource(R.drawable.ic_light_blue);
                else imageView4.setImageResource(R.drawable.ic_light_black);
                if(light.isLightOn5()) imageView5.setImageResource(R.drawable.ic_light_purple);
                else imageView5.setImageResource(R.drawable.ic_light_black);
            }
        });

        aSwitch1 = findViewById(R.id.switch1);
        aSwitch2 = findViewById(R.id.switch2);
        aSwitch3 = findViewById(R.id.switch3);
        aSwitch4 = findViewById(R.id.switch4);
        aSwitch5 = findViewById(R.id.switch5);
        imageView1 = findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);
        imageView4 = findViewById(R.id.imageView4);
        imageView5 = findViewById(R.id.imageView5);

        aSwitch1.setOnCheckedChangeListener(this);
        aSwitch2.setOnCheckedChangeListener(this);
        aSwitch3.setOnCheckedChangeListener(this);
        aSwitch4.setOnCheckedChangeListener(this);
        aSwitch5.setOnCheckedChangeListener(this);

        imageView1.setVisibility(View.INVISIBLE);
        imageView2.setVisibility(View.INVISIBLE);
        imageView3.setVisibility(View.INVISIBLE);
        imageView4.setVisibility(View.INVISIBLE);
        imageView5.setVisibility(View.INVISIBLE);
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
        viewModel.setBinder(binder);
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