package com.Diseno.TPDiseno2025;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class})
public class TpDiseno2025Application {

	public static void main(String[] args) {
		SpringApplication.run(TpDiseno2025Application.class, args);
	}

}
