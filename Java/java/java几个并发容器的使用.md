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

