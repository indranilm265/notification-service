package com.example.notificationassignment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class NotificationAssignmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotificationAssignmentApplication.class, args);
	}

}
