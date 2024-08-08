package com.nft.cn.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TwitterTaskListResp {

    private Long taskId;

    private Integer twitterType;

    private String tweetId;

    private Integer valid;

    private List<TaskIncomeResp> taskIncomeRespList;
    private String depict;

    private Integer fulfil;

    private Integer taskType;

    private String twitterUserId;

    private String twitterUsername;

}
