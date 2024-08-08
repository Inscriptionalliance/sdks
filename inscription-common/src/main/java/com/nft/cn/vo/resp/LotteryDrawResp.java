package com.nft.cn.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class LotteryDrawResp {

    private Integer winLottery;

    private Integer type;

    private String prizeName;

    private String prizeDepict;

    private BigDecimal prizeNum;

    private Integer prizeUnit;


}
