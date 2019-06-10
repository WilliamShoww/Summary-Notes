#	关于Mysql在Spring boot中的配置问题

最近在帮朋友做一个微信小程序，从零开始搭建，发现做的很慢。主要是对一些配置不熟悉。在搭建过程中遇到过一些小问题，虽然百度都能找到问题，但是还是记录一下。

##	Mysql配置项目容易搞错

一开始写成如下配置：

```properties
# 数据库配置
spring.datasource.url=jdbc:mysql://localhost:3306/lemonfit
spring.datasource.data-username=root
spring.datasource.data-password=123456
```

我记得以前配置错误，Spring boot会启动时报异常，可能是新版本没有。所以在查询的时候报以下异常：

```java
java.sql.SQLException: Access denied for user ''@'localhost' (using password: NO)
	at com.mysql.cj.jdbc.exceptions.SQLError.createSQLException(SQLError.java:129) ~[mysql-connector-java-8.0.16.jar:8.0.16]
	at com.mysql.cj.jdbc.exceptions.SQLError.createSQLException(SQLError.java:97) ~[mysql-connector-java-8.0.16.jar:8.0.16]
	at com.mysql.cj.jdbc.exceptions.SQLExceptionsMapping.translateException(SQLExceptionsMapping.java:122) ~[mysql-connector-java-8.0.16.jar:8.0.16]
	at com.mysql.cj.jdbc.ConnectionImpl.createNewIO(ConnectionImpl.java:835) ~[mysql-connector-java-8.0.16.jar:8.0.16]
	at com.mysql.cj.jdbc.ConnectionImpl.<init>(ConnectionImpl.java:455) ~[mysql-connector-java-8.0.16.jar:8.0.16]
	at com.mysql.cj.jdbc.ConnectionImpl.getInstance(ConnectionImpl.java:240) ~[mysql-connector-java-8.0.16.jar:8.0.16]
	at com.mysql.cj.jdbc.NonRegisteringDriver.connect(NonRegisteringDriver.java:199) ~[mysql-connector-java-8.0.16.jar:8.0.16]
	at com.zaxxer.hikari.util.DriverDataSource.getConnection(DriverDataSource.java:119) ~[HikariCP-3.2.0.jar:na]
	at com.zaxxer.hikari.pool.PoolBase.newConnection(PoolBase.java:369) ~[HikariCP-3.2.0.jar:na]
	at com.zaxxer.hikari.pool.PoolBase.newPoolEntry(PoolBase.java:198) ~[HikariCP-3.2.0.jar:na]
	at com.zaxxer.hikari.pool.HikariPool.createPoolEntry(HikariPool.java:467) [HikariCP-3.2.0.jar:na]
	at com.zaxxer.hikari.pool.HikariPool.checkFailFast(HikariPool.java:541) [HikariCP-3.2.0.jar:na]
	at com.zaxxer.hikari.pool.HikariPool.<init>(HikariPool.java:115) [HikariCP-3.2.0.jar:na]
	at com.zaxxer.hikari.HikariDataSource.getConnection(HikariDataSource.java:112) [HikariCP-3.2.0.jar:na]
	at org.springframework.jdbc.datasource.DataSourceUtils.fetchConnection(DataSourceUtils.java:157) [spring-jdbc-5.1.7.RELEASE.jar:5.1.7.RELEASE]
	at org.springframework.jdbc.datasource.DataSourceUtils.doGetConnection(DataSourceUtils.java:115) [spring-jdbc-5.1.7.RELEASE.jar:5.1.7.RELEASE]
	at org.springframework.jdbc.datasource.DataSourceUtils.getConnection(DataSourceUtils.java:78) [spring-jdbc-5.1.7.RELEASE.jar:5.1.7.RELEASE]
	at org.mybatis.spring.transaction.SpringManagedTransaction.openConnection(SpringManagedTransaction.java:82) [mybatis-spring-2.0.1.jar:2.0.1]
	at org.mybatis.spring.transaction.SpringManagedTransaction.getConnection(SpringManagedTransaction.java:68) [mybatis-spring-2.0.1.jar:2.0.1]
	at org.apache.ibatis.executor.BaseExecutor.getConnection(BaseExecutor.java:336) [mybatis-3.5.1.jar:3.5.1]
	at org.apache.ibatis.executor.SimpleExecutor.prepareStatement(SimpleExecutor.java:85) [mybatis-3.5.1.jar:3.5.1]
	at org.apache.ibatis.executor.SimpleExecutor.doQuery(SimpleExecutor.java:62) [mybatis-3.5.1.jar:3.5.1]
	at org.apache.ibatis.executor.BaseExecutor.queryFromDatabase(BaseExecutor.java:324) [mybatis-3.5.1.jar:3.5.1]
	at org.apache.ibatis.executor.BaseExecutor.query(BaseExecutor.java:156) [mybatis-3.5.1.jar:3.5.1]
	at org.apache.ibatis.executor.CachingExecutor.query(CachingExecutor.java:109) [mybatis-3.5.1.jar:3.5.1]
	at org.apache.ibatis.executor.CachingExecutor.query(CachingExecutor.java:83) [mybatis-3.5.1.jar:3.5.1]
	at org.apache.ibatis.session.defaults.DefaultSqlSession.selectList(DefaultSqlSession.java:147) [mybatis-3.5.1.jar:3.5.1]
	at org.apache.ibatis.session.defaults.DefaultSqlSession.selectList(DefaultSqlSession.java:140) [mybatis-3.5.1.jar:3.5.1]
	at org.apache.ibatis.session.defaults.DefaultSqlSession.selectOne(DefaultSqlSession.java:76) [mybatis-3.5.1.jar:3.5.1]
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[na:1.8.0_131]
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62) ~[na:1.8.0_131]
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[na:1.8.0_131]
	at java.lang.reflect.Method.invoke(Method.java:498) ~[na:1.8.0_131]
	at org.mybatis.spring.SqlSessionTemplate$SqlSessionInterceptor.invoke(SqlSessionTemplate.java:433) [mybatis-spring-2.0.1.jar:2.0.1]
	at com.sun.proxy.$Proxy61.selectOne(Unknown Source) [na:na]
	at org.mybatis.spring.SqlSessionTemplate.selectOne(SqlSessionTemplate.java:166) [mybatis-spring-2.0.1.jar:2.0.1]
	at org.apache.ibatis.binding.MapperMethod.execute(MapperMethod.java:87) [mybatis-3.5.1.jar:3.5.1]
	at org.apache.ibatis.binding.MapperProxy.invoke(MapperProxy.java:58) [mybatis-3.5.1.jar:3.5.1]
	at com.sun.proxy.$Proxy64.findAdminByAccountAndPassword(Unknown Source) [na:na]
	at com.lemonfit.manager.service.AdminServiceImpl.login(AdminServiceImpl.java:27) [classes/:na]
	at com.lemonfit.manager.controller.AdminController.login(AdminController.java:28) [classes/:na]
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[na:1.8.0_131]
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62) ~[na:1.8.0_131]
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[na:1.8.0_131]
	at java.lang.reflect.Method.invoke(Method.java:498) ~[na:1.8.0_131]
	at org.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:190) [spring-web-5.1.7.RELEASE.jar:5.1.7.RELEASE]
	at org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:138) [spring-web-5.1.7.RELEASE.jar:5.1.7.RELEASE]
	at org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:104) [spring-webmvc-5.1.7.RELEASE.jar:5.1.7.RELEASE]
	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:892) [spring-webmvc-5.1.7.RELEASE.jar:5.1.7.RELEASE]
	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:797) [spring-webmvc-5.1.7.RELEASE.jar:5.1.7.RELEASE]
	at org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:87) [spring-webmvc-5.1.7.RELEASE.jar:5.1.7.RELEASE]
	at org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:1039) [spring-webmvc-5.1.7.RELEASE.jar:5.1.7.RELEASE]
	at org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:942) [spring-webmvc-5.1.7.RELEASE.jar:5.1.7.RELEASE]
	at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1005) [spring-webmvc-5.1.7.RELEASE.jar:5.1.7.RELEASE]
	at org.springframework.web.servlet.FrameworkServlet.doPost(FrameworkServlet.java:908) [spring-webmvc-5.1.7.RELEASE.jar:5.1.7.RELEASE]
	at javax.servlet.http.HttpServlet.service(HttpServlet.java:660) [tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:882) [spring-webmvc-5.1.7.RELEASE.jar:5.1.7.RELEASE]
	at javax.servlet.http.HttpServlet.service(HttpServlet.java:741) [tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:231) [tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166) [tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:53) [tomcat-embed-websocket-9.0.19.jar:9.0.19]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193) [tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166) [tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:99) [spring-web-5.1.7.RELEASE.jar:5.1.7.RELEASE]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107) [spring-web-5.1.7.RELEASE.jar:5.1.7.RELEASE]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193) [tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166) [tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.springframework.web.filter.FormContentFilter.doFilterInternal(FormContentFilter.java:92) [spring-web-5.1.7.RELEASE.jar:5.1.7.RELEASE]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107) [spring-web-5.1.7.RELEASE.jar:5.1.7.RELEASE]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193) [tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166) [tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.springframework.web.filter.HiddenHttpMethodFilter.doFilterInternal(HiddenHttpMethodFilter.java:93) [spring-web-5.1.7.RELEASE.jar:5.1.7.RELEASE]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107) [spring-web-5.1.7.RELEASE.jar:5.1.7.RELEASE]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193) [tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166) [tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:200) [spring-web-5.1.7.RELEASE.jar:5.1.7.RELEASE]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107) [spring-web-5.1.7.RELEASE.jar:5.1.7.RELEASE]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193) [tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166) [tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:200) [tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:96) [tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:490) [tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:139) [tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:92) [tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:74) [tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:343) [tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:408) [tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:66) [tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:836) [tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1747) [tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:49) [tomcat-embed-core-9.0.19.jar:9.0.19]
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142) [na:1.8.0_131]
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617) [na:1.8.0_131]
	at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61) [tomcat-embed-core-9.0.19.jar:9.0.19]
	at java.lang.Thread.run(Thread.java:748) [na:1.8.0_131]
```

当时很郁闷，为什么？其他工具都没问题啊，怎么会被拒绝呢，想了以下就百度了一下，发现是配置项错了，正确应该如下：

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/lemonfit
spring.datasource.username=root
spring.datasource.password=123456
```

到这里能连接上了，但是查询的时候又报以下异常：

```java
java.sql.SQLException: The server time zone value '�й���׼ʱ��' is unrecognized or represents more than one time zone. You must configure either the server or JDBC driver (via the serverTimezone configuration property) to use a more specifc time zone value if you want to utilize time zone support.
	at com.mysql.cj.jdbc.exceptions.SQLError.createSQLException(SQLError.java:129) ~[mysql-connector-java-8.0.16.jar:8.0.16]
	at com.mysql.cj.jdbc.exceptions.SQLError.createSQLException(SQLError.java:97) ~[mysql-connector-java-8.0.16.jar:8.0.16]
	at com.mysql.cj.jdbc.exceptions.SQLError.createSQLException(SQLError.java:89) ~[mysql-connector-java-8.0.16.jar:8.0.16]
	at com.mysql.cj.jdbc.exceptions.SQLError.createSQLException(SQLError.java:63) ~[mysql-connector-java-8.0.16.jar:8.0.16]
	at com.mysql.cj.jdbc.exceptions.SQLError.createSQLException(SQLError.java:73) ~[mysql-connector-java-8.0.16.jar:8.0.16]
	at com.mysql.cj.jdbc.exceptions.SQLExceptionsMapping.translateException(SQLExceptionsMapping.java:76) ~[mysql-connector-java-8.0.16.jar:8.0.16]
	at com.mysql.cj.jdbc.ConnectionImpl.createNewIO(ConnectionImpl.java:835) ~[mysql-connector-java-8.0.16.jar:8.0.16]
	at com.mysql.cj.jdbc.ConnectionImpl.<init>(ConnectionImpl.java:455) ~[mysql-connector-java-8.0.16.jar:8.0.16]
	at com.mysql.cj.jdbc.ConnectionImpl.getInstance(ConnectionImpl.java:240) ~[mysql-connector-java-8.0.16.jar:8.0.16]
	at com.mysql.cj.jdbc.NonRegisteringDriver.connect(NonRegisteringDriver.java:199) ~[mysql-connector-java-8.0.16.jar:8.0.16]
	at com.zaxxer.hikari.util.DriverDataSource.getConnection(DriverDataSource.java:136) ~[HikariCP-3.2.0.jar:na]
	at com.zaxxer.hikari.pool.PoolBase.newConnection(PoolBase.java:369) ~[HikariCP-3.2.0.jar:na]
	at com.zaxxer.hikari.pool.PoolBase.newPoolEntry(PoolBase.java:198) ~[HikariCP-3.2.0.jar:na]
	at com.zaxxer.hikari.pool.HikariPool.createPoolEntry(HikariPool.java:467) [HikariCP-3.2.0.jar:na]
	at com.zaxxer.hikari.pool.HikariPool.checkFailFast(HikariPool.java:541) [HikariCP-3.2.0.jar:na]
	at com.zaxxer.hikari.pool.HikariPool.<init>(HikariPool.java:115) [HikariCP-3.2.0.jar:na]
	at com.zaxxer.hikari.HikariDataSource.getConnection(HikariDataSource.java:112) [HikariCP-3.2.0.jar:na]
	at org.springframework.jdbc.datasource.DataSourceUtils.fetchConnection(DataSourceUtils.java:157) [spring-jdbc-5.1.7.RELEASE.jar:5.1.7.RELEASE]
	at org.springframework.jdbc.datasource.DataSourceUtils.doGetConnection(DataSourceUtils.java:115) [spring-jdbc-5.1.7.RELEASE.jar:5.1.7.RELEASE]
	at org.springframework.jdbc.datasource.DataSourceUtils.getConnection(DataSourceUtils.java:78) [spring-jdbc-5.1.7.RELEASE.jar:5.1.7.RELEASE]
	at org.mybatis.spring.transaction.SpringManagedTransaction.openConnection(SpringManagedTransaction.java:82) [mybatis-spring-2.0.1.jar:2.0.1]
	at org.mybatis.spring.transaction.SpringManagedTransaction.getConnection(SpringManagedTransaction.java:68) [mybatis-spring-2.0.1.jar:2.0.1]
	at org.apache.ibatis.executor.BaseExecutor.getConnection(BaseExecutor.java:336) [mybatis-3.5.1.jar:3.5.1]
	at org.apache.ibatis.executor.SimpleExecutor.prepareStatement(SimpleExecutor.java:85) [mybatis-3.5.1.jar:3.5.1]
	at org.apache.ibatis.executor.SimpleExecutor.doQuery(SimpleExecutor.java:62) [mybatis-3.5.1.jar:3.5.1]
	at org.apache.ibatis.executor.BaseExecutor.queryFromDatabase(BaseExecutor.java:324) [mybatis-3.5.1.jar:3.5.1]
	at org.apache.ibatis.executor.BaseExecutor.query(BaseExecutor.java:156) [mybatis-3.5.1.jar:3.5.1]
	at org.apache.ibatis.executor.CachingExecutor.query(CachingExecutor.java:109) [mybatis-3.5.1.jar:3.5.1]
	at org.apache.ibatis.executor.CachingExecutor.query(CachingExecutor.java:83) [mybatis-3.5.1.jar:3.5.1]
	at org.apache.ibatis.session.defaults.DefaultSqlSession.selectList(DefaultSqlSession.java:147) [mybatis-3.5.1.jar:3.5.1]
	at org.apache.ibatis.session.defaults.DefaultSqlSession.selectList(DefaultSqlSession.java:140) [mybatis-3.5.1.jar:3.5.1]
	at org.apache.ibatis.session.defaults.DefaultSqlSession.selectOne(DefaultSqlSession.java:76) [mybatis-3.5.1.jar:3.5.1]
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[na:1.8.0_131]
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62) ~[na:1.8.0_131]
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[na:1.8.0_131]
	at java.lang.reflect.Method.invoke(Method.java:498) ~[na:1.8.0_131]
	at org.mybatis.spring.SqlSessionTemplate$SqlSessionInterceptor.invoke(SqlSessionTemplate.java:433) [mybatis-spring-2.0.1.jar:2.0.1]
	at com.sun.proxy.$Proxy61.selectOne(Unknown Source) [na:na]
	at org.mybatis.spring.SqlSessionTemplate.selectOne(SqlSessionTemplate.java:166) [mybatis-spring-2.0.1.jar:2.0.1]
	at org.apache.ibatis.binding.MapperMethod.execute(MapperMethod.java:87) [mybatis-3.5.1.jar:3.5.1]
	at org.apache.ibatis.binding.MapperProxy.invoke(MapperProxy.java:58) [mybatis-3.5.1.jar:3.5.1]
	at com.sun.proxy.$Proxy64.findAdminByAccountAndPassword(Unknown Source) [na:na]
	at com.lemonfit.manager.service.AdminServiceImpl.login(AdminServiceImpl.java:27) [classes/:na]
	at com.lemonfit.manager.controller.AdminController.login(AdminController.java:28) [classes/:na]
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[na:1.8.0_131]
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62) ~[na:1.8.0_131]
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[na:1.8.0_131]
	at java.lang.reflect.Method.invoke(Method.java:498) ~[na:1.8.0_131]
	at org.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:190) [spring-web-5.1.7.RELEASE.jar:5.1.7.RELEASE]
	at org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:138) [spring-web-5.1.7.RELEASE.jar:5.1.7.RELEASE]
	at org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:104) [spring-webmvc-5.1.7.RELEASE.jar:5.1.7.RELEASE]
	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:892) [spring-webmvc-5.1.7.RELEASE.jar:5.1.7.RELEASE]
	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:797) [spring-webmvc-5.1.7.RELEASE.jar:5.1.7.RELEASE]
	at org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:87) [spring-webmvc-5.1.7.RELEASE.jar:5.1.7.RELEASE]
	at org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:1039) [spring-webmvc-5.1.7.RELEASE.jar:5.1.7.RELEASE]
	at org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:942) [spring-webmvc-5.1.7.RELEASE.jar:5.1.7.RELEASE]
	at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1005) [spring-webmvc-5.1.7.RELEASE.jar:5.1.7.RELEASE]
	at org.springframework.web.servlet.FrameworkServlet.doPost(FrameworkServlet.java:908) [spring-webmvc-5.1.7.RELEASE.jar:5.1.7.RELEASE]
	at javax.servlet.http.HttpServlet.service(HttpServlet.java:660) [tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:882) [spring-webmvc-5.1.7.RELEASE.jar:5.1.7.RELEASE]
	at javax.servlet.http.HttpServlet.service(HttpServlet.java:741) [tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:231) [tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166) [tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:53) [tomcat-embed-websocket-9.0.19.jar:9.0.19]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193) [tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166) [tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:99) [spring-web-5.1.7.RELEASE.jar:5.1.7.RELEASE]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107) [spring-web-5.1.7.RELEASE.jar:5.1.7.RELEASE]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193) [tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166) [tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.springframework.web.filter.FormContentFilter.doFilterInternal(FormContentFilter.java:92) [spring-web-5.1.7.RELEASE.jar:5.1.7.RELEASE]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107) [spring-web-5.1.7.RELEASE.jar:5.1.7.RELEASE]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193) [tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166) [tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.springframework.web.filter.HiddenHttpMethodFilter.doFilterInternal(HiddenHttpMethodFilter.java:93) [spring-web-5.1.7.RELEASE.jar:5.1.7.RELEASE]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107) [spring-web-5.1.7.RELEASE.jar:5.1.7.RELEASE]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193) [tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166) [tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:200) [spring-web-5.1.7.RELEASE.jar:5.1.7.RELEASE]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107) [spring-web-5.1.7.RELEASE.jar:5.1.7.RELEASE]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193) [tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166) [tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:200) [tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:96) [tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:490) [tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:139) [tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:92) [tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:74) [tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:343) [tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:408) [tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:66) [tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:836) [tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1747) [tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:49) [tomcat-embed-core-9.0.19.jar:9.0.19]
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142) [na:1.8.0_131]
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617) [na:1.8.0_131]
	at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61) [tomcat-embed-core-9.0.19.jar:9.0.19]
	at java.lang.Thread.run(Thread.java:748) [na:1.8.0_131]
Caused by: com.mysql.cj.exceptions.InvalidConnectionAttributeException: The server time zone value '�й���׼ʱ��' is unrecognized or represents more than one time zone. You must configure either the server or JDBC driver (via the serverTimezone configuration property) to use a more specifc time zone value if you want to utilize time zone support.
	at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method) ~[na:1.8.0_131]
	at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:62) ~[na:1.8.0_131]
	at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45) ~[na:1.8.0_131]
	at java.lang.reflect.Constructor.newInstance(Constructor.java:423) ~[na:1.8.0_131]
	at com.mysql.cj.exceptions.ExceptionFactory.createException(ExceptionFactory.java:61) ~[mysql-connector-java-8.0.16.jar:8.0.16]
	at com.mysql.cj.exceptions.ExceptionFactory.createException(ExceptionFactory.java:85) ~[mysql-connector-java-8.0.16.jar:8.0.16]
	at com.mysql.cj.util.TimeUtil.getCanonicalTimezone(TimeUtil.java:132) ~[mysql-connector-java-8.0.16.jar:8.0.16]
	at com.mysql.cj.protocol.a.NativeProtocol.configureTimezone(NativeProtocol.java:2243) ~[mysql-connector-java-8.0.16.jar:8.0.16]
	at com.mysql.cj.protocol.a.NativeProtocol.initServerSession(NativeProtocol.java:2267) ~[mysql-connector-java-8.0.16.jar:8.0.16]
	at com.mysql.cj.jdbc.ConnectionImpl.initializePropsFromServer(ConnectionImpl.java:1319) ~[mysql-connector-java-8.0.16.jar:8.0.16]
	at com.mysql.cj.jdbc.ConnectionImpl.connectOneTryOnly(ConnectionImpl.java:966) ~[mysql-connector-java-8.0.16.jar:8.0.16]
	at com.mysql.cj.jdbc.ConnectionImpl.createNewIO(ConnectionImpl.java:825) ~[mysql-connector-java-8.0.16.jar:8.0.16]
	... 91 common frames omitted
```

大致看一眼就是时区不对，百度了一下，原来新版mysql需要指定时区，所以最终的配置应该如下：

```properties
# 数据库配置
spring.datasource.url=jdbc:mysql://localhost:3306/lemonfit?serverTimezone=Hongkong&useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true
spring.datasource.username=root
spring.datasource.password=123456
```

参数名称说明：

| 参数名                | 说明                                                         | 默认值 |
| --------------------- | ------------------------------------------------------------ | ------ |
| user                  | 数据库用户名（用于连接数据库）                               |        |
| password              | 用户密码（用于连接数据库）                                   |        |
| useUnicode            | 是否使用Unicode字符集，如果参数`characterEncoding`设置为`gb2312`或`gbk`，本参数值必须设置为true | false  |
| characterEncoding     | 当`useUnicode`设置为`true`时，指定字符编码。比如可设置为`gb2312`或`gbk `，用的`utf-8`编码 | false  |
| autoReconnect         | 当数据库连接异常中断时，是否自动重新连接                     | false  |
| autoReconnectForPools | 是否使用针对数据库连接池的重连策略                           | false  |
| failOverReadOnly      | 自动重连成功后，连接是否设置为只读                           | true   |
| maxReconnects         | autoReconnect设置为true时，重试连接的次数                    | 3      |
| initialTimeout        | autoReconnect设置为true时，两次重连之间的时间间隔，单位：秒  | 2      |
| connectTimeout        | 和数据库服务器建立socket连接时的超时，单位：毫秒。 0表示永不超时 | 0      |
| socketTimeout         | socket操作（读写）超时，单位：毫秒。 0表示永不超时           | 0      |
| serverTimezone        | 所在时区，国内`Hongkong/Shanghai`不然会相差八个小时。**新版MYSQL必须设置** |        |
| allowMultiQueries     | 是否批量处理，**不设置批量执行语句会报错**                   | false  |

