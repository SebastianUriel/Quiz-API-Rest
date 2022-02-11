package com.test.main.testrest.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;

@TestConfiguration
@ComponentScan("org.springframework.cloud.sleuth.autoconfig")
@Slf4j
@EnableAutoConfiguration
public class TestConfig {

}
