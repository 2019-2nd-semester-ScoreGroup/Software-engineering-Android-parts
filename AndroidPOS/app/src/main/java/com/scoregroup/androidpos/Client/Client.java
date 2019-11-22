package com.scoregroup.androidpos.Client;

public class Client {
    private static Client c;
    ClientManger cm;

    private Client(){}

    public static Client getInstance(){
        if(c == null)
            c = new Client();
        return c;
    }

    public void getDB(String msg){
        cm = new ClientManger(msg);
    }
}
