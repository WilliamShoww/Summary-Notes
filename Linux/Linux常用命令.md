#		Linux常用命令总结

##		Redis相关

###		正则匹配批量删除键

1. 通过管道xargs方式

   ```shell
   redis-cli -h 192.168.0.80 -p 6379 -a "******" -n 0 keys "*825*"|xargs redis-cli -h 192.168.0.80 -p 7379 -a "******" -n 0 del {}
   ```

   shell 程序如下：

   ```shell
   echo "请选择DB: "
   read db
   echo "匹配字符："
   read patter
   redis-cli -h 192.168.0.80 -p 7379 -a "******" -n $db keys "$patter"|xargs redis-cli -h 192.168.0.80 -p 6379 -a "*****" -n $db del {}
   echo "Batch del keys: $patter"
   
   ```

   **注意的是：** del后面的`{}`不能少，不然可能会报错：`(error) ERR wrong number of arguments for 'del' command`



##		JDK相关的

1. 启动jar包的方式

   ```shell
   java -server -Xms2048m -Xmx2048m -XX:MetaspaceSize=128m  -XX:+PrintGCDetails -cp ${cur_path}/:${cur_path}/config/*:${cur_path}/se-lib/*:${cur_path}/lib/*:${cur_path}/bbs.jar com.leixing.bbs.BBSApplication
   ```

   **注意的是：** 多个路径下的jar包之间，linux 用`:`隔开，windows 用`.;`隔开

   

2. 杀掉当前路径下的程序进程的方式

   ```shell
   ps uxfa | grep java | grep `pwd` | awk '{print $2}' | xargs kill -9
   ```



##		nohup

nohup 是 no hang up 的缩写，就是不挂断的意思;但没有后台运行，终端不能标准输入。

nohup命令：如果你正在运行一个进程，而且你觉得在退出帐户时该进程还不会结束，那么可以使用nohup命令。该命令可以在你退出帐户/关闭终端之后继续运行相应的进程。

在缺省情况下该作业的所有输出都被重定向到一个名为nohup.out的文件中。或者 > /dev/null 进行丢弃到黑洞中

1. 用途一：不挂断的运行命令；用法：nohup Command [Args..] [ &]。
2. 用途二：后台运行程序；用法：nohup Command & 。

*详情：自行搜索*