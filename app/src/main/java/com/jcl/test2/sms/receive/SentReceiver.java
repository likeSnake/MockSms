package com.jcl.test2.sms.receive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/*
 * 发送消息广播
 * */
public class SentReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("---Receiver---", "-----发送消息-----:" + intent.getAction());

    }
}
