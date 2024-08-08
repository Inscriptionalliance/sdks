package com.nft.cn.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MintTransferListResp {

    private String userAddress;

    private Integer type;

    private BigDecimal mintNum;

    private String fromAddress;

    private String toAddress;

    private LocalDateTime createTime;



}
