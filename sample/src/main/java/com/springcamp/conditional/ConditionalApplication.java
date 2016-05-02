package com.springcamp.conditional;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.core.type.AnnotationMetadata;

import javax.annotation.PostConstruct;
import java.util.Arrays;

//@Import(TestImportSelector.class)
@SpringBootApplication
public class ConditionalApplication {
	public static void main(String[] args) {
        ConfigurableApplicationContext ac = SpringApplication.run(ConditionalApplication.class, args);
        Arrays.stream(ac.getBeanDefinitionNames()).forEach(System.out::println);
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

//class TestImportSelector implements DeferredImportSelector {
//    @Override
//    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
//        return new String[] {
//            ConditionalTestConfig.class.getName()
//        };
//    }
//}

@Configuration
class HelloConfig {
    @Bean
    public HelloWorld helloWorld() {
        return new HelloWorld("test!");
    }
}

@Configuration
class ConditionalTestConfig {

    @Bean
    @Conditional(PropertiesCondition.class)
    public CommandLineRunner propertiesConditional() {
        return (args) -> {
            System.out.println("****** Conditional Application ******");
            System.out.println("test.hasValue 환경변수가 있으면 보입니다");
            System.out.println("*********************************");
        };
    }

    @Bean
    @Conditional(BeanNotFoundCondition.class)
    public CommandLineRunner helloConditional() {
        return (args) -> {
            System.out.println("****** Conditional Application ******");
            System.out.println("HelloWorld Bean이 없으면 보입니다.");
            System.out.println("*********************************");
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