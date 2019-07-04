package com.example.numbertoword;

import android.content.Context;

public class NumAdapter {

    private static final String[] Yekan = {
            "",
            "یک ", //[1]
            "دو ", //[2]
            "سه ", //[3]
            "چهار ", //[4]
            "پنج ", //[5]
            "شش ", //[6]
            "هفت ", //[7]
            "هشت ", //[8]
            "نه ", //[9]
            "ده ", //[10]
            "یازده ", //[11]
            "دوازده ", //[12]
            "سیزده ", //[13]
            "چهارده ", //[14]
            "پانزده ", //[15]
            "شانزده ", //[16]
            "هفده ", //[17]
            "هجده ", //[18]
            "نوزده ", //[19]
    };
    private static final String[] DahGun = {
            "بیست ", //[0]
            "سی ", //[1]
            "چهل ", //[2]
            "پنجاه ", //[3]
            "شصت ", //[4]
            "هفتاد ", //[5]
            "هشتاد ", //[6]
            "نود ", //[7]
    };
    private static final String[] SadGun = {
            "",
            "یکصد ", //[0]
            "دویست ", //[1]
            "سیصد ", //[2]
            "چهارصد ", //[3]
            "پانصد ", //[4]
            "شیشصد ", //[5]
            "هفتصد ", //[6]
            "هشتصد ", //[7]
            "نهصد ", //[8]

    };
    private static final String[] Unlimited = {
            "صد ", //[0]
            "هزار ", //[1]
            "میلیون ", //[2]
            "میلیارد ", //[3]
            "بیلیون ", //[4]
            "بیلیارد ", //[5]
    };

    public static String convertNumberToWords(long number , boolean rial , boolean tomaan) {

        String Unit = "";

        String s = null;
        int t = 0;
        while (number > 0) {
            if (number % 1000 != 0) {
                String s2 = convert9999(number % 1000);
                if (t > 0) {
                    s2 = s2 + "" + Unlimited[t]+ " و ";
                }
                if (s == null) {
                    s = s2;
                } else {
                    s = s2 + s;
                }
            }
            number /= 1000;
            t++;
        }
        if(s.endsWith(" و ")){

            s = s.substring(0,s.length() - 3);
        }
        if (rial){
            Unit= "ریال";
        }
        if (tomaan){
            Unit= "تومان";
        }
        return s + Unit;
    }

    // Range 0 to 9999.
    private static String convert9999(long n) {
        String s1 = Yekan[(int) (n / 1000)];
        String s2 = convert999(n % 1000);
        if (n <= 999) {
            return s2;
        } else if (n % 100 == 0) {
            return s1;
        } else {
            return s1 + "و " + s2;
        }
    }
    // Range 0 to 999.
    private static String convert999(long n) {
        String s1 = SadGun[(int) (n / 100)];
        String s2 = convert99(n % 100);
        if (n <= 99) {
            return s2;
        } else if (n % 100 == 0) {
            return s1;
        } else {
            return s1 + "و " + s2;
        }
    }

    // Range 0 to 99.
    private static String convert99(long n) {
        if (n < 20) {
            return Yekan[(int) n];
        }
        String s = DahGun[(int) (n / 10 - 2)];
        if (n % 10 == 0) {
            return s;
        }
        return s + "و " + Yekan[(int) (n % 10)];
    }
}
