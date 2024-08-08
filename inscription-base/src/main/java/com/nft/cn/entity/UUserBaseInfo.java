package com.nft.cn.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UUserBaseInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long userId;

    private String userAddress;

    private Integer currentPage;

    private Integer isBindOkx;

    private Integer isBindUnisat;

    private Integer isBindReferee;

    private Integer isInterestTwitter;

    private Integer isForwardTwitter;

    private Integer isJoinDiscord;

    private Integer isVip;

    private Integer isNodeVip;

    private Integer isAdvancedVip;

    private Integer isStandardVip;

    private Integer isWhite;

    private BigDecimal freeTicket;

    private BigDecimal mintTicket;

    private BigDecimal credit;

    private Long rarityTicket;

    private Long epicTicket;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private LocalDateTime lastLoginTime;


}
