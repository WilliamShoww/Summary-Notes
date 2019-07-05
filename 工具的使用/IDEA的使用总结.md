#		IDEA的使用常见设置

##	常见设置

1. 文件编码格式设置

   File | Settings | Editor | File Encodings

2. 字体大小设置

   File | Settings | Editor | Font

3. 背景主题设置

   File | Settings | Editor | Color Scheme

4. git版本管理工具设置

   File | Settings | Version Control | Git
   
5. git版本树图设置

   窗口VCS | Browse VCS Repository | Show Git Repository Log | 选择相应的项目

##	常用的插件

1. `CamelCase` 驼峰式命名和下划线命名交替变化，使用：Ctrl+Shift+u
2. `GsonFormat` 把 JSON 字符串直接实例化成类
3. `Ace Jump AceJump`其实是一款能够代替鼠标的软件，只要安装了这款插件，可以在代码中跳转到任意位置。使用：Ctrl+;
4.  `Lombok` 用注解生成setter和getter方法
5. `Maven Helper` maven依赖检测工具，冲突检测等等
6. `Alibaba Java Code Guidelines` 阿里巴巴代码规范约束插件
7. `Free Mybatis plugin`  mybatis代码一键到对应xml的插件
8. `Translate` 翻译插件，使用：窗口右上角点击翻译图标
9. `A8Translate` 英文翻译插件，使用：Alt+t
10. `.ignore` 将文件添加到git的忽略文件列表中
11. `SequenceDiagram` 生成函数时序图



##	乱码问题

###	tomcat 日志中文乱码问题

1. 检测文件格式编码 `File | Settings | Editor | File Encodings` 设置为UTF-8
2. tomcat启动配置中的`VM options` 设置为：-Dfile.encoding=UTF-8
3. IDEA 配置文件中，`bin`目录下 的`idea64.exe.vmoptions` 和 `idea.exe.vmoptions`(32位的时候) 添加配置项目`-Dfile.encoding=UTF-8`

