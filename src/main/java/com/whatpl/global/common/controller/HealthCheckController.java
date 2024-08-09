package com.whatpl.global.common.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class HealthCheckController {

    @GetMapping("/health-check")
    public String healthCheck() {
        log.info("health-check called.");
        return "I'm alive";
    }
}
