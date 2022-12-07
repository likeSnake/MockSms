package com.jcl.test2.Util;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.jcl.test2.pojo.ContactsInfo;

import java.util.ArrayList;
import java.util.List;

public class GetLinkManUtil {

    public static List<ContactsInfo> getLinkMan(Context context) {
        List<ContactsInfo> contactList = new ArrayList<>();
        Cursor cursor = null;
        //查询联系人数据
        cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                null, null, null);
        if (cursor != null) {
            // Log.e("tag","*****************************");
            while (cursor.moveToNext()) {
                List<String> allNumber = new ArrayList<>();
                String id = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                //获取联系人姓名
                //test
                //联系人号码
                String oNumber = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String number = oNumber.replaceAll("\\s*", "");
                //名字
                String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
                //获取首字母
                String firstChar = cursor.getString(cursor.getColumnIndexOrThrow("phonebook_label"));
                contactList.add(new ContactsInfo(name, number, firstChar));
                   /* //获取联系人号码
                    int numberCount = cursor.getInt(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                    if (numberCount > 0) {
                        System.out.println(numberCount);
                        Cursor phoneNumberCursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "="+id,null, null);
                        System.out.println(phoneNumberCursor.moveToNext());
                        if (phoneNumberCursor.moveToFirst()) {
                            System.out.println("进入判断");
                            do {
                                String phoneNumber = phoneNumberCursor.getString(phoneNumberCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                if (!allNumber.contains(phoneNumber)) {
                                    allNumber.add(phoneNumber);
                                    System.out.println("名字："+name+" 号码"+phoneNumber);
                                    contactList.add(new ContactsInfo(name,phoneNumber));
                                }
                            }while (phoneNumberCursor.moveToNext());

                        }
                        if (phoneNumberCursor != null) {
                            phoneNumberCursor.close();
                        }
                    }
*/
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return contactList;
    }
}
