package com.bell.arithmetic.partner.dto.read;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@EqualsAndHashCode
@NoArgsConstructor
@Getter
@Setter
public class Sales {

    private String partnerId;

    private BigDecimal sales;

    private String sales1;

}
