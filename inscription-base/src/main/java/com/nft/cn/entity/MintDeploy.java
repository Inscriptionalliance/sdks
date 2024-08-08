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
public class MintDeploy implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String accord;

    private String ethscription;

    private String tick;

    private BigDecimal mintAll;

    private BigDecimal mintNum;

    private BigDecimal mintOne;

    private Integer fileType;

    private String deployAddress;

    private String hash;

    private Long blockNum;

    private Integer peopleNum;

    private BigDecimal tradeAmount;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
