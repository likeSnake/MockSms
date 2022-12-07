package com.jcl.test2;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jcl.test2.Adapter.ContactsAdapter;
import com.jcl.test2.Util.GetLinkManUtil;
import com.jcl.test2.pojo.ContactsInfo;

import java.util.ArrayList;
import java.util.List;


public class Contacts extends AppCompatActivity implements View.OnClickListener{
    ContactsAdapter contactsAdapter;
    RecyclerView recyclerView;
    ImageButton imageButton;
    EditText search_content;
    RelativeLayout layout_hint;
    TextView send_phone;
    ImageView no_contact;
    ImageView img_c;
    private String No_phone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_activity);
        List<ContactsInfo> linkMan = GetLinkManUtil.getLinkMan(getApplicationContext());
        recyclerView = (RecyclerView) findViewById(R.id.contact_recycler);
        imageButton = findViewById(R.id.back_btn);
        search_content = findViewById(R.id.search_content);
        layout_hint = findViewById(R.id.layout_hint);
        send_phone = findViewById(R.id.send_phone);
        no_contact = findViewById(R.id.no_contact);
        img_c = findViewById(R.id.img_c);
        img_c.setOnClickListener(this);

        search_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int t = 0;
                System.out.println("赋值零");
                List<ContactsInfo> list = new ArrayList<>();
                System.out.println(charSequence.toString());
                if (charSequence.toString().length()>0){
                for (int i3 = 0; i3 < linkMan.size(); i3++) {
                    String phone = linkMan.get(i3).getPhone();
                    String name = linkMan.get(i3).getName();
                    if ( phone.indexOf(charSequence.toString()) != -1 || name.indexOf(charSequence.toString()) != -1){
                        t = 1;
                        System.out.println("存在");
                        list.add(new ContactsInfo(name,phone,linkMan.get(i3).getfName()));
                    }
                }
                if (t != 0){// 搜索到
                    System.out.println("隐藏 no contact");
                    no_contact.setVisibility(View.GONE);
                    layout_hint.setVisibility(View.GONE);
                    System.out.println(t);
                    start(false,list);
                }else {// 未搜索到
                    layout_hint.setVisibility(View.VISIBLE);
                    send_phone.setText("Send To : "+charSequence.toString());
                    No_phone = charSequence.toString();
                    start(false,list);
                    no_contact.setVisibility(View.VISIBLE);
                }
                }else {//
                    System.out.println("清空输入框");
                    layout_hint.setVisibility(View.GONE);
                    no_contact.setVisibility(View.GONE);
                    start(false,linkMan);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        List<ContactsInfo> contactList =  new ArrayList<>();
        contactList = GetLinkManUtil.getLinkMan(getApplicationContext());
        start(false,contactList);

    }

    public void start(Boolean b,List<ContactsInfo> list){
        LinearLayoutManager manager = new LinearLayoutManager(Contacts.this, LinearLayoutManager.VERTICAL, b);
        contactsAdapter = new ContactsAdapter(list,getApplicationContext());
        recyclerView.setLayoutManager(manager);
        if (b){
            System.out.println("执行");
        recyclerView.scrollToPosition(contactsAdapter.getItemCount()-3);
        }
        recyclerView.setAdapter(contactsAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_c:
                Intent intent = new Intent(Contacts.this,MockMsgActivity.class);
                if (No_phone != null){
                    intent.putExtra("phone",No_phone);
                    startActivity(intent);
                }
        }
    }
}