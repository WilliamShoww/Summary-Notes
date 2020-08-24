#		Mybatis-plus多数据源配置中遇到的一些问题

实际项目中用到多数据源，之前对`Mybatis`也不是很熟悉，后来看了源码之后，稍微有点认识，至少知道几个关键组件及其作用。使用`Mybatis-plus`在配置多数据源的时候，还是遇到不少问题的。总结一下。

先把配置类代码贴一下：

- 数据源_1的配置

```java
@Configuration
// 数据源1 的dao层的包路径和引用的sqlSessionFactory
@MapperScan(basePackages = "com.**.dao_1",
        sqlSessionFactoryRef = "sqlSessionFactory_1")
public class Data1Configuration {


    @Bean(name = "datasource_1")
    @ConfigurationProperties(prefix = "spring.datasource.d1")
    public DataSource adminDataSource() {
        return DataSourceBuilder.create( ).type(DruidDataSource.class).build( );
    }

    @Bean("sqlSessionFactory_1")
    public SqlSessionFactory mybatisSqlSessionFactoryBean(@Qualifier("datasource_1") DataSource adminDataSource,
                                                          MybatisPlusProperties properties, List<Interceptor> interceptors) throws Exception {
        MybatisSqlSessionFactoryBean sessionFactory = new MybatisSqlSessionFactoryBean( );
        sessionFactory.setDataSource(adminDataSource);
        Interceptor[] intercepts = new Interceptor[interceptors.size( )];
        for (int i = 0; i < interceptors.size( ); i++) {
            intercepts[i] = interceptors.get(i);
        }
        sessionFactory.setPlugins(intercepts);
 // 数据源1的mapper文件位置
        sessionFactory.setMapperLocations(MybatisPluConfiguration.getResources("classpath*:/mapper/d1/*Mapper.xml"));
        sessionFactory.setTypeAliasesPackage(properties.getTypeAliasesPackage( ));
        sessionFactory.setTypeAliasesSuperType(properties.getTypeAliasesSuperType( ));
        sessionFactory.setTypeHandlersPackage(properties.getTypeHandlersPackage( ));
        sessionFactory.setTypeEnumsPackage(properties.getTypeEnumsPackage( ));
        return sessionFactory.getObject( );
    }

    @Bean("transaction_1")
    public DataSourceTransactionManager adminTransactionManager(@Qualifier("dataSource_1") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
```

- 数据源_2的配置

```java
@Configuration
@MapperScan(basePackages = "com.**.dao_2",
        sqlSessionFactoryRef = "sqlSessionFactory_2")
public class AppDataConfiguration {


    @Bean(name = "dataSource_2")
    @ConfigurationProperties(prefix = "spring.datasource.d2")
    public DataSource appDataSource() {
        return DataSourceBuilder.create( ).type(DruidDataSource.class).build( );
    }

    @Bean("sqlSessionFactory_2")
    public SqlSessionFactory mybatisSqlSessionFactoryBean(@Qualifier("appDataSource") DataSource adminDataSource,
                                                          MybatisPlusProperties properties, List<Interceptor> interceptors) throws Exception {
        MybatisSqlSessionFactoryBean sessionFactory = new MybatisSqlSessionFactoryBean( );
        sessionFactory.setDataSource(adminDataSource);
        Interceptor[] intercepts = new Interceptor[interceptors.size( )];
        for (int i = 0; i < interceptors.size( ); i++) {
            intercepts[i] = interceptors.get(i);
        }
        sessionFactory.setPlugins(intercepts);
 // 数据源2的mapper文件位置和数据源1隔离 
        sessionFactory.setMapperLocations(MybatisPluConfiguration.getResources("classpath*:/mapper/d2/*Mapper.xml"));
        sessionFactory.setTypeAliasesPackage(properties.getTypeAliasesPackage( ));
        sessionFactory.setTypeAliasesSuperType(properties.getTypeAliasesSuperType( ));
        sessionFactory.setTypeHandlersPackage(properties.getTypeHandlersPackage( ));
        sessionFactory.setTypeEnumsPackage(properties.getTypeEnumsPackage( ));
        return sessionFactory.getObject( );
    }

    @Bean("transaction_2")
    public DataSourceTransactionManager adminTransactionManager(@Qualifier("dataSource_2") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
```

- Mybatis-plus的配置

```java
@Configuration
public class MybatisPluConfiguration {

    @Bean
    @ConfigurationProperties(Constants.MYBATIS_PLUS)
    public MybatisPlusProperties mybatisPlusProperties() {
        return new MybatisPlusProperties( );
    }

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor( );
    }

    // 解析对应路径的下的mapper文件为Resource列表
    //TODO 代码参考com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties#resolveMapperLocations代码
    public static Resource[] getResources(String location) {
        if (StringUtils.isEmpty(location)) {
            return null;
        }
        try {
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver( );
            return resolver.getResources(location);
        } catch (IOException e) {
            e.printStackTrace( );
        }
        return null;
    }
}
```

- yml配置文件配置

```yaml
mybatis-plus:
  mapper-locations: classpath*:/mapper/**/*Mapper.xml # 此配置项作废
  global-config:
    banner: false
    db-config:
      update-strategy: ignored
  configuration:
    cache-enabled: false
    map-underscore-to-camel-case: true
spring:
  datasource:
    d1:
      url: jdbc:mysql://localhost/d1?characterEncoding=utf8&useSSL=false
      driver-class-name: com.mysql.cj.jdbc.Driver
      username: root
      password: *****
    d2:
      url: jdbc:mysql://localhost/d2?characterEncoding=utf8&useSSL=false
      driver-class-name: com.mysql.cj.jdbc.Driver
      username: root
      password: *****
```

配置中，本人的想法是Mybatis-plus的配置是通用的，包括插件的注入和配置。所以将Mybatis-plus配置独立配置并且共用一个，但是需要注意的是：**mapper-locations这个配置每个数据源独立**

##		部分方法生效

表现就是继承了`ServiceImpl`的类，调用它的非事务方法是没问题的。但是调用批量操作就会失败，数据源切换不正确。后查阅资料参考：

[mybatis plus配置多数据源时批量接口调用失败以及解决方案](https://blog.csdn.net/evasnowind/article/details/104753453/)

原因就是我们没有把`MapperLocation`配置各个数据源隔离起来，用的同一个配置。也是上面说的注意的点。

这里一定要注意：**mapper-locations是一个Resource列表，不然会出现只加载了一个mapper文件配置的情况。**



