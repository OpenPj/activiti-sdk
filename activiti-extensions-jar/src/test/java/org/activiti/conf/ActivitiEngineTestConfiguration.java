package org.activiti.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@PropertySources({
    @PropertySource(value="classpath:/META-INF/activiti-app-test/TEST-db.properties", ignoreResourceNotFound=false),
    @PropertySource(value="classpath:/META-INF/activiti-app-test/TEST-activiti-app-whitelisting.properties", ignoreResourceNotFound=false),
    @PropertySource(value="classpath:/META-INF/activiti-app-test/TEST-activiti-ldap.properties", ignoreResourceNotFound=false)
})
public class ActivitiEngineTestConfiguration {
	
	@Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
	
}