package com.bell.arithmetic.partner.dto.read;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class PNameScore {

    private String partnerId;
    private String name;
    private String score;

}
