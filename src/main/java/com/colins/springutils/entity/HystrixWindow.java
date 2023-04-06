package com.colins.springutils.entity;

/**
 * @Description
 * @Author czl
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2023/4/6
 */
public class HystrixWindow {
    private final int windowLengthInMs;

    private long windowStartInMs;

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
     * @Author czl
     * @Description 重置窗口
     **/
    public HystrixWindow resetTo(long startTime) {
        this.windowStartInMs = startTime;
        hystrixEntity.resetValue();
        return this;
    }

    /**
     * @Author czl
     * @Description 判断时间是否属于该窗口
     **/
    public boolean isTimeInWindow(long timeMillis) {
        return windowStartInMs <= timeMillis && timeMillis < windowStartInMs + windowLengthInMs;
    }
}
