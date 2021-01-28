package com.github.prkaspars.myzoo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {
  private final MessageRepository messageRepository;

  @Autowired
  public ApiController(MessageRepository messageRepository) {
    this.messageRepository = messageRepository;
  }

  @GetMapping("/messages")
  public Iterable<Message> allMessages() {
    return messageRepository.findAll();
  }
}
