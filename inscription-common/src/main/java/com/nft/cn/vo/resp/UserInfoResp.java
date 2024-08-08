package com.nft.cn.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserInfoResp {

    private Integer status;

    private Integer isBind;

    private Integer isActivation;

    private Integer isBindTwitter;


    private Integer currentPage;

    private Integer isBindOkx;

    private Integer isBindUnisat;

    private Integer isBindReferee;

    private Integer isInterestTwitter;
    private Integer isForwardTwitter;

    private Integer isJoinDiscord;

    private Integer isVip;


}
