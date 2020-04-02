#		Spring源码分析之启动流程

本文进行解析的`ClassPathXmlApplicationContext`的启动流程。由于`Spring`源码比较复杂，本文只讲解它的启动流程做了哪些东西，后续文章再解析各个重要步骤的源码解析。

##  1. 构造方法解析

要分析源码，肯定是根据启动入口入手。我们初始化一个Spring的方式之一就是`new ClassPathXmlApplicationContext("配置文件") `，所以第一步就去解析构造方法。我们点进去，到最底下的构造方法源码如下：

```java
public ClassPathXmlApplicationContext(
			String[] configLocations, boolean refresh, @Nullable ApplicationContext parent)
			throws BeansException {

		super(parent);
		// 设置配置文件信息
		setConfigLocations(configLocations);
		if (refresh) {
			refresh();
		}
	}
```

从源码看到，第一步就是设置我们传递进来的配置文件位置信息，然后根据参数`refresh`执行`refresh`方法。

##		2. refresh方法

该方法是整个`Spring`启动流程中最重要的一个方法，也是整个启动流程，源码如下：

```java
	@Override
	public void refresh() throws BeansException, IllegalStateException {
		synchronized (this.startupShutdownMonitor) {
			// Prepare this context for refreshing.
			/**
			 * 1. 准备刷新
			 * 1.1 切换Spring状态
			 * 1.2 校验环境变量
			 * 1.3 初始化监听器容器
 			 */
			prepareRefresh();

			/**
			 * 2. 创建BeanFactory并加装Bean的配置信息
			 * 2.1 销毁Spring容器之前的BeanFactory和Beans
			 * 2.2 实例化DefaultListableBeanFactory
			 * 2.3 设置BeanFactory的唯一ID
			 * 2.4 设置BeanFactory的是否支持BeanDefinition覆盖和是否支持循环引用两个配置
			 * 2.5 加载Bean的配置文件并且将配置信息封装成BeanDefinition注册到BeanDefinitionRegistry的beanDefinitionMap中，
			 * 并且注册Bean的别名
			 * 2.6 返回实例化的DefaultListableBeanFactory
			 */
			// Tell the subclass to refresh the internal bean factory.
			ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();

			/**
			 * 3. BeanFactory预处理, 对BeanFactory进行一系列设置
			 */
			// Prepare the bean factory for use in this context.
			prepareBeanFactory(beanFactory);

			try {
				/**
				 * 4. 给子类实现，可以扩展BeanFactory
				 */
				// Allows post-processing of the bean factory in context subclasses.
				postProcessBeanFactory(beanFactory);

				/**
				 * 5. 实例化BeanFactoryPostProcessor并且执行
				 * 5.1 创建实现了BeanFactoryPostProcessor的类
				 * 5.2 执行BeanFactoryPostProcessor的postProcessBeanFactory方法
				 */
				// Invoke factory processors registered as beans in the context.
				invokeBeanFactoryPostProcessors(beanFactory);

				/**
				 * 6. 创建BeanPostProcessor
				 * 6.1 创建实现BeanPostProcessor的类（创建的时候加入的是单例池）
				 * 6.2 将创建好的BeanPostProcessor注册进BeanFactory的beanPostProcessors中
				 */
				// Register bean processors that intercept bean creation.
				registerBeanPostProcessors(beanFactory);

				/**
				 * 7. 初始化消息资源，比如国际化等
				 */
				// Initialize message source for this context.
				initMessageSource();

				/**
				 * 8. 创建Spring的事件转发器
				 * 8.1 创建事件转发器
				 * 8.1 将事件转发器注册到BeanFactory的单例池中
				 */
				// Initialize event multicaster for this context.
				initApplicationEventMulticaster();

				/**
				 * 9. 给子类实现，在实例化单例bean之前实例化其他的一些特殊Bean
				 */
				// Initialize other special beans in specific context subclasses.
				onRefresh();

				/**
				 * 10. 初始化事件监听器
				 * 10.1 创建ApplicationListener事件监听器
				 * 10.2 将创建好的事件监听器注册进事件转发器
				 * 10.3 发布Spring的早期事件
				 */
				// Check for listener beans and register them.
				registerListeners();

				/**
				 * 11. 创建剩下的非懒加载的单例Bean
				 * 11.1 实例化剩下的非懒加载的单例Bean
				 * 11.2 为实例化的Bean充填属性
				 * 11.3 执行Bean的生命周期方法
				 * 11.4 注册Bean进单例池中
				 */
				// Instantiate all remaining (non-lazy-init) singletons.
				finishBeanFactoryInitialization(beanFactory);

				/**
				 * 12. 完成容器的刷新
				 * 12.1 清理缓存的资源
				 * 12.2 创建LifecycleProcessor的Bean并且注册进BeanFactory的单例池
				 * 12.3 执行LifecycleProcessor的onRefresh方法
				 * 12.4 发布ContextRefreshedEvent上下文刷新事件
				 * 12.5 将完成的Spring上下文注册进applicationContexts(Spring支持多容器的)
				 */
				// Last step: publish corresponding event.
				finishRefresh();
			}

			catch (BeansException ex) {
				if (logger.isWarnEnabled()) {
					logger.warn("Exception encountered during context initialization - " +
							"cancelling refresh attempt: " + ex);
				}

				// Destroy already created singletons to avoid dangling resources.
				destroyBeans();

				// Reset 'active' flag.
				cancelRefresh(ex);

				// Propagate exception to caller.
				throw ex;
			}

			finally {
				// Reset common introspection caches in Spring's core, since we
				// might not ever need metadata for singleton beans anymore...
				resetCommonCaches();
			}
		}
	}
```

其中 比较重的几个方法，其中是：第二步`obtainFreshBeanFactory`、 第五步`invokeBeanFactoryPostProcessors`、第六步`registerBeanPostProcessors`和第十一步`finishBeanFactoryInitialization`由于篇幅有限，后续逐一解析。