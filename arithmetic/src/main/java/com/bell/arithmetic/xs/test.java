package com.bell.arithmetic.xs;

import java.util.ArrayList;
import java.util.List;

public class test {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("dadsa");

        if(list.size() > 10){
            list = list.subList(0, 10);
        }


    }
}
