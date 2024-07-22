package com.unni.corservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication(scanBasePackages = {"com.unni.corservice"})
public class CorServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CorServiceApplication.class, args);
	}

//	@Override
//	protected SpringApplicationBuilder configure(SpringApplicationBuilder springApplicationBuilder) {
//		return springApplicationBuilder.sources(CorServiceApplication.class);
//	}

}
