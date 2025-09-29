package org.htx.vigil.jvm.dto;

import java.io.Serializable;
import java.util.List;

public class JvmMetrics implements Serializable {

    /** 内存基本信息 */
    private MemoryInfo memory;

    /** 内存池信息（Eden/Survivor/Old Gen/Metaspace/Code Cache 等） */
    private List<MemoryPoolInfo> memoryPools;

    /** 缓冲区池信息（Direct Memory、Mapped Memory） */
    private List<BufferPoolInfo> bufferPools;

    /** 垃圾回收器信息（分代/区域级别） */
    private List<GcInfo> garbageCollectors;

    /** JIT 编译器信息 */
    private CompilerInfo compiler;

    /** 线程统计信息 */
    private ThreadInfo threads;

    /** 死锁线程 ID 列表 */
    private List<Long> deadlockedThreads;

    /** 类加载信息 */
    private ClassLoadingInfo classes;

    /** JVM 运行时信息 */
    private RuntimeInfo runtime;

    /** 操作系统及物理资源信息 */
    private OsInfo os;

    // ================= 内存基本信息 =================
    public static class MemoryInfo {
        /** 已使用堆内存（字节） */
        private long heapUsed;
        /** 已提交给 JVM 的堆内存（字节） */
        private long heapCommitted;
        /** 堆内存最大值（字节，-1 表示未设置） */
        private long heapMax;
        /** 已使用非堆内存（字节） */
        private long nonHeapUsed;
        /** 已提交给 JVM 的非堆内存（字节） */
        private long nonHeapCommitted;
        /** 非堆内存最大值（字节，-1 表示不限制） */
        private long nonHeapMax;

        // getter / setter
        public long getHeapUsed() { return heapUsed; }
        public void setHeapUsed(long heapUsed) { this.heapUsed = heapUsed; }
        public long getHeapCommitted() { return heapCommitted; }
        public void setHeapCommitted(long heapCommitted) { this.heapCommitted = heapCommitted; }
        public long getHeapMax() { return heapMax; }
        public void setHeapMax(long heapMax) { this.heapMax = heapMax; }
        public long getNonHeapUsed() { return nonHeapUsed; }
        public void setNonHeapUsed(long nonHeapUsed) { this.nonHeapUsed = nonHeapUsed; }
        public long getNonHeapCommitted() { return nonHeapCommitted; }
        public void setNonHeapCommitted(long nonHeapCommitted) { this.nonHeapCommitted = nonHeapCommitted; }
        public long getNonHeapMax() { return nonHeapMax; }
        public void setNonHeapMax(long nonHeapMax) { this.nonHeapMax = nonHeapMax; }
    }

    // ================= 内存池信息 =================
    public static class MemoryPoolInfo {
        /** 内存池名称（如 Eden Space、Survivor Space、Metaspace、Code Cache 等） */
        private String name;
        /** 内存类型：HEAP / NON_HEAP */
        private String type;
        /** 已使用内存（字节） */
        private long used;
        /** 已提交内存（字节） */
        private long committed;
        /** 最大内存值（字节，可能为 -1 表示不限制） */
        private long max;

        // getter / setter
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public long getUsed() { return used; }
        public void setUsed(long used) { this.used = used; }
        public long getCommitted() { return committed; }
        public void setCommitted(long committed) { this.committed = committed; }
        public long getMax() { return max; }
        public void setMax(long max) { this.max = max; }
    }

    // ================= BufferPool 信息 =================
    public static class BufferPoolInfo {
        /** 缓冲区池名称，如 direct 或 mapped */
        private String name;
        /** 当前缓冲区数量 */
        private long count;
        /** 已使用内存（字节） */
        private long memoryUsed;
        /** 缓冲区总容量（字节） */
        private long totalCapacity;

        // getter / setter
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public long getCount() { return count; }
        public void setCount(long count) { this.count = count; }
        public long getMemoryUsed() { return memoryUsed; }
        public void setMemoryUsed(long memoryUsed) { this.memoryUsed = memoryUsed; }
        public long getTotalCapacity() { return totalCapacity; }
        public void setTotalCapacity(long totalCapacity) { this.totalCapacity = totalCapacity; }
    }

    // ================= GC 信息 =================
    public static class GcInfo {
        /** GC 名称（如 G1 Young Generation、G1 Old Generation） */
        private String name;
        /** GC 执行次数 */
        private long collectionCount;
        /** GC 总耗时（毫秒） */
        private long collectionTime;

        // getter / setter
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public long getCollectionCount() { return collectionCount; }
        public void setCollectionCount(long collectionCount) { this.collectionCount = collectionCount; }
        public long getCollectionTime() { return collectionTime; }
        public void setCollectionTime(long collectionTime) { this.collectionTime = collectionTime; }
    }

    // ================= JIT 编译信息 =================
    public static class CompilerInfo {
        /** JIT 编译器名称（如 HotSpot 64-Bit Tiered Compilers） */
        private String name;
        /** 累计编译耗时（毫秒） */
        private long totalCompilationTime;

        // getter / setter
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public long getTotalCompilationTime() { return totalCompilationTime; }
        public void setTotalCompilationTime(long totalCompilationTime) { this.totalCompilationTime = totalCompilationTime; }
    }

    // ================= 线程信息 =================
    public static class ThreadInfo {
        /** 当前活跃线程数 */
        private int live;
        /** 当前守护线程数 */
        private int daemon;
        /** 自 JVM 启动以来的峰值线程数 */
        private int peak;
        /** 自 JVM 启动以来已启动线程总数 */
        private long totalStarted;

        // getter / setter
        public int getLive() { return live; }
        public void setLive(int live) { this.live = live; }
        public int getDaemon() { return daemon; }
        public void setDaemon(int daemon) { this.daemon = daemon; }
        public int getPeak() { return peak; }
        public void setPeak(int peak) { this.peak = peak; }
        public long getTotalStarted() { return totalStarted; }
        public void setTotalStarted(long totalStarted) { this.totalStarted = totalStarted; }
    }

    // ================= 类加载信息 =================
    public static class ClassLoadingInfo {
        /** 当前已加载类数量 */
        private int loaded;
        /** 自 JVM 启动以来加载过的类总数 */
        private long totalLoaded;
        /** 已卸载的类数量 */
        private long unloaded;

        // getter / setter
        public int getLoaded() { return loaded; }
        public void setLoaded(int loaded) { this.loaded = loaded; }
        public long getTotalLoaded() { return totalLoaded; }
        public void setTotalLoaded(long totalLoaded) { this.totalLoaded = totalLoaded; }
        public long getUnloaded() { return unloaded; }
        public void setUnloaded(long unloaded) { this.unloaded = unloaded; }
    }

    // ================= 运行时信息 =================
    public static class RuntimeInfo {
        /** JVM 启动时间（Epoch 毫秒） */
        private long startTime;
        /** JVM 已运行时长（毫秒） */
        private long uptime;
        /** JVM 名称（如 OpenJDK 64-Bit Server VM） */
        private String vmName;
        /** JVM 版本（如 17.0.10+7） */
        private String vmVersion;
        /** JVM 提供商（如 Eclipse Adoptium） */
        private String vmVendor;
        /** JVM 启动参数列表（如 -Xmx512m、-XX:+UseG1GC） */
        private List<String> inputArguments;

        // getter / setter
        public long getStartTime() { return startTime; }
        public void setStartTime(long startTime) { this.startTime = startTime; }
        public long getUptime() { return uptime; }
        public void setUptime(long uptime) { this.uptime = uptime; }
        public String getVmName() { return vmName; }
        public void setVmName(String vmName) { this.vmName = vmName; }
        public String getVmVersion() { return vmVersion; }
        public void setVmVersion(String vmVersion) { this.vmVersion = vmVersion; }
        public String getVmVendor() { return vmVendor; }
        public void setVmVendor(String vmVendor) { this.vmVendor = vmVendor; }
        public List<String> getInputArguments() { return inputArguments; }
        public void setInputArguments(List<String> inputArguments) { this.inputArguments = inputArguments; }
    }

    // ================= 操作系统信息 =================
    public static class OsInfo {
        /** 操作系统名称（如 Linux、Windows 10） */
        private String name;
        /** 操作系统版本号 */
        private String version;
        /** 系统架构（如 amd64） */
        private String arch;
        /** 可用 CPU 核心数 */
        private int availableProcessors;
        /** 系统负载平均值（1 分钟），-1 表示不可用 */
        private double systemLoadAverage;
        /** JVM 进程 CPU 使用率（0.0 ~ 1.0） */
        private double processCpuLoad;
        /** 系统 CPU 使用率（0.0 ~ 1.0） */
        private double systemCpuLoad;
        /** 总物理内存（字节） */
        private long totalPhysicalMemory;
        /** 空闲物理内存（字节） */
        private long freePhysicalMemory;
        /** 总 Swap 内存（字节） */
        private long totalSwapSpace;
        /** 空闲 Swap 内存（字节） */
        private long freeSwapSpace;

        // getter / setter
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }
        public String getArch() { return arch; }
        public void setArch(String arch) { this.arch = arch; }
        public int getAvailableProcessors() { return availableProcessors; }
        public void setAvailableProcessors(int availableProcessors) { this.availableProcessors = availableProcessors; }
        public double getSystemLoadAverage() { return systemLoadAverage; }
        public void setSystemLoadAverage(double systemLoadAverage) { this.systemLoadAverage = systemLoadAverage; }
        public double getProcessCpuLoad() { return processCpuLoad; }
        public void setProcessCpuLoad(double processCpuLoad) { this.processCpuLoad = processCpuLoad; }
        public double getSystemCpuLoad() { return systemCpuLoad; }
        public void setSystemCpuLoad(double systemCpuLoad) { this.systemCpuLoad = systemCpuLoad; }
        public long getTotalPhysicalMemory() { return totalPhysicalMemory; }
        public void setTotalPhysicalMemory(long totalPhysicalMemory) { this.totalPhysicalMemory = totalPhysicalMemory; }
        public long getFreePhysicalMemory() { return freePhysicalMemory; }
        public void setFreePhysicalMemory(long freePhysicalMemory) { this.freePhysicalMemory = freePhysicalMemory; }
        public long getTotalSwapSpace() { return totalSwapSpace; }
        public void setTotalSwapSpace(long totalSwapSpace) { this.totalSwapSpace = totalSwapSpace; }
        public long getFreeSwapSpace() { return freeSwapSpace; }
        public void setFreeSwapSpace(long freeSwapSpace) { this.freeSwapSpace = freeSwapSpace; }
    }

    // ================= 主类 getter / setter =================
    public MemoryInfo getMemory() { return memory; }
    public void setMemory(MemoryInfo memory) { this.memory = memory; }
    public List<MemoryPoolInfo> getMemoryPools() { return memoryPools; }
    public void setMemoryPools(List<MemoryPoolInfo> memoryPools) { this.memoryPools = memoryPools; }
    public List<BufferPoolInfo> getBufferPools() { return bufferPools; }
    public void setBufferPools(List<BufferPoolInfo> bufferPools) { this.bufferPools = bufferPools; }
    public List<GcInfo> getGarbageCollectors() { return garbageCollectors; }
    public void setGarbageCollectors(List<GcInfo> garbageCollectors) { this.garbageCollectors = garbageCollectors; }
    public CompilerInfo getCompiler() { return compiler; }
    public void setCompiler(CompilerInfo compiler) { this.compiler = compiler; }
    public ThreadInfo getThreads() { return threads; }
    public void setThreads(ThreadInfo threads) { this.threads = threads; }
    public List<Long> getDeadlockedThreads() { return deadlockedThreads; }
    public void setDeadlockedThreads(List<Long> deadlockedThreads) { this.deadlockedThreads = deadlockedThreads; }
    public ClassLoadingInfo getClasses() { return classes; }
    public void setClasses(ClassLoadingInfo classes) { this.classes = classes; }

    public RuntimeInfo getRuntime() {
        return runtime;
    }

    public void setRuntime(RuntimeInfo runtime) {
        this.runtime = runtime;
    }

    public OsInfo getOs() {
        return os;
    }

    public void setOs(OsInfo os) {
        this.os = os;
    }
}
   
