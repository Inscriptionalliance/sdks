package com.nft.cn.vo.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nft.cn.comfig.LocalDateTimeConfig;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OfficialWebsiteNewsFirstResp {

    private Long id;

    private String title;

    private String content;

    @JsonSerialize(using = LocalDateTimeConfig.LocalDateTimeSerializer.class)
    private LocalDateTime releaseTime;

    private List<String> imgList;


}
