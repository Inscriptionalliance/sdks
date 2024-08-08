package com.nft.cn.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
public class MintCommunityTeamAchieve implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long userId;

    private String userAddress;

    private BigDecimal mintNum;

    private BigDecimal paidMintNum;

    private Long communityUserIdMax;

    private String communityUserAddressMax;

    private Long communityUserIdPaidMax;

    private String communityUserAddressPaidMax;

    private BigDecimal communityMintNumMax;

    private BigDecimal communityPaidMintNumMax;

    private BigDecimal communityMintNum;

    private BigDecimal communityPaidMintNum;

    private BigDecimal teamMintNum;

    private BigDecimal teamPaidMintNum;

    private BigDecimal refereeMintNum;

    private BigDecimal refereePaidMintNum;

    private Integer isInit;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @TableField(exist = false)
    private Integer isVip;
    @TableField(exist = false)
    private Integer isNodeVip;
    @TableField(exist = false)
    private Integer isAdvancedVip;


}
