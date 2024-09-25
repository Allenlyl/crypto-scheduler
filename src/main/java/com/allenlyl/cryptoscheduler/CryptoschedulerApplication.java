package com.allenlyl.cryptoscheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CryptoschedulerApplication {
	// @schedulering cronexpress * * * * *
	public static void main(String[] args) {
		SpringApplication.run(CryptoschedulerApplication.class, args);
	}

}
