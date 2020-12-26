

##		mongodb复制集环境搭建

###		创建文件及文件目录如下表  

| 编号 | 服务信息         | 端口号 | dbpath                    | logpath                            | 配置文件目录                                                 |
| ---- | ---------------- | ------ | ------------------------- | ---------------------------------- | ------------------------------------------------------------ |
| 1    | 配置服务1        | 17011  | /data/config/config_17011 | /data/config/logs/config_17011.log | /mongo_cluster/mongodb-linux-x86_64-4.1.3/cluster_config/config/config-17011.conf |
| 2    | 配置服务2        | 17012  | /data/config/config_17012 | /data/config/logs/config_17012.log | /mongo_cluster/mongodb-linux-x86_64-4.1.3/cluster_config/config/config-17012.conf |
| 3    | 配置服务3        | 17013  | /data/config/config_17012 | /data/config/logs/config_17013.log | /mongo_cluster/mongodb-linux-x86_64-4.1.3/cluster_config/config/config-17013.conf |
| 4    | 1号分片1号服务器 | 37011  | /data/shard1/node_37011   | /data/shard1/logs/node_37011.log   | /mongo_cluster/mongodb-linux-x86_64-4.1.3/cluster_config/shard1/node-37011.conf |
| 5    | 1号分片2号服务器 | 37013  | /data/shard1/node_37013   | /data/shard1/logs/node_37013.log   | /mongo_cluster/mongodb-linux-x86_64-4.1.3/cluster_config/shard1/node-37013.conf |
| 6    | 1号分片3号服务器 | 37015  | /data/shard1/node_37015   | /data/shard1/logs/node_37015.log   | /mongo_cluster/mongodb-linux-x86_64-4.1.3/cluster_config/shard1/node-37015.conf |
| 7    | 1号分片4号服务器 | 37017  | /data/shard2/node_37017   | /data/shard1/logs/node_37017.log   | /mongo_cluster/mongodb-linux-x86_64-4.1.3/cluster_config/shard1/node-37017.conf |
| 8    | 2号分片1号服务器 | 47011  | /data/shard2/node_47017   | /data/shard2/logs/node_47011.log   | /mongo_cluster/mongodb-linux-x86_64-4.1.3/cluster_config/shard2/node-47011.conf |
| 9    | 2号分片2号服务器 | 47013  | /data/shard2/node_47013   | /data/shard2/logs/node_47013.log   | /mongo_cluster/mongodb-linux-x86_64-4.1.3/cluster_config/shard2/node-47013.conf |
| 10   | 2号分片3号服务器 | 47015  | /data/shard2/node_47015   | /data/shard2/logs/node_47015.log   | /mongo_cluster/mongodb-linux-x86_64-4.1.3/cluster_config/shard2/node-47015.conf |
| 11   | 2号分片4号服务器 | 47017  | /data/shard2/node_47017   | /data/shard2/logs/node_47017.log   | /mongo_cluster/mongodb-linux-x86_64-4.1.3/cluster_config/shard2/node-47017.conf |
| 12   | 路由节点服务器   | 27017  |                           | /data/route/logs/route.log         | /mongo_cluster/mongodb-linux-x86_64-4.1.3/cluster_config/route/route-27017.conf |

###		在各个配置中如下

####		配置集配置如下：

```shell
#1号配置服务器配置
dbpath=/data/config/config_17011
logpath=/data/config/logs/config_17011.log
logappend=true
fork=true
bind_ip=0.0.0.0
port=17011
configsvr=true
replSet=configsvr
#2号配置服务器配置
dbpath=/data/config/config_17012
logpath=/data/config/logs/config_17012.log
logappend=true
fork=true
bind_ip=0.0.0.0
port=17012
configsvr=true
replSet=configsvr
#3号配置服务器配置
dbpath=/data/config/config_17013
logpath=/data/config/logs/config_17013.log
logappend=true
fork=true
bind_ip=0.0.0.0
port=17013
configsvr=true
replSet=configsvr
```

####		分片1配置如下：

```shell
#1号服务器配置
dbpath=/data/shard1/node_37011
logpath=/data/shard1/logs/node_37011.log
logappend=true
fork=true
bind_ip=0.0.0.0
port=37011
shardsvr=true
replSet=shard1
#2号服务器配置
dbpath=/data/shard1/node_37013
logpath=/data/shard1/logs/node_37013.log
logappend=true
fork=true
bind_ip=0.0.0.0
port=37013
shardsvr=true
replSet=shard1
#2号服务器配置
dbpath=/data/shard1/node_37015
logpath=/data/shard1/logs/node_37015.log
logappend=true
fork=true
bind_ip=0.0.0.0
port=37015
shardsvr=true
replSet=shard1
#4号服务器配置
dbpath=/data/shard1/node_37017
logpath=/data/shard1/logs/node_37017.log
logappend=true
fork=true
bind_ip=0.0.0.0
port=37017
shardsvr=true
replSet=shard1
```

####		分片2配置如下：

```shell
#1号服务器配置
dbpath=/data/shard2/node_47011
logpath=/data/shard2/logs/node_47011.log
logappend=true
fork=true
bind_ip=0.0.0.0
port=47011
shardsvr=true
replSet=shard2
#2号服务器配置
dbpath=/data/shard2/node_47013
logpath=/data/shard2/logs/node_47013.log
logappend=true
fork=true
bind_ip=0.0.0.0
port=47013
shardsvr=true
replSet=shard2
#3号服务器配置
dbpath=/data/shard2/node_47015
logpath=/data/shard2/logs/node_47015.log
logappend=true
fork=true
bind_ip=0.0.0.0
port=47015
shardsvr=true
replSet=shard2
#4号服务器配置
dbpath=/data/shard2/node_47017
logpath=/data/shard2/logs/node_47017.log
logappend=true
fork=true
bind_ip=0.0.0.0
port=47017
shardsvr=true
replSet=shard2
```

####		路由节点配置如下：

```shell
port=27017
bind_ip=0.0.0.0
fork=true
logpath=/data/route/logs/route.log
configdb=configsvr/172.17.0.2:17011,172.17.0.2:17012,172.17.0.2:17013
```



###		执行启动脚本启动各个复制集

####		启动配置集

```shell
./bin/mongod -f cluster_config/config/config-17011.conf
./bin/mongod -f cluster_config/config/config-17012.conf
./bin/mongod -f cluster_config/config/config-17013.conf
```

####		启动分片1

```shell
./bin/mongod -f cluster_config/shard1/node-37011.conf
./bin/mongod -f cluster_config/shard1/node-37013.conf
./bin/mongod -f cluster_config/shard1/node-37015.conf
./bin/mongod -f cluster_config/shard1/node-37017.conf
```

####		启动分片2

```shell
./bin/mongod -f cluster_config/shard2/node-47011.conf
./bin/mongod -f cluster_config/shard2/node-47013.conf
./bin/mongod -f cluster_config/shard2/node-47015.conf
./bin/mongod -f cluster_config/shard2/node-47017.conf
```

####		路由节点

```shell
./bin/mongos -f cluster_config/route/route-27017.conf
```



###		配置各个复制集

###		配置配置集

```javascript
./bin/mongo --port 17011 
use admin 
var cfg ={"_id":"configsvr", "members":[ {"_id":1,"host":"172.17.0.2:17011"}, {"_id":2,"host":"172.17.0.2:17012"}, {"_id":3,"host":"172.17.0.2:17013"}]};
rs.initiate(cfg)
rs.status()
```

####		配置分片1复制集

```javascript
./bin/mongo --port 37011
use admin 
var cfg ={"_id":"shard1", "protocolVersion":1,"members":[ {"_id":1,"host":"172.17.0.2:37011"}, {"_id":2,"host":"172.17.0.2:37013"}, {"_id":3,"host":"172.17.0.2:37015"},{"_id":4,"host":"172.17.0.2:37017"}] };
rs.initiate(cfg)
rs.status()
```

####		配置分片2复制集

```javascript
./bin/mongo --port 47011
use admin 
var cfg ={"_id":"shard2", "protocolVersion":1,"members":[ {"_id":1,"host":"172.17.0.2:47011"}, {"_id":2,"host":"172.17.0.2:47013"}, {"_id":3,"host":"172.17.0.2:47015"},{"_id":4,"host":"172.17.0.2:47017"}] };
rs.initiate(cfg)
rs.status()
```

####		配置路由服务

```javascript
./bin/mongo --port 27017
sh.status()
sh.addShard("shard1/172.17.0.2:37011,172.17.0.2:37013,172.17.0.2:37015,172.17.0.2:37017");
sh.addShard("shard1/172.17.0.2:47011,172.17.0.2:47013,172.17.0.2:47015,172.17.0.2：47017");
sh.status()
// 为lg_resume数据库开启分片功能 
sh.enableSharding("lg_resume") 
// 为lg_resume_datas集合开启hash分片功能
sh.shardCollection("lg_resume.lg_resume_datas",{"name":"hashed"})
```

###		插入数据测试

```javascript
use lg_resume;
for(var i=1;i<20;i++){
    db.lg_resume_datas.insert({"name":"test"+i,"introduction":"我是测试"+i,"expectSalary":(Math.random()*20000).toFixed(2)})
}
```

去两个分片查看数据

**分片1**

```javascript
./bin/mongo --port 37011
show dbs
use lg_resume
db.lg_resume_datas.find()
```

数据如下：

```verilog
{ "_id" : ObjectId("5f6cbcc3b5fe0beccfd40c8f"), "name" : "test3", "introduction" : "我是测试3", "expectSalary" : "11249.27" }
{ "_id" : ObjectId("5f6cbcc3b5fe0beccfd40c90"), "name" : "test4", "introduction" : "我是测试4", "expectSalary" : "9876.11" }
{ "_id" : ObjectId("5f6cbcc3b5fe0beccfd40c91"), "name" : "test5", "introduction" : "我是测试5", "expectSalary" : "18741.65" }
{ "_id" : ObjectId("5f6cbcc3b5fe0beccfd40c95"), "name" : "test9", "introduction" : "我是测试9", "expectSalary" : "4971.74" }
```



**分片2**

```javascript
./bin/mongo --port 47011
show dbs
use lg_resume
db.lg_resume_datas.find()
```



```verilog
{ "_id" : ObjectId("5f6cbcc3b5fe0beccfd40c8d"), "name" : "test1", "introduction" : "我是测试1", "expectSalary" : "268.32" }
{ "_id" : ObjectId("5f6cbcc3b5fe0beccfd40c8e"), "name" : "test2", "introduction" : "我是测试2", "expectSalary" : "17980.44" }
{ "_id" : ObjectId("5f6cbcc3b5fe0beccfd40c92"), "name" : "test6", "introduction" : "我是测试6", "expectSalary" : "6176.18" }
{ "_id" : ObjectId("5f6cbcc3b5fe0beccfd40c93"), "name" : "test7", "introduction" : "我是测试7", "expectSalary" : "6853.70" }
{ "_id" : ObjectId("5f6cbcc3b5fe0beccfd40c94"), "name" : "test8", "introduction" : "我是测试8", "expectSalary" : "6847.30" }
{ "_id" : ObjectId("5f6cbcc3b5fe0beccfd40c96"), "name" : "test10", "introduction" : "我是测试10", "expectSalary" : "1995.83" }
{ "_id" : ObjectId("5f6cbcc3b5fe0beccfd40c97"), "name" : "test11", "introduction" : "我是测试11", "expectSalary" : "7412.38" }
{ "_id" : ObjectId("5f6cbcc3b5fe0beccfd40c98"), "name" : "test12", "introduction" : "我是测试12", "expectSalary" : "16255.63" }
{ "_id" : ObjectId("5f6cbcc3b5fe0beccfd40c99"), "name" : "test13", "introduction" : "我是测试13", "expectSalary" : "16243.42" }
{ "_id" : ObjectId("5f6cbcc3b5fe0beccfd40c9a"), "name" : "test14", "introduction" : "我是测试14", "expectSalary" : "16102.51" }
{ "_id" : ObjectId("5f6cbcc3b5fe0beccfd40c9b"), "name" : "test15", "introduction" : "我是测试15", "expectSalary" : "7888.73" }
{ "_id" : ObjectId("5f6cbcc3b5fe0beccfd40c9c"), "name" : "test16", "introduction" : "我是测试16", "expectSalary" : "14714.69" }
{ "_id" : ObjectId("5f6cbcc3b5fe0beccfd40c9d"), "name" : "test17", "introduction" : "我是测试17", "expectSalary" : "4480.28" }
{ "_id" : ObjectId("5f6cbcc3b5fe0beccfd40c9e"), "name" : "test18", "introduction" : "我是测试18", "expectSalary" : "6037.69" }
{ "_id" : ObjectId("5f6cbcc3b5fe0beccfd40c9f"), "name" : "test19", "introduction" : "我是测试19", "expectSalary" : "14425.48" }
```

**发现数据还挺不均衡的**

###		权限配置

- 进入路由服务设置账号

  ```javascript
  ./bin/mongo --port 27017
  use admin
  db.createUser({
  user:"root",
  pwd:"root",
  roles:[{role:"root",db:"admin"}]
  });
  db.auth("root","root");
  
  show dbs
  use lg_resume
  db.createUser({
  user:"lagou_gx",
  pwd:"abc321",
  roles:[{role:"readWrite",db:"lg_resume"}]
  });
  db.auth("lagou_gx","abc321");
  ```

- 关闭所有服务

  ```shell
  yum install psmisc -y
  killall mongod
  ```

- 生成秘钥文件

  ```shell
  yum install openssl -y
  openssl rand -base64 756 > /data/mongodb/mongKeyFile.file 
  chmod 600 /data/mongodb/keyfile/mongKeyFile.file
  ```

- 配置权限认证和秘钥文件

  ```shell
  echo "auth=true" >> t.txt
  echo "keyFile=/data/mongodb/mongKeyFile.file" >> t.txt
  
  cat t.txt >> cluster_config/config/config-17011.conf
  cat t.txt >> cluster_config/config/config-17012.conf
  cat t.txt >> cluster_config/config/config-17013.conf
  
  cat t.txt >> cluster_config/shard1/node-37011.conf
  cat t.txt >> cluster_config/shard1/node-37013.conf
  cat t.txt >> cluster_config/shard1/node-37015.conf
  cat t.txt >> cluster_config/shard1/node-37017.conf
  
  cat t.txt >> cluster_config/shard2/node-47011.conf
  cat t.txt >> cluster_config/shard2/node-47013.conf
  cat t.txt >> cluster_config/shard2/node-47015.conf
  cat t.txt >> cluster_config/shard2/node-47017.conf
  ```

- 重新启动所有服务

  ```shell
  ./configStart.sh
  ./shard1Start.sh
  ./shard2Start.sh
  ```

  **注意的是：一定要先启动配置集群，后启动分片集群**