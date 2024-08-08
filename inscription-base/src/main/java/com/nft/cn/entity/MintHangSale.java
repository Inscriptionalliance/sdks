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
public class MintHangSale implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long userId;

    private String userAddress;

    private Long deployId;

    private String accord;

    private String tick;

    private BigDecimal unitPrice;

    private BigDecimal totalPrice;

    private String payAccord;

    private BigDecimal mintNum;

    private String hangSaleHash;

    private String withdrawHash;

    private String withdrawFreeHash;

    private String payHash;

    private String payAmountHash;

    private String payUserAddress;

    private String mintJson;

    private Integer status;

    private Integer fileType;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
