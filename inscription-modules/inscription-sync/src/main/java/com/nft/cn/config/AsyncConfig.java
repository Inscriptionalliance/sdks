package com.nft.cn.config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.*;

@Configuration
@EnableAsync
@ComponentScan("com.nft.cn")
@Slf4j
public class AsyncConfig {

    @Bean
    public ThreadPoolExecutor asyncExecutorMint() {
        ThreadFactory factory = new ThreadFactoryBuilder()
                .setUncaughtExceptionHandler((t, e) -> log.error(t.getName() + " excute error:", e))
                .setNameFormat("mint-pool-%d").build();
        return new ThreadPoolExecutor(20, 50, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<>(500), factory, new ThreadPoolExecutor.CallerRunsPolicy());
    }
}
