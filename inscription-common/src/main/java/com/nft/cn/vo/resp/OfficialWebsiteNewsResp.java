package com.nft.cn.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OfficialWebsiteNewsResp {

    private OfficialWebsiteNewsFirstResp officialWebsiteNewsFirstResp;

    private List<OfficialWebsiteAnnouncementListResp> officialWebsiteAnnouncementListRespList;

    private PageRespVO<OfficialWebsiteNewsListResp> officialWebsiteNewsListRespList;



}
