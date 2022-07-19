package com.bell.arithmetic.all;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class test {

    public static void main(String[] args) {
        getActIds();
    }

    public static void getActIds() {
        Set<String> actIds = new HashSet<>();
        String file = "/Users/belltinkle/my/code/java/letcode/letcode/test.txt";
        File textFile = new File(file);
        try (FileReader fileReader = new FileReader(textFile);
             BufferedReader br = new BufferedReader(fileReader)) {
            String line = null;
            while ((line = br.readLine()) != null) {
                actIds.add(line);
            }
            genOutFile(actIds);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void genOutFile(Set<String> strs){
        String file = "/Users/belltinkle/my/code/java/letcode/letcode/out.sql";
        StringBuilder write = new StringBuilder();
        try( FileOutputStream fileOutputStream = new FileOutputStream(file);
             BufferedOutputStream buff = new BufferedOutputStream(fileOutputStream);) {
            if(strs.size() > 0){
                for (String str : strs) {
                    write.append("UPDATE mtstat.partner_cost_binlog SET binlog_value = 0.00 WHERE id = '");
//                    write.append("'");
                    write.append(str);
//                    write.append("',\n");
                    write.append("';\n");
                }
                buff.write(write.toString().getBytes(StandardCharsets.UTF_8));
                buff.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


