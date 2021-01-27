package com.github.prkaspars.myzoo.queue;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.queue.QueueConsumer;
import org.apache.curator.framework.state.ConnectionState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class BasicQueueConsumer implements QueueConsumer<String> {
  private final static Logger log = LoggerFactory.getLogger(BasicQueueConsumer.class);

  @Override
  public void consumeMessage(String message) {
    log.info("New message received. {message='{}'}", message);
  }

  @Override
  public void stateChanged(CuratorFramework client, ConnectionState newState) {
    log.info("New connection state. {state={}}", newState);
  }
}
