#		Shell如何得到执行脚本的绝对路径？

##		readlink命令

```shell
readlink [选项] 文件
# 输出符号链接值或者绝对路径
  -f, --canonicalize			递归跟随给出文件名的所有符号链接以标准化，除最后一个外所有组件必须存在（eg. /a/b/c.sh c.sh可以不存在，但是/a和/a/b 必须存在，否则输出空）
  -e, --canonicalize-existing	递归跟随给出文件名的所有符号链接以标准化，所有组件都必须存在（整个文件必须存在）
  -m, --canonicalize-missing	递归跟随给出文件名的所有符号链接以标准化，但不对组件存在性作出要求（上级目录和文件都可以不存在）
  -n, --no-newline				不输出尾随的新行
  -q, --quiet,
  -s, --silent					缩减大多数的错误消息
  -v, --verbose					报告所有错误消息
      --help					显示此帮助信息并退出
      --version					显示版本信息并退出
# readlink -f ./{path}/shell.sh ----> /{绝对路径}/shell.sh (如果shell.sh不存在输出一致,如果目录错误也输出空)
# readlink -e ./{path}/shell.sh ----> /{绝对路径}/shell.sh (如果shell.sh不存在输出空)
# # readlink -m ./{path}/shell.sh ----> /{绝对路径}/shell.sh （目录和文件无论谁不存在都输出一致）
```

该命令能得到某个文件的绝对路径，那能得到执行的脚本文件那就可以得到绝对路径了

##		获取脚本文件

1. 方法一

   ```shell
   shell_name="${BASH_SOURCE[0]}";
   # /{path}/shell.sh  ---> /{path}/shell.sh
   # ./shell.sh ----------> ./shell.sh
   # ./{path}/shell.sh ----> ./{path}/shell.sh方法二
   ```

2. 方法二

   ```shell
   # $0: Shell本身的文件名
   shell_name=$0;
   # /{path}/shell.sh  ---> /{path}/shell.sh
   # ./shell.sh ----------> ./shell.sh
   # ./{path}/shell.sh ----> ./{path}/shell.sh方法二
   ```



##		绝对路径的方法

1. 方法一

   ```shell
   shell_name="${BASH_SOURCE[0]}";
   script_path=$(readlink -f $shell_name);
   ```

2. 方法二

   ```shell
   script_path=$(readlink -f $0);
   ```

   

