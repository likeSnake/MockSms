package com.jcl.test2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.role.RoleManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jcl.test2.Util.MsgDbUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Start extends AppCompatActivity {

    Button button;
    Button btn_default;
    String defaultSmsApp;
    String MyPackage;
    private TextView ok;
    private TextView no;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        button = findViewById(R.id.btn_cancel);
        System.out.println(Telephony.Sms.getDefaultSmsPackage(this));
        MyPackage = getPackageName();
        defaultSmsApp = Telephony.Sms.getDefaultSmsPackage(Start.this);//获取手机当前设置的默认短信应用的包名
        btn_default = findViewById(R.id.btn_default);
        isMR();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog1();

            }
        });


    }

    public void isMR() {
            if (defaultSmsApp == null ||MyPackage.equals(defaultSmsApp)) {
                start();
        }
    }
    // 请求多个权限
    private void request_permissions() {
        // 创建一个权限列表，把需要使用而没用授权的的权限存放在这里
        List<String> permissionList = new ArrayList<>();

        // 判断权限是否已经授予，没有就把该权限添加到列表中
        //短信
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_SMS);
        }
        //联系人
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_CONTACTS);
        }
        // 如果列表为空，就是全部权限都获取了，不用再次获取了。不为空就去申请权限
        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    permissionList.toArray(new String[permissionList.size()]), 1002);
        } else {
            System.out.println("直接启动");
            start();
        }
    }
    // 请求权限回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1002:

                // 1002请求码对应的是申请多个权限
                if (grantResults.length > 0) {
                    List<String> list = new ArrayList<>();
                    System.out.println("一共需要："+grantResults.length);
                    // 因为是多个权限，所以需要一个循环获取每个权限的获取情况
                    for (int i = 0; i < grantResults.length; i++) {
                        // PERMISSION_DENIED 这个值代表是没有授权，我们可以把被拒绝授权的权限显示出来
                        if (grantResults[i] == PackageManager.PERMISSION_DENIED){
                            String permission = permissions[i];
                            list.add(permission);
                           // Toast.makeText(Start.this, permissions[i] + "权限被拒绝了,请手动打开权限", Toast.LENGTH_SHORT).show();
                           // getAppDetailSettingIntent(Start.this);
                        }
                    }
                    if (list.isEmpty()){
                        start();
                    }else{
                        showDialog1();
                        /*AlertDialog alertDialog = new AlertDialog.Builder(this)
                                .setTitle("温馨提示")
                                .setMessage("如果您要使用本软件，请给予所需的必要权限，我们承诺保护用户的隐私权限，读取只在本地进行,这是模拟短信必不可少的一步！")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        request_permissions();
                                    }
                                }).setNegativeButton("取消使用", new DialogInterface.OnClickListener() {//添加取消
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        finish();
                                    }
                                })
                                .create();
                        alertDialog.show();*/
                    }
                }
                break;
        }
    }
    private void mms(){
        System.out.println("执行mms");
        Uri uri = Uri.parse("content://mms/inbox");
        ContentResolver resolver = getContentResolver();

        Cursor cursor = resolver.query(uri, new String[]{"_id", "date", "thread_id","sub"}, null, null, null);
    }
    /**
     * 跳转到权限设置界面
     */
    private void getAppDetailSettingIntent(Context context){
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if(Build.VERSION.SDK_INT >= 9){
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", getPackageName(), null));
        } else if(Build.VERSION.SDK_INT <= 8){
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings","com.android.settings.InstalledAppDetails");
            intent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
        }
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("设置默认后回调");
        if (requestCode == 12 && resultCode == RESULT_OK){
            System.out.println("获取所有权限");
            request_permissions();
           // start();
        }else {
            finish();
        }
        /*else {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P ) {
                System.out.println("运行第一个");
                RoleManager roleManager = getSystemService(RoleManager.class);
                Intent roleRequestIntent = roleManager.createRequestRoleIntent(RoleManager.ROLE_SMS);
                startActivityForResult(roleRequestIntent, 12);
            } else {//获取当前程序包名
                System.out.println("运行第二个");
                Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
                intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, getPackageName());
                startActivityForResult(intent, 12);
            }
        }*/
    }

    private void showDialog1() {
        // 创建一个 dialogView 弹窗
        AlertDialog.Builder builder = new
                AlertDialog.Builder(Start.this);
        final AlertDialog dialog = builder.create();
        View dialogView = null;
        //设置对话框布局
        dialogView = View.inflate(Start.this,
                R.layout.start_dialog, null);
        dialog.setView(dialogView);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(com.google.android.material.R.color.mtrl_btn_transparent_bg_color);
        dialog.show();
        // 获取布局控件
        no= (TextView) dialogView.findViewById(R.id.no);
        ok = (TextView) dialogView.findViewById(R.id.ok);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                dialog.dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT){
                    System.out.println("默认短信包名："+defaultSmsApp);
                    System.out.println("当前短信包名："+MyPackage);
                }
                if (defaultSmsApp == null){
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P ) {
                        System.out.println("运行第一个");
                        RoleManager roleManager = getSystemService(RoleManager.class);
                        Intent roleRequestIntent = roleManager.createRequestRoleIntent(RoleManager.ROLE_SMS);
                        startActivityForResult(roleRequestIntent, 12);
                    } else {//获取当前程序包名
                        System.out.println("运行第二个");
                        Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
                        intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, MyPackage);
                        startActivityForResult(intent, 12);
                    }
                }else if (MyPackage.equals(defaultSmsApp)){
                   start();
                }else{
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P ) {
                        System.out.println("运行第一个");
                        RoleManager roleManager = getSystemService(RoleManager.class);
                        Intent roleRequestIntent = roleManager.createRequestRoleIntent(RoleManager.ROLE_SMS);
                        startActivityForResult(roleRequestIntent, 12);
                    } else {//获取当前程序包名
                        System.out.println("运行第二个");
                        Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
                        intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, MyPackage);
                        startActivityForResult(intent, 12);
                    }

                }
                dialog.dismiss();
            }
        });

    }
    public void start(){
        Intent intent = new Intent();
        intent.setClass(Start.this,MockMsgActivity.class);
        startActivity(intent);
        finish();
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.send_more,menu);
        return true;
    }
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equalsIgnoreCase("MenuBuilder")) {
                try {
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    //该方法对菜单的item进行监听
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                Toast.makeText(this, "点击了第" + 1 + "个", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_scheduled:
                Toast.makeText(this, "点击了第" + 2 + "个", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_call:
                Toast.makeText(this, "点击了第" + 3 + "个", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }*/


}