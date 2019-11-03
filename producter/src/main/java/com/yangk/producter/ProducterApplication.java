package com.yangk.producter;

import com.yangk.producter.config.RabbitConfig;
import com.yangk.producter.service.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProducterApplication implements CommandLineRunner {

    @Autowired
    private Product product;

    public static void main(String[] args) {
        SpringApplication.run(ProducterApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        for (int i = 0; i < 10; i++) {
            product.sendMsg("hello rabbit " + i, RabbitConfig.EXCHANGE_A, RabbitConfig.ROUTINGKEY_A);
            product.sendMsg("hello rabbitB " + i, RabbitConfig.EXCHANGE_B, RabbitConfig.ROUTINGKEY_B);
        }
    }
}
