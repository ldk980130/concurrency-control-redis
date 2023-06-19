package com.practice.concurrencycontrolredis.application;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Aspect
@RequiredArgsConstructor
@Order(Integer.MIN_VALUE)
public class ConcurrencyControlAspect {

    private final RedissonClient redissonClient;

    @Around("@annotation(com.practice.concurrencycontrolredis.application.ConcurrencyControl)")
    public Object execute(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        ConcurrencyControl concurrencyControl = signature.getMethod().getAnnotation(ConcurrencyControl.class);
        String lockName = concurrencyControl.target();

        RLock lock = redissonClient.getLock(lockName);
        try {
            boolean available = lock.tryLock(10, 1, TimeUnit.SECONDS);
            if (!available) {
                throw new IllegalArgumentException("redisson getLock timeout");
            }
            return joinPoint.proceed();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
}
