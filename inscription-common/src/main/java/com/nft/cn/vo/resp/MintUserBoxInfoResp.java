package com.nft.cn.vo.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MintUserBoxInfoResp {

    private Integer isMintBox;


    private String userAddress;

    private String num;

    private String accord;

    private String createHash;

    private Long blockNum;

    private Integer status;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss",timezone="Asia/Shanghai")
    private LocalDateTime createTime;

}
