package com.myCompany.gymBro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class GymBroApplication {

	public static void main(String[] args) {
		SpringApplication.run(GymBroApplication.class, args);
	}

}
