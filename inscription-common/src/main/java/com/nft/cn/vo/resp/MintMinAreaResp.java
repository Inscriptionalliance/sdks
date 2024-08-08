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
public class MintMinAreaResp {

    private Long id;

    private String userAddress;

    private Long userId;

    private BigDecimal mintNum;

    private BigDecimal mintNumYesterday;

}
