package com.lcpk.mtype;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class MtypeApplication {

	public static void main(String[] args) {
		SpringApplication.run(MtypeApplication.class, args);
	}

}
