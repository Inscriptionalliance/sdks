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
public class MintUserBoxBridgePayResp {

    private String sign;

    private BigDecimal free;

    private BigDecimal price;

    private String link;

    private String accord;

    private String inscription;

}
