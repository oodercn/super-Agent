# 开发环境搭建

## 1. 概述

本文档详细介绍了 ooder-agent-rad 开发环境的搭建过程，包括系统要求、软件安装、环境配置和验证等方面。

## 2. 系统要求

| 系统 | 版本 | 备注 |
|------|------|------|
| Windows | 10/11 | 64位 |
| Linux | CentOS 7+, Ubuntu 18.04+ | 64位 |
| macOS | 10.15+ | 64位 |

## 3. 软件安装

### 3.1 JDK 安装

#### 3.1.1 Windows 安装

1. 下载 JDK 1.8+ 安装包：https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html
2. 双击安装包，按照向导进行安装
3. 选择安装路径，建议安装到 `C:\Program Files\Java\jdk1.8.0_xxx`
4. 完成安装

#### 3.1.2 Linux 安装

1. 下载 JDK 1.8+ 安装包
2. 解压到指定目录：
   ```bash
   tar -zxvf jdk-8uxxx-linux-x64.tar.gz -C /usr/local/
   ```
3. 重命名目录：
   ```bash
   mv /usr/local/jdk1.8.0_xxx /usr/local/jdk1.8
   ```

#### 3.1.3 macOS 安装

1. 下载 JDK 1.8+ 安装包：https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html
2. 双击安装包，按照向导进行安装
3. 安装完成后，JDK 会自动安装到 `/Library/Java/JavaVirtualMachines/jdk1.8.0_xxx.jdk/Contents/Home`

### 3.2 Maven 安装

#### 3.2.1 Windows 安装

1. 下载 Maven 3.6+ 安装包：https://maven.apache.org/download.cgi
2. 解压到指定目录，建议解压到 `D:\apache-maven-3.6.xxx`
3. 配置环境变量

#### 3.2.2 Linux/macOS 安装

1. 下载 Maven 3.6+ 安装包：https://maven.apache.org/download.cgi
2. 解压到指定目录：
   ```bash
   tar -zxvf apache-maven-3.6.xxx-bin.tar.gz -C /usr/local/
   ```
3. 重命名目录：
   ```bash
   mv /usr/local/apache-maven-3.6.xxx /usr/local/maven
   ```

### 3.3 Git 安装

#### 3.3.1 Windows 安装

1. 下载 Git 安装包：https://git-scm.com/download/win
2. 双击安装包，按照向导进行安装
3. 选择默认选项即可

#### 3.3.2 Linux 安装

1. CentOS：
   ```bash
   yum install git
   ```
2. Ubuntu：
   ```bash
   apt-get install git
   ```

#### 3.3.3 macOS 安装

1. 使用 Homebrew 安装：
   ```bash
   brew install git
   ```
2. 或下载安装包：https://git-scm.com/download/mac



## 4. 环境配置

### 4.1 JDK 环境变量配置

#### 4.1.1 Windows 配置

1. 右键点击 "此电脑"，选择 "属性"
2. 点击 "高级系统设置"
3. 点击 "环境变量"
4. 在 "系统变量" 中点击 "新建"
5. 变量名：`JAVA_HOME`，变量值：JDK 安装路径（如 `C:\Program Files\Java\jdk1.8.0_xxx`）
6. 在 "系统变量" 中找到 `Path` 变量，点击 "编辑"
7. 添加 `%JAVA_HOME%\bin`
8. 点击 "确定" 保存配置

#### 4.1.2 Linux/macOS 配置

1. 编辑 `/etc/profile` 或 `~/.bash_profile` 文件：
   ```bash
   vi ~/.bash_profile
   ```
2. 添加以下内容：
   ```bash
   export JAVA_HOME=/usr/local/jdk1.8
   export PATH=$JAVA_HOME/bin:$PATH
   export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
   ```
3. 保存文件并执行：
   ```bash
   source ~/.bash_profile
   ```

### 4.2 Maven 环境变量配置

#### 4.2.1 Windows 配置

1. 在 "系统变量" 中点击 "新建"
2. 变量名：`MAVEN_HOME`，变量值：Maven 安装路径（如 `D:\apache-maven-3.6.xxx`）
3. 在 "系统变量" 中找到 `Path` 变量，点击 "编辑"
4. 添加 `%MAVEN_HOME%\bin`
5. 点击 "确定" 保存配置

#### 4.2.2 Linux/macOS 配置

1. 编辑 `/etc/profile` 或 `~/.bash_profile` 文件：
   ```bash
   vi ~/.bash_profile
   ```
2. 添加以下内容：
   ```bash
   export MAVEN_HOME=/usr/local/maven
   export PATH=$MAVEN_HOME/bin:$PATH
   ```
3. 保存文件并执行：
   ```bash
   source ~/.bash_profile
   ```

### 4.3 Maven 配置文件修改

1. 编辑 Maven 安装目录下的 `conf/settings.xml` 文件
2. 配置本地仓库路径：
   ```xml
   <localRepository>/path/to/local/repository</localRepository>
   ```
3. 配置镜像源（可选，建议配置国内镜像以提高下载速度）：
   ```xml
   <mirrors>
       <mirror>
           <id>aliyunmaven</id>
           <mirrorOf>*</mirrorOf>
           <name>阿里云公共仓库</name>
           <url>https://maven.aliyun.com/repository/public</url>
       </mirror>
   </mirrors>
   ```
4. 配置 JDK 版本：
   ```xml
   <profiles>
       <profile>
           <id>jdk-1.8</id>
           <activation>
               <activeByDefault>true</activeByDefault>
               <jdk>1.8</jdk>
           </activation>
           <properties>
               <maven.compiler.source>1.8</maven.compiler.source>
               <maven.compiler.target>1.8</maven.compiler.target>
               <maven.compiler.compilerVersion>1.8</maven.compiler.compilerVersion>
           </properties>
       </profile>
   </profiles>
   ```

## 5. 环境验证

### 5.1 验证 JDK 安装

```bash
java -version
```

预期输出：
```
java version "1.8.0_xxx"
Java(TM) SE Runtime Environment (build 1.8.0_xxx-xxx)
Java HotSpot(TM) 64-Bit Server VM (build 25.xxx-bxx, mixed mode)
```

### 5.2 验证 Maven 安装

```bash
mvn -version
```

预期输出：
```
Apache Maven 3.6.xxx
Maven home: /usr/local/maven
Java version: 1.8.0_xxx, vendor: Oracle Corporation, runtime: /usr/local/jdk1.8/jre
Default locale: zh_CN, platform encoding: UTF-8
OS name: "linux", version: "xxx", arch: "amd64", family: "unix"
```

### 5.3 验证 Git 安装

```bash
git --version
```

预期输出：
```
git version 2.xxx.xxx
```



## 6. 项目导入与构建

### 6.1 克隆项目

```bash
git clone https://github.com/ooder/ooder-agent-rad.git
cd ooder-agent-rad
```

### 6.2 构建项目

```bash
mvn clean compile
```

预期输出：
```
[INFO] Scanning for projects...
[INFO] 
[INFO] ---------------------< net.ooder:ooder-agent-rad >----------------------
[INFO] Building ooder-agent-rad 1.0
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- maven-clean-plugin:3.2.0:clean (default-clean) @ ooder-agent-rad ---   
[INFO] 
[INFO] --- maven-resources-plugin:3.2.0:resources (default-resources) @ ooder-agent-rad ---   
[INFO] Using 'UTF-8' encoding to copy filtered resources.
[INFO] Using 'UTF-8' encoding to copy filtered properties files.
[INFO] Copying 1 resource
[INFO] 
[INFO] --- maven-compiler-plugin:3.10.1:compile (default-compile) @ ooder-agent-rad ---   
[INFO] Changes detected - recompiling the module!
[INFO] Compiling 123 source files to /path/to/ooder-agent-rad/target/classes
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  3.456 s
[INFO] Finished at: 2026-01-24T16:00:00+08:00
[INFO] ------------------------------------------------------------------------
```

## 7. 开发工具配置

### 7.1 Git 配置

#### 7.1.1 基本配置

```bash
git config --global user.name "Your Name"
git config --global user.email "your.email@example.com"
git config --global core.autocrlf false
git config --global core.safecrlf true
git config --global core.quotepath false
git config --global i18n.commit.encoding utf-8
git config --global i18n.logoutputencoding utf-8
```

## 9. 常见问题与解决方案

### 9.1 JDK 安装问题

**问题**：安装 JDK 后，执行 `java -version` 显示的是旧版本

**解决方案**：
1. 检查系统环境变量中的 PATH 配置，确保新安装的 JDK 路径在最前面
2. 检查是否有其他 JDK 版本的环境变量配置
3. 重启命令行窗口或系统

### 9.2 Maven 构建问题

**问题**：构建项目时提示依赖下载失败

**解决方案**：
1. 检查网络连接
2. 配置国内镜像源
3. 删除本地仓库中对应的依赖目录，重新构建
4. 检查 Maven 配置文件是否正确

### 9.3 IDE 导入问题

**问题**：IDE 导入项目后，无法识别 Maven 项目

**解决方案**：
1. 确保 IDE 已安装 Maven 插件
2. 重新导入 Maven 项目
3. 检查项目根目录下是否存在 pom.xml 文件
4. 检查 IDE 的 Maven 配置是否正确

### 9.4 Lombok 配置问题

**问题**：使用 Lombok 注解后，IDE 提示编译错误

**解决方案**：
1. 确保已安装 Lombok 插件
2. 启用注解处理
3. 重启 IDE
4. 检查 Lombok 版本是否与 JDK 版本兼容

## 10. 总结

通过本文档，您已经了解了 ooder-agent-rad 开发环境的搭建过程，包括系统要求、软件安装、环境配置和验证等方面。搭建好开发环境后，您就可以开始 ooder-agent-rad 的开发工作了。

如果您在环境搭建过程中遇到任何问题，可以参考本文档的常见问题与解决方案，或查阅相关软件的官方文档。