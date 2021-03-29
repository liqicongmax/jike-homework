1.并行收集
并行收集器可以理解是多线程串行收集，在串行收集基础上采用多线程方式进行GC，很好的弥补了串行收集的不足，可以大幅缩短停顿时间（如下图表示的停顿时长高度，并发比并行要短），因此对于空间不大的区域（如young generation），采用并行收集器停顿时间很短，回收效率高，适合高频率执行。
-XX:+UseParallelGC -Xmx500m -Xms500m
第一次youngGC的日志：
[GC (Allocation Failure) [PSYoungGen: 128000K->21238K(149248K)] 128000K->43089K(490752K), 0.0066139 secs] [Times: user=0.05 sys=0.02, real=0.01 secs] 

128000K->21238K(149248K) 表示young区内存从128m被压缩到21m，括号内为整个young区大小
128000K->43089K(490752K) 表示整个堆内存大小的占用从128m到了43m
21m和43m差出来的22m就是gc到老年代的内容
GC花费时间6ms
第一次fullGC：
[Full GC (Ergonomics) [PSYoungGen: 19711K->0K(113664K)] [ParOldGen: 312729K->238311K(341504K)] 332441K->238311K(455168K), [Metaspace: 2148K->2148K(4480K)], 0.0298262 secs] [Times: user=0.11 sys=0.00, real=0.03 secs] 
Young区整个清空，老年代从312m到238m，花费时间是younggc的几倍29ms

调整XMX XMS大小，如果改小，GC频率会明显提高，如果改大，就会降低GC频率，FULLgc也更少

2.串行收集
-XX:+PrintGCDetails -Xmx500m -Xms500m -XX:+UseSerialGC
日志类似并行收集
3.CMS
初始标记（CMS initial mark）

并发标记（CMS concurrent mark）

并发预清理（CMS-concurrent-preclean）

重新标记（CMS remark）

并发清除（CMS concurrent sweep）

并发重置（CMS-concurrent-reset）
-XX:+PrintGCDetails -Xmx500m -Xms500m -XX:+UseConcMarkSweepGC

[GC (CMS Initial Mark) [1 CMS-initial-mark: 194968K(341376K)] 212576K(494976K), 0.0005301 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
Cms初始化标记，这个步骤会发生gc暂停。194968K(341376K)194m为老年代当前占用内存大小，341m为老年代总内存大小，212576K(494976K) 212m为当前堆内存大小，494m为总堆内存大小

[CMS-concurrent-abortable-preclean-start]
[GC (Allocation Failure) [ParNew: 153600K->17023K(153600K), 0.0148560 secs] 348568K->257406K(494976K), 0.0148772 secs] [Times: user=0.06 sys=0.01, real=0.02 secs] 
[GC (Allocation Failure) [ParNew: 152833K->17023K(153600K), 0.0139124 secs] 393216K->300329K(494976K), 0.0139398 secs] [Times: user=0.06 sys=0.02, real=0.01 secs] 
[GC (Allocation Failure) [ParNew: 153583K->17023K(153600K), 0.0135558 secs] 436888K->345986K(494976K), 0.0135764 secs] [Times: user=0.06 sys=0.02, real=0.01 secs] 
并发预清理过程中进行了多次的younggc。

[GC (CMS Final Remark) [YG occupancy: 20026 K (153600 K)][Rescan (parallel) , 0.0001334 secs][weak refs processing, 0.0000069 secs][class unloading, 0.0002075 secs][scrub symbol table, 0.0002602 secs][scrub string table, 0.0000183 secs][1 CMS-remark: 328962K(341376K)] 348989K(494976K), 0.0006646 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
最终标记结束，进行并发清理->并发重置


4.G1GC
在cms的基础上更优化，使用标记-整理的方法不会产生空间碎片
-XX:+PrintGC -Xmx500m -Xms500m -XX:+UseG1GC
GC步骤类似CMS，在调高堆内存的情况下，会显著降低老年代的GC频率，甚至只有younggc