package com.bell.arithmetic.spi;

import java.util.ServiceLoader;

public class Test {
    public static void main(String[] args) {
        ServiceLoader<Search> s = ServiceLoader.load(Search.class);
        for (Search search : s) {
            search.searchDocs("hello world");
        }
    }
}
