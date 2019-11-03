package com.yangk.producter.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @Description TODO
 * @Author yangkun
 * @Date 2019/6/26
 * @Version 1.0
 */
@Component
@Slf4j
public class Product implements RabbitTemplate.ConfirmCallback {

    /**
     * 由于rabbitTemplate的scope属性设置为ConfigurableBeanFactory.SCOPE_PROTOTYPE，所以不能自动注入
     * 在大部分情况下，容器中的bean都是singleton类型的。
     * 如果一个singleton bean要引用另外一个singleton bean，或者一个非singleton bean要引用另外一个非singleton bean时，
     * 通常情况下将一个bean定义为另一个bean的property值就可以了。不过对于具有不同生命周期的bean来说这样做就会有问题了，
     * 比如在调用一个singleton类型bean A的某个方法时，需要引用另一个非singleton（prototype）类型的bean B，
     * 对于bean A来说，容器只会创建一次，这样就没法在需要的时候每次让容器为bean A提供一个新的的bean B实例。
     */
    private RabbitTemplate rabbitTemplate;

    @Autowired
    public Product(RabbitTemplate rabbitTemplate) {
        /*如果需要在生产者需要消息发送后的回调，需要对rabbitTemplate设置ConfirmCallback对象，
        由于不同的生产者需要对应不同的ConfirmCallback，
        如果rabbitTemplate设置为单例bean，则所有的rabbitTemplate
        实际的ConfirmCallback为最后一次申明的ConfirmCallback。*/
        this.rabbitTemplate = rabbitTemplate;
        rabbitTemplate.setConfirmCallback(this);
    }

    public void sendMsg(String msg, String exchange, String routingKey) {
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(exchange, routingKey, msg, correlationData);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        log.info("confirm id:" + correlationData);
        if (ack) {
            log.info("send success");
        } else {
            log.error("send fail:" + cause);
        }
    }

}
