package com.scoregroup.androidpos.Client;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
//쉐얼드 프리퍼런스 구현...
public class Client {
    Socket sock = null; // 서버 연결 클라이언트 소켓
    BufferedReader in = null; // Server로부터 데이터를 읽어들이기 위한 입력스트림
    PrintWriter out = null; // 서버로 내보내기 위한 출력 스트림
    boolean connection = false; // 서버 연결 확인

    public Client(String ip){ // 클라이언트 생성자 생성시 연결시도
        new Thread(()->{
            try {
                sock = new Socket(ip, 8080);
                in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sock.getOutputStream())));
                Log.i("connect", "서버 연결");
                connection = true;
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("connect", "서버 연결 실패");
            }
        }).start();
    }

    public void PushMsg(String msg){
        new Thread(()->{
            while(connection == false)
                System.out.println("wait...");
            out.println(msg);                        //서버로 데이터 전송
            out.flush();
            Log.i("push", msg);
            ReadMsg();
        });
    }

    public void ReadMsg(){
        String rec_msg = null;
        try {
            rec_msg = in.readLine();                //Client로부터 데이터를 읽어옴
            Log.i("read", rec_msg);
            ClientManger.wait_false();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}