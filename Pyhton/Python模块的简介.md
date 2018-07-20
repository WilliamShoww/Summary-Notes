#	Python第三方模块的简介

本文档都是基于Python 3.0以上的模块简介，Python 3.0 以下仅供参考，未必可使用。Python 模块非常丰富，这些模块对一些功能进行封装之后，使用起来会简单，愉快。

##	第三方模块的安装

1. **pip包管理工具**

Python安装第三方模块是通过包管理工具pip完成的。Mac或Linux系统自带pip管理工具，Windows 需要安装Python时勾选pip并且把它加入环境变量中。

使用方法：

```
pip install 模块名
```

一般来说，第三方库都会在*[Python官方模块库](https://pypi.org/)*注册

2.**Anaconda**

pip安装需要一个一个安装，费时费力，*[Anaconda](https://www.anaconda.com/)* 是一个基于Python的数据处理和科学计算平台，它内置了非常多有用的库，所以我们装上它就相当于装了很多第三方模块

下载后直接安装，Anaconda会把系统Path中的python指向自己自带的Python，并且，Anaconda安装的第三方模块会安装在Anaconda自己的路径下，不影响系统已安装的Python目录。 

##	内部模块

内部模块是指Python 内部自带的模块，不用额外安装就可使用。

