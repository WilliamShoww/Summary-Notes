#		docker容器端口号映射书写错误导致无法访问容器内的服务

最近在自学docker，刚好懒得装mysql了，想直接利用容器搞一个mysql就好了，于是利用docker拉取镜像。步骤如下：

```shell
#	拉取mysql镜像
docker pull mysql:5.7
# 	查看镜像
docker images
#	启动容器
#	-itd 后台运行等  -p 端口映射  --name  容器名字  -e  启动环境变量(这里设置root账号的密码)
docker run -itd -p 3306:3306 --name mysql -e MYSQL_ROOT_PASSWORD=123456 mysql:5.7
```

一切行云流水，mysql就这样装好了，用navicat连接一下，没问题。

##		问题的产生

随后我又想，啥时候搞个集群不是很方便吗？试一下启动多个mysql容器，命令如下：

```shell
docker run -itd -p 13306:13306 --name mysql -e MYSQL_ROOT_PASSWORD=123456 mysql:5.7
```

然后用navicat连接，各种连接不上。要么是1060或者1061错误。后来各种百度，密码访问权限各种一顿折腾，无果。后来直接受不了了，问问朋友吧。

***后来才知道：-p 只是端口映射，我理解成启动的时候不仅端口映射，还会修改容器内服务的启动端口号。内部服务启动端口号要变化必须修改容器内部的配置文件才行，-p启动参数是不行的***

所以启动命令应该如下：

```shell
docker run -p {映射主机端口}:{需要暴露的容器端口}
#	容器内mysql的启动端口号是3306，所以容器一定是暴露3306端口号(除非修改容器的配置文件，修改mysql的启动端口号)。
docker run -itd -p 13306:3306 --name mysql -e MYSQL_ROOT_PASSWORD=123456 mysql:5.7
```

这样navicat就可以连上localhost:13306的mysql服务了。