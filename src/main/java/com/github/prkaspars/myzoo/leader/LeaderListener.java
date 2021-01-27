package com.github.prkaspars.myzoo.leader;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.framework.state.ConnectionState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class LeaderListener extends LeaderSelectorListenerAdapter {
  private final static Logger log = LoggerFactory.getLogger(LeaderListener.class);

  private final AtomicInteger leaderCount = new AtomicInteger(1);
  private final String name;
  private final LeadershipChangeSubscriber leadershipChangeSubscriber;

  private CountDownLatch countDownLatch = new CountDownLatch(1);

  @Autowired
  public LeaderListener(String name, LeadershipChangeSubscriber leadershipChangeSubscriber) {
    this.name = name;
    this.leadershipChangeSubscriber = leadershipChangeSubscriber;
  }

  @Override
  public void stateChanged(CuratorFramework client, ConnectionState newState) {
    switch (newState) {
      case SUSPENDED, LOST -> {
        log.info("Leadership revoked from {} ", name);
        countDownLatch.countDown();
        leadershipChangeSubscriber.accept(false);
      }
      case CONNECTED, RECONNECTED -> {
        log.info("client {}. Attempting for leadership {} ", newState, name);
        countDownLatch = new CountDownLatch(1);
      }
    }
  }

  @Override
  public void takeLeadership(CuratorFramework client) throws Exception {
    log.info("Leader is {} for {} time ", name, this.leaderCount.getAndIncrement());
    try {
      new Thread(() -> leadershipChangeSubscriber.accept(true)).start();
    } catch (Exception e) {
      log.error("Error starting thread", e);
    }
    countDownLatch.await();
    log.debug("{} is not a Leader anymore ", name);
  }
}
