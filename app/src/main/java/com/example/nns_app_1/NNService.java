package com.example.nns_app_1;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 伟宸 on 2017/2/24.
 */

public class NNService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Date date_over = (Date)intent.getSerializableExtra("date_over");

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {

            }
        };

        Timer timer = new Timer();
        timer.schedule(timerTask, date_over);
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class MyBinder extends Binder {
        public void TurnOnGPRS_WIFI() {

        }
    }
}
