package com.amarasiricoreservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AmarasiriCoreServiceApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		System.out.println("Hello...... I'm running...");
		SpringApplication.run(AmarasiriCoreServiceApplication.class, args);
	}

}
