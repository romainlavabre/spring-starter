package com.replace.replace;

import com.replace.replace.api.environment.Environment;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class ReplaceApplication {

	public static void main(String[] args) {
		new ClassPathXmlApplicationContext( "ApplicationContext.xml" );
		SpringApplication.run(ReplaceApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer( Environment environment ) {


		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings( CorsRegistry registry ) {

				String pattern = "/**";
				String origins = environment.getEnv( "request.allowed-origin" );

				registry.addMapping( pattern )
						.allowedMethods( "GET", "POST", "PUT", "DELETE" )
						.allowedOrigins( origins )
						.exposedHeaders( "Location", "Authorization" );
			}
		};

	}
}
