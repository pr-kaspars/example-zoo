package com.github.prkaspars.myzoo.queue;

import com.github.prkaspars.myzoo.Message;
import com.github.prkaspars.myzoo.MessageRepository;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.queue.QueueConsumer;
import org.apache.curator.framework.state.ConnectionState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class BasicQueueConsumer implements QueueConsumer<String> {
  private final static Logger log = LoggerFactory.getLogger(BasicQueueConsumer.class);
  private final MessageRepository messageRepository;

  @Autowired
  public BasicQueueConsumer(MessageRepository messageRepository) {
    this.messageRepository = messageRepository;
  }

  @Override
  public void consumeMessage(String message) {
    log.info("New message received. {message='{}'}", message);
    Message msg = new Message();
    msg.setId(UUID.randomUUID().toString());
    msg.setText(message);
    messageRepository.save(msg);
  }

  @Override
  public void stateChanged(CuratorFramework client, ConnectionState newState) {
    log.info("New connection state. {state={}}", newState);
  }
}
