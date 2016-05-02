package com.example.mymodule;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyModuleConfiguration {

    @Bean
    public HelloService helloService() {
        HelloService helloService = new HelloService();
        helloService.setHelloWorld("Hello Test");
        return helloService;
    }
}
