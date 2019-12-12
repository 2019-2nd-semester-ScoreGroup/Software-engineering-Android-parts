package com.scoregroup.androidpos;

import java.util.StringTokenizer;

public class Test {
    public static void main(String[] args) {
        String msg = "asd 12_3 dsa";
        StringTokenizer stringTokenizer = new StringTokenizer(msg, " ");

        String msg1 = stringTokenizer.nextToken();

        System.out.println(msg1);

        String msg2 = "";
        System.out.println(msg2);
        msg2 = msg2.concat(stringTokenizer.nextToken());
        System.out.println(msg2);
        String msg3 = msg2.replace("_", " ");
        System.out.println(msg3);
        String msg4 = stringTokenizer.nextToken();
        System.out.println(msg4);
    }
}
