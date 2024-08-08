package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.dao.UUserRefereeMapper;
import com.nft.cn.entity.UUserReferee;
import com.nft.cn.entity.UUserRefereeNum;
import com.nft.cn.service.UUserRefereeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UUserRefereeServiceImpl extends ServiceImpl<UUserRefereeMapper, UUserReferee> implements UUserRefereeService {

    @Override
    public List<String> getAllSubordinate(Long id) {
        return baseMapper.getAllSubordinate(id);
    }

    @Override
    public List<UUserReferee> getAllSubordinateList(Long id) {
        return baseMapper.getAllSubordinateList(id);
    }

    @Override
    public List<UUserReferee> selectChildListByAddress(String userAddress) {
        return baseMapper.selectChildListByAddress(userAddress);
    }

    @Override
    public List<UUserReferee> listNoRelation() {
        return baseMapper.listNoRelation();
    }


}
