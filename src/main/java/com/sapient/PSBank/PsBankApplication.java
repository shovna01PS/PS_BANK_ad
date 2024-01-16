package com.sapient.PSBank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient

@SpringBootApplication
public class PsBankApplication {

	public static void main(String[] args) {
		SpringApplication.run(PsBankApplication.class, args);
	}

}
