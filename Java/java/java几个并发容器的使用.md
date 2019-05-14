#	CountDownLatch、CyclicBarrier和Semaphore几个并发容器的使用

在讲这几个容器之前，本人讲述一个小面试题，该题的描述：让A、B、C三个线程同时执行，并且依次输出A、B、C三个字母十次。面试时，想法方向是对的，但是结果是错的。我想到的是两种实现，分别是：wait、notifyAll配合使用和并发容器的使用。

##	CountDownLatch

该容器主要的作用是：多个线程之间，主线程等待其他线程执行完毕开始往下执行。代码如下：

```java
public static void main(String[] args) throws InterruptedException {
        CountDownLatch downLatch = new CountDownLatch(3);
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName());
            downLatch.countDown();
        }).start();
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName());
            downLatch.countDown();
        }).start();
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName());
            downLatch.countDown();
        }).start();
        // 等待前面三个线程执行完毕
        downLatch.await();
        System.out.println(Thread.currentThread().getName());
    }
```

打印的结果如下：

```java
Thread-0
Thread-2
Thread-1
main
```

##	CyclicBarrier

该容器的作用是：所有线程会在某个时间等待其他线程都准备完毕才开始执行。代码如下：

```java
public static void main(String[] args) {
        CyclicBarrier barrier = new CyclicBarrier(3);
        new Thread(() -> {
            System.out.println("===== " + Thread.currentThread().getName() + " Start");
            try {
                barrier.await();
                System.out.println("===== " + Thread.currentThread().getName() + " Run");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "A").start();
        new Thread(() -> {
            System.out.println("===== " + Thread.currentThread().getName() + " Start");
            try {
                barrier.await();
                System.out.println("===== " + Thread.currentThread().getName() + " Run");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "B").start();
        new Thread(() -> {
            System.out.println("===== " + Thread.currentThread().getName() + " Start");
            try {
                barrier.await();
                System.out.println("===== " + Thread.currentThread().getName() + " Run");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "C").start();
    }
```

结果如下：

```java
===== A Start
===== C Start
===== C Run
===== A Run
===== B Run
```

统一执行完start就等待其他线程到start完毕后开始一起执行run

##	Semaphore

该容器的作用是： 限制同时执行的线程数量，所以可以用于限流作用；代码如下：

```java
public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(1);
        CyclicBarrier barrier = new CyclicBarrier(3);
        new Thread(() -> {
            try {
                System.out.println(" ===" + Thread.currentThread().getName());
                barrier.await();
                // 获取令牌
                semaphore.acquire();
                System.out.println(" ===" + Thread.currentThread().getName() + " Run");
                Thread.sleep(3000);
                // 返还令牌
                semaphore.release();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }).start();

        new Thread(() -> {
            try {
                System.out.println(" ===" + Thread.currentThread().getName());
                barrier.await();
                // 获取令牌
                semaphore.acquire();
                System.out.println(" ===" + Thread.currentThread().getName() + " Run");
                Thread.sleep(3000);
                // 返还令牌
                semaphore.release();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }).start();

        new Thread(() -> {
            try {
                System.out.println(" ===" + Thread.currentThread().getName());
                barrier.await();
                // 获取令牌
                semaphore.acquire();
                System.out.println(" ===" + Thread.currentThread().getName() + " Run");
                Thread.sleep(3000);
                // 返还令牌
                semaphore.release();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }).start();
    }
```

结果如下：

```
 ===Thread-0
 ===Thread-1
 ===Thread-2
 ===Thread-2 Run
 ===Thread-1 Run
 ===Thread-0 Run
```

2 Run 、1 Run、0 Run 之间会有间隔，间隔时间为3S,等待获取执行权的线程归还令牌；如果去掉`semaphore.acquire();`和`semaphore.release();`这两句，三个Run之间瞬间执行完毕

最后我们再次回到开始的面试题，这题有两个关键点是同时执行和循序打印ABC三个字母；考察的是对多线程之间，线程控制的掌握程度。

**同时执行** 我们可以使用wait 和 notifyAll配合使用解决 或 CyclicBarrier容器解决；方法2上面代码示例代码已经给出；1方法代码如下：

```java
	
	private static volatile int flag = 0;
    private static final Object object = new Object();
    private static void test() throws InterruptedException {
        new Thread(() -> {
            try {
                synchronized (object) {
                    flag++;
                    object.wait();
                    System.out.println(" = A = ");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                synchronized (object) {
                    flag++;
                    object.wait();
                    System.out.println(" = B = ");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                synchronized (object) {
                    flag++;
                    object.wait();
                    System.out.println(" = C = ");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        // 三个线程都启动完毕
        while (flag < 3) {
        }
        System.out.println(" ============= ");
        Thread.sleep(5000);
        synchronized (object) {
            object.notifyAll();
        }
    }
```

效果：打印完` ============= `暂停了5秒，然后打印ABC

**依次循环打印ABC**

1. 普通方法是通过定义一个可见变量分别取余判定打印，代码如下：

   ```java
       //TODO 可见性 或者使用 int 类型 为什么？
   	// 好像是基本类型变量存储在栈中，然后呢？求解
   	private static volatile Integer ctr = 1;
   
       private static void test2() {
           new Thread(() -> print(), "A").start();
           new Thread(() -> print(), "B").start();
           new Thread(() -> print(), "C").start();
       }
   
       private static void print() {
           int i = 1;
           String name = Thread.currentThread().getName();
           char ch = name.charAt(0);
           while (i <= 10) {
               if ((ctr % 3) == (ch + 1 - 'A') || ((ctr % 3 == 0) && ch == 'C')) {
                   System.out.println("i = " + i + "--" + ch);
                   i++;
                   ctr++;
               }
           }
       }
   ```

   效果如下：

   ```
   i = 1--A
   i = 1--B
   i = 1--C
   i = 2--A
   i = 2--B
   i = 2--C
   i = 3--A
   i = 3--B
   i = 3--C
   i = 4--A
   i = 4--B
   i = 4--C
   ....
   ```




2. 用Semaphore容器，思路是定义三个该容器，两两交叉互相释放（返还）对方的令牌；代码如下：

   ```java
   	private static Semaphore sepA = new Semaphore(1);
       private static Semaphore sepB = new Semaphore(0);
       private static Semaphore sepC = new Semaphore(0);
       private static void test2() {
           // A线程
           new Thread(() -> {
               try {
                   for (int i = 0; i < 10; i++) {
                       // 取A的令牌
                       sepA.acquire();
                       System.out.println("i = " + (i+1) + "--A");
                       // 返还B的令牌
                       sepB.release();
                   }
               } catch (Exception e) {
                   e.printStackTrace();
               }
           }).start();
   		
           // B线程
           new Thread(() -> {
               try {
                   for (int i = 0; i < 10; i++) {
                       // 获取B的令牌，A执行完才有B的令牌
                       sepB.acquire();
                       System.out.println("i = " + (i+1) + "--B");
                       // 释放C的令牌
                       sepC.release();
                   }
               } catch (Exception e) {
                   e.printStackTrace();
               }
           }).start();
   
           new Thread(() -> {
               try {
                   for (int i = 0; i < 10; i++) {
                       // 获取C的令牌，B执行完才有C的令牌
                       sepC.acquire();
                       System.out.println("i = " + (i+1) + "--C");
                       // 释放A的令牌，进入下一次循环
                       sepA.release();
                   }
               } catch (Exception e) {
                   e.printStackTrace();
               }
           }).start();
       }
   ```

   效果如下：

   ```
   i = 1--A
   i = 1--B
   i = 1--C
   i = 2--A
   i = 2--B
   i = 2--C
   i = 3--A
   i = 3--B
   i = 3--C
   i = 4--A
   i = 4--B
   i = 4--C
   ....
   ```



##	CountDownLatch源码分析

删掉注释，发现该类的代码很少，如下：

```java

package java.util.concurrent;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class CountDownLatch {
    /**
     * Synchronization control For CountDownLatch.
     * Uses AQS state to represent count.
     * 用于CountDownLatch的同步控制，使用AQS状态表示计数
     */
    private static final class Sync extends AbstractQueuedSynchronizer {
        private static final long serialVersionUID = 4982264981922014374L;

        Sync(int count) {
            setState(count);
        }

        int getCount() {
            return getState();
        }

        protected int tryAcquireShared(int acquires) {
            return (getState() == 0) ? 1 : -1;
        }

        protected boolean tryReleaseShared(int releases) {
            // Decrement count; signal when transition to zero
            for (;;) {
                int c = getState();
                if (c == 0)
                    return false;
                int nextc = c-1;
                if (compareAndSetState(c, nextc))
                    return nextc == 0;
            }
        }
    }

    private final Sync sync;

    /**
     * CountDownLatch 的代码如下
     */
    public CountDownLatch(int count) {
        if (count < 0) throw new IllegalArgumentException("count < 0");
        this.sync = new Sync(count);
    }

    public void await() throws InterruptedException {
        sync.acquireSharedInterruptibly(1);
    }

    public boolean await(long timeout, TimeUnit unit)
        throws InterruptedException {
        return sync.tryAcquireSharedNanos(1, unit.toNanos(timeout));
    }

    public void countDown() {
        sync.releaseShared(1);
    }
    
    public long getCount() {
        return sync.getCount();
    }
}

```

发现`CountDownLatch`的代码没啥看的，只是包装了一个API供调用，*在这里很**疑惑**为什么不直接暴露`Sync`而要再次包装一层* ，希望有人解答一下。

所以我们看一下`Sync`类做了什么，第一眼看到的是继承了`AbstractQueuedSynchronizer`类，该类就是同步容器`AQS`。所以说`CountDownLatch`的其实是基于`AQS`。对于AQS鉴于篇幅过长，暂不讲述。

##	CyclicBarrier源码分析

我们看一下几个重要的方法代码

###	字段和构造方法

```java
    /** The lock for guarding barrier entry */
    private final ReentrantLock lock = new ReentrantLock();
    /** Condition to wait on until tripped */
    private final Condition trip = lock.newCondition();

	/** The number of parties */
    private final int parties;
    /* The command to run when tripped */
    private final Runnable barrierCommand;
	/**
     * Number of parties still waiting. Counts down from parties to 0
     * on each generation.  It is reset to parties on each new
     * generation or when broken.
     */
    private int count;
	public CyclicBarrier(int parties) {
        this(parties, null);
    }    

	public CyclicBarrier(int parties, Runnable barrierAction) {
        if (parties <= 0) throw new IllegalArgumentException();
        this.parties = parties;
        this.count = parties;
        this.barrierCommand = barrierAction;
    }
```

`parties`  等待线程群组数，不知道这个翻译对不对

`barrierCommand` 

`count` 

构造方法没什么说的，初始化配置参数

###	await方法

代码如下：

```java
    public int await() throws InterruptedException, BrokenBarrierException {
        try {
            return dowait(false, 0L);
        } catch (TimeoutException toe) {
            throw new Error(toe); // cannot happen
        }
    }

    public int await(long timeout, TimeUnit unit)
        throws InterruptedException,
               BrokenBarrierException,
               TimeoutException {
        return dowait(true, unit.toNanos(timeout));
    }

    private int dowait(boolean timed, long nanos)
        throws InterruptedException, BrokenBarrierException,
               TimeoutException {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            final Generation g = generation;
			// 默认是false 如果为true说明打破屏障了，线程已经运行了
            if (g.broken)
                throw new BrokenBarrierException();

            if (Thread.interrupted()) {
                // 当前线程（用户线程）是否被打断
                breakBarrier();
                throw new InterruptedException();
            }
			
            // 每调用一次改方法，count 自减1
            int index = --count;
            if (index == 0) {  
                // tripped 如果count 为0 线程开始执行
                boolean ranAction = false;
                try {
                    final Runnable command = barrierCommand;
                    if (command != null)
                        command.run();
                    ranAction = true;
                    // 唤醒所有的等待线程并且重置容器配置
                    nextGeneration();
                    return 0;
                } finally {
                    if (!ranAction)
                        // 如果上面唤醒失败 打破屏障，唤醒所有线程
                        breakBarrier();
                }
            }

            // loop until tripped, broken, interrupted, or timed out
            // 一直循环等待
            for (;;) {
                try {
                    if (!timed)
                        // 没有超时设置 当前线程等待
                        trip.await();
                    else if (nanos > 0L)
                        // 没有超时设置 当前线程时效等待
                        nanos = trip.awaitNanos(nanos);
                } catch (InterruptedException ie) {
                    if (g == generation && ! g.broken) {
                        breakBarrier();
                        throw ie;
                    } else {
                        // We're about to finish waiting even if we had not
                        // been interrupted, so this interrupt is deemed to
                        // "belong" to subsequent execution.
                        Thread.currentThread().interrupt();
                    }
                }

                if (g.broken)
                    throw new BrokenBarrierException();

                if (g != generation)
                    return index;

                if (timed && nanos <= 0L) {
                    // 等待超时 打破屏障唤醒所有线程
                    breakBarrier();
                    throw new TimeoutException();
                }
            }
        } finally {
            lock.unlock();
        }
    }
	
	/**
	* 打破屏障，singnal所有线程，运行
	*/
    private void breakBarrier() {
        generation.broken = true;
        count = parties;
        trip.signalAll();
    }
```

两个`public`方法都最终调用的是`private`方法，具体解释看代码片段注释。从源码中我们可以发现`CyclicBarrier`的实现原理是基于`ReentrantLock` 和 `Condition` 的实现，这两玩意是什么，自行百度。其实和 对象的`await`  和 `notify/notifyAll` 方法类似的功能，而`ReentrantLock` 和 `Condition` 又有一部分是基于 `AQS`实现的。此时是不是会发现`AQS`在并发控制和多线程的地位很高，有时间该去分析一下它的源码.

###	Semaphore的源码浅析

###	内部类

```java
    // 又是基于AQS的实现
	abstract static class Sync extends AbstractQueuedSynchronizer {
        private static final long serialVersionUID = 1192457210091910933L;

        Sync(int permits) {
            setState(permits);
        }

        final int getPermits() {
            return getState();
        }
		
        // 非公平的获取许可认证
        final int nonfairTryAcquireShared(int acquires) {
            for (;;) {
                int available = getState();
                int remaining = available - acquires;
                if (remaining < 0 || // 就因为没有剩余判断就非公平，为啥？求解释
                    compareAndSetState(available, remaining))
                    return remaining;
            }
        }
		
        // 释放许可认证
        protected final boolean tryReleaseShared(int releases) {
            for (;;) {
                int current = getState();
                int next = current + releases;
                if (next < current) // overflow
                    throw new Error("Maximum permit count exceeded");
                if (compareAndSetState(current, next))
                    return true;
            }
        }
		
        // 减少许可认证
        final void reducePermits(int reductions) {
            for (;;) {
                int current = getState();
                int next = current - reductions;
                if (next > current) // underflow
                    throw new Error("Permit count underflow");
                if (compareAndSetState(current, next))
                    return;
            }
        }
		
        // 清空许可认证
        final int drainPermits() {
            for (;;) {
                int current = getState();
                if (current == 0 || compareAndSetState(current, 0))
                    return current;
            }
        }
    }

    /**
     * NonFair version
     */
    static final class NonfairSync extends Sync {
        private static final long serialVersionUID = -2694183684443567898L;

        NonfairSync(int permits) {
            super(permits);
        }

        protected int tryAcquireShared(int acquires) {
            return nonfairTryAcquireShared(acquires);
        }
    }

    /**
     * Fair version
     */
    static final class FairSync extends Sync {
        private static final long serialVersionUID = 2014338818796000944L;

        FairSync(int permits) {
            super(permits);
        }

        protected int tryAcquireShared(int acquires) {
            for (;;) {
                if (hasQueuedPredecessors())
                    return -1;
                int available = getState();
                int remaining = available - acquires;
                if (remaining < 0 ||
                    compareAndSetState(available, remaining))
                    return remaining;
            }
        }
    }
```

发现这些内部类又是基于`AQS`的实现。



###	构造方法

```java
    public Semaphore(int permits) {
        sync = new NonfairSync(permits);
    }

    public Semaphore(int permits, boolean fair) {
        sync = fair ? new FairSync(permits) : new NonfairSync(permits);
    }
```

构造方法比较简单，就两个。通过构造方法发现默认是非公平实现，要公平实现一定要构造方法中指明。

###	acquire和release方法

```java
    public void acquire() throws InterruptedException {
        sync.acquireSharedInterruptibly(1);
    }

    public void acquire(int permits) throws InterruptedException {
        if (permits < 0) throw new IllegalArgumentException();
        sync.acquireSharedInterruptibly(permits);
    }

    public void acquireUninterruptibly() {
        sync.acquireShared(1);
    }

    public void acquireUninterruptibly(int permits) {
        if (permits < 0) throw new IllegalArgumentException();
        sync.acquireShared(permits);
    }

    public boolean tryAcquire() {
        return sync.nonfairTryAcquireShared(1) >= 0;
    }

    public boolean tryAcquire(int permits) {
        if (permits < 0) throw new IllegalArgumentException();
        return sync.nonfairTryAcquireShared(permits) >= 0;
    }

    public boolean tryAcquire(long timeout, TimeUnit unit)
        throws InterruptedException {
        return sync.tryAcquireSharedNanos(1, unit.toNanos(timeout));
    }


    public boolean tryAcquire(int permits, long timeout, TimeUnit unit)
        throws InterruptedException {
        if (permits < 0) throw new IllegalArgumentException();
        return sync.tryAcquireSharedNanos(permits, unit.toNanos(timeout));
    }

    public void release() {
        sync.releaseShared(1);
    }

    public void release(int permits) {
        if (permits < 0) throw new IllegalArgumentException();
        sync.releaseShared(permits);
    }
```

每一个`acquire`和`release`都有两个，一个是默认获取/释放一个令牌和获取/释放自定义数量令牌。



## 	总结

最后我们发现其实我们可以基与`ReentrantLock`和`Condition`再实现等待一起执行，代码如下：

```java
    public static void main(String[] args) throws InterruptedException {
        thread();
        System.out.println("before sleep");
        Thread.sleep(20000);
        System.out.println("after sleep");
        thread();
    }

    private static ReentrantLock lock = new ReentrantLock();
    private static Condition condition = lock.newCondition();
    private static volatile Integer count = 2;

    private static void thread() {
        new Thread(() -> {
            try {
                lock.lock();
                System.out.println(Thread.currentThread().getName() + " Start");
                if (count > 1) {
                    count--;
                    condition.await();
                } else {
                    condition.signalAll();
                }
                System.out.println(Thread.currentThread().getName() + " Run");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }

        }).start();
    }
```

效果如下：

```
before sleep
Thread-0 Start
after sleep
Thread-1 Start
Thread-1 Run
Thread-0 Run
```

线程0 会等待 线程1 Start 之后开始一起执行

**发现`AQS`在多线程和并发控制地位非常重要，离不开它的身影。**

本人没有仔细观看`AQS`的源码，所以没有解释和`AQS`相关的方法功能以及作用，后面有时间看了`AQS`源码后再来补全吧。还有用词不准确的地方希望大家指出。只是通过这么一个面试题，学习了一下线程之间控制的方法方式。