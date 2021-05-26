#		Maven可以推送至私服Nexus上，但是没法更新依赖包问题记录

##		问题回顾

在构建项目组的基础设施过程中，构建Nexus私服的时候，Nexus私服搭建完成，测试可以推送自己的包到私服但是没法拉取。将依赖添加到项目中，Maven提示找不到对应的包。

##		排查过程

1. 检查配置是否正确，也就是用户信息配置是否正确（settings文件的server配置的id是否一致），仔细检查发现是一致的，绝对一致。原因：可以推送成功。
2. 检查依赖包上传的是否正确，检测是否可以访问到依赖包。检测是没错的，检测的大致步骤就是将依赖包地址直接在浏览器看是否可以访问到。参考：[测试maven镜像是否可用](<https://blog.csdn.net/gsls200808/article/details/78039620>)
3.  上面两个都没问题，所以想了很久都没有找到原因，后来百度也没出什么好的结果，无意中百度到了别人《解决maven私服nexus无法下载snapshot版本包》相关问题，然后发现自己那时候依赖的正是SNAPSHOT版本。这才真正找到问题的根源。根据前人的经验修改配置即可。

##  总结

SNAPSHOT版本可能会没法拉取，需要更改对应的配置。

参考：

[idea中maven配置及时拉取snapshot的更新](<https://blog.csdn.net/zlj1217/article/details/81210453>)    	<https://blog.csdn.net/zlj1217/article/details/81210453>
[解决maven私服nexus无法下载snapshot版本包](https://blog.csdn.net/devotedwife/article/details/85096625) <https://blog.csdn.net/devotedwife/article/details/85096625>
[Nexus添加的包怎么就下载不下来？](https://blog.csdn.net/yin138/article/details/91807885)   			  	 <https://blog.csdn.net/yin138/article/details/91807885>