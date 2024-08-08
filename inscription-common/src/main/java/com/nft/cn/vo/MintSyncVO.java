package com.nft.cn.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MintSyncVO {

    private String from;

    private String to;

    private String hash;

    private String mintJson;

    private Long blockNum;

}
