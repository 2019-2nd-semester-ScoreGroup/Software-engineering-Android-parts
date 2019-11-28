package com.scoregroup.androidpos.Client;

public class ClientManger {
    private static ClientManger cm; // 액티비티가 모두 접근 가능
    private static String ip = "127.0.0.1";// 아이피를 저장
    private static boolean wait = true; // 응답을 기다리는 변수

    private ClientManger(){} // 임의로 생성 불가

    public static ClientManger getInstance(){ // 클라이언트 매니저는 한번만 생성되어 여러 액티비티에서 사용
        if(cm == null)
            cm = new ClientManger();
        return cm;
    }

    public static boolean get_wait(){ // 데이터에 응답을 기다리는 메소드
        return wait;
    }

    public static void wait_false(){ // 데이터에 응답을 기다리는 메소드
        wait = false;
    }

    public static void set_IP(String ip_num){ // IP를 기억함
        ip = ip_num;
    }

    public String getDB(String msg){ // DB와 통신을 위한 메소드
        Client c = new Client(ip); // 서버에 연결 시도

        if(c.Communication(msg) == 2) // 2실패, 예외 처리
            return "NetworkError";
        else{ // 연결 성공
            while(wait) // DB로 온 데이터를 기다림
                System.out.println("data wait...");
            wait = true; // 통신 완료 시 다시 응답을 기다리게

            return c.send_Data(); // DB로 온 데이터 전송
        }
    }
}
