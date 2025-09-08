package com.dynamic_scheduler_automation.dy_sch_au;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class DySchAuApplication {

	public static void main(String[] args) {
		SpringApplication.run(DySchAuApplication.class, args);
	}

}
