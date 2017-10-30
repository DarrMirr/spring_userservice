package com.github.darrmirr.config;

import com.github.darrmirr.repository.UserStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

/*
 * @author Darr Mirr
 */

@Configuration
@EnableRedisRepositories(basePackageClasses = UserStatusRepository.class)
@PropertySource(value = {"classpath:application.properties"})
public class UserAppRedisConfig {

    @Autowired
    Environment environment;

    @Bean
    LettuceConnectionFactory lettuceConnectionFactory() {
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory();
        lettuceConnectionFactory.setHostName(environment.getProperty("redis.hostName"));
        lettuceConnectionFactory.setPort(Integer.parseInt(environment.getProperty("redis.port")));
        return lettuceConnectionFactory;
    }

    @Bean
    public RedisTemplate<?, ?> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        RedisTemplate<byte[], byte[]> template = new RedisTemplate<>();
        template.setConnectionFactory(lettuceConnectionFactory);
        return template;
    }
}
