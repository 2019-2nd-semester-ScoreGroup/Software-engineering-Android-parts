package com.scoregroup.androidpos.Client;

import android.util.Log;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Client implements Runnable {
    private static String ip = "localhost";
    private ReceiveListener callback;
    private boolean isReceived = false;
    private boolean alreadySent = false;
    private Socket sock;
    private String msg;
    private String data;

    /**아이피 설정*/
    public Client setIP(String ip) {
        this.ip = ip;
        return this;
    }

    /**콜백 설정*/
    public Client setOnReceiveListener(ReceiveListener callback) {
        this.callback = callback;
        return this;
    }

    /**송신 메세지 설정*/
    public Client setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    /**수신 메세지 Get*/
    public String getData() {
        return data;
    }

    /**수신 여부*/
    public boolean isReceived() {
        return isReceived;
    }

    /**송신 시작*/
    public void send() {
        if (alreadySent) {
            // 이미 보낸 요청
            return;
        }
        new Thread(this).start();

    }

    @Override
    public void run() {
        try {
            Log.i("ju", "런어블 실행");
            //소켓 설정
            InetSocketAddress sock_address = new InetSocketAddress(ip, 12142); // 소켓 설정
            sock = new Socket();
            sock.connect(sock_address, 2000);
            //스트림 설정
            BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sock.getOutputStream())));

            //송신
            out.println(msg);
            out.flush();

            //중복송신 방지
            alreadySent = true;

            //수신
            sock.setSoTimeout(5000); // Read 타임아웃
            data = in.readLine();
            isReceived = true;
            Log.i("ju", "받은 데이터: " + data);

            //연결종료
            sock.close();
        } catch (IOException e) {
            Log.i("ju", e.getLocalizedMessage());
            e.printStackTrace();
        } finally {
            //이벤트 실행
            if (callback != null){
                Log.i("ju", "이벤트 실행");
                callback.onReceive(this);
            }
        }
    }
}