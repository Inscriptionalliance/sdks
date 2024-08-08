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
public class TeamInfoResp {

    private Integer isVip;

    private Integer isNodeVip;

    private Integer isAdvancedVip;

    private Integer isStandardVip;

    private Integer isWhite;

    private BigDecimal freeTicket;

    private BigDecimal mintTicket;

    private BigDecimal credit;

    private Long rank;

    private Long refereeNum;

    private Long validRefereeNum;

    private String userAddress;

    private String twitterUsername;




}
