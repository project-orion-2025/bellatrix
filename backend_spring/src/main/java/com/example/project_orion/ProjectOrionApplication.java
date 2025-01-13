package com.example.project_orion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ProjectOrionApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectOrionApplication.class, args);
	}

}
