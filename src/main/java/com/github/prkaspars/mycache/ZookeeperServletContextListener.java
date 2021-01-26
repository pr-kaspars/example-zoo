package com.github.prkaspars.mycache;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.queue.DistributedQueue;
import org.apache.curator.utils.CloseableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

@Component
public class ZookeeperServletContextListener implements ServletContextListener {
  private final CuratorFramework curatorFramework;
  private final LeaderSelector leaderSelector;
  private final DistributedQueue<String> distributedQueue;
  private boolean initialized = false;

  @Autowired
  public ZookeeperServletContextListener(
    CuratorFramework curatorFramework,
    LeaderSelector leaderSelector,
    DistributedQueue<String> distributedQueue
  ) {
    this.curatorFramework = curatorFramework;
    this.leaderSelector = leaderSelector;
    this.distributedQueue = distributedQueue;
  }

  @Override
  public void contextInitialized(ServletContextEvent event) {
    if (initialized) {
      return;
    }

    synchronized (this) {
      if (initialized) {
        return;
      }
      curatorFramework.start();
      leaderSelector.start();
      try {
        distributedQueue.start();
      } catch (Exception e) {
        e.printStackTrace();
      }
      initialized = true;
    }
  }

  @Override
  public void contextDestroyed(ServletContextEvent event) {
    CloseableUtils.closeQuietly(distributedQueue);
    CloseableUtils.closeQuietly(leaderSelector);
    CloseableUtils.closeQuietly(curatorFramework);
  }
}
