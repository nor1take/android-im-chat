package com.example.chat;

import static com.example.chat.utils.RequestMapping.LOGIN;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.chat.pojo.Post;
import com.example.chat.pojo.User;
import com.example.chat.utils.Application_Util;
import com.example.chat.utils.Code;
import com.example.chat.utils.Okhttp_LoginOrRegist;
import com.example.chat.utils.RequestMapping;
import com.example.chat.utils.Result;

public class A_Login_Activity extends AppCompatActivity {
    private EditText uname;
    private EditText pwd;
    String username;
    String password;
    int id;

    Application_Util application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = (Application_Util) getApplication();

        setContentView(R.layout.a_login_activity);
        TextView register = findViewById(R.id.toRegister);
        ImageView login = findViewById(R.id.enter);
        uname = findViewById(R.id.username_login);
        pwd = findViewById(R.id.password_login);

        //获取SharedPreferences对象
        final SharedPreferences sp = getSharedPreferences("userinfo_sp", MODE_MULTI_PROCESS);
        /*******实现自动登录功能*******/
        username = sp.getString("username", null);//获取账号信息
        password = sp.getString("password", null);//获取密码
        if (username != null && password != null) {
            uname.setText(username);
            pwd.setText(password);
            Result result = Okhttp_LoginOrRegist.init(username, password, LOGIN, A_Login_Activity.this);

            if (result.getCode().equals(Code.GET_OK)) {
                User user = JSON.parseObject(result.getData().toString(), User.class);
                application.setUid(user.getId()); //设置全局变量 uid
                startActivity(new Intent(A_Login_Activity.this, B_Container_Activity.class));
            }
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = uname.getText().toString();
                password = pwd.getText().toString();
                //是输入框失去焦点
                uname.clearFocus();
                pwd.clearFocus();
                //强制收起软键盘
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(uname.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(pwd.getWindowToken(), 0);
                if (username.equals("") || password.equals(""))
                    Toast.makeText(A_Login_Activity.this, "用户名或密码不为空", Toast.LENGTH_SHORT).show();
                else {
                    Result result = Okhttp_LoginOrRegist.init(username, password, LOGIN, A_Login_Activity.this);

                    if (result.getCode().equals(Code.GET_OK)) {
                        User user = JSON.parseObject(result.getData().toString(), User.class);

                        application.setUid(user.getId()); //设置全局变量 uid
                        sp.edit()
                                .putString("username", username)
                                .putString("password", password)
                                .commit();
                        startActivity(new Intent(A_Login_Activity.this, B_Container_Activity.class));
                    }
                }
            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(A_Login_Activity.this, A_Register_Activity.class);
                startActivity(registerIntent);
            }
        });
    }


}