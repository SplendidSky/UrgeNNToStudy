package com.example.nns_app_1;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

/**
 * Created by 伟宸 on 2017/3/6.
 */

public class UnlockReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(Intent.ACTION_SCREEN_ON)) {
            if (!NNAPPApplicaion.FINISH_STUDY) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("还没到时间呢！！")
                        .setMessage("你不认真念书，CC会生气的！")
                        .setPositiveButton("乖乖去念书", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NNAPPApplicaion.devicePolicyManager.lockNow();
                            }
                        }).show();
            }
            else {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("乖乖的NN")
                        .setMessage("NN以后也会这么乖的")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })
                        .setNegativeButton("告诉CC", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                MainActivity.sendMail("NN 学完了", "NN 以后也会像现在这样乖乖的");
                            }
                        }).show();
            }
        }
    }
}
