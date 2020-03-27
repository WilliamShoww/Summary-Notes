#		Spring boot 项目在打成war包在tomcat部署的坑

由于项目交接给公司运维，运维那边要求必须打成war包部署，在打包过程中还是遇到不少坑的。记录一下。

##	context-path

这个配置在内嵌容器中没有问题，但是在外部tomcat容器中会出现，所有的接口访问都被识别成静态资源，也就是用ResourceHttpRequestHandler去处理请求，而改路由的资源又没有，所以会一直错误，然后跳转至/error

##		jar包冲突

jar包冲突也是很常见，所以需要检查jar包冲突。排除冲突的jar包。