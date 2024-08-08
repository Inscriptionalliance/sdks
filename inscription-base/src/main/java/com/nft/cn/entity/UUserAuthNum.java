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
public class UUserAuthNum implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long userId;

    private String userAddress;

    private String payBoxAuthNum;

    private LocalDateTime payBoxAuthNumExpire;

    private String payBoxAuthNumJson;

    private Integer payBoxAuthNumStatus;

    private String mintAuthNum;

    private LocalDateTime mintAuthNumExpire;

    private String mintAuthNumJson;

    private Integer mintAuthNumStatus;

    private String mintTransferAuthNum;

    private LocalDateTime mintTransferAuthNumExpire;

    private String mintTransferAuthNumJson;

    private Integer mintTransferAuthNumStatus;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
