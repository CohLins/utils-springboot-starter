package com.colins.springutils.utils;


import cn.hutool.core.collection.CollectionUtil;
import com.colins.springutils.entity.HystrixEntity;
import com.sun.management.OperatingSystemMXBean;

import java.lang.management.ManagementFactory;
import java.util.List;

public class HystrixUtils {

    public static final int DEFAULT_CPU_LIMIT=95;
    public static final int DEFAULT_HEAP_MEMORY_LIMIT=90;
    public static final int DEFAULT_WINDOW_TIME_LIMIT=1000;
    public static final int DEFAULT_WINDOW_COUNT_LIMIT=2;





    public static int getCurrentCpu(){
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        return (int) Math.round(osBean.getProcessCpuLoad() * 100);
    }

    public static int getCurrentHeapMemory(){
        double heapUsage = ((double) ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed() / ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getMax()) * 100;
        return (int) Math.round(heapUsage);
    }

    public static int getCurrentErrorCount(List<HystrixEntity> hystrixEntities){
        if(CollectionUtil.isEmpty(hystrixEntities)){
            return 0;
        }
        return hystrixEntities.stream().map(HystrixEntity::getErrorCountValue).reduce(Integer::sum).get().intValue();
    }

    public static int getCurrentErrorRatio(List<HystrixEntity> hystrixEntities){
        if(CollectionUtil.isEmpty(hystrixEntities)){
            return 0;
        }
        Integer errorCount = hystrixEntities.stream().map(HystrixEntity::getErrorCountValue).reduce(Integer::sum).get();
        Integer requestCount = hystrixEntities.stream().map(HystrixEntity::getRequestCountValue).reduce(Integer::sum).get();
        return Math.round((errorCount*100)/requestCount);
    }

}
