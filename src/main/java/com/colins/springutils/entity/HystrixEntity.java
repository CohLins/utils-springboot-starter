package com.colins.springutils.entity;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description
 * @Author czl
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2023/4/6
 */
public class HystrixEntity {
    private AtomicInteger requestCount;
    private AtomicInteger errorCount;

    public HystrixEntity(){
        this.requestCount=new AtomicInteger(0);
        this.errorCount=new AtomicInteger(0);

    }

    public int getRequestCountValue() {
        return requestCount.get();
    }

    public int getErrorCountValue() {
        return errorCount.get();
    }

    public void resetValue() {
        this.errorCount.set(0);
        this.requestCount.set(0);
    }

    public void addErrorCount(){
        this.errorCount.addAndGet(1);
    }

    public void addRequestCount(){
        this.requestCount.addAndGet(1);
    }
}
