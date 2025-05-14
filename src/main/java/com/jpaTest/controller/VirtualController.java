package com.jpaTest.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class VirtualController {
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/sleep")
    public void sleep() throws InterruptedException {
        log.info("1) counter: {}, thread: {}", counter.incrementAndGet(), Thread.currentThread());
        Thread.sleep(5000);
        log.info("2) thread: {}", Thread.currentThread());
    }

    @GetMapping("/test")
    public String test(){
        return "hello";
    }
}
