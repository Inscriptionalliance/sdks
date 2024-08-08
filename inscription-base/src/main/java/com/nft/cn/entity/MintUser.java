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
public class MintUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long userId;

    private String userAddress;

    private BigDecimal mintNum;

    private BigDecimal amount;

    private Long deployId;

    private String accord;

    private String tick;

    private BigDecimal mintTicket;

    private BigDecimal freeTicket;

    private String payHash;

    private String hash;

    private String mintJson;

    private Integer status;

    private Long blockNum;

    private Integer isSync;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
