package com.example.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.chat.utils.Application_Util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;

public class B_Container_Activity extends AppCompatActivity implements View.OnClickListener {
    private int[] tabList = new int[]{R.id.tab1, R.id.tab2, R.id.tab3, R.id.tab4};
    private int[] iconChosenList = new int[]{
            R.mipmap.ic_desktop_transitions_filled,
            R.mipmap.ic_public_app_filled,
            R.mipmap.ic_public_contacts_group_filled,
            R.mipmap.ic_public_community_messages_filled
    };
    private int[] iconList = new int[]{
            R.mipmap.ic_desktop_transitions,
            R.mipmap.ic_public_app,
            R.mipmap.ic_public_contacts_group,
            R.mipmap.ic_public_community_messages
    };
    private Fragment[] tabFragment = new Fragment[]{new B_Tab1_Fragment(), new B_Tab2_Fragment(), new B_Tab3_Fragment(), new B_Tab4_Fragment()};

    int last_index = 0;

    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.b_container_activity);

        /**
         * 开辟 Socket
         */
        Application_Util application = (Application_Util) getApplication();
        application.init();

        //用首页替换空的FrameLayout
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        for (Fragment f : tabFragment) {
            fragmentTransaction.add(R.id.frameLayout, f);
            fragmentTransaction.hide(f);
        }
        fragmentTransaction.show(tabFragment[0]);
        fragmentTransaction.commit();
        for (int i : tabList) {
            findViewById(i).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tab1 || view.getId() == R.id.tab2 || view.getId() == R.id.tab3 || view.getId() == R.id.tab4) {
            match(view.getId());
        }
    }

    void match(int id) {
        int i;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        for (i = 0; i < tabList.length; i++)
            if (id == tabList[i]) {
                if (last_index != -1) fragmentTransaction.hide(tabFragment[last_index]);
                last_index = i;
                fragmentTransaction.show(tabFragment[i]);
                fragmentTransaction.commit();
                break;
            }
        ImageView viewById = findViewById(id);
        viewById.setImageResource(iconChosenList[i]);
        changeIcon(i);
    }

    void changeIcon(int i) {
        for (int j = 0; j < tabList.length; j++)
            if (j != i) {
                ImageView viewById = findViewById(tabList[j]);
                viewById.setImageResource(iconList[j]);
            }
    }
}
