package com.luisow748.springvue3.inertia;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InertiaViewConfigurer {

    @Bean
    public InertiaView inertiaView(ApplicationContext applicationContext) {
        return new InertiaView(applicationContext);
    }
}
