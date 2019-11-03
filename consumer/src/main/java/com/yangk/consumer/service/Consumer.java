package com.yangk.consumer.service;

import com.yangk.consumer.config.RabbitConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @Description TODO
 * @Author yangkun
 * @Date 2019/6/26
 * @Version 1.0
 */
@Component
@RabbitListener(queues = RabbitConfig.QUEUE_A)
@Slf4j
public class Consumer {

    @RabbitHandler
    public void access(String msg) {
        log.info(msg);
    }
}
