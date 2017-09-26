package com.example.jqk.codecountdown;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button getCode;
    private int number;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case AppConstants.COUNTDOWN:
                    number--;
                    if (number == 0) {
                        handler.removeMessages(AppConstants.COUNTDOWN);
                        getCode.setClickable(true);
                        getCode.setText("获取验证码");
                        SPUtils.put(MainActivity.this, AppConstants.KEY_NUMBER, 0);
                    } else {
                        getCode.setClickable(false);
                        getCode.setText(number + "");
                        handler.sendEmptyMessageDelayed(AppConstants.COUNTDOWN, 1000);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getCode = (Button) findViewById(R.id.getCode);

        getCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (number == 0) {
                    number = 60;
                    getCode.setText(number + "");
                    startTimeService();
                } else {
                    getCode.setText(number);
                }
                handler.sendEmptyMessageDelayed(AppConstants.COUNTDOWN, 1000);
            }
        });

        number = (int) SPUtils.get(MainActivity.this, AppConstants.KEY_NUMBER, 0);
        Log.i(AppConstants.TAG, "得到number  = " + number);

        if (number > 0) {
            handler.sendEmptyMessage(AppConstants.COUNTDOWN);
            startTimeService();
        }
    }

    public void startTimeService() {
        Intent intent = new Intent();
        intent.putExtra("number", number);
        intent.setClass(MainActivity.this, TimeService.class);
        startService(intent);
        Log.i(AppConstants.TAG, "启动服务");
    }

    public void stopTimeService() {

        Log.d(AppConstants.TAG, "销毁服务");

        Intent intent = new Intent();
        intent.setClass(MainActivity.this, TimeService.class);
        stopService(intent);

//        SPUtils.put(this, AppConstants.KEY_NUMBER, number);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        stopTimeService();
    }
}
