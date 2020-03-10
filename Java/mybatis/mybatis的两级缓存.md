

#		Mybatis的两级缓存

Mybatis的缓存有两种，分为一级缓存和二级缓存，它们的作用域不同。

##		一级缓存

一级缓存我个人也叫session缓存，它默认是开启的，不可配置的。为啥叫session缓存，是因为它的作用域是session范围内的，也就是说同一个session的情况才能使用到一级缓存，目前我遇到的情况就是在一个事务内查询两次数据就会使用到一级缓存。所以说这个一级缓存其实很坑的，基本使用不到，在分布式环境下还可能有副作用。

特点总结如下：

1. 默认开启；
2. 作用范围同一个session，也就是说在同一个事务内，两次查询才会用到缓存；非事务的情况下，每次调用mapper都是新的session，所以不会用到缓存；
3. 缓存的效果很鸡肋，分布式环境还可能有副作用。

源码在`BaseExecutor`类下，如下：

```java
@Override
public <E> List<E> query(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler) throws SQLException {
    BoundSql boundSql = ms.getBoundSql(parameter);
    // 缓存的Key
    CacheKey key = createCacheKey(ms, parameter, rowBounds, boundSql);
    return query(ms, parameter, rowBounds, resultHandler, key, boundSql);
  }

public <E> List<E> query(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, CacheKey key, BoundSql boundSql) throws SQLException {
    ErrorContext.instance().resource(ms.getResource()).activity("executing a query").object(ms.getId());
    if (closed) {
      throw new ExecutorException("Executor was closed.");
    }
    if (queryStack == 0 && ms.isFlushCacheRequired()) {
      // flushCache 配置为true每次查询前都会清空缓存(默认是false)
      clearLocalCache();
    }
    List<E> list;
    try {
      queryStack++;
      list = resultHandler == null ? (List<E>) localCache.getObject(key) : null;
      if (list != null) {
        // 缓存命中
        handleLocallyCachedOutputParameters(ms, key, parameter, boundSql);
      } else {
        list = queryFromDatabase(ms, parameter, rowBounds, resultHandler, key, boundSql);
      }
    } finally {
      queryStack--;
    }
    if (queryStack == 0) {
      for (DeferredLoad deferredLoad : deferredLoads) {
        deferredLoad.load();
      }
      // issue #601
      deferredLoads.clear();
      if (configuration.getLocalCacheScope() == LocalCacheScope.STATEMENT) {
        // issue #482
        // 一级缓存的作用域SESSION 或者 STATEMENT
        // 默认是SESSION 通过 localCacheScope 配置参数配置
        clearLocalCache();
      }
    }
    return list;
  }

/**
在事务提交或者回滚的时候，会清空缓存。
**/
@Override
  public void commit(boolean required) throws SQLException {
    if (closed) {
      throw new ExecutorException("Cannot commit, transaction is already closed");
    }
    clearLocalCache();
    flushStatements();
    if (required) {
      transaction.commit();
    }
  }

  @Override
  public void rollback(boolean required) throws SQLException {
    if (!closed) {
      try {
        clearLocalCache();
        flushStatements(true);
      } finally {
        if (required) {
          transaction.rollback();
        }
      }
    }
  }
```

在事务提交或者回滚的时候，会清空缓存。

##		二级缓存

二级缓存默认是关闭的，需要自己手动开启，开启的方式有`cache`标签和`@CacheNamespace`。默认实现是`org.apache.ibatis.cache.impl.PerpetualCache` 。二级缓存的作用域是`mapper`也就是`namespace`，也就是说一个mapper内都可以使用，但是`mapper`内任意一个方法都会导致缓存失效，所以个人认为这个缓存也很鸡肋，有点像MySQL的缓存一样。

源码分析，主要入口在`org.apache.ibatis.executor.CachingExecutor`  中query方法和update方法，里面有个成员变量`org.apache.ibatis.cache.TransactionalCacheManager` 对象，管理着`org.apache.ibatis.cache.decorators.TransactionalCache`事务，在事务中提交或者回滚事务都会清空对应缓存，如下：

CachingExecutor类代码

```java
@Override
  public int update(MappedStatement ms, Object parameterObject) throws SQLException {
    flushCacheIfRequired(ms);
    return delegate.update(ms, parameterObject);
  }

// 根据配置是否每次都刷新缓存
private void flushCacheIfRequired(MappedStatement ms) {
    Cache cache = ms.getCache();
    if (cache != null && ms.isFlushCacheRequired()) {
      tcm.clear(cache);
    }
  }

  @Override
  public <E> List<E> query(MappedStatement ms, Object parameterObject, RowBounds rowBounds, ResultHandler resultHandler) throws SQLException {
    BoundSql boundSql = ms.getBoundSql(parameterObject);
    CacheKey key = createCacheKey(ms, parameterObject, rowBounds, boundSql);
    return query(ms, parameterObject, rowBounds, resultHandler, key, boundSql);
  }

@Override
  public <E> List<E> query(MappedStatement ms, Object parameterObject, RowBounds rowBounds, ResultHandler resultHandler, CacheKey key, BoundSql boundSql)
      throws SQLException {
    Cache cache = ms.getCache();
    if (cache != null) {
      flushCacheIfRequired(ms);
      if (ms.isUseCache() && resultHandler == null) {
        ensureNoOutParams(ms, boundSql);
          // 从缓存中获取值
        @SuppressWarnings("unchecked")
        List<E> list = (List<E>) tcm.getObject(cache, key);
        if (list == null) {
          list = delegate.query(ms, parameterObject, rowBounds, resultHandler, key, boundSql);
            // 设置进缓存
          tcm.putObject(cache, key, list); // issue #578 and #116
        }
        return list;
      }
    }
    return delegate.query(ms, parameterObject, rowBounds, resultHandler, key, boundSql);
  }
```

TransactionalCache类代码

```java
@Override
  public void clear() {
    clearOnCommit = true;
    entriesToAddOnCommit.clear();
  }

  public void commit() {
    if (clearOnCommit) {
        // 清空缓存
      delegate.clear();
    }
    flushPendingEntries();
    reset();
  }

  public void rollback() {
    unlockMissedEntries();
    reset();
  }

  private void reset() {
    clearOnCommit = false;
    entriesToAddOnCommit.clear();
    entriesMissedInCache.clear();
  }

// 刷新合并缓存键值对
private void flushPendingEntries() {
    for (Map.Entry<Object, Object> entry : entriesToAddOnCommit.entrySet()) {
      delegate.putObject(entry.getKey(), entry.getValue());
    }
    for (Object entry : entriesMissedInCache) {
      if (!entriesToAddOnCommit.containsKey(entry)) {
        delegate.putObject(entry, null);
      }
    }
  }
```



##		总结

Mybatis的缓存都比较鸡肋，不推荐使用。



[参考]: https://mp.weixin.qq.com/s/4Puee_pPCNArkgnFaYlIjg	"MyBatis一级缓存详解"
[参考]: https://www.cnblogs.com/cxuanBlog/p/11333021.html	"MyBatis 二级缓存全详解"





