package com.github.prkaspars.myzoo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
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
}
