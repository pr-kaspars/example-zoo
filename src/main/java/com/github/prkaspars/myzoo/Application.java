package com.github.prkaspars.myzoo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.servlet.ServletContextListener;

@EnableScheduling
@SpringBootApplication
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Bean
  public ServletListenerRegistrationBean<ServletContextListener> servletListener(ZookeeperServletContextListener zookeeperServletContextListener) {
    ServletListenerRegistrationBean<ServletContextListener> srb = new ServletListenerRegistrationBean<>();
    srb.setListener(zookeeperServletContextListener);
    return srb;
  }

  @Bean
  public StringRedisTemplate template(RedisConnectionFactory connectionFactory) {
    return new StringRedisTemplate(connectionFactory);
  }

  @Bean
  public RedisConfiguration redisConfiguration() {
    return new RedisStandaloneConfiguration("localhost", 6379);
  }

  @Bean
  public LettuceConnectionFactory redisConnectionFactory(RedisConfiguration redisConfiguration) {
    return new LettuceConnectionFactory(redisConfiguration);
  }
}
