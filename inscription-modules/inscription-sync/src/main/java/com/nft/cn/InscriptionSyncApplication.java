package com.nft.cn;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//@EnableDiscoveryClient
@EnableScheduling
@EnableAsync
@MapperScan("com.nft.cn.dao")
public class InscriptionSyncApplication {
    public static void main(String[] args) {
        SpringApplication.run(InscriptionSyncApplication.class, args);
    }
}
