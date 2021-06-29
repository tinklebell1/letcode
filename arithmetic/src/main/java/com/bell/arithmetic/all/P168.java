package com.bell.arithmetic.all;


public class P168 {
    public static void main(String[] args) {

        System.out.println(convertToTitle(2147483647));
    }

    public static  String convertToTitle(int n) {
        if (n <= 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        while (n > 0) {
            n--;
            sb.append((char) (n % 26 + 'A'));
            n =n / 26;
        }
        return sb.reverse().toString();
    }

}
