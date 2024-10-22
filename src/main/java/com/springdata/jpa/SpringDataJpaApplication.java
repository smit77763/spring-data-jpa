package com.springdata.jpa;

import com.github.javafaker.Faker;
import com.springdata.jpa.models.Author;
import com.springdata.jpa.repository.AuthorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import java.util.List;

@SpringBootApplication
@EnableSpringDataWebSupport //provides support for data in web applications
public class SpringDataJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringDataJpaApplication.class, args);
		
	}
	
	
	
//	@Bean
//	public CommandLineRunner commandLineRunner(
//			AuthorRepository repository
//	) {
//		return args -> {
//			for(int i = 0; i < 20; i++) {
//				Faker faker = new Faker();
//				Author author = Author.builder()
//						.firstName(faker.name().firstName())
//						.lastName(faker.name().lastName())
//						.email(faker.internet().emailAddress())
//						.age(faker.number().numberBetween(20, 50))
//						.build();
//				repository.save(author);
//
//			}
////			
//			repository.updateAllAuthorAge(20);
//			
//		};
//	}

}
