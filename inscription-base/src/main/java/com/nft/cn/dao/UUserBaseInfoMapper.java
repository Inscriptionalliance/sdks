package com.nft.cn.dao;

import com.nft.cn.entity.UUserBaseInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UUserBaseInfoMapper extends BaseMapper<UUserBaseInfo> {

    UUserBaseInfo selecttest1(@Param("userId") Long userId);

    UUserBaseInfo selectCounttest1(@Param("userId") Long userId);

    List<UUserBaseInfo> getVipUserList(@Param("userId") Long userId);

}
