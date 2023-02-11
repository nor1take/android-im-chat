package com.example.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chat.utils.Okhttp_LoginOrRegist;

public class A_Register_Activity extends AppCompatActivity {
    private EditText uname;
    private EditText pwd;
    String username;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_register_activity);
        TextView textView = findViewById(R.id.back_register);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ImageView register = findViewById(R.id.register);
        uname = findViewById(R.id.username_register);
        pwd = findViewById(R.id.password_register);

        register.setOnClickListener(new View.OnClickListener() {
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
                    Toast.makeText(A_Register_Activity.this, "用户名或密码不为空", Toast.LENGTH_SHORT).show();
                else {
                    String resp = Okhttp_LoginOrRegist.init(username, password, Okhttp_LoginOrRegist.REGISTER, A_Register_Activity.this);
                    if ("注册成功".equals(resp)) {
                        startActivity(new Intent(A_Register_Activity.this, B_Container_Activity.class));
                    }
                }
            }
        });
    }
}