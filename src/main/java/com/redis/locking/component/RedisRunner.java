package com.redis.locking.component;

import jakarta.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Log4j2
@RequiredArgsConstructor
public class RedisRunner {

  @Value("${instanceId}")
  private String instanceId;

  private final RedissonClient redissonClient;

  @PostConstruct
  public void init() {
    RLock lock;
    boolean lockAcquired = false;
    try {
      while (true) {
        lock = redissonClient.getFairLock("MyLock");
        lockAcquired =
            lock.tryLock(
                10, // wait for lock
                30, // lock expiry
                TimeUnit.SECONDS);
        if (lockAcquired) {
          int lockHoldTimeInSeconds = (int) (5 + Math.random() * (8));
          log.info(
              "Lock acquired by instanceId: {} for {} seconds", instanceId, lockHoldTimeInSeconds);
          // holds thread for 5 - 12 seconds
          Thread.sleep(lockHoldTimeInSeconds * 1000L);
          lock.unlock();
        } else {
          log.info("Lock is in use by another instance");
        }
      }
    } catch (Exception ignored) {
    }
  }
}
