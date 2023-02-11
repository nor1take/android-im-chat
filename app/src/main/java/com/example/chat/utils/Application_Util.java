package com.example.chat.utils;

import android.app.Application;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Application_Util extends Application {
    public static final String ADDRESS = "n58770595y.zicp.fun";
    public static final int PORT = 24725;

    private Socket socket;
    private BufferedReader brClient;
    private BufferedWriter bwServer;
    private int uid;
    String data;

    public void init() {
        Thread open = new Thread(new Runnable() {
            @Override
            public void run() {
                //与服务器建立连接
                try {
                    socket = new Socket(ADDRESS, PORT);
                } catch (Exception e) {
                    Toast.makeText(Application_Util.this, "连接TCP服务器失败", Toast.LENGTH_SHORT).show();
                    // e.printStackTrace();
                }
            }
        });
        open.start();

        try {
            open.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            brClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bwServer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    bwServer.write(uid);
                    bwServer.newLine();
                    bwServer.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(new receiveMsg()).start();
    }

    public Socket getSocket() {
        return socket;
    }

    public BufferedReader getBrClient() {
        return brClient;
    }

    public BufferedWriter getBwServer() {
        return bwServer;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getData() {
        return data;
    }

    private class receiveMsg implements Runnable {
        @Override
        public void run() {
            try {
                while ((data = brClient.readLine()) != null) {
                    data = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

