package com.springcourse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.springcourse.security.AppProperties;
import com.sun.glass.ui.Application;

//we used spring security thus all end point will be protected
//we will make usercreate part only public others will require authentication
@SpringBootApplication
public class MobileApp2Application extends SpringBootServletInitializer { // extended clss for standalone tomcat ,so
																			// that we dont have inbuilt tomcat instead
																			// to archive it to one
	// to run from windows go to app directory
	// do mvn install will build the project : it ll make a deployable file which we
	// can run on our local machine or remote server
	// it places this deployable and other class files under targer created by maven
	// (target me jar file hogi)
	// 1)method copy .jar file on pc and go to the copied dir and do java -jar
	// mobile-app2-0.0.1-SNAPSHOT.jar (file name jar) (methof to run via java)
	// 2)method mvn spring-boot:run will run the app now we can test on postman
	// apache tomcat is built-in in our executable (deplayeable) .jar file

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(MobileApp2Application.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(MobileApp2Application.class, args);
	}
//conetxt path in app.properties tells tomcat the end point so that i can use tomcat to hit my other projects api too
//adding spring security and json web token will popup a page for login and while running the app i ll get the below one
	// Using generated security password: 7e1630ea-06c6-4008-a6b1-011ecd6fdfc6

	// bcrptpwrdencode is a class to encode password
	// it uses a hasing fucnton ( blowfish ciper ) called bcrypt
	@Bean // @bean to enable autowiring
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean // will be used to get userID by userSERIVE IMPLEMENTATUON CLASS
	public SpringApplicationContext springApplicationContext() {
		return new SpringApplicationContext();
	}

	@Bean(name = "AppProperties")
	public AppProperties getAppProperties() {
		return new AppProperties();
	}

}
