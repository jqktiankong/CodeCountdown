package com.example.jqk.codecountdown;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by YASCN on 2017/3/24.
 */

public class TimeService extends Service {

    private int number;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == AppConstants.COUNTDOWN) {

                if (number == 0) {
                    Log.d(AppConstants.TAG, "销毁服务");
                    stopSelf();
                    return;
                }

                number--;
                SPUtils.put(TimeService.this, AppConstants.KEY_NUMBER, number);
                Log.d(AppConstants.TAG, "存储number = " + number);
                mHandler.sendEmptyMessageDelayed(AppConstants.COUNTDOWN, 1000);
            }
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(AppConstants.TAG, "服务启动成功");
        if (intent != null) {
            number = intent.getIntExtra("number", 0);
            Log.d(AppConstants.TAG, "服务得到number = " + number);
            mHandler.sendEmptyMessage(AppConstants.COUNTDOWN);
        }

        return super.onStartCommand(intent, Service.START_REDELIVER_INTENT, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(AppConstants.TAG, "服务destroy");
        mHandler.removeMessages(AppConstants.COUNTDOWN);
        mHandler = null;
    }
}
