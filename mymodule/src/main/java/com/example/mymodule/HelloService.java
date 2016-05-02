package com.example.mymodule;

public class HelloService {

    private String helloWorld;

    public void setHelloWorld(String helloWorld) {
        this.helloWorld = helloWorld;
    }

    public String hello() {
        return helloWorld;
    }
}
