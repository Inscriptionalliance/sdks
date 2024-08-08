package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.UUserReferee;
import com.nft.cn.entity.UUserRefereeNum;

import java.util.List;

public interface UUserRefereeService extends IService<UUserReferee> {

    List<String> getAllSubordinate(Long id);

    List<UUserReferee> getAllSubordinateList(Long id);

    List<UUserReferee> selectChildListByAddress(String userAddress);

    List<UUserReferee> listNoRelation();

}
