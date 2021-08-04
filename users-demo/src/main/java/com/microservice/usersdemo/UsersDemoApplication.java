package com.microservice.usersdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

@SpringBootApplication
@EnableHystrix
public class UsersDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsersDemoApplication.class, args);
	}

}
