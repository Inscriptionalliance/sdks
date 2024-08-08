package com.nft.cn.dao;

import com.nft.cn.entity.MintTicketRecord;
import com.nft.cn.entity.UUserReferee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nft.cn.entity.UUserRefereeNum;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UUserRefereeMapper extends BaseMapper<UUserReferee> {
    List<String> getAllSubordinate(@Param("id") Long id);

    List<UUserReferee> getAllSubordinateList(@Param("id") Long id);

    List<UUserReferee> selectChildListByAddress(@Param("userAddress") String userAddress);

    List<UUserReferee> selectChildListById(@Param("id") Long id);

    List<UUserReferee> listNoRelation();

    UUserReferee selectByUserAddress(@Param("userAddress") String userAddress);


    List<UUserReferee> listNoRelationByAddressList(@Param("list") List<String> list);

    List<UUserReferee> getAllSubordinateNoOrder(@Param("id") Integer id);

    Long getAllSubordinateCount(@Param("id") Long id);

    Integer selectChildCount(@Param("id") Long id);

    List<Long> selectChildIdList(@Param("id") Long id);

    List<UUserReferee> selectVipList(@Param("id") Long id);

    List<UUserReferee> selectBefore(@Param("id") Long id);

}
