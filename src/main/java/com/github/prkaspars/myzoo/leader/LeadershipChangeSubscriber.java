package com.github.prkaspars.myzoo.leader;

import org.apache.curator.framework.recipes.queue.DistributedQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

@Component
public class LeadershipChangeSubscriber implements Consumer<Boolean> {
  private final static Logger log = LoggerFactory.getLogger(LeadershipChangeSubscriber.class);
  private final AtomicBoolean leader = new AtomicBoolean(false);
  private final DistributedQueue<String> distributedQueue;

  public LeadershipChangeSubscriber(DistributedQueue<String> distributedQueue) {
    this.distributedQueue = distributedQueue;
  }

  @Override
  public void accept(Boolean newValue) {
    boolean oldValue = this.leader.getAndSet(newValue);
    log.info("Leader state changed. {new={}, prev={}}", newValue, oldValue);
  }

  @Scheduled(fixedRate = 1000)
  public void run() {
    if (!leader.get()) {
      return;
    }
    String message = "Message{date=" + LocalDateTime.now().toString() + "}";
    try {
      log.info("Publishing message. {}", message);
      distributedQueue.put(message);
    } catch (Exception e) {
      log.error("Could not put message in queue.", e);
    }
  }
}
