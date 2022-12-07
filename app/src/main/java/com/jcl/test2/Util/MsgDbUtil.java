package com.jcl.test2.Util;

import static android.content.ContentValues.TAG;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.jcl.test2.pojo.ContactsInfo;

import java.util.ArrayList;
import java.util.List;

public class MsgDbUtil {
    private Context context;

    public MsgDbUtil(Context context){
        this.context = context;
    }

    public void deleteSmsById(int id){
        Uri uri = Uri.parse("content://sms/");
        ContentResolver resolver = context.getContentResolver();
        int result = resolver.delete(uri, "_id=?", new String[]{String.valueOf(id)});
        System.out.println("删除状态:"+result);
    }

    /**
     * 获得短信内容
     */
    public static String getSmsBody(Intent intent) {

        String tempString = "";
        Bundle bundle = intent.getExtras();
        if (null == intent) return "";
        Object messages[] = (Object[]) bundle.get("pdus");
        if (null == messages) return "";
        SmsMessage[] smsMessage = new SmsMessage[messages.length];
        for (int n = 0; n < messages.length; n++) {
            smsMessage[n] = SmsMessage.createFromPdu((byte[]) messages[n]);
            // 短信有可能因为使用了回车而导致分为多条，所以要加起来接受
            tempString += smsMessage[n].getDisplayMessageBody();
        }
        return tempString;

    }

    /**
     * 获得短信地址
     */
    public static String getSmsAddress(Intent intent) {

        Bundle bundle = intent.getExtras();
        if (null == bundle) return "";
        Object messages[] = (Object[]) bundle.get("pdus");
        return SmsMessage.createFromPdu((byte[]) messages[0])
                .getDisplayOriginatingAddress();
    }

    public  void insertSms(Context context, String address, String sms_body,int type) {

       /* int last = Integer.valueOf(getTime().substring(0,7));
        int now = Integer.valueOf(String.valueOf(System.currentTimeMillis()).substring(0,7));
        System.out.println(last+"---"+now);
        System.out.println(System.currentTimeMillis()-Long.parseLong(getTime()));
        long l = System.currentTimeMillis() - Long.parseLong(getTime());
        if (l < 500){
            System.out.println("间隔时间太短，不保存");
            return;
        }*/
        ContentResolver resolver = context.getContentResolver();
        Uri uri = Uri.parse("content://sms/");
        ContentValues values = new ContentValues();
        values.put("address", address); //发件人号码
        values.put("type", type);
        values.put("date", System.currentTimeMillis());//发送时间
        values.put("body", sms_body);//信息内容
        resolver.insert(uri, values);
        Log.d(TAG, "insertSms   address: " + address + "----sms_body:" + sms_body);
    }
    public String getTime() {
        String time = "";
        Uri uri = Uri.parse("content://sms/");
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(uri, new String[]{"date"}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            System.out.println("判断不为空执行");
            String date;
            cursor.moveToFirst();
            time = cursor.getString(0);

        }
        return time;
    }

}
