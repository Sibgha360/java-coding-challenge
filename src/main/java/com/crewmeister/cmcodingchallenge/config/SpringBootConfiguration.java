package com.crewmeister.cmcodingchallenge.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.*;
import org.springframework.stereotype.*;

@ConfigurationProperties(prefix="spring.boot.config.example")
@Component
@Getter
@Setter
public class SpringBootConfiguration {

    private String company;
    private int suite;
    private boolean active;
    private String bundesbankWebUrl;
}