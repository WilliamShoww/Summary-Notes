##		Logback日志配置

`Logback`越来越流行，不过使用过程中觉得还不错，配置简单明了。不过有几点需要注意的是：

1. `Spring-boot`中使用`Logback`不需要再引入`Logback`的三个`jar`包，因为在`spring-boot-starter-logging`已经集成了它。
2. 几种日志过滤器，需要了解明白，不然不能配置到自己想要的效果的日志。

本人配置如下：

```xml
<configuration debug="true" scan="true" scanPeriod="1 seconds">
    <contextName>logback</contextName>
    <!--定义参数,后面可以通过${app.name}使用-->
    <property name="app.name" value="appName"/>
    <property name="log.path" value="/logs"/>
    <!--ConsoleAppender 用于在屏幕上输出日志-->
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <!--定义了一个过滤器,在LEVEL之下的日志输出不会被打印出来-->
        <!--这里定义了DEBUG，也就是控制台不会输出比ERROR级别小的日志-->
        <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
            <level>INFO</level>
        </filter>
        <!-- encoder 默认配置为PatternLayoutEncoder -->
        <!--定义控制台输出格式-->
        <encoder>
            <pattern>%d [%thread] %highlight(%-5level) %logger{36} [%file : %line] - %msg%n</pattern>
        </encoder>
    </appender>

    <!--输出INFO日志到文件-->
    <appender name="INFO_FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <!--定义日志输出的路径-->
        <file>${log.path}/${app.name}.log</file>

        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>INFO</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>NEUTRAL</onMismatch>
        </filter>
        <!--<filter class="ch.qos.logback.classic.filter.ThresholdFilter">
            <level>INFO</level>
        </filter>-->
        <!--定义日志滚动的策略-->
        <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
            <!--定义文件滚动时的文件名的格式 %i必须要有，不然报错-->
            <fileNamePattern>${log.path}/${app.name}.%d{yyyy-MM-dd}%i.log</fileNamePattern>
            <maxFileSize>10MB</maxFileSize>
            <!--60天的时间周期，日志量最大20GB-->
            <maxHistory>60</maxHistory>
            <!-- 该属性在 1.1.6版本后 才开始支持-->
            <totalSizeCap>20GB</totalSizeCap>
        </rollingPolicy>
        <!--定义输出格式-->
        <encoder>
            <pattern>%d [%thread] %highlight(%-5level) %logger{36} [%file : %line] - %msg%n</pattern>
        </encoder>
    </appender>

    <!--输出到文件的日志-->
    <appender name="ERROR_FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <!--定义日志输出的路径-->
        <!--这里的 没有在上面的配置中设定，所以会使用java启动时配置的值-->
        <file>${log.path}/error/${app.name}_error.log</file>
        <!--级别过滤器 日志级别等于配置级别-->
        <!--<filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>WARN</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>NEUTRAL</onMismatch>
        </filter>-->
        <!--临界值过滤器 日志级别大于等于配置的级别-->
        <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
            <level>WARN</level>
        </filter>
        <!--定义日志滚动的策略-->
        <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
            <!--定义文件滚动时的文件名的格式-->
            <fileNamePattern>${log.path}/error/${app.name}_error.%d{yyyy-MM-dd}%i.log</fileNamePattern>
            <maxFileSize>10MB</maxFileSize>
            <!--60天的时间周期，日志量最大20GB-->
            <maxHistory>60</maxHistory>
            <!-- 该属性在 1.1.6版本后 才开始支持-->
            <totalSizeCap>20GB</totalSizeCap>
        </rollingPolicy>
        <!--定义输出格式-->
        <encoder>
            <pattern>%d [%thread] %-5level %logger{36} [%file : %line] - %msg%n</pattern>
        </encoder>
    </appender>

    <!--root是默认的logger 这里设定输出级别是debug-->
    <root level="DEBUG">
        <!--定义了两个appender，日志会通过往这两个appender里面写-->
        <appender-ref ref="STDOUT"/>
        <appender-ref ref="INFO_FILE"/>
        <appender-ref ref="ERROR_FILE"/>
    </root>
</configuration>

```

###		过滤器FILTER

####	级别过滤器LevelFilter

级别过滤器是根据日志级别进行过滤的，过滤器会根据`onMatch`和`onMismatch`接收或拒绝

用法如下：

```xml
<filter class="ch.qos.logback.classic.filter.LevelFilter">   
    <level>INFO</level>   
    <onMatch>ACCEPT</onMatch>   
    <onMismatch>DENY</onMismatch>   
</filter>
```

`match`值有三个，分别是：`DENY`、`NEUTRAL`和`ACCEPT`

1. `DENY`：日志立即被抛弃不再经过其他过滤器；
2. `NEUTRAL`：有序列表里的下个过滤器接着处理日志(一个`appender` 可以配置多个`filter`)；
3. `ACCEPT`：日志被立即处理，不再经过剩余过滤器。

*所以如果只显示`WARN`和`INFO`级别的日志怎么配置过滤器？*如下：

```xml
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>INFO</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>NEUTRAL</onMismatch>
        </filter>
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>WARN</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
```

####	临界值过滤器ThresholdFilter

临界值过滤器，过滤掉低于配置的临界值的日志。当日志级别等于或高于临界值时，过滤器返回NEUTRAL；当日志级别低于临界值时，日志会被拒绝。

用法如下：

```xml
	<!-- 过滤掉 TRACE 和 DEBUG 级别的日志-->   
    <filter class="ch.qos.logback.classic.filter.ThresholdFilter">   
      <level>INFO</level>   
    </filter> 
```

####	求值过滤器EvaluatorFilter

求值过滤器，评估、鉴别日志是否符合指定条件。需要额外的两个JAR包，commons-compiler.jar和janino.jar有以下子节点。

本人几乎没用过，详细用法百度。



参考：

[logback logback.xml常用配置详解](https://www.iteye.com/blog/aub-1110008)

