package com.dynamic_scheduler_automation.dy_sch_au;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableMongoAuditing
@EnableScheduling
public class DySchAuApplication {

	public static void main(String[] args) {
		SpringApplication.run(DySchAuApplication.class, args);
	}

}
