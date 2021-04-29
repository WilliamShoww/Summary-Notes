#		【转载】[在shell中如何判断一个变量是否为空](https://blog.csdn.net/varyall/article/details/79140753)

##		变量通过" "引号引起来

如下所示:

```shell
#!/bin/sh  
para1=  
if [ ! -n "$para1" ]; then  
  echo "IS NULL"  
else  
  echo "NOT NULL"  
fi  
```

可以得到结果为 IS NULL。

##		直接通过变量判断

如下所示：

```shell
#!/bin/sh  
para1=  
if [ ! $para1 ]; then  
  echo "IS NULL"  
else  
  echo "NOT NULL"  
fi 
```

得到的结果为：IS NULL。

##		使用test判断

如下所示：

```shell
#!/bin/sh  
dmin=  
if test -z "$dmin"  
then  
  echo "dmin is not set!"  
else    
  echo "dmin is set !"  
fi   
```

得到的结果就是：dmin is not set! 

##		使用""判断

如下所示：

```shell
#!/bin/sh   
dmin=  
if [ "$dmin" = "" ]  
then  
  echo "dmin is not set!"  
else    
  echo "dmin is set !"  
fi
```

得到的结果就是：dmin is not set!

【参考】[Shell test 命令](https://www.runoob.com/linux/linux-shell-test.html)  <https://www.runoob.com/linux/linux-shell-test.html>

【参考】[Shell 流程控制](https://www.runoob.com/linux/linux-shell-process-control.html)  <https://www.runoob.com/linux/linux-shell-process-control.html>

