package com.d103.dddev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing;


//@EnableReactiveMongoAuditing

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@EnableMongoAuditing
public class DddevApplication {
	public static void main(String[] args) {
		SpringApplication.run(DddevApplication.class, args);
	}
}
