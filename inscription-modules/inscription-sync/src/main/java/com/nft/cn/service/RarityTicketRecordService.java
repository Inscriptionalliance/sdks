package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.RarityTicketRecord;

public interface RarityTicketRecordService extends IService<RarityTicketRecord> {

    boolean saveRarityTicket(String userAddress, Long rarityTicketNum, Integer type, String relationAddress);

}
