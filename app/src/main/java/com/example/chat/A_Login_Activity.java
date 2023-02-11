package com.example.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chat.utils.Application_Util;
import com.example.chat.utils.Okhttp_LoginOrRegist;

public class A_Login_Activity extends AppCompatActivity {
    private EditText uname;
    private EditText pwd;
    String username;
    String password;
    int id;

    Application_Util application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        application = (Application_Util) getApplication();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_login_activity);
        TextView register = findViewById(R.id.toRegister);
        ImageView login = findViewById(R.id.enter);
        uname = findViewById(R.id.username_login);
        pwd = findViewById(R.id.password_login);

        //获取SharedPreferences对象
        final SharedPreferences sp = getSharedPreferences("userinfo_sp", MODE_MULTI_PROCESS);
        /*******实现自动登录功能*******/
        id = sp.getInt("id", -1); //获取用户id

        application.setUid(id); //设置全局变量 uid

        username = sp.getString("username", null);//获取账号信息
        password = sp.getString("password", null);//获取密码
        if (username != null && password != null) {
            uname.setText(username);
            pwd.setText(password);
            String resp = Okhttp_LoginOrRegist.init(username, password, Okhttp_LoginOrRegist.LOGIN, A_Login_Activity.this);
            if (resp != null)
                if ("登录成功".equals(resp.substring(0, 4))) {
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
                    String resp = Okhttp_LoginOrRegist.init(username, password, Okhttp_LoginOrRegist.LOGIN, A_Login_Activity.this);
                    if (resp != null) {
                        if ("登录成功".equals(resp.substring(0, 4))) {
                            String s_id = resp.substring(4);
                            id = Integer.parseInt(s_id);

                            application.setUid(id); //设置全局变量 uid

                            sp.edit()
                                    .putInt("id", id)
                                    .putString("username", username)
                                    .putString("password", password)
                                    .commit();
                            startActivity(new Intent(A_Login_Activity.this, B_Container_Activity.class));
                        }
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