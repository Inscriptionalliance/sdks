package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.dao.BtiaIncomeMapper;
import com.nft.cn.entity.BtiaIncome;
import com.nft.cn.entity.UUser;
import com.nft.cn.service.BtiaIncomeService;
import com.nft.cn.service.I18nService;
import com.nft.cn.service.MintUserService;
import com.nft.cn.service.UUserService;
import com.nft.cn.util.BaseResult;
import com.nft.cn.util.Hex;
import com.nft.cn.vo.req.PageReqVO;
import com.nft.cn.vo.req.PledgeIncomeReceiveReq;
import com.nft.cn.vo.resp.PageRespVO;
import com.nft.cn.vo.resp.PledgeIncomeListResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class BtiaIncomeServiceImpl extends ServiceImpl<BtiaIncomeMapper, BtiaIncome> implements BtiaIncomeService {


}
