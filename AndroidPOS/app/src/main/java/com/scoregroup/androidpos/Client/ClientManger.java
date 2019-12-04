package com.scoregroup.androidpos.Client;

import android.util.Log;

public class ClientManger {
    private static ClientManger cm; // 액티비티가 모두 접근 가능
    private static String ip = "192.168.43.148";// 아이피를 저장

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
    /**
     * 현재 "getStocks" 작동 확인
     * Network Manager에서 받은 소켓에서 보내는 데이터를 해석하고 DBManager를 호출한 후, ack를 반환
     * 인수와 반환값은 String
     * []안의 값은 대치되어야 함
     * @param "editStock" + " " + [key] + " " + [name] + " " + [price]
     * @return  true or false : 성공, 실패
     * @param "getStock"
     * @return [key] [name] [price] : 띄워쓰기로 연결된 문자열
     * @param "getStocks"
     * @return [key] [name] [price],[key] [name] [price] [amount] ,... : stock마다 ,로 구분된 띄워쓰기로 연결된 문자열
     * @param "getEvent" + " " + [eventKey]
     * @return [type] [time] [memo] [c.stockKey] [c.amount] [c.eventKey] + [c.key],... : change마다 ,로 구분된 띄워쓰기로 연결된 문자열
     * @param "tryChangEvent" + " " + [eventKey] + " " + [status] : status(0 Normal, 1 Cancel, 2 NaN)
     * @return true or false : 성공, 실패
     * @param "getEventList" + " " + [type] : type(1 Sell, 2 delivery, 3 NaN)
     * @return [key] [type] [totalPrice],... : event마다 ,로 구분된 띄워쓰기로 연결된 문자열
     * @param "getSelling" + " " + [startTime] + " " + [endTime] : startTime, endTime은 타임스탬프 형식(yyyy-MM-dd hh:mm:ss)
     * @return [key] [name] [price] [amount],... : startTime과 endTime 사이에 판매된 stock마다 ,로 구분된 띄워쓰기로 연결된 문자열
     * @param "addEvent" + " " + [type] + " " + [time] + " " + [status] + " " + [memo]
     * @return [eventKey]
     * @param "addChange" + " " + [eventKey] + " " + [stockKey] + " " + [changedAmount]
     * @return [eventKey]
     */
    public Client getDB(String msg) {
        Log.i("ju", "클라인트매니저 겟디비");
        Client t = new Client().setIP(ip).setMsg(msg);
        return t;
    }
}
