package com.crewmeister.cmcodingchallenge;

import com.crewmeister.cmcodingchallenge.config.SpringBootConfiguration;
import com.crewmeister.cmcodingchallenge.helper.Util;
import com.crewmeister.cmcodingchallenge.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class CmCodingChallengeApplication {
	@Autowired
	CurrencyRepository currencyRepository;

	@Autowired
	SpringBootConfiguration config;

	public static void main(String[] args) {
		SpringApplication.run(CmCodingChallengeApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void doSomethingAfterStartup() throws Throwable {
		Util.populateCurrencyRate(currencyRepository,"USD", config.getBundesbankWebUrl());
	}
}
