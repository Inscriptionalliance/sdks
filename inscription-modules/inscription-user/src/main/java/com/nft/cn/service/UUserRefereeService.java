package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.UUser;
import com.nft.cn.entity.UUserReferee;
import com.nft.cn.vo.req.PageReqVO;
import com.nft.cn.vo.resp.LastRefereeResp;
import com.nft.cn.vo.resp.PageRespVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UUserRefereeService extends IService<UUserReferee> {

    void saveRelationship(UUser newUser, String refereeUserAddress, String refereeNum);

    PageRespVO<LastRefereeResp> lastReferee(PageReqVO pageReqVO);

    List<String> getAllSubordinate(Long id);

    List<UUserReferee> getAllSubordinateList(Long id);

    List<UUserReferee> selectChildList(Long id);

    UUserReferee selectByUserAddress(String userAddress);


    List<UUserReferee> listNoRelation();

    List<UUserReferee> listNoRelation(List<String> userAddressList);


    Long getAllSubordinateCount(Long id);

    Integer selectChildCount(Long id);

    List<Long> selectChildIdList(Long id);

    List<UUserReferee> selectVipList(Long id);

}
