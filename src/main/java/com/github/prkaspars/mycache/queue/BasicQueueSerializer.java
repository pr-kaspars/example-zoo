package com.github.prkaspars.mycache.queue;

import org.apache.curator.framework.recipes.queue.QueueSerializer;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class BasicQueueSerializer implements QueueSerializer<String> {

  @Override
  public byte[] serialize(String item) {
    return item.getBytes(StandardCharsets.UTF_8);
  }

  @Override
  public String deserialize(byte[] bytes) {
    return new String(bytes, StandardCharsets.UTF_8);
  }
}
