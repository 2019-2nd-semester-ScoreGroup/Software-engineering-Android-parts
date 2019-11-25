package com.scoregroup.androidpos.Client;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

//쉐얼드 프리퍼런스 구현...
public class Client {
    SocketAddress sock_address = null;
    Socket sock = null; // 서버 연결 클라이언트 소켓
    BufferedReader in = null; // Server로부터 데이터를 읽어들이기 위한 입력스트림
    PrintWriter out = null; // 서버로 내보내기 위한 출력 스트림
    String rec_msg = null; // DB로 받은 데이터 저장
    int connection = 0; // 0대기, 1성공, 2실패

    public Client(String ip){ // 클라이언트 생성자 생성시 연결시도
        new Thread(()->{
            try {
                sock_address = new InetSocketAddress(ip, 8080); // 소켓 설정
                sock = new Socket();
                sock.connect(sock_address, 2000); // 서버 연결, 타임 아웃 설정
                in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sock.getOutputStream())));
                Log.i("connect", "서버 연결 성공");
                connection = 1;
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("connect", "서버 연결 실패");
                connection = 2;
            }
        }).start();
    }

    public int Communication(String msg){
        while (connection == 0) // 0대기
            System.out.println("wait...");
        if(connection == 2) // 2실패
            return 2;
        else{ // 연결 성공
            new Thread(()->{
                PushMsg(msg);
                ReadMsg();
            }).start();
            return 1;
        }
    }

    public void PushMsg(String msg){ // 서버에 메시지 전송
        out.println(msg);                        //서버로 데이터 전송
        out.flush();
        Log.i("push", msg);
    }

    public void ReadMsg(){ // DB로 데이터 받음
        try {
            rec_msg = in.readLine(); // DB로 온 데이터 저장
            Log.i("read", rec_msg);
            ClientManger.wait_false();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                sock.close(); // 정상적으로 통신종료 시 소켓 닫음
                Log.i("sock", "소켓 클로즈 성공");
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("sock", "소켓 클로즈 실패");
            }
        }
    }

    public String send_Data(){
        return rec_msg;
    }
}