package com.example.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.api.services.PostureService;

@SpringBootApplication
public class ApiApplication {

	@Autowired
	private static PostureService postureService;

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
		Long dataSize = Long.parseLong(System.getenv("INITIAL_SAMPLE_DATA_SIZE"));
		postureService.createSamplePostures(dataSize);
	}

}
