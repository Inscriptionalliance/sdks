package com.nft.cn.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class MintCommunityPaidAchieve implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long userId;

    private String userAddress;

    private BigDecimal paidMintNum;

    private BigDecimal paidMintNum1;

    private BigDecimal paidMintNum2;

    private BigDecimal paidMintNum3;

    private BigDecimal paidMintNum4;

    private BigDecimal teamPaidMintNum;

    private BigDecimal teamPaidMintNum1;

    private BigDecimal teamPaidMintNum2;

    private BigDecimal teamPaidMintNum3;

    private BigDecimal teamPaidMintNum4;

    private BigDecimal refereePaidMintNum;

    private BigDecimal refereePaidMintNum1;

    private BigDecimal refereePaidMintNum2;

    private BigDecimal refereePaidMintNum3;

    private BigDecimal refereePaidMintNum4;

    private Integer isInit;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
