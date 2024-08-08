package com.nft.cn.form;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LoginPasswordForm {


    private String userAddress;

    private String password;

    private BigDecimal userPower;


    private String refereeUserAddress;

    private String ethAddress;

    private String sign;

    private String msg;

}
