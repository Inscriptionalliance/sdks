package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.MintUserBox;

public interface MintUserBoxService extends IService<MintUserBox> {

    MintUserBox getByBoxNum(String boxNum);
}
