package com.nft.cn.vo.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MintMintUserListResp {


    private Long id;

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

    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy.MM.dd HH:mm:ss",timezone="Asia/Shanghai")
    private LocalDateTime updateTime;

    private BigDecimal mintNumFree;

    private BigDecimal mintNumPaid;
}
