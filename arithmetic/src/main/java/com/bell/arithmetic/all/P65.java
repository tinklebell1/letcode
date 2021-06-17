package com.bell.arithmetic.all;


public class P65 {
    public static void main(String[] args) {

    }
    public static boolean isNumber(String s) {
        String trim = "^[+-]?((\\d+\\.?)|(\\d*\\.?\\d+))([Ee][+-]?\\d+)?$";
        return s.trim().matches(trim);
    }
    public static boolean isNumber1(String s) {
        char[] c = s.toCharArray();
        int n = s.length();
        boolean numFlag = false, dotFlag = false, eFlag = false;
        for (int i = 0; i < n; i++) {
            if(c[i] >= '0' && c[i] <= '9'){
                numFlag = true;
            }else if(c[i] == '.' && !dotFlag && !eFlag){
                dotFlag = true;
            }else if((c[i] == 'e' || c[i] == 'E') && !eFlag && numFlag){
                eFlag = true;
                numFlag = false; // 出现e之后重新判断整数
            }else if((c[i] == '+' || c[i] == '-') && (i == 0 || (c[i - 1] == 'e' || c[i -1] == 'E'))){
                continue;
            }else{
                return false;
            }
        }
        return numFlag;
    }
}
