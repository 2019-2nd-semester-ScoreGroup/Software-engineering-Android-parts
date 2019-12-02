package com.scoregroup.androidpos.Client;

import android.util.Log;

public class ClientManger {
    private static ClientManger cm; // 액티비티가 모두 접근 가능
    private static String ip = "localhost";// 아이피를 저장

    private ClientManger(){}

    /**클라이언트 매너저 생성 메소드*/
    public static ClientManger getInstance(){
        if(cm == null)
            cm = new ClientManger();
        return cm;
    }

    /**IP를 받는 메소드*/
    public static void getIP(String ip_num){
        ip = ip_num;
    }

    /**DB와 통신을 위한 메소드*/
    public Client getDB(String msg) {
        Log.i("ju", "클라인트매니저 겟디비");
        Client t = new Client().setIP(ip).setMsg(msg);
        return t;
    }
}
