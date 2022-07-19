package com.bell.arithmetic;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.bell.arithmetic.Excel.File2;
import com.bell.arithmetic.Excel.write.WriteFile1;
import com.bell.arithmetic.partner.AESUtilForShardingsphere;
import com.bell.arithmetic.xs.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@SpringBootTest
@Slf4j
public class XSTest {

    private static final String PATH = "/Users/belltinkle/work/sql/xsang/13551/";

    @Test
    void contextLoads() {
        String base = PATH + "22.xlsx";
        String user = PATH + "222.xlsx";

        String write11 = PATH + "2测试.xlsx";

        List<File5> baseList = new ArrayList<>();
        List<File6> userList = new ArrayList<>();
        List<Write5> write5List = new ArrayList<>();

        EasyExcel.read(base, File5.class, new PageReadListener<File5>(dataList -> {
            for (File5 p : dataList) {
                baseList.add(p);
            }
        })).sheet().doRead();
        Map<String, File5> baseMap = baseList.stream().collect(Collectors.toMap(File5::getOrderId, Function.identity()));

        EasyExcel.read(user, File6.class, new PageReadListener<File6>(dataList -> {
            for (File6 p : dataList) {
                userList.add(p);
            }
        })).sheet().doRead();

        for (File6 file6 : userList) {
            Write5 write5 = new Write5();
            if(baseMap.containsKey(file6.getOrderId())){
                File5 file5 = baseMap.get(file6.getOrderId());
                write5.setOrderCode(file5.getOrderCode());
                write5.setActCode(file5.getActivityCode());
                write5.setTitle(file5.getTitle());
                if(file5.getStatus().equals("1")){
                    write5.setOrderStatus("已付款");
                }else if(file5.getStatus().equals("6")){
                    write5.setOrderStatus("已确认");
                }else if(file5.getStatus().equals("8")){
                    write5.setOrderStatus("已完成");
                }else{
                    write5.setOrderStatus("未知状态");
                }
            }
            write5.setBirthday(file6.getBirthdate());
            write5.setCredNo(AESUtilForShardingsphere.decrypt(file6.getCredNo()));
            if(StringUtils.isNotBlank(file6.getCredType())){
                if(file6.getCredType().equals("1")){
                    write5.setCredType("身份证");
                }else if(file6.getCredType().equals("3")){
                    write5.setCredType("护照");
                }else if(file6.getCredType().equals("8")){
                    write5.setCredType("港澳通行证");
                }else if(file6.getCredType().equals("4")){
                    write5.setCredType("军官证");
                }else if(file6.getCredType().equals("5")){
                    write5.setCredType("士兵证");
                }else if(file6.getCredType().equals("6")){
                    write5.setCredType("回乡证");
                }else if(file6.getCredType().equals("7")){
                    write5.setCredType("台胞证");
                }else{
                    write5.setCredType("未知状态");
                }
            }

            write5.setUsername(AESUtilForShardingsphere.decrypt(file6.getRealName()));

            write5List.add(write5);
        }


        EasyExcel.write(write11, Write5.class)
                .sheet("用户参与活动基础信息表")
                .doWrite(() -> {
                    // 分页查询数据
                    return write5List;
                });

    }

    @Test
    void test2() {
        String sum = PATH + "2222.xlsx";
        String write11 = PATH + "2测试2.xlsx";

        List<Write6> writeList = new ArrayList<>();
        List<File7> file7s = new ArrayList<>();

        EasyExcel.read(sum, File7.class, new PageReadListener<File7>(dataList -> {
            for (File7 p : dataList) {
                file7s.add(p);
            }
        })).sheet().doRead();
        for (File7 file7 : file7s) {
            Write6 write6 = new Write6();
            write6.setMobile(AESUtilForShardingsphere.decrypt(file7.getMobile()));
            write6.setSum(file7.getPaidFee());
            write6.setUsername(file7.getNickname());
            write6.setUserId(file7.getUserId());
            writeList.add(write6);
        }

        EasyExcel.write(write11, Write6.class)
                .sheet("用户参与活动基础信息表")
                .doWrite(() -> {
                    // 分页查询数据
                    return writeList;
                });
    }
}
