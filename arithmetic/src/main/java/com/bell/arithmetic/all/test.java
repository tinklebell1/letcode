package com.bell.arithmetic.all;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class test {

    public static void main(String[] args) {

        getActIds();

    }

    public static void getActIds() {
        List<String> actIds = new ArrayList<>();
        String file = "test.txt";
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

    public static void genOutFile(List<String> strs){
        String file = "out.sql";
        StringBuilder write = new StringBuilder();
        try( FileOutputStream fileOutputStream = new FileOutputStream(file);
             BufferedOutputStream buff = new BufferedOutputStream(fileOutputStream);) {
            if(strs.size() > 0){
                for (String str : strs) {
                    write.append("update maitao.act_city set status=9 where act_city_id='");
                    write.append(str);
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


