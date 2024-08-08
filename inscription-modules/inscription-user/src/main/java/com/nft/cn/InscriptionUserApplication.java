package com.nft.cn;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.nft.cn.dao")
@EnableScheduling
public class InscriptionUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(InscriptionUserApplication.class, args);
    }






}
