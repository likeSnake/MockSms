package com.jcl.test2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.role.RoleManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.i18n.phonenumbers.Phonenumber;
import com.google.i18n.phonenumbers.geocoding.PhoneNumberOfflineGeocoder;
import com.jcl.test2.Adapter.AllAdapter;
import com.jcl.test2.Util.MsgDbUtil;
import com.jcl.test2.pojo.AllInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import kotlin.jvm.internal.Intrinsics;

public class MockMsgActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText mock_send_name;
    private TextView gsd;
    private EditText msg;
    private ImageView bt_send;
    private ImageView mode;
    private AlertDialog alertDialog2;
    private int choose = 0;
    private boolean isQf = false;
    private RadioButton send_mock;
    private RadioButton reception_mock;
    private Button choose_ok;
    private int a = 1;
    private AllAdapter allAdapter;
    private List<AllInfo> allInfo = new ArrayList<>();
    private RecyclerView recyclerView;
    private RadioGroup radioGroup;
    private ImageView back_btn;
    private EditText qf_number;
    private EditText qf_content;
    private RadioButton qf_mock;
    private TextView qf_title;
    private String qfTitle = "";
    private String[] qf_phones;
    private String qf_contents;
    private Button send_qf;
    private TextView ok;
    private TextView no;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mock_msg);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);


        mock_send_name = findViewById(R.id.mock_send_name);
        mock_send_name.setOnClickListener(this);
        gsd = findViewById(R.id.gsd);
        bt_send=(ImageView)findViewById(R.id.bt_send_message);
        recyclerView = (RecyclerView) findViewById(R.id.mock_msg_recycler_view);
        msg = findViewById(R.id.msg);
        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(this);
        msg.setOnClickListener(this);
        mode = findViewById(R.id.mode);
        mode.setOnClickListener(this);
        mock_send_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                } else {
                    String phone = mock_send_name.getText().toString();
                    if (!phone.isEmpty()){
                        mock_send_name.setBackground(null);
                        String geo = getGeo(phone);
                        if (geo.contains("省")){
                            geo = geo.replace("省","");
                        }
                        if (geo.contains("市")){
                            geo = geo.replace("市","");
                        }
                        gsd.setText(geo);
                        gsd.setVisibility(View.VISIBLE);
                    }else {
                        gsd.setVisibility(View.GONE);
                    }

                }
            }
        });
        msg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length()>0){
                    bt_send.setVisibility(View.VISIBLE);
                }else {
                    bt_send.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        bt_send.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (!mock_send_name.getText().toString().isEmpty()) {
                    String s = String.valueOf(System.currentTimeMillis());
                    String substring = s.substring(0, 10);
                    Integer time = Integer.valueOf(substring);
                    String content = msg.getText().toString().trim();
                    String p = mock_send_name.getText().toString().trim();
                    if (a == 1) {
                        new MsgDbUtil(MockMsgActivity.this).insertSms(MockMsgActivity.this, p, content, 2);
                        allInfo.add(0, new AllInfo(999, content, time, 2, null, null, 666));
                        msg.setText("");
                        Toast.makeText(MockMsgActivity.this, "已发送", Toast.LENGTH_SHORT).show();
                        start2();
                    } else if (a == 2) {
                        new MsgDbUtil(MockMsgActivity.this).insertSms(MockMsgActivity.this, p, content, 1);
                        allInfo.add(0, new AllInfo(999, content, time, 1, null, null, 666));
                        msg.setText("");
                        Toast.makeText(MockMsgActivity.this, "已发送", Toast.LENGTH_SHORT).show();
                        start2();
                    }
                }else {
                    Toast.makeText(MockMsgActivity.this, "请输入需要发送的手机号", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.msg:
                break;
            case R.id.mode:
                showSingleAlertDialog(v);
                break;
            case R.id.back_btn:
               // ContactsPermission();
                showQfDialog(v);
                break;
            case R.id.qf_mock:
                System.out.println(qf_mock.isChecked());
                if (isQf){
                    qf_mock.setChecked(true);
                    isQf = false;
                }else {
                    qf_mock.setChecked(false);
                    isQf = true;
                }
                break;
            case R.id.send_qf:

        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showDialogBack();
            return true;
        }

        return super.onKeyUp(keyCode, event);
    }
    public void ContactsPermission(){
        if (ContextCompat.checkSelfPermission(MockMsgActivity.this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    MockMsgActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, 1);
        }
        else {
            System.out.println("已获取联系人权限");
            Intent intent = new Intent(MockMsgActivity.this,Contacts.class);
            startActivity(intent);
        }
    }
    private void sendSMSS(String[] phones,String content) {
        System.out.println("准备群发");
        for (int i = 0; i < phones.length; i++) {
            if (a == 1){
                new MsgDbUtil(MockMsgActivity.this).insertSms(MockMsgActivity.this,phones[i],content,2);
            }else {
                new MsgDbUtil(MockMsgActivity.this).insertSms(MockMsgActivity.this,phones[i],content,1);
            }
        }
        Toast.makeText(MockMsgActivity.this, "已发送"+phones.length+"条短信", Toast.LENGTH_LONG).show();
        start2();
    }
   /* private void requestPermission() {
        //判断Android版本是否大于23
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                System.out.println("获取权限");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 100);
                return;
            } else {
                System.out.println("已有权限------");
                sendSMSS();
                //已有权限
            }
        } else {
            System.out.println("API低版本 23-----");
            //API 版本在23以下
        }
    }*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //已授权获取联系人
            Intent intent = new Intent(MockMsgActivity.this,Contacts.class);
            startActivity(intent);
        } else {
            ContactsPermission();
        }
    }
    public String getGeo(String number){
        PhoneNumberOfflineGeocoder instance = PhoneNumberOfflineGeocoder.getInstance();
        Phonenumber.PhoneNumber pn = new Phonenumber.PhoneNumber();
        pn.setCountryCode(86);
        pn.setNationalNumber(Long.parseLong(number));
        return instance.getDescriptionForNumber(pn, Locale.CHINESE);
    }
    private void showPopupMenu(final View view) {
        final PopupMenu popupMenu = new PopupMenu(this,view);
        //menu 布局
        popupMenu.getMenuInflater().inflate(R.menu.send_more,popupMenu.getMenu());
        //点击事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_delete:
                        break;
                    case R.id.action_call:
                }
                return false;
            }
        });
        //关闭事件
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
            }
        });
        //显示菜单，不要少了这一步
        popupMenu.show();
    }
    private void showDialogBack() {
        // 创建一个 dialogView 弹窗
        android.app.AlertDialog.Builder builder = new
                android.app.AlertDialog.Builder(MockMsgActivity.this);
        final android.app.AlertDialog dialog = builder.create();
        View dialogView = null;
        //设置对话框布局
        dialogView = View.inflate(MockMsgActivity.this,
                R.layout.back_dialog, null);
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
                dialog.dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    public void start2(){
        LinearLayoutManager manager = new LinearLayoutManager(MockMsgActivity.this, LinearLayoutManager.VERTICAL, true);
        allAdapter = new AllAdapter(allInfo,this);
        recyclerView.scrollToPosition(allAdapter.getItemCount()-1);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(allAdapter);
    }
    public void showSingleAlertDialog(View view){
        TextView title = new TextView(this);
        //AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        // 创建一个 dialogView 弹窗
        android.app.AlertDialog.Builder alertBuilder = new
                android.app.AlertDialog.Builder(MockMsgActivity.this);
        final android.app.AlertDialog dialog = alertBuilder.create();
        View dialogView = null;
        //设置对话框布局
        dialogView = View.inflate(MockMsgActivity.this,
                R.layout.choose_dialog, null);
        dialog.setView(dialogView);
        dialog.getWindow().setBackgroundDrawableResource(com.google.android.material.R.color.mtrl_btn_transparent_bg_color);

        dialog.setView(dialogView);
        dialog.setCustomTitle(title);
        dialog.show();
        send_mock = dialogView.findViewById(R.id.send_mock);
        reception_mock = dialogView.findViewById(R.id.reception_mock);
        radioGroup = dialogView.findViewById(R.id.choose);
        qf_mock = dialogView.findViewById(R.id.qf_mock);
        qf_mock.setOnClickListener(this);
        choose_ok = dialogView.findViewById(R.id.choose_ok);
        if (a == 1){
            radioGroup.check(R.id.send_mock);
        }else {
            radioGroup.check(R.id.reception_mock);
        }

        choose_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (send_mock.isChecked()) {
                    Toast.makeText(MockMsgActivity.this, "发送模式", Toast.LENGTH_SHORT).show();
                    qfTitle = "群发（发送）";
                    a = 1;
                    } else if (reception_mock.isChecked()) {
                        Toast.makeText(MockMsgActivity.this, "接收模式", Toast.LENGTH_SHORT).show();
                        qfTitle = "群发（接收）";
                        a = 2;
                    }
                if (qf_mock.isChecked()){
                    showQfDialog(v);
                }
                dialog.dismiss();
            }
        });

    }
    public void showQfDialog(View view){
        TextView title = new TextView(this);
        //AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        // 创建一个 dialogView 弹窗
        android.app.AlertDialog.Builder alertBuilder = new
                android.app.AlertDialog.Builder(MockMsgActivity.this);
        final android.app.AlertDialog dialog = alertBuilder.create();
        View dialogView = null;
        //设置对话框布局
        dialogView = View.inflate(MockMsgActivity.this,
                R.layout.qf_dialog, null);
        dialog.setView(dialogView);
        dialog.getWindow().setBackgroundDrawableResource(com.google.android.material.R.color.mtrl_btn_transparent_bg_color);

        dialog.setView(dialogView);
        dialog.setCustomTitle(title);
        dialog.show();
        qf_content = dialogView.findViewById(R.id.qf_content);
        qf_number = dialogView.findViewById(R.id.qf_number);
        qf_title = dialogView.findViewById(R.id.qf_title);
        qf_title.setText(qfTitle);
        send_qf = dialogView.findViewById(R.id.send_qf);

        qf_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                qf_phones = s.toString().split(",");

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        qf_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                qf_contents = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        send_qf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!qf_contents.isEmpty() && qf_phones.length>0){
                    sendSMSS(qf_phones,qf_contents);
                }
                dialog.dismiss();
            }
        });


    }

}