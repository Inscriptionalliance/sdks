package com.nft.cn.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OfficialWebsiteNews implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Integer type;

    private String titleEn;

    private String titleZh;

    private String contentEn;

    private String contentZh;

    private String contentEnText;

    private String contentZhText;

    private String contentEnImg;

    private String contentZhImg;

    private Long weight;

    private LocalDateTime releaseTime;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
