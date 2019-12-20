package com.scoregroup.androidpos.Client;

import android.content.Context;
import android.util.Log;

import com.scoregroup.androidpos.R;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

public class Client implements Runnable {
    public final static String Diff="_";
    private String ip;
    private int port;
    private ReceiveListener callback;
    private boolean isReceived = false;
    private boolean alreadySent = false;
    private SSLSocket sock;
    private Context con;
    private String msg;
    private String data;
    protected Client(Context con){
        this.con=con;
    }
    /**아이피 설정*/
    public Client setIP(String ip) {
        this.ip = ip;
        return this;
    }

    /**포트 설정*/
    public Client setPORT(int port) {
        this.port = port;
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
            //SSL 모듈 로드
            KeyStore keystore = KeyStore.getInstance("BKS");
            keystore.load(con.getResources().openRawResource(R.raw.client), "12142".toCharArray());
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            tmf.init(keystore);
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);

            // 클라이언트 소켓 생성.
            SSLSocket sock = (SSLSocket) context.getSocketFactory().createSocket(ip, port);


            //소켓 설정
            Log.i("ju", "런어블 실행" + " " + ip + ":" + port);
            sock.setSoTimeout(15000);

            //스트림 설정
            BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sock.getOutputStream())));

            //송신
            Log.i("ju", "송신 메시지" + msg);
            out.println(msg);
            out.flush();

            //중복송신 방지
            alreadySent = true;

            //수신
            data = in.readLine();
            isReceived = true;
            Log.i("ju", "받은 데이터: " + data);

            //연결종료
            sock.close();
        } catch (Exception e) {
            Log.i("ju", e.getLocalizedMessage());
            e.printStackTrace();
        } finally {
            //이벤트 실행
            if (callback != null){
                Log.i("ju", "이벤트 콜백");
                callback.onReceive(this);
            }
        }
    }
}