package com.nft.cn.entity;

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
public class NonceBridge implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userAddress;

    private Integer nonce;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
