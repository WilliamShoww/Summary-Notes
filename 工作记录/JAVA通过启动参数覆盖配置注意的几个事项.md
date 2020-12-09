#  JAVA通过启动参数传递参数到程序注意的几个事项

##		传递SpringBoot的配置覆盖掉程序自带Properties配置

使用场景是Java程序打成jar包，但是部分配置项需要根据实际情况可配置。所以想在启动脚本替换上去。

注意事项如下：

1. 参数放置的顺序不要错了，不然没有用。**`SpringBoot`的配置要在jar包之后通过`--<name>=<value>`设置进去。**

   ```shell
   #  错误示例，放在jar包之前
   java -jar -server --spring.config.location=${LAST_PATH}/conf/application.properties ${CURR_PATH}/${JAR_FILE_NAME}
   
   #  正确的示例，放在jar包之后
   java -jar -server ${CURR_PATH}/${JAR_FILE_NAME} --spring.config.location=${LAST_PATH}/conf/application.properties
   ```

   

##		传递LogBack的变量配置覆盖掉logback.xml的配置

使用场景是Java程序打成jar包，日志打印的部分配置需要根据实际情况配置，所以在脚本启动的时候通过启动参数替换上去。logback会加载JVM的环境参数去替换原来的默认配置。

JVM环境参数配置注意事项如下：

1. 参数顺序不要错了，不然没法传递进去。**`JVM`的系统属性配置要在jar包之前通过`-D<name>=<value>`设置进去。**

   ```shell
   # 错误的示例，放在jar包之后
   java -jar -server ${CURR_PATH}/${JAR_FILE_NAME} -Dlog.path=${LOG_PATH}
   # 正确的示例，放在jar包之前
   java -jar -server -Dlog.path=${LOG_PATH} ${CURR_PATH}/${JAR_FILE_NAME} 
   ```

   

