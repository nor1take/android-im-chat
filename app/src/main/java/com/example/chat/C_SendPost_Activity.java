package com.example.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chat.utils.QuickOkhttp_Util;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class C_SendPost_Activity extends AppCompatActivity implements View.OnClickListener {
    private String labelText;
    private String bodyText;
    private EditText diy;
    EditText body;
    int id;
    private int[] labels = new int[]{R.id.diy, R.id.read, R.id.sports, R.id.game};

    boolean isdiy = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c_sendpost_activity);


        final SharedPreferences sp = getSharedPreferences("userinfo_sp", MODE_MULTI_PROCESS);
        id = sp.getInt("id", -1);//获取用户id

        //返回
        ImageView imageView = findViewById(R.id.back_sendpost);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        diy = findViewById(R.id.diy);
        diy.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onFocusChange(View view, boolean isFocus) {
                if (isFocus) {
                    isdiy = true;
                    labelText = diy.getText().toString();
                    diy.setBackgroundResource(R.drawable.tab_radius_l_blacktheme_bg);
                    diy.setTextColor(getResources().getColor(R.color.white));
                    //diy.setHintTextColor(R.color.white);
                    changeTab(R.id.diy);
                } else {
                    if (isdiy)
                        labelText = diy.getText().toString();
                }
            }
        });
        for (int id : labels) {
            findViewById(id).setOnClickListener(this);
        }
        body = findViewById(R.id.body);
        ImageView button_sendpost = findViewById(R.id.button_sendpost);
        button_sendpost.setOnClickListener(this);
    }

    private String sendPostOkhttp() {
        RequestBody requestBody = new FormBody.Builder()
                .add("poster", String.valueOf(id))
                .add("label", labelText)
                .add("peopleNum", "0")
                .add("body", bodyText)
                .build();
        return QuickOkhttp_Util.init(requestBody, "send");
    }

    //点击更换标签背景
    @Override
    public void onClick(View view) {
        if (view.getId() != R.id.diy) {
            diy.clearFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(diy.getWindowToken(), 0);
        }
        if (view.getId() == R.id.diy || view.getId() == R.id.read || view.getId() == R.id.sports || view.getId() == R.id.game) {
            handleLabel(view.getId());
        }
        //发布按钮
        else if (view.getId() == R.id.button_sendpost) {
            bodyText = body.getText().toString();
            if ("".equals(labelText) || labelText == null || "".equals(bodyText) || bodyText == null) {
                Toast.makeText(this, "标签或内容不能为空", Toast.LENGTH_SHORT).show();
            } else {
                String response = sendPostOkhttp();
                if (response != null) {
                    Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
                    finish();
                } else Toast.makeText(this, "请求服务器失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    void handleLabel(int id) {
        if (id != R.id.diy) isdiy = false;
        TextView label = findViewById(id);
        label.setBackgroundResource(R.drawable.tab_radius_l_blacktheme_bg);
        label.setTextColor(getResources().getColor(R.color.white));
        labelText = label.getText().toString();
        changeTab(id);
    }

    //改变标签的颜色
    void changeTab(int id) {
        for (int i_id : labels)
            if (i_id != id) {
                TextView textView = findViewById(i_id);
                textView.setBackgroundResource(R.drawable.tab_radius_l_white_bg);
                textView.setTextColor(getResources().getColor(R.color.introduce));
            }
    }
}
