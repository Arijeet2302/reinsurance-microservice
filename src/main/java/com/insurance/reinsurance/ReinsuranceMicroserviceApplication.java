package com.insurance.reinsurance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main Spring Boot Application for Reinsurance Microservice
 * 
 * This application handles policy synchronization and transaction management
 * for reinsurance operations.
 */
@SpringBootApplication
@EnableScheduling
public class ReinsuranceMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReinsuranceMicroserviceApplication.class, args);
    }
}