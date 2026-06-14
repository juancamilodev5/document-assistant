package com.docassistant.da_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class DaBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(DaBackendApplication.class, args);
	}

}
