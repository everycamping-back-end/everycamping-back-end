package com.zerobase.everycampingbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class EverycampingBackEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(EverycampingBackEndApplication.class, args);
	}

}
