package com.nft.cn.vo.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class MintDeployInfoResp {

    private String rank;

    private BigDecimal myMintNum;

    private String castRatio;

    private Integer isMintBox;

    private Integer isAllowMint;

    private String accord;

    private String ethscription;

    private String tick;

    private String mintAll;

    private BigDecimal mintNum;

    private BigDecimal mintOne;

    private String hash;

    private Integer peopleNum;

    private String deployAddress;

    private Long blockNum;

    private BigDecimal tradeAmount;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss",timezone="Asia/Shanghai")
    private LocalDateTime createTime;

}
