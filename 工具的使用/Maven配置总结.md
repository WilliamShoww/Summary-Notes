#		Maven配置总结

##  多模块项目中注意事项

1. 在多模块项目中，如果子项目总是编译失败，可能是没有先编译一下父项目并且`install`一下。



###	插件的使用

####	compiler插件使用

引入方式如下：

```xml
<build>
    <plugins>
        <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <source>8</source>
                    <target>8</target>
                </configuration>
            </plugin>
    </plugins>
</build>
```

`Spring-boot` 项目如果用默认插件，`java` 输出环境老是会变到低于项目配置的`JDK`版本，可以不要用默认插件，改成上面的编译插件，并且指定目标和源码版本即可



####	打成jar包插件

引入方式如下：

```xml
<build>
    <plugins>
        <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-jar-plugin</artifactId>
                <version>3.0.0</version>
                <executions>
                    <execution>
                        <phase>package</phase>
                        <goals>
                            <goal>jar</goal>
                        </goals>
                    </execution>
                </executions>
                <configuration>
                    <!-- 最终的包名。可以写死或者引用项目名 -->
                    <finalName>${project.name}</finalName>
                    <archive>
                        <manifest>
                            <addClasspath>false</addClasspath>
                            <!-- 依赖的包路径 相对于target路径 -->
                            <classpathPrefix>lib/</classpathPrefix>
                            <!-- 启动类设置，如果想通过 java -jar 启动必现设置 -->
                            <mainClass>com.TestApplication</mainClass>
                        </manifest>
                    </archive>
                    <excludes>
                        <!-- 打包的时候过滤掉打包的文件 相对于target 的classes路径 -->
                        <exclude>/*.properties</exclude>
                        <exclude>/*.xml</exclude>
                    </excludes>
                </configuration>
            </plugin>
    </plugins>
</build>
```

详细使用教程 自行百度，使用过程中遇到几个注意的点如下：

1. 排除文件是相对于`classes`的路径，并不是项目的路径，特别是排除配置文件的时候，不是相对于`/resources/*` 而是去查看一下`classes` 的情况，再填写。



####	复制依赖包到某个文件夹的插件

依赖如下：

```xml
<build>
    <plugins>
        <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-dependency-plugin</artifactId>
                <executions>
                    <execution>
                        <id>copy</id>
                        <phase>package</phase>
                        <goals>
                            <goal>copy-dependencies</goal>
                        </goals>
                        <configuration>      
 <!-- 输出路径 -->                           <outputDirectory>${project.build.directory}/lib</outputDirectory>
                            <!--已存在的Release版本不重复copy-->
                            <overWriteReleases>false</overWriteReleases>
                            <!--已存在的SnapShot版本不重复copy-->
                            <overWriteSnapshots>false</overWriteSnapshots>
                            <!--不存在或者有更新版本的依赖才copy-->
                            <overWriteIfNewer>true</overWriteIfNewer>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
    </plugins>
</build>
```

**该插件的作用是：**能将依赖的`jar`包复制到统一的路径下，方便部署。

详情使用自行百度。