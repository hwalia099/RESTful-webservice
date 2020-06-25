package com.springcourse.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component //class for using token secret as app.properties unde resource
public class AppProperties {

	@Autowired
	private Environment env;
	
	public String getTokenSecret()
	{
		return env.getProperty("tokenSecret");
	}
}
