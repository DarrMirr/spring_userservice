package com.github.darrmirr.config;

import com.github.darrmirr.service.UserStatusService;
import com.github.darrmirr.service.UserStatusServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/*
 * @author Darr Mirr
 */

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.github.darrmirr")
public class UserAppConfig {

    @Bean
    public UserStatusService userStatusService() {
        return new UserStatusServiceImpl();
    }

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(5);
        return threadPoolTaskScheduler;
    }
}
