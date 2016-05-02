package com.springcamp.custommodule;

import com.example.mymodule.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@SpringBootApplication
public class CustomModuleApplication {
	public static void main(String[] args) {
        ConfigurableApplicationContext ac = SpringApplication.run(CustomModuleApplication.class, args);
        Arrays.stream(ac.getBeanDefinitionNames()).forEach(System.out::println);
    }
}

@Configuration
class HelloModuleConfiguration {
    @Autowired
    HelloService helloService;
    @PostConstruct
    public void init() {
        System.out.println("************************************");
        System.out.printf("My Module Test %s\n", helloService.hello());
        System.out.println("************************************");
    }
}