package com.gp.radioanalytics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class RadioAnalyticsApplication {

	static void main(String[] args) {
		SpringApplication.run(RadioAnalyticsApplication.class, args);

		System.out.println("RadioAnalytics app started...");
	}

}
