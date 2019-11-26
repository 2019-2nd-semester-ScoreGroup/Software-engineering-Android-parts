package com.scoregroup.androidpos.util;

public class EAN13 {
    public static boolean checkAvailability(String barcode) {
        if (barcode.length() != 13) return false;
        if(!barcode.substring(0,3).equals("880"))return false;
        int[] arrs = new int[13];
        int index = 0;
        for (char buffer : barcode.toCharArray()) {
            arrs[index++] = buffer - '0';
        }
        int even = 0, odd = 0;
        for (int i=0;i<12;i++) {
            if (i % 2 == 0)even += arrs[i];
            else odd += arrs[i];
        }
        if(10-((odd*3+even)%10)==arrs[12])return true;
        else return false;
    }
}
