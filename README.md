# yapi-cooler
yapi接口管理平台定时任务备份工具。使用http请求的形式，对内网部署的yapi平台进行定时任务备份。

### 使用方法（依赖java8环境）

**一、通过jar包运行**

1.1 点击[release](https://github.com/tanzzj/yapi-cooler/releases "release")，下载最新的jar包到本地

1.2 进入jar包修改 src/main/resources/application.yml 并保存
```yaml
#corn表达式定时任务，默认一分钟备份一次，生产推荐一天备份一次
cooler:
  schedule: "0/59 * * * * ? "
yapi:
  #输入yapi域名地址
  host: http://192.168.0.100
  #备份文件的输出路径
  outputPath: /main/yapi
  #登录yapi的账户与密码（推荐使用admin或同等级的账户以备份完全数据）
  adminUsername:
  adminPassword:
http:
  connectTimeout: 60000

#日志打印级别以及输出路径
logging:
  level:
    root: info
  file: /main/yapi/logs/yapi-cooler.log
```
1.3 运行java -jar yapi-cooler.jar
1.4 采用外部配置文件运行 java jar yapi-cooler.jar -spring.config.location=application.yml

**二、下载源码打包（依赖maven） **

2.1 执行 git clone https://github.com/tanzzj/yapi-cooler.git

2.2 修改 src/main/resources/application.yml 并保存，配置修改如上

2.3 执行mvn clean package -Dmaven.skip.test=true

2.4 进入/target，后同1.3

### 环境要求
- jdk 1.8+
- maven




