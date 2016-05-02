package com.springcamp.profile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@SpringBootApplication
public class ProfileApplication {
	public static void main(String[] args) {
        ConfigurableApplicationContext ac = SpringApplication.run(ProfileApplication.class, args);
        Arrays.stream(ac.getBeanDefinitionNames()).forEach(System.out::println);
    }
}

@Profile("dev")
@Configuration
class HelloProfileDevConfig {

    @Bean
    public HelloWorld helloDevWorld() {
        return new HelloWorld("dev");
    }
}

@Profile("test")
@Configuration
class HelloProfileTestConfig {

    @Bean
    public HelloWorld helloTestWorld() {
        return new HelloWorld("test");
    }
}

class HelloWorld {
    private String profile;

    public HelloWorld(String profile) {
        this.profile = profile;
    }

    @PostConstruct
    public void init() {
        System.out.println("****** Profile Application ******");
        System.out.printf("Hello %s world!\n", profile);
        System.out.println("*********************************");
    }
}