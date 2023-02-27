package com.banco.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
@EnableScheduling
//@EnableJpaAuditing
@EntityScan("com.banco.backend.entity")
@ComponentScan("com.banco.backend.service")
public class BancoAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(BancoAppApplication.class, args);
	}

}
