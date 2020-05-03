package com.neulogic.sendit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SenditApplication {
	public static void main(String[] args) {
		SpringApplication.run(SenditApplication.class, args);
	}

}
