package com.redis.locking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RedisDistributedLockingApplication {

  public static void main(String[] args) {
    SpringApplication.run(RedisDistributedLockingApplication.class, args);
  }
}
