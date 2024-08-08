package com.nft.cn.dao;

import com.nft.cn.entity.UserLogin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;

public interface UserLoginMapper extends BaseMapper<UserLogin> {

    Integer countByUserAddress(@Param("startDay") LocalDate startDay, @Param("endDay") LocalDate endDay);

}
