package com.example.springbootjpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringbootJpaApplication {

    public static void main(String[] args) {
        Hello hello = new Hello();
        hello.setData("hello");
        System.out.println("hello: " + hello.getData());

        SpringApplication.run(SpringbootJpaApplication.class, args);
    }

}
