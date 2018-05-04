package com.newaim.purchase.config;

import org.activiti.spring.SpringAsyncExecutor;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.AbstractProcessEngineAutoConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;

/**
 *  Activiti数据源配置
 */
@Configuration
public class ActivitiDataSourceConfig extends AbstractProcessEngineAutoConfiguration{

	@Value("${activiti.datasource.driver-class-name}")
	private String driverClassName;

	@Value("${activiti.datasource.url}")
	private String url;

	@Value("${activiti.datasource.username}")
	private String username;

	@Bean
	@ConfigurationProperties(prefix = "activiti.datasource")
	public DataSource activitiDataSource() {
		return DataSourceBuilder.create().url(url).url(username).driverClassName(driverClassName).build();
	}
	
	@Bean
	public SpringProcessEngineConfiguration springProcessEngineConfiguration(
			PlatformTransactionManager transactionManager,
			SpringAsyncExecutor springAsyncExecutor) throws IOException {
		return baseSpringProcessEngineConfiguration(activitiDataSource(), transactionManager, springAsyncExecutor);
	}
	

}
