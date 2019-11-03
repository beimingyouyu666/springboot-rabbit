package com.yangk.producter.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @Description TODO
 * @Author yangkun
 * @Date 2019/6/26
 * @Version 1.0
 */
@Configuration
@Slf4j
public class RabbitConfig {

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.port}")
    private int port;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;


    public static final String EXCHANGE_A = "yk-mq-exchange_A";
    public static final String EXCHANGE_B = "yk-mq-exchange_B";
    public static final String EXCHANGE_C = "yk-mq-exchange_C";


    public static final String QUEUE_A = "QUEUE_A";
    public static final String QUEUE_B = "QUEUE_B";
    public static final String QUEUE_C = "QUEUE_C";

    public static final String ROUTINGKEY_A = "spring-boot-routingKey_A";
    public static final String ROUTINGKEY_B = "spring-boot-routingKey_B";
    public static final String ROUTINGKEY_C = "spring-boot-routingKey_C";


    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host, port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost("/");
        //业务需要发送消息确认机制,设置为true
        connectionFactory.setPublisherConfirms(true);
        return connectionFactory;
    }

    /**
     * 有不同的回调机制的情况下才设置为原型，如果你的业务就一种回调机制，是可以设置为单例的
     *
     * @return
     */
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RabbitTemplate rabbitTemplate() {
        return new RabbitTemplate(connectionFactory());
    }

    @Bean
    public DirectExchange defaultExchange() {
        return new DirectExchange(EXCHANGE_A);
    }

    @Bean
    public Queue queue() {
        return new Queue(QUEUE_A);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(defaultExchange()).with(ROUTINGKEY_A);
    }

    @Bean
    public DirectExchange exchangeB() {
        return new DirectExchange(EXCHANGE_B);
    }

    @Bean
    public Queue queueB() {
        return new Queue(QUEUE_B);
    }

    @Bean
    public Binding bindingB() {
        return BindingBuilder.bind(queueB()).to(exchangeB()).with(ROUTINGKEY_B);
    }


}
