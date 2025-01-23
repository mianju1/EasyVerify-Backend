# 伊泽网络验证管理系统 (EasyVerify-Backend)

![License](https://img.shields.io/badge/License-MIT-blue.svg)![GitHub Repo stars](https://img.shields.io/github/stars/mianju1/EasyVerify-Backend?style=social)



## 介绍

伊泽网络验证管理系统是一个为开发者设计的后端系统，旨在通过HTTP协议提供账号、注册码、激活码验证等功能。开发者可以通过API轻松集成验证功能，并管理软件的相关信息及用户控制。

**完整使用请配合 [EasyVerify-Web](https://github.com/mianju1/EasyVerify-Web/) 项目使用**



## 主要功能

- **账号验证**：验证用户账号的有效性。
- **注册码验证**：验证用户提供的注册码是否有效。
- **激活码验证**：验证用户提供的激活码是否有效。
- **软件信息管理**：开发者可以管理创建的软件相关信息。
- **用户控制**：提供对使用用户的管理功能。



## 安装与运行

### 前提条件

- Docker
- Docker Compose



### 安装步骤

**使用 docker-compose 进行安装**

```yaml
services:
  easyverify-backend:
    image: ibm-semeru-runtimes:open-17.0.13_11-jre
    container_name: easyverify-backend
    working_dir: /app
    logging:
      driver: "json-file"  # 使用 json-file 日志驱动
      options:
        max-size: "100m"    # 单个日志文件最大 10MB
        max-file: "5"      # 最多保留 3 个日志文件
    environment:
      - PORT=10086 # 端口号
      # ... 按照下面配置要求填写环境变量
      
    ports:
      - "10086:10086" # API项目映射端口
    restart: always
    volumes:
      - xxx:/app/easyverify.jar # xxx为打包好的jar包路径
    entrypoint: ["java", "-jar", "easyverify.jar"]

# 请配合 Easyverify-web 项目进行使用
  easyverify-web:
    image: node:22.13.0
    container_name: easyverify-web
    working_dir: /app
    environment:
      - NODE_ENV=production
      - TZ=Asia/Shanghai
    ports:
      - "2121:2121" # 前端项目映射端口
    volumes:
      - xxx:/app # xxx为前端项目路径
    restart: always
    command: >
      sh -c "npm config set registry https://registry.npmmirror.com && npm install && npm run build && npm run preview"
    depends_on:
      - easyverify-backend
```



#### 环境变量配置

以下环境变量需要在运行项目前配置，以确保系统正常运行：

```bash
# 服务端口号
- PORT=10086

# 邮件服务配置
- MAIL_HOST=your_mail_host          # 邮件服务器地址（例如：smtp.163.com）
- MAIL_USERNAME=your_mail_username  # 邮件账号
- MAIL_PASSWORD=your_mail_password  # 邮件账号密码或授权码

# RabbitMQ 消息队列配置
- RABBITMQ_HOST=your_rabbitmq_host     # RabbitMQ 服务器地址
- RABBITMQ_PORT=your_rabbitmq_port     # RabbitMQ 端口
- RABBITMQ_USERNAME=your_rabbitmq_username # RabbitMQ 用户名
- RABBITMQ_PASSWORD=your_rabbitmq_password # RabbitMQ 密码

# Redis 配置
- REDIS_HOST=your_redis_host        # Redis 服务器地址
- REDIS_PORT=your_redis_port        # Redis 端口
- REDIS_PASSWORD=your_redis_password # Redis 密码

# 数据库配置
- DB_HOST=your_db_host              # 数据库服务器地址
- DB_PORT=your_db_port              # 数据库端口
- DB_NAME=your_db_name              # 数据库名称
- DB_USER=your_db_user              # 数据库用户名
- DB_PASSWORD=your_db_password      # 数据库密码

# JWT 鉴权配置
- JWT_KEY=your_jwt_key              # JWT 密钥（建议使用高强度随机字符串）
- JWT_EXPIRE=your_jwt_expire        # JWT 过期时间（单位：小时）

# 邮件发送频率限制
- MAIL_LIMIT=your_mail_limit        # 邮件发送频率限制（单位：秒）

# 流量限制配置
- FLOW_LIMIT_TIME=your_flow_limit_time      # 流量限制时间窗口（单位：秒）
- FLOW_LIMIT_COUNT=your_flow_limit_count    # 流量限制请求次数
- FLOW_LIMIT_PERIOD=your_flow_limit_period  # 流量限制周期（单位：秒）

# 时区配置
- TZ=your_timezone                  # 系统时区（例如：Asia/Shanghai）
```

#### 配置说明

1. **邮件服务**：确保 `MAIL_PASSWORD` 是有效的授权码，而不是邮箱的登录密码。
2. **RabbitMQ**：确保 RabbitMQ 服务已启动，并且配置的用户名和密码正确。
3. **Redis**：确保 Redis 服务已启动，并且配置的密码正确。
4. **数据库**：确保数据库服务已启动，并且配置的用户名和密码正确。
5. **JWT**：`JWT_KEY` 是用于生成和验证 Token 的密钥，请妥善保管。
6. **流量限制**：根据实际需求调整流量限制参数，防止恶意请求。
7. **时区**：`TZ` 设置为系统时区，确保系统时间与本地时间一致。



#### 启动项目：

```shell
docker-compose up -d
```



1. 服务启动后，后端项目 将默认运行在 `http://localhost:10086`（具体端口请参考 `docker-compose.yml` 配置 `Easyverify-backend` 中）。
2. 前端项目默认运行在`http://localhost:2121`（具体端口请参考 `docker-compose.yml` 配置 `Easyverify-web` 中）。



## 报告问题

如果你在使用过程中遇到任何问题，请在 [Issues](https://github.com/mianju1/EasyVerify-Backend/issues) 页面提交问题。



## 许可证

本项目采用 [MIT 许可证](https://github.com/mianju1/EasyVerify-Backend/blob/master/LICENSE)。







