#		MyBatis源码简要分析

在分析MyBatis源码之前，先想一下MyBatis的启动过程。过程应该如下图：

![自己所想流程图](./assets/Mybatis流程图1.png)

这是是我个人自己分析的流程。我们再来观察一下使用MyBatis的步骤，如下：

1. 配置配置文件，SqlSession.xml和Mapper.xml；
2. 编码

代码如下：

```java
@Test
public void mybatis() throws IOException {
        InputStream stream = Resources.getResourceAsStream("dao/mapper/SqlSessionConfig.xml");
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder( ).build(stream);
        SqlSession sqlSession = sessionFactory.openSession( );
        User user = new User( );
        user.setId(1);
        user.setUsername("zhansan");
        User user1 = sqlSession.selectOne("user.findOne", user);
        System.out.println("user1 = " + user1);
}
```

