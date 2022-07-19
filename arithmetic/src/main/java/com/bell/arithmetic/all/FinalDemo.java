package com.bell.arithmetic.all;

import org.elasticsearch.search.sort.MinAndMax;

public class FinalDemo {

    private int a;  //普通域
    private final int b; //final域
    private static FinalDemo finalDemo;

    public FinalDemo() {
        System.out.println("111");
        a = 1; // 1. 写普通域
        b = 2; // 2. 写final域
    }

    public static void writer() {
        finalDemo = new FinalDemo();
        System.out.println("@222");
    }

    public static void reader() {
        System.out.println("33333");
        FinalDemo demo = finalDemo; // 3.读对象引用
        int a = demo.a;    //4.读普通域
        int b = demo.b;    //5.读final域
        System.out.println("44444");
    }
}
class A{
    public static void main(String[] args) {
        FinalDemo.reader();
    }
}
