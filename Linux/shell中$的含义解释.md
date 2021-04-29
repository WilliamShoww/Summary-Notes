#		【转发】 [linux中shell变量$#,$@,$0,$1,$2的含义解释](https://www.cnblogs.com/fhefh/archive/2011/04/15/2017613.html)

摘抄自：ABS_GUIDE

下载地址：http://www.tldp.org/LDP/abs/abs-guide.pdf

```shell
# linux中shell变量$#,$@,$0,$1,$2的含义解释: 
# 变量说明: 
$$ 		# Shell本身的PID（ProcessID） 
$! 		# Shell最后运行的后台Process的PID 
$? 		# 最后运行的命令的结束代码（返回值） 
$- 		# 使用Set命令设定的Flag一览 
$* 		# 所有参数列表。如"$*"用「"」括起来的情况、以"$1 $2 … $n"的形式输出所有参数。 
$@ 		# 所有参数列表。如"$@"用「"」括起来的情况、以"$1" "$2" … "$n" 的形式输出所有参数。 
$# 		# 添加到Shell的参数个数 
$0 		# Shell本身的文件名 
$1～$n  # 添加到Shell的各参数值。$1是第1参数、$2是第2参数…。 
```

示例：

```shell
# !/bin/bash
printf "The complete list is %s\n" "$$";
printf "The complete list is %s\n" "$!";
printf "The complete list is %s\n" "$?";
printf "The complete list is %s\n" "$*";
printf "The complete list is %s\n" "$@";
printf "The complete list is %s\n" "$#";
printf "The complete list is %s\n" "$0";
printf "The complete list is %s\n" "$1";
printf "The complete list is %s\n" "$2";
```

结果：

```shell
bash ./params.sh 123456 QQ
# 以下输出：
# The complete list is 24249
# The complete list is 
# The complete list is 0
# The complete list is 123456 QQ
# The complete list is 123456
# The complete list is QQ
# The complete list is 2
# The complete list is ./params.sh
# The complete list is 123456 
# The complete list is QQ
```