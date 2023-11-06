package com.dddev.log;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
public class LogApplication {

	@PostConstruct
	public void started() {
		TimeZone.setDefault(TimeZone.getTimeZone("KST"));
	}
	public static void main(String[] args) {
		SpringApplication.run(LogApplication.class, args);
	}
}
