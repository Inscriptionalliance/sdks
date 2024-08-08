package com.nft.cn.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    public static final String SCAN_EXCHANGE = "inscription_scan_exchange";

    public static final String SCAN_QUEUE = "inscription_scan_queue";

    public static final String SCAN_ROUTING_KEY = "inscription_scan_key";

    @Bean
    public Queue getScanQueue(){
//        return new Queue(SCAN_QUEUE);
        return QueueBuilder.durable(SCAN_QUEUE).lazy().build();
    }

    @Bean
    public Exchange getScanExchange(){
        return ExchangeBuilder.directExchange(SCAN_EXCHANGE).durable(true).build();
    }

    @Bean
    public Binding bindScanNotice(){
        return BindingBuilder.bind(getScanQueue()).to(getScanExchange()).with(SCAN_ROUTING_KEY).noargs();
    }






}
