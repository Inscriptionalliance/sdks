package com.nft.cn.vo.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class VipUserInfoResp {

    private Integer isHave;

    private Integer type;

    private String num;

    private String userAddress;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss",timezone="Asia/Shanghai")
    private LocalDateTime createTime;

    private String communityName;

    private Integer teamNum;

    private BigDecimal teamMint;

    private String hash;

    private BigDecimal divvyAmount;

    private BigDecimal freeMintNum;

    private BigDecimal paidMintNum;



    private BigDecimal communityMintNum;

    private Integer communityPartAddress;

    private BigDecimal communityMintNumYesterday;

    private Integer refereeAddressNum;

    private BigDecimal refereeMintNum;

    private BigDecimal refereeMintNumYesterday;




    private BigDecimal bigAreaMintNum;

    private BigDecimal bigAreaMintNumYesterday;

    private List<MintMinAreaResp> minMintAreaList;


    private List<MintRefereeUserResp> refereeUserRespList;

}
