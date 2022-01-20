package com.bell.arithmetic.partner.dto.write;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@EqualsAndHashCode
public class PSalesAndImage {

    @ExcelProperty("供应商Id")
    private String partnerId;
    @ExcelProperty("供应商name")
    private String partnerName;
    @ExcelProperty("营业执照图片地址")
    private String img;
    @ExcelProperty("销售额")
    private BigDecimal sale;
    @ExcelProperty("经营额度特别标识")
    private int saleFlag;
    @ExcelProperty("本地图片地址")
    private String localImg;
    @ExcelProperty("分数")
    private String score;
    @ExcelProperty("麦淘url")
    private String mtUrl;
    @ExcelProperty("创建时间")
    private String date;

}
