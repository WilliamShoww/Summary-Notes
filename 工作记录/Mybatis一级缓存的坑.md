#		Mybatis一级缓存的坑

##		前序

接着上篇文章[插入唯一数据的各种问题](https://gitee.com/cfacebook/Summary-Notes/blob/master/工作记录/Spring事物认识不清.md)。中间过程说Spring的事务，是不是在生命周期的时候程序提交了事务，但是数据库没有提交事务。其实不会的，当时写文章的时候只是根据当时的现象去推测，后来我用代码测试实际是会提交事务的。Spring是没有错的。代码如下：

```java
@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
    public void update(User user) {
        // 根据username查询用户，username普通索引
        // SQL: select * from user where username = #{username} limit 1
        User top = userMapper.findTopByUsername(user);
        if (null == top) {
            System.out.println("lock 之前 = " + Thread.currentThread().getName());
            lock.lock();
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                @SneakyThrows
                @Override
                public void afterCompletion(int status) {
                    System.out.println("解锁 = " + Thread.currentThread().getName());
                    Thread.sleep(30000);
                    lock.unlock();
                    super.afterCompletion(status);
                }
            });
            // SQL: select * from user where username = #{username} limit 1
            top = userMapper.findTopByUsername(user);
            System.out.println("second top = " + top);
            if (null == top) {
                top = new User();
                top.setUsername(user.getUsername())
                        .setNum(user.getNum())
                ;
                userMapper.save(top);
                return;
            }
        }
        userMapper.update(user);
    }
```

之前说过，并发进入第一个null判断的线程会串行化执行lock到unlock的代码，但是表现的是，并发的线程在第二次查询里就会读不到值，整段表现的是不可重复读的现象。但**实际隔离级别是读提交**，不符合原理。于是我就猜测Spring事务提交和真实事务提交可能有差别，还在Spring源码中找到了一段注释。后来想想还是亲手验证一下，以防误人子弟，那就不好了，所以就改成上面代码。如果我的猜测正确，那在unlock执行之前，数据库就查不到新插入的这条记录。实际上在该线程sleep期间，我去数据库是可以查询到最新插入的记录，这说明我的猜测是错误，对那段源码注释也理解错了。

到此，问题又没有思绪了，突然有一天，朋友在讨论Mybatis缓存。此时豁然开朗了，在Spring事务中，是会使用Mybatis的一级缓存的。瞬间可以解释所有的现象了。为什么呢？因为并发进入第一个空判断的线程，之前执行过一次查询，所以第二次查询不会去查询数据库，而是会直接缓存读。因此就像可重复读一样，在此出现了。那为什么for update查询又是可以查到的呢？因为for update查询Mybatis不会走一级缓存，所以for update查询是可以读到上一个线程插入的值。后来看Mybatis的查询日志，发现和这个设想一模一样。

##		缓存坑总结

后来百度了一下，发现在多线程环境下mybatis的缓存会很多坑，各种莫名其妙的问题出现，因为它的session线程不安全，看源码就知道，各种字段变量。

1. 破坏事务的语义，例子在上面；
2. 脏数据；



**【参考】**

[mybatis一级缓存让我憔悴](https://www.cnblogs.com/Yatces/p/12342481.html)                                     https://www.cnblogs.com/Yatces/p/12342481.html

[MyBatis 一级缓存在分布式下的坑](https://blog.csdn.net/valada/article/details/104012588)                         https://blog.csdn.net/valada/article/details/104012588

[MyBatis一级缓存引起的无穷递归](https://www.cnblogs.com/Leo_wl/p/5377121.html)                          https://www.cnblogs.com/Leo_wl/p/5377121.html

[mybatis的一级缓存会不会产生脏数据问题？](https://www.zhihu.com/question/53321129)       https://www.zhihu.com/question/53321129

