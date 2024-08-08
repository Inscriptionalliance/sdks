package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.EpicTicketRecord;

public interface EpicTicketRecordService extends IService<EpicTicketRecord> {

    boolean saveEpicTicket(String userAddress, Long epicTicketNum, Integer type, String relationAddress);

}
