package com.bell.arithmetic.xs;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class Write6 {

    @ExcelProperty("用户ID")
    private String userId;
    @ExcelProperty("用户昵称")
    private String username;
    @ExcelProperty("注册手机号码")
    private String mobile;
    @ExcelProperty("累积消费金额")
    private int sum;

}
