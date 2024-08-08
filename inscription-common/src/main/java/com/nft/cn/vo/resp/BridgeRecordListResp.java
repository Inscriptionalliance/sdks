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
public class BridgeRecordListResp {

    private Long id;

    private String userAddress;

    private BigDecimal targetNum;

    private String targetLink;

    private String targetAccord;

    private String targetInscription;

    private String contractHash;

    private String transferHash;

    @JsonFormat(pattern = "yyyy.MM.dd HH:mm:ss",timezone="Asia/Shanghai")
    private LocalDateTime createTime;


}
