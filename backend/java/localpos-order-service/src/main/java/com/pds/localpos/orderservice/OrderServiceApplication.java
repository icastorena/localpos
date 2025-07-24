package com.pds.localpos.orderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "com.pds.localpos.orderservice.client")
@SpringBootApplication(scanBasePackages = {
        "com.pds.localpos",
        "com.pds.localpos.security"
})
public class OrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }

}
