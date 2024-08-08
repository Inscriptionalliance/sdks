package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.UUser;
import com.nft.cn.entity.UUserRefereeNum;
import com.nft.cn.vo.resp.InvalidRefereeNumListResp;
import com.nft.cn.vo.resp.RefereeNumUserListResp;
import com.nft.cn.vo.resp.TeamInfoResp;
import com.nft.cn.vo.resp.ValidRefereeNumListResp;

import java.util.List;

public interface UUserRefereeNumService extends IService<UUserRefereeNum> {


    UUserRefereeNum getByRefereeNum(String refereeNum);

    List<ValidRefereeNumListResp> validRefereeNumList();

    List<InvalidRefereeNumListResp> invalidRefereeNumList();

    List<RefereeNumUserListResp> refereeNumUserList();


    void awardRefereeNum(UUser uUser);

    List<UUserRefereeNum> selectUser50();

}
