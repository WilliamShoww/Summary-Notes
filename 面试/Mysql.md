##	Mysql相关的面试题

1. **存储引擎有哪些及区别？**

2. **有一个超大表，怎么优化？**

3. **怎么垂直分表和水平分表？**

4. **索引失效的场景**

   a.多列索引不符合最左原则

   b.使用函数或计算

   c.范围条件之后

   d.使用<>!= 不等于

   e.is null 或者 not null 条件

   f.前缀通配符 '%abc'

   g.字符串类型不加单引号

   参考：[MYSQL调优之索引——索引失效情况](https://www.jianshu.com/p/9c9a0057221f)

5. **行转列的SQL**

   比如给了这么一个表：
   
   | class | name | score |
   | ----- | ---- | ----- |
   | 班级1 | 张三 | 59    |
   | 班级2 | 李四 | 69    |
   | 班级3 | 小吴 | 99    |
   
   ```sql
   select class 班级,
   	sum(case when score>=60 then 1 else 0 end) as 及格人数,
   	sum(case when score<60 then 1 else 0 end) as 不及格人数
   from tb1
       group by class
   ```
   
6. **表结构设计的总结/原则**

   

7. **字段为NULL会产生什么问题？**

   a.代码中可能会引发一些错误异常

   b.null值到非null值更新无法做到原地更新，容易发生索引分裂而影响性能

   c.NOT IN、!= 等负向条件查询在有 NULL 值的情况下返回永远为空结果，查询容易出错，不会返回为null的列

   d.count 查询统计的数量出错，为null的列不会统计进来