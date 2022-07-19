package com.bell.arithmetic.xs;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
public class Write5 {

    @ExcelProperty("订单编号")
    private String orderCode;
    @ExcelProperty("预约活动ID")
    private String actCode;
    @ExcelProperty("预约活动名称")
    private String title;
    @ExcelProperty("订单状态")
    private String orderStatus;
    @ExcelProperty("出行人姓名")
    private String username;
    @ExcelProperty("证件类型")
    private String credType;
    @ExcelProperty("证件号码")
    private String credNo;
    @ExcelProperty("出生日期")
    private Date birthday;

}
