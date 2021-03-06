#		Mysql相关的一些命令及查询信息的SQL语句

##		命令

###	启动命令

```shell
systemctl start mysqld.service
```

###		重启命令

```shell
systemctl restart mysqld.service
```

###		连接SQL命令

```shell
mysql -uroot -p7m38uzSrDMNkvS1DbVt1 -h192.168.0.81 -P3306
#	-u 用户名
#	-p 密码
#	-h 连接IP
#	-P 端口号
```



##	SQL语句

```sql
-- 授权语句MySQL8.0开始不需要 尾巴的WITH GRANT OPTION，不然会报语法错误
-- 授予建表权限的时候，需要同时给PROCESS权限，不然使用Navicat工具连接建表的时候会提示：
-- 1227 - Access denied; you need (at least one of) the PROCESS privilege(s) for this operation 但是实际可以建表成功
GRANT SELECT,INSERT,UPDATE,DELETE,CREATE,DROP,ALTER,PROCESS,INDEX,LOCK TABLES ON [db].[table] TO '[username]'@'[host]';
-- 开启事务
BEGIN;
-- 提交事务
COMMIT;
-- 显示引擎状态信息，可以查看最近的死锁信息
SHOW ENGINE INNODB STATUS;
-- 模糊搜索相关配置信息
SHOW VARIABLES LIKE '%{info}%';
-- 显示某个配置变量的信息
SELECT @@{var_name};
-- 显示当前innodb锁状态事务信息
SELECT * FROM information_schema.INNODB_LOCKS;
-- 显示innod等待锁的事务信息
SELECT * FROM information_schema.INNODB_LOCK_WAITS;
-- 查看innodb当前事务的情况
SELECT * FROM information_schema.INNODB_TRX;
-- 杀掉上面查询的事务的线程id
kill {thrad_id};
-- 显示正在运行的线程情况，前一百条
SHOW PROCESSLIST;
-- 显示所有正在运行的线程情况
SHOW FULL PROCESSLIST;
-- 查询表信息
SHOW OPEN TABLES;
-- 查询某个数据库正的表信息
SHOW OPEN TABLES FROM {database};
-- In_use列表示有多少线程正在使用某张表
SHOW OPEN TABLES where In_use > 0;
-- 查看语句优化器优化信息
-- 先执行
EXPLAIN EXTENDED {sql}
-- 后执行
SHOW WARNINGS
-- 同一个实例中跨库迁移整表数据
RENAME TABLE db1.table1 TO db2.table2
```

##		死锁排查

```mysql
# 得到死锁信息
SHOW ENGINE INNODB STATUS;
# 查询死锁的线程
SHOW PROCESSLIST;
# 目前死锁的线程号
SELECT * FROM information_schema.INNODB_TRX;
# 杀掉死锁线程
KILL {thread_id};
```

