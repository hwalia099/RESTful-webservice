package com.springcourse;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration   //class tp enable cors
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {

		registry
		.addMapping("/**")   //enable cors for all rest classes and methods
		.allowedMethods("*") //.allowedMethods("GET","POST","PUT") TO allow specific methods adn same for origins
		.allowedOrigins("*");

	}
	
}
