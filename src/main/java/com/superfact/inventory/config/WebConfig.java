package com.superfact.inventory.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewInterceptor;

@Configuration
public class WebConfig {

    @Bean
    public OpenEntityManagerInViewInterceptor openEntityManagerInViewInterceptor(@Qualifier("entityManagerFactory") EntityManagerFactory entityManagerFactory) {
        OpenEntityManagerInViewInterceptor interceptor = new OpenEntityManagerInViewInterceptor();
        interceptor.setEntityManagerFactory(entityManagerFactory);
        return interceptor;
    }
}
