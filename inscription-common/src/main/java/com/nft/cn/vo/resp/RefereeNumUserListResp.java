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
public class RefereeNumUserListResp {

    @JsonFormat(pattern = "yyyy.MM.dd",timezone="Asia/Shanghai")
    private LocalDateTime refereeTime;

    private String refereeNum;

    private String refereeAddress;

    private Integer incomeStatus;

}
