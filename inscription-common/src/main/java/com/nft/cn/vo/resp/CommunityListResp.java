package com.nft.cn.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CommunityListResp {

    private Long id;

    private String name;

    private String num;

    private String logoImage;

    private Long likeNum;

    private Long partakeNum;

    private Integer todayLike;

    private Integer area;

}
