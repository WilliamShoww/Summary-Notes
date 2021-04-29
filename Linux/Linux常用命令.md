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

   **注意的是：** 多个路径下的jar包之间，linux 用`:`隔开，windows 用`.;`隔开（windows 未亲自手动验证）

   

2. 杀掉当前路径下的程序进程的方式

   ```shell
   ps uxfa | grep java | grep `pwd` | awk '{print $2}' | xargs kill -9
   ```
   
3. JAVA项目启动的脚本

   ```shell
   # 获取脚本的绝对路径
   script_path=$(readlink -f '$0');
   # 获取项目的home目录
   project_home_dir=`dirname $script_path`;
   echo "APP dir: $project_home_dir";
   # 移除旧的日志文件
   if [ ! -f "$project_home_dir/nohup.old" ];then
   	echo "nohup.old file not exist."
   else
   	rm $project_home_dir/nohup.old -f;
   	sleep 2;
   	echo "Done rm nohup.old";
   fi
   # 备份目前的日志文件
   if [ ! -f "$project_home_dir/nohup.out" ]; then
   	echo "nohup.out file not exist."
   else
   	mv nohup.out nohup.old;
   	sleep 2;
   	echo "Done mv nohup.out to nohup.old";
   fi
   # 获取当前应用的PID
   app_pid=`ps uxfa | grep java | grep $project_home_dir | awk '{print $2}'`;
   # 有当前应用的PID，杀掉旧的进程
   if [ $app_pid ]; then
   	kill -9 $app_pid;
   	sleep 6;
   	echo "Done kill app: $app_pid";
   else
   	echo "This app not active"
   fi
   echo "Start doing.........................";
   # JAVA启动参数
   java_opts="-server -XX:+UseG1GC -Xms1024m -Xmx1024m -XX:G1HeapRegionSize=16m -XX:MetaspaceSize=512m -XX:MaxMetaspaceSize=512m -XX:+PrintGCDetails";
   # 项目的类信息
   project_class_opts="${project_home_dir}/se-lib/*:${project_home_dir}/lib/*:${project_home_dir}/xx.jar com.xx.xx.{mainclass}";
   # 启动应用
   nohup java $java_opts -cp $project_class_opts &
   echo "Done Deploy";
   sleep 2;
   echo "Deploy sucsess.";
   # 写入新启动的应用的PID
   echo "This app new PID: $!" >> $project_home_dir/nohup.out;
   tail -f nohup.out;
   ```

   

##		nohup

nohup 是 no hang up 的缩写，就是不挂断的意思;但没有后台运行，终端不能标准输入。

nohup命令：如果你正在运行一个进程，而且你觉得在退出帐户时该进程还不会结束，那么可以使用nohup命令。该命令可以在你退出帐户/关闭终端之后继续运行相应的进程。

在缺省情况下该作业的所有输出都被重定向到一个名为nohup.out的文件中。或者 > /dev/null 进行丢弃到黑洞中

1. 用途一：不挂断的运行命令；用法：nohup Command [Args..] [ &]。
2. 用途二：后台运行程序；用法：nohup Command & 。

*详情：自行搜索*



##		VIM

###		查找

```shell
/keyword         //按默认方向查找关键字
?keyword         //按与默认方向相反的方向查找关键字
n                //按与之前相同的查找顺序匹配下一个出现相同关键字的位置
N                //按与之前相反的查找顺序匹配下一个出现相同关键字的位置   　　　　　　　
				 //用户也可以通过Ctrl + o 和 Ctrl + i 进行位置的切换
```



###	显示/隐藏行号

命令:	`:set number` 显示行号

命令： `:set nonumber` 隐藏行号

###		移动

上一页：Ctr+f

下一页：Ctr+b

首行：gg

尾行：G

n行：nG

返回上一次光标的位置：Ctr+o



##	账号切换

###	用当前账号密码切换到root账号权限

```shell
sudo su -l root
```

然后输入当前账号密码

##  路径相关

###		获取当前脚本的位置

```shell
readlink -f "$0" # 获取到的是全路径包括文件名
```

###		获取文件名

```shell
# 格式
basename [pathname] [suffix]
# 用法实例
basename /test/file.sh 		 # 得到file.sh
basename /test/file.sh .sh 	 # 得到file
basename $1 /test/file.sh aa # 得到file.sh
```

###		获取上一级目录

```shell
# 格式
dirname [pathname]
# 用法实例
dirname file.sh				 # 得到 .
dirname ./file.sh 	 		 # 得到 .
dirname ~/test/test2/    	 # 得到 /home/xiaocf/test  其中/home/xiaocf/是我的家目录
dirname ~/test/test2/file.sh # 得到 /home/xiaocf/test/test2 其中/home/xiaocf/是我的家目录
```

参考：[basename命令dirname命令和readlink -f命令的使用](https://blog.csdn.net/modi000/article/details/107001274) 

