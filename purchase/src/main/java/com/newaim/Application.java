package com.newaim;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.sql.DataSource;
import java.util.TimeZone;

@Configuration
@SpringBootApplication
@EnableScheduling
public class Application extends SpringBootServletInitializer{

	static {
		//全局默认时区设定为UTC
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}

	@Bean
	@Primary
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource primaryDataSource() {
		return DataSourceBuilder.create().build();
	}

	/**
	 * 实现SpringBootServletInitializer可以让spring-boot项目在web容器中运行 
	 */
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		builder.sources(this.getClass());
		return super.configure(builder);
	}


	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
