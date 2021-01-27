package com.github.prkaspars.myzoo.config;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.recipes.queue.DistributedQueue;
import org.apache.curator.framework.recipes.queue.QueueBuilder;
import org.apache.curator.framework.recipes.queue.QueueConsumer;
import org.apache.curator.framework.recipes.queue.QueueSerializer;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ZookeeperProperties.class)
public class ZookeeperConfig {
  private final ZookeeperProperties properties;

  @Autowired
  public ZookeeperConfig(ZookeeperProperties properties) {
    this.properties = properties;
  }

  @Bean
  public CuratorFramework curatorFramework() {
    RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
    return CuratorFrameworkFactory
      .newClient(properties.getConnection(), retryPolicy);
  }

  @Bean
  public LeaderSelector leaderSelector(CuratorFramework curatorFramework, LeaderSelectorListener listener) {
    LeaderSelector leaderSelector = new LeaderSelector(curatorFramework, properties.getPaths().get("leader"), listener);
    leaderSelector.autoRequeue();
    return leaderSelector;
  }

  @Bean
  public DistributedQueue<String> distributedQueue(
    CuratorFramework curatorFramework,
    QueueConsumer<String> consumer,
    QueueSerializer<String> serializer
  ) {
    return QueueBuilder
      .builder(curatorFramework, consumer, serializer, properties.getPaths().get("queue"))
      .buildQueue();
  }
}

