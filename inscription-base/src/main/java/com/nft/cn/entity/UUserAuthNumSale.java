package com.nft.cn.entity;

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
public class UUserAuthNumSale implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long userId;

    private String userAddress;

    private String withdrawAuthNum;

    private LocalDateTime withdrawAuthNumExpire;

    private String withdrawAuthNumJson;

    private Integer withdrawAuthNumStatus;

    private String payAuthNum;

    private LocalDateTime payAuthNumExpire;

    private String payAuthNumJson;

    private Integer payAuthNumStatus;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
