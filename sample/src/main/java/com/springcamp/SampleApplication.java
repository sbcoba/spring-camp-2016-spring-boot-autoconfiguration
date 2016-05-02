package com.springcamp;

import com.example.mymodule.HelloService;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.core.type.AnnotatedTypeMetadata;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@SpringBootApplication
public class SampleApplication {
	public static void main(String[] args) {
        ConfigurableApplicationContext ac = SpringApplication.run(SampleApplication.class, args);
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
        System.out.printf("Hello %s world!\n", profile);
    }
}

@AutoConfigureBefore(HelloWorld.class)
@Configuration
class ConditionalTest {

    @Bean
    @Conditional(PropertiesCondition.class)
    public CommandLineRunner propertiesConditional() {
        return (args) -> {
            System.out.println("test.hasValue 환경변수가 있으면 보입니다");
        };
    }

    @Bean
    @Conditional(BeanNotFoundCondition.class)
    public CommandLineRunner helloConditional() {
        return (args) -> {
            System.out.println("HelloWorld Bean이 없으면 보입니다.");
        };
    }
}

class PropertiesCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return context.getEnvironment().containsProperty("test.hasValue");
    }
}
class BeanNotFoundCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        Arrays.stream(beanFactory.getBeanDefinitionNames()).forEach(System.out::println);
        try {
            beanFactory.getBean(HelloWorld.class);
        } catch (NoUniqueBeanDefinitionException e) {
            // bean을 찾았음
            return false;
        } catch (NoSuchBeanDefinitionException e) {
            // bean을 못찾았음
            return true;
        }
        // 어쨋든 bean은 있음
        return false;
    }
}