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
public class LotteryDrawTypeInfoResp {

    private Integer type;

    private BigDecimal depleteNum;

    private Integer depleteUnit;

    private BigDecimal userHoldNum;

    private Integer valid;

}
