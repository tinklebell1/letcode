package com.bell.arithmetic.spi;

import lombok.extern.slf4j.Slf4j;

import java.util.List;


public class FileSearch implements Search{
    @Override
    public List<String> searchDocs(String keyword) {
        System.out.println("文件搜索 " + keyword);
        return null;
    }
}
