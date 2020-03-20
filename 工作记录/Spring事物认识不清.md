#		插入唯一数据的各种问题

上篇文章中解决了，死锁问题，但是新问题出现，我发现user_data表中有重复的user_id记录。原因分析不言而喻，并发插入引起。上次代码：

```java
@Transactional(propagation = Propagation.REQUIRES_NEW)
public void update(userId, numData){
   D userData = selectByUserId(userId);
    if(null == userData){
        // 初始化且合并数据
        merge(userData,numData);
        save(userData);
    }
    // 合并数据
    merge(userData,numData);
    update(userData);
}
```

当两个请求同时到达update方法的时候，都发现不存在，在没有唯一索引的情况下，就会同时插入两条一模一样user_id的数据。说一下解决问题的过程，很曲折。在说解决问题过程中，交代一个背景就是我们系统还是单服，不用考虑分布式并发问题的解决方案，数据库的隔离级别是读提交(READ-COMMITTED)。

##		简单啊，并发问题而已，方法加上关键字synchronized

有人肯定会说整个方法加锁，并发度低的问题，暂时我们不去考虑这个问题，因为这段代码执行操作不多，速度很快。加上关键字synchronized后，代码如下：

```java
@Transactional(propagation = Propagation.REQUIRES_NEW)
public synchronized void update(userId, numData){
   D userData = selectByUserId(userId);
    if(null == userData){
        // 初始化且合并数据
        merge(userData,numData);
        save(userData);
    }
    // 合并数据
    merge(userData,numData);
    update(userData);
}
```

当时的自己是如此的潇洒自如啊，洋洋自得。然后测试一下，第一次没问题，第二次，一首凉凉送给自己。为什么？一样出现重复数据，这是为什么呢?因为自己对Spring事物的理解不透彻。后来百度一下，查到原因了。

**原因：Spring的事物是基于动态代理AOP实现的，所以锁会在事物提交之前释放。**

想想，如果事物没有提交，但是锁释放了，就会有第二个线程的select还是不存在，所以插入重复的user_id数据记录了。那怎么办呢？后来终于找到了一个方法（神坑方法）

##		注册Spring事物的生命回调

方法就是：注册Spring的事物的生命回调，使用`ReentrantLock`，在回调里unlock；两个类：`TransactionSynchronizationManager` 和 `TransactionSynchronizationAdapter`不知道怎么使用，自行百度，改完之后代码如下：

```java
private final Lock lock = new ReentrantLock( );
@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
    public void update(Long userId, UserRoleData data) {
        // 方法1
        UserData userData = selectByUserId(userId);
        if (null == userData) {
            lock.lock( );
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter( ) {
                @Override
                public void afterCompletion(int status) {
                    try {
                        super.afterCompletion(status);
                    } finally {
                        lock.unlock( );
                    }
                }
            });
            // 方法2
            userData = selectByUserId(userId);
            if (null == userData) {
                // 初始化且合并数据
        		merge(userData,numData);
        		save(userData);
                return;
            }
        }
        // 合并数据
    	merge(userData,numData);
    	update(userData);
    }
```

以上代码，我想了一下，锁整个方法，并发性低，其实大部分情况都是更新，插入情况很少，这不禁让我想起来单例模式的双重锁检查的写法，于是改成上述上代码。

再说一下，我此时根据百度的认识，`TransactionSynchronizationAdapter`中的方法，各个方法的使用时机，认识如下：

1. afterCommit 在事物提交之后被调用；
2. afterCompletion 在事物完成之后会被调用。

先说一下结局吧，结局就是并发执行，还是会有重复user_id的记录。瞬间泪奔了，我也在此处卡了很久。因为从目前的认识来说，不应该存在这种情况。我们分析一下，为啥不会啊？

1. 第一个线程判断不存在数据所以会在进入第二个判断之前，lock住代码，在事物完成之后unlock；
2. 就算有一个线程进入了第一次判空里，在上一个线程释放锁后，进入lock块，第二次查询肯定是存在数据的（因为事物读提交，所以上个线程提交之后，这里是可以查询到数据的），因此不会进入查询，而是走到最后进行更新。

但是为啥结局不是这样的呢？我瞬间没有了放向，问了很多人，很多人也一时间不知道什么原因，反正各种说法。

说法如下：

1. Spring的事物传播特性是REQUIRES_NEW的时候，会有危险的事情发生；大部分都是讲另起事物，将上个事物挂起，所以可能发生锁等待事情；子事物发生异常不会让父事物回滚。这么多好像跟我这个没关系。
2. 还有就是事物隔离级别确定有没有错，结果我反复确认，没有错（就是读提交，代码和数据库都标记上）。
3. 

