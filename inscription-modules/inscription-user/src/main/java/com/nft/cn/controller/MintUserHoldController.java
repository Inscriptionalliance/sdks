package com.nft.cn.controller;


import com.nft.cn.service.MintUserHoldService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/mintUserHold")
public class MintUserHoldController {

    @Autowired
    private MintUserHoldService mintUserHoldService;



}

