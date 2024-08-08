package com.nft.cn.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MintRankUserResp {


    private String userAddress;

    private Long rank;

    private BigDecimal mintNum;

    private BigDecimal mintNumYesterday;

}
