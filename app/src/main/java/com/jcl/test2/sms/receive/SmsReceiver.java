/*
 * Copyright 2014 Jacob Klinker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jcl.test2.sms.receive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import com.jcl.test2.Util.MsgDbUtil;

/**
 * Needed to make default sms app for testing
 */
public class SmsReceiver extends BroadcastReceiver {

    private static final int MESSAGE_BACK = 1;
    private boolean isFlag = true;

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("系统短信接收到短信通知");
        String body = MsgDbUtil.getSmsBody(intent);
        String address = MsgDbUtil.getSmsAddress(intent);
        new MsgDbUtil(context).insertSms(context,address,body,1);

    }
}
