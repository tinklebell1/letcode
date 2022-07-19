package com.bell.arithmetic.Excel.write;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
public class WriteFile1 {
    @ExcelProperty("用户Id")
    private String userId;
    @ExcelProperty("佣金数量")
    private String amount;
    @ExcelProperty("用户手机号")
    private String mobile;
    @ExcelProperty("用户昵称")
    private String nickname;
    @ExcelProperty("最近下单时间")
    private Date orderTime;
    @ExcelProperty("合伙人id")
    private String brokerId;
    @ExcelProperty("是否关注合伙人公众号")
    private String followed;
}
