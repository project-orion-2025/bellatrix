package com.example.project_orion;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@OpenAPIDefinition(
		info = @Info(
				title = "Project Orion APIs",
				version = "1.2.0",
				description = "API documentation for dev-security branch"
		)
)
public class ProjectOrionApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectOrionApplication.class, args);
	}

}
