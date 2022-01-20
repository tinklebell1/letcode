package com.bell.arithmetic.partner.dto.read;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class PDate {

    private String partnerId;

    private String name;

    private Date date;

}
