package com.nft.cn.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MintSwapInfoResp {

    private String userAddress;

    private BigDecimal mintSwapFreeAmount;

    private BigDecimal mintNum;

    private BigInteger mintSwapMinNum;





}
