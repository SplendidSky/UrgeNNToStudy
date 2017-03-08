package com.example.nns_app_1;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.mail.MessagingException;
import javax.mail.Transport;

public class MainActivity extends AppCompatActivity {

    private TimePickerView pvTime_begin;
    private TimePickerView pvTime_over;
    //private Button tvTime_begin;
    private Button tvTime_over;
    private Button btn_ready;

    private Date date_begin;
    private Date date_over;
    private Toast toast;

    private WifiManager wifiManager;
    private ConnectivityManager connectivityManager;
    private ComponentName componentName;

    private Timer timer;
    private TimerTask timerTask;

    private static MailSenderInfo mailSenderInfo;

    private UnlockReceiver unlockReceiver;

//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            if (msg.what == TURN_ON_WIFI_GPRS) {
//                wifiManager.setWifiEnabled(true);
//                //gprsEnabled(true);
//            }
//        }
//    };


    private static final int MY_REQUEST_CODE = 9999;
    private static final int TURN_ON_WIFI_GPRS = 9998;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //tvTime_begin = (Button) findViewById(R.id.time_begin);
        tvTime_over = (Button) findViewById(R.id.time_over);
        btn_ready = (Button) findViewById(R.id.ready);

        Date now = new Date(System.currentTimeMillis());
        date_begin = now;
        date_over = now;
        //tvTime_begin.setText(getTime(now));
        tvTime_over.setText(getTime(now));

        mailSenderInfo = new MailSenderInfo();

        unlockReceiver = new UnlockReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(unlockReceiver, filter);

        if (now.getHours() > 22) {
            toast = Toast.makeText(getApplicationContext(), "夜深了，别学太晚啊", Toast.LENGTH_SHORT);
            toast.show();
        } else if (now.getHours() < 8) {
            toast = Toast.makeText(getApplicationContext(), "好勤奋啊~这么早就起来学习", Toast.LENGTH_SHORT);
            toast.show();
        } else if (now.getHours() == 12 && now.getMinutes() == 17) {
            toast = Toast.makeText(getApplicationContext(), "12:17诶~", Toast.LENGTH_SHORT);
            toast.show();
        } else if (now.getHours() == 11 && now.getMinutes() == 17) {
            toast = Toast.makeText(getApplicationContext(), "爱你噢~", Toast.LENGTH_SHORT);
            toast.show();
        }

        initTimePicker();

//        tvTime_begin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                pvTime_begin.show();
//            }
//        });

        tvTime_over.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvTime_over.show();
            }
        });

        btn_ready.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WifiManagerLeak")
            @Override
            public void onClick(View view) {
                NNAPPApplicaion.FINISH_STUDY = false;

                boolean valid = true;
                if (date_over.getHours() < date_begin.getHours() || (date_over.getHours() == date_begin.getHours() && date_over.getMinutes() <= date_begin.getMinutes())) {
                    toast = Toast.makeText(getApplicationContext(), "你是想学到明天吗？", Toast.LENGTH_SHORT);
                    toast.show();
                    valid = false;
                } else if (date_over.getHours() - date_begin.getHours() > 5) {
                    toast = Toast.makeText(getApplicationContext(), "不行，学太久了，脑子会坏掉的", Toast.LENGTH_SHORT);
                    toast.show();
                    valid = false;
                } else if (date_over.getHours() > 22) {
                    toast = Toast.makeText(getApplicationContext(), "学这么晚啊，早点睡喔", Toast.LENGTH_SHORT);
                    toast.show();
                }

                if (!valid) {
                    pvTime_over.show();
                } else {
//                    Intent startIntent = new Intent(getApplicationContext(), NNService.class);
//                    startIntent.putExtra("date_over", date_over);

                    //关闭WIFI和GPRS
//                    wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
//                    if (wifiManager.getWifiState() != WifiManager.WIFI_STATE_DISABLED)
//                        wifiManager.setWifiEnabled(false);
//                    connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    //gprsEnabled(false);

                    //定时打开WIFI和GPRS
                    timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            NNAPPApplicaion.FINISH_STUDY = true;
//                            Message message = new Message();
//                            message.what = TURN_ON_WIFI_GPRS;
//                            handler.sendMessage(message);
                        }
                    };

                    int delay = (date_over.getHours() - date_begin.getHours()) * 3600 + (date_over.getMinutes() - date_begin.getMinutes()) * 60;
                    timer = new Timer();
                    try {
                        timer.schedule(timerTask, delay * 1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    String mail_subject = "NN 开始学习了！";
                    String mail_content = "NN 打算从 " + getTime(date_begin) + " 学习到 " + getTime(date_over);
                    //String mail_content = "test";
                    sendMail(mail_subject, mail_content);

                    //锁屏
                    componentName = new ComponentName(getApplicationContext(), AdminReceiver.class);
                    NNAPPApplicaion.devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
                    if (NNAPPApplicaion.devicePolicyManager.isAdminActive(componentName)) {
                        NNAPPApplicaion.devicePolicyManager.lockNow();// 锁屏
                    } else {
                        activeManage(); //获取权限
                    }
                    NNAPPApplicaion.devicePolicyManager.lockNow();
                }

            }
        });
    }

    private void initTimePicker() {
        pvTime_begin = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                //tvTime_begin.setText(getTime(date));
                date_begin = date;
            }
        }).setType(TimePickerView.Type.HOURS_MINS)
                .setCancelText("还没想好什么时候开始念书")
                .setSubmitText("这个时候念书！")
                .setLabel("年", "月", "日", "时", "分", "秒")
                .build();

        pvTime_over = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                tvTime_over.setText(getTime(date));
                date_over = date;
            }
        }).setType(TimePickerView.Type.HOURS_MINS)
                .setCancelText("没有想好念到什么时候")
                .setSubmitText("念到这个时候！")
                .setLabel("年", "月", "日", "时", "分", "秒")
                .build();

    }

    private String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("HH 时 mm 分");
        return format.format(date);
    }

    //检测GPRS是否打开
//    private boolean gprsIsOpenMethod(String methodName) {
//        Class cmClass = connectivityManager.getClass();
//        Class[] argClasses = null;
//        Object[] argObject = null;
//
//        Boolean isOpen = false;
//        try {
//            Method method = cmClass.getMethod(methodName, argClasses);
//
//            isOpen = (Boolean) method.invoke(connectivityManager, argObject);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return isOpen;
//    }
//
//    //开启/关闭GPRS
//    private void setGprsEnabled(String methodName, boolean isEnable) {
//        Class cmClass = connectivityManager.getClass();
//        Class[] argClasses = new Class[1];
//        argClasses[0] = boolean.class;
//
//        try {
//            Method method = cmClass.getMethod(methodName, argClasses);
//            method.invoke(connectivityManager, isEnable);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private boolean gprsEnabled(boolean bEnable) {
//        Object[] argObjects = null;
//
//        boolean isOpen = gprsIsOpenMethod("getMobileDataEnabled");
//        if (isOpen == !bEnable) {
//            setGprsEnabled("setMobileDataEnabled", bEnable);
//        }
//
//        return isOpen;
//    }

    private void activeManage() {
        // 启动设备管理(隐式Intent) - 在AndroidManifest.xml中设定相应过滤器
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);

        // 权限列表
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);

        // 描述(additional explanation) 在申请权限时出现的提示语句
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                "激活一次以后它就会自己锁屏了");

        startActivityForResult(intent, MY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 获取权限成功，立即锁屏并finish自己，否则继续获取权限
        if (requestCode == MY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            NNAPPApplicaion.devicePolicyManager.lockNow();
            finish();
        } else {
            //activeManage();
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static native void initSendMailSenderInfo();

    public static void sendMail(String mail_subject, String mail_content) {
//        mailSenderInfo.setMailServerHost("smtp.qq.com");
//        mailSenderInfo.setMailServerPort("465");
//        mailSenderInfo.setValidate(true);
//        mailSenderInfo.setUserName("chenweichen1117@qq.com");
//        mailSenderInfo.setPassword("puhmebaysdombhch");
//        mailSenderInfo.setFromAddress("chenweichen1117@qq.com");
//        mailSenderInfo.setToAddress("chenweichen1117@qq.com");
//        mailSenderInfo.setSubject(mail_subject);
//        mailSenderInfo.setContent(mail_content);

        initSendMailSenderInfo();
        mailSenderInfo.setSubject(mail_subject);
        mailSenderInfo.setContent(mail_content);


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    SimpleMailSender simpleMailSender = new SimpleMailSender();
                    Transport.send(simpleMailSender.sendTextMail(mailSenderInfo));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
//        Message message = new Message();
//            message.obj = mailSenderInfo;
//            message.what = SEND_EMAIL;
//            handler.sendMessage(message);

    }

    public static boolean isRunningForeground (Context context) {
        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        String currentPackageName = cn.getPackageName();
        if(!TextUtils.isEmpty(currentPackageName) && currentPackageName.equals(context.getPackageName())) {
            return true ;
        }
        return false ;
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (!NNAPPApplicaion.FINISH_STUDY && !isRunningForeground(getApplicationContext())) {
            Date now = new Date(System.currentTimeMillis());
            String mail_subject = "NN 不乖";
            String mail_content = "NN 在 " + getTime(now) + " 时玩了会儿手机";
            sendMail(mail_subject, mail_content);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (pvTime_begin.isShowing()) {
                pvTime_begin.dismiss();
                return true;
            }
            if (pvTime_over.isShowing()) {
                pvTime_over.dismiss();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // cancel timer
        timer.cancel();
    }

    static {
        System.loadLibrary("native-lib");
    }

}
