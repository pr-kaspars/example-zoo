package com.github.prkaspars.mycache.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "zookeeper")
public class ZookeeperProperties {
  private String connection;
  private Map<String, String> paths;

  public String getConnection() {
    return connection;
  }

  public void setConnection(String connection) {
    this.connection = connection;
  }

  public Map<String, String> getPaths() {
    return paths;
  }

  public void setPaths(Map<String, String> paths) {
    this.paths = paths;
  }
}
