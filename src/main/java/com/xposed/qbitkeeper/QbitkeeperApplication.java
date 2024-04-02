package com.xposed.qbitkeeper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})

public class QbitkeeperApplication {

	public static void main(String[] args) {
		SpringApplication.run(QbitkeeperApplication.class, args);
	}

}
