package com.myCompany.gymBro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@ConfigurationPropertiesScan
public class GymBroApplication {

	public static void main(String[] args) {
		SpringApplication.run(GymBroApplication.class, args);
	}

}
