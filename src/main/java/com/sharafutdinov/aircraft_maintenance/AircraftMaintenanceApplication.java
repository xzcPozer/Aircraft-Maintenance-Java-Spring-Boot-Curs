package com.sharafutdinov.aircraft_maintenance;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableAsync
@SpringBootApplication
public class AircraftMaintenanceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AircraftMaintenanceApplication.class, args);
	}

}
