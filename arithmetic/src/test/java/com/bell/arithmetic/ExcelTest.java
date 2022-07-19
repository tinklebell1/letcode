package com.bell.arithmetic;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.alibaba.fastjson.JSON;
import com.bell.arithmetic.Excel.File1;
import com.bell.arithmetic.Excel.File2;
import com.bell.arithmetic.Excel.File3;
import com.bell.arithmetic.Excel.File4;
import com.bell.arithmetic.Excel.write.WriteFile1;
import com.bell.arithmetic.partner.AESUtilForShardingsphere;
import com.bell.arithmetic.partner.dto.read.PDate;
import com.bell.arithmetic.partner.dto.write.PSalesAndImage;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@SpringBootTest
@Slf4j
public class ExcelTest {

    private static final String PATH = "/Users/belltinkle/work/sql/zliang/";

    @Test
    void contextLoads() {

        String orderDate = PATH + "1111.xlsx";
        String amountData = PATH + "0000.xlsx";
        String followData = PATH + "2222.xlsx";
        String brokerData = PATH + "3333.xlsx";
        String baseData = PATH + "4444.xlsx";
        String write = PATH + "13537.xlsx";
        List<File2> file2s = new ArrayList<>();
        List<File1> file1s = new ArrayList<>();
        List<File3> file3s = new ArrayList<>();
        List<File4> file4s = new ArrayList<>();
        List<File4> file5s = new ArrayList<>();


        List<WriteFile1> writeFile1s = new ArrayList<>();



        EasyExcel.read(orderDate, File2.class, new PageReadListener<File2>(dataList -> {
            for (File2 p : dataList) {
                file2s.add(p);
            }
        })).sheet().doRead();
        Map<String, File2> file2Map = file2s.stream().collect(Collectors.toMap(File2::getUserId, Function.identity()));

        EasyExcel.read(brokerData, File4.class, new PageReadListener<File4>(dataList -> {
            for (File4 p : dataList) {
                file4s.add(p);
            }
        })).sheet().doRead();

        Map<String, File4> file4Map = new HashMap<>();
        for (File4 file4 : file4s) {
            if(file4Map.containsKey(file4.getUserId())){
                continue;
            }
            file4Map.put(file4.getUserId(), file4);
        }

        EasyExcel.read(baseData, File4.class, new PageReadListener<File4>(dataList -> {
            for (File4 p : dataList) {
                file5s.add(p);
            }
        })).sheet().doRead();
        Map<String, File4> file5Map = file5s.stream().collect(Collectors.toMap(File4::getUserId, Function.identity()));


        EasyExcel.read(amountData, File1.class, new PageReadListener<File1>(dataList -> {
            for (File1 p : dataList) {
                file1s.add(p);
            }
        })).sheet().doRead();

        EasyExcel.read(followData, File3.class, new PageReadListener<File3>(dataList -> {
            for (File3 p : dataList) {
                file3s.add(p);
            }
        })).sheet().doRead();
        Map<String, File3> file3Map = new HashMap<>();
        for (File3 file3 : file3s) {
            if(file3Map.containsKey(file3.getUserId())){
                log.info(JSON.toJSONString(file3));
                file3Map.put(file3.getUserId(), file3);
            }
            file3Map.put(file3.getUserId(), file3);
        }

        for (File1 file1 : file1s) {

            WriteFile1 writeFile1 = new WriteFile1();
            writeFile1.setAmount(file1.getAmount());
            writeFile1.setUserId(file1.getUserId());
            if(file2Map.containsKey(file1.getUserId())){
                File2 file2 = file2Map.get(file1.getUserId());
                writeFile1.setOrderTime(file2.getOrderTime());
            }
            if(file4Map.containsKey(file1.getUserId())){
                File4 file4 = file4Map.get(file1.getUserId());
                writeFile1.setBrokerId(file4.getBrokerId());
            }
            if(file5Map.containsKey(file1.getUserId())){
                File4 file5 = file5Map.get(file1.getUserId());
                writeFile1.setMobile(AESUtilForShardingsphere.decrypt(file5.getMobile()));
                writeFile1.setNickname(file5.getNickName());
            }
            if(file3Map.containsKey(file1.getUserId())){
                writeFile1.setFollowed(StringUtils.hasText(file3Map.get(file1.getUserId()).getFollowed())?file3Map.get(file1.getUserId()).getFollowed() : "未关注");
            }else{
                writeFile1.setFollowed("未关注");
            }
            writeFile1s.add(writeFile1);
        }

        EasyExcel.write(write, WriteFile1.class)
                .sheet("sheet1")
                .doWrite(() -> {
                    // 分页查询数据
                    return writeFile1s;
                });







    }
}
