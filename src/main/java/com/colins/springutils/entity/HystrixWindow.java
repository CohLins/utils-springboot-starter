package com.colins.springutils.entity;


public class HystrixWindow {
    // 窗口的长度 单位：ms
    private final int windowLengthInMs;
    // 窗口的开始时间戳  单位：ms
    private long windowStartInMs;
    // 窗口内存放的实体类
    private HystrixEntity hystrixEntity;

    public HystrixWindow(int windowLengthInMs, long windowStartInMs, HystrixEntity hystrixEntity) {
        this.windowLengthInMs = windowLengthInMs;
        this.windowStartInMs = windowStartInMs;
        this.hystrixEntity = hystrixEntity;
    }

    public int getWindowLengthInMs() {
        return windowLengthInMs;
    }

    public long getWindowStartInMs() {
        return windowStartInMs;
    }

    public HystrixEntity getHystrixEntity() {
        return hystrixEntity;
    }

    public void setHystrixEntity(HystrixEntity hystrixEntity) {
        this.hystrixEntity = hystrixEntity;
    }

    /**
     * @Description 重置窗口
     **/
    public HystrixWindow resetTo(long startTime) {
        this.windowStartInMs = startTime;
        hystrixEntity.resetValue();
        return this;
    }

    /**
     * @Description 判断时间是否属于该窗口
     **/
    public boolean isTimeInWindow(long timeMillis) {
        return windowStartInMs <= timeMillis && timeMillis < windowStartInMs + windowLengthInMs;
    }
}
