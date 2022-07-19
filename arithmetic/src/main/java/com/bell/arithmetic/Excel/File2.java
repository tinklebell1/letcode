package com.bell.arithmetic.Excel;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class File2 {

    private String userId;

    private String mobile;

    private String nickName;

    private Date orderTime;

    private String brokerId;
}
