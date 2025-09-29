package org.htx.vigil.jvm.controller;

import org.htx.vigil.jvm.dto.JvmMetrics;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.lang.management.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * JVM 指标收集控制器
 * 
 * 提供 JVM 运行时指标的 REST API 接口，包括：
 * - 内存使用情况（堆内存、非堆内存、内存池）
 * - 垃圾回收器信息
 * - 线程统计信息
 * - 类加载信息
 * - 运行时信息
 * - 操作系统信息
 * 
 * @author Hao Tong Xue
 * @since 1.0.0
 */
@RestController
@RequestMapping("/vigil")
@CrossOrigin
public class JvmMetricsCollector {

    /**
     * 获取 JVM 运行时指标
     * 
     * 收集并返回当前 JVM 的完整运行时指标，包括内存、GC、线程等信息。
     * 
     * @return ResponseEntity<JvmMetrics> 包含完整 JVM 指标的响应实体
     */
    @GetMapping("/metrics")
    public ResponseEntity<JvmMetrics> metrics() {
        JvmMetrics metrics = new JvmMetrics();

        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heap = memoryMXBean.getHeapMemoryUsage();
        MemoryUsage nonHeap = memoryMXBean.getNonHeapMemoryUsage();

        // 构建内存基本信息对象
        JvmMetrics.MemoryInfo memory = new JvmMetrics.MemoryInfo();
        memory.setHeapUsed(heap.getUsed());
        memory.setHeapCommitted(heap.getCommitted());
        memory.setHeapMax(heap.getMax());
        memory.setNonHeapUsed(nonHeap.getUsed());
        memory.setNonHeapCommitted(nonHeap.getCommitted());
        memory.setNonHeapMax(nonHeap.getMax());
        metrics.setMemory(memory);

        // 收集所有内存池的详细信息（如 Eden Space、Survivor Space、Metaspace 等）
        List<JvmMetrics.MemoryPoolInfo> memoryPools = ManagementFactory.getMemoryPoolMXBeans()
                .stream()
                .map(pool -> {
                    MemoryUsage usage = pool.getUsage();
                    JvmMetrics.MemoryPoolInfo info = new JvmMetrics.MemoryPoolInfo();
                    info.setName(pool.getName());
                    info.setType(pool.getType().toString());
                    info.setUsed(usage.getUsed());
                    info.setCommitted(usage.getCommitted());
                    info.setMax(usage.getMax());
                    return info;
                })
                .collect(Collectors.toList());
        metrics.setMemoryPools(memoryPools);

        // 收集直接内存和映射内存的缓冲区池信息
        List<JvmMetrics.BufferPoolInfo> bufferPools = ManagementFactory.getPlatformMXBeans(BufferPoolMXBean.class)
                .stream()
                .map(bp -> {
                    JvmMetrics.BufferPoolInfo info = new JvmMetrics.BufferPoolInfo();
                    info.setName(bp.getName());
                    info.setCount(bp.getCount());
                    info.setMemoryUsed(bp.getMemoryUsed());
                    info.setTotalCapacity(bp.getTotalCapacity());
                    return info;
                })
                .collect(Collectors.toList());
        metrics.setBufferPools(bufferPools);

        // 收集所有垃圾回收器的统计信息（如 G1 Young Generation、G1 Old Generation 等）
        List<JvmMetrics.GcInfo> gcInfos = ManagementFactory.getGarbageCollectorMXBeans()
                .stream()
                .map(gcBean -> {
                    JvmMetrics.GcInfo gc = new JvmMetrics.GcInfo();
                    gc.setName(gcBean.getName());
                    gc.setCollectionCount(gcBean.getCollectionCount());
                    gc.setCollectionTime(gcBean.getCollectionTime());
                    return gc;
                })
                .collect(Collectors.toList());
        metrics.setGarbageCollectors(gcInfos);

        // 收集 JIT 编译器的统计信息
        CompilationMXBean compilerMXBean = ManagementFactory.getCompilationMXBean();
        JvmMetrics.CompilerInfo compiler = new JvmMetrics.CompilerInfo();
        compiler.setName(compilerMXBean.getName());
        compiler.setTotalCompilationTime(compilerMXBean.getTotalCompilationTime());
        metrics.setCompiler(compiler);

        // 收集线程相关的统计信息
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        JvmMetrics.ThreadInfo threadInfo = new JvmMetrics.ThreadInfo();
        threadInfo.setLive(threadMXBean.getThreadCount());
        threadInfo.setDaemon(threadMXBean.getDaemonThreadCount());
        threadInfo.setPeak(threadMXBean.getPeakThreadCount());
        threadInfo.setTotalStarted(threadMXBean.getTotalStartedThreadCount());
        metrics.setThreads(threadInfo);

        // 检测当前是否存在死锁线程
        long[] deadlockedIds = threadMXBean.findDeadlockedThreads();
        if (deadlockedIds != null) {
            metrics.setDeadlockedThreads(
                    Arrays.stream(deadlockedIds).boxed().collect(Collectors.toList())
            );
        }

        // 收集类加载相关的统计信息
        ClassLoadingMXBean classLoadingMXBean = ManagementFactory.getClassLoadingMXBean();
        JvmMetrics.ClassLoadingInfo classInfo = new JvmMetrics.ClassLoadingInfo();
        classInfo.setLoaded(classLoadingMXBean.getLoadedClassCount());
        classInfo.setTotalLoaded(classLoadingMXBean.getTotalLoadedClassCount());
        classInfo.setUnloaded(classLoadingMXBean.getUnloadedClassCount());
        metrics.setClasses(classInfo);

        // 收集 JVM 运行时环境信息
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        JvmMetrics.RuntimeInfo runtime = new JvmMetrics.RuntimeInfo();
        runtime.setStartTime(runtimeMXBean.getStartTime());
        runtime.setUptime(runtimeMXBean.getUptime());
        runtime.setVmName(runtimeMXBean.getVmName());
        runtime.setVmVersion(runtimeMXBean.getVmVersion());
        runtime.setVmVendor(runtimeMXBean.getVmVendor());
        runtime.setInputArguments(runtimeMXBean.getInputArguments());
        metrics.setRuntime(runtime);

        // 收集操作系统和物理资源信息
        OperatingSystemMXBean osMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

        JvmMetrics.OsInfo osInfo = new JvmMetrics.OsInfo();
        osInfo.setName(osMXBean.getName());
        osInfo.setVersion(osMXBean.getVersion());
        osInfo.setArch(osMXBean.getArch());
        osInfo.setAvailableProcessors(osMXBean.getAvailableProcessors());
        osInfo.setSystemLoadAverage(osMXBean.getSystemLoadAverage());

        // 如果支持 HotSpot 扩展信息，则收集更详细的系统资源信息
        if (osMXBean instanceof com.sun.management.OperatingSystemMXBean){
            com.sun.management.OperatingSystemMXBean hotSpotOsBean = (com.sun.management.OperatingSystemMXBean) osMXBean;
            double processCpuLoad = hotSpotOsBean.getProcessCpuLoad();
            double systemCpuLoad = hotSpotOsBean.getSystemCpuLoad();
            long totalMemory = hotSpotOsBean.getTotalPhysicalMemorySize();
            long freeMemory = hotSpotOsBean.getFreePhysicalMemorySize();
            long totalSwap = hotSpotOsBean.getTotalSwapSpaceSize();
            long freeSwap = hotSpotOsBean.getFreeSwapSpaceSize();
            osInfo.setProcessCpuLoad(processCpuLoad);
            osInfo.setSystemCpuLoad(systemCpuLoad);
            osInfo.setTotalPhysicalMemory(totalMemory);
            osInfo.setFreePhysicalMemory(freeMemory);
            osInfo.setTotalSwapSpace(totalSwap);
            osInfo.setFreeSwapSpace(freeSwap);
            metrics.setOs(osInfo);
        }

        return ResponseEntity.ok(metrics);
    }

}
