package com.emc.documentum.springdata.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.SpringApplicationConfiguration;

import com.emc.documentum.springdata.core.tests.Person;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class);
		Documentum doc = new Documentum(new UserCredentials("dmadmin", "password"),"FPIRepo");
		DSTemplate temp = new DSTemplate(doc);
		temp.findAll(Person.class);
	}

}
