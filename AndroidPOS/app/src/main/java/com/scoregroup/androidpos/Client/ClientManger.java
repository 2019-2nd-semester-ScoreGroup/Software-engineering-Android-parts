package com.scoregroup.androidpos.Client;

public class ClientManger {
    private static ClientManger cm; // 액티비티가 모두 사용가능
    private static String ip;// 아이피를 저장
    private static boolean wait = true; // 응답을 기다림

    private ClientManger(){} // 임의로 생성 불가

    public static ClientManger getInstance(){ // 클라이언트 매니저는 한번만 생성되어 여러 액티비티에서 사용
        if(cm == null)
            cm = new ClientManger();
        return cm;
    }

    public static void wait_false(){ // 데이터에 응답을 기다리는 메소드
        wait = false;
    }

    public static void set_IP(String ip_num){ // IP를 기억함
        ip = ip_num;
    }

    public void getDB(String msg){ // DB에 msg대한 데이터 요청
        Client c = new Client(ip); // 서버에 연결 시도
        c.PushMsg(msg);
        c.ReadMsg();
        while(wait)
            System.out.println("wait...");
    }
}
