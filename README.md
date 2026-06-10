# ai-web

`ai-web` 是一个从 0 开始搭建的 GPT 风格 AI 聊天网页项目。项目目标是实现一个前后端分离的聊天系统：用户可以注册登录、创建会话、发送消息，并通过流式输出实时看到 AI 回复。

当前仓库处于基础搭建阶段，已经包含 Spring Boot 后端骨架和项目实现计划。完整功能会继续围绕登录、会话管理、消息存储和模型流式输出逐步补齐。

## 技术栈

后端：

- Java 17
- Spring Boot 2.7.18
- Spring MVC
- Spring Security
- JWT
- Spring WebClient
- Spring Data JPA
- MySQL
- Maven

前端规划：

- React
- Vite
- TypeScript
- SSE 流式消息接收

AI 接口：

- 后端通过 `WebClient` 请求 OpenAI 兼容接口
- 前端不直接接触 API Key
- 后端通过 `SseEmitter` 向浏览器输出流式回复

## 项目结构

```text
ai-web/
  backend/                  Spring Boot 2.7 后端
  docs/                     项目设计和实现计划
  sql/                      MySQL 建表脚本，后续补充
  frontend/                 React + Vite 前端，后续补充
  README.md                 项目介绍和部署说明
```

当前已存在的核心文件：

```text
backend/
  pom.xml
  src/main/resources/application.yml
  src/main/java/com/example/gpt/GptApplication.java
  src/main/java/com/example/gpt/config/

docs/
  implementation-plan.md
```

## 核心功能规划

- 用户注册和登录
- JWT 鉴权
- 获取当前登录用户
- 创建、查询、重命名、删除聊天会话
- 保存用户消息和 AI 回复
- 使用 WebClient 调用 AI 模型接口
- 使用 SSE 实现前端流式显示
- 用户只能访问自己的聊天记录

## 后端配置

后端配置位于：

```text
backend/src/main/resources/application.yml
```

运行前需要准备以下环境变量：

```bash
SERVER_PORT=8080

MYSQL_URL=jdbc:mysql://localhost:3306/gpt_mirror?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
MYSQL_USERNAME=root
MYSQL_PASSWORD=root

JWT_SECRET=please-change-this-secret-to-at-least-32-characters
JWT_EXPIRATION_MINUTES=1440

OPENAI_API_KEY=your_openai_api_key
OPENAI_BASE_URL=https://api.openai.com/v1
OPENAI_MODEL=gpt-4.1-mini
```

注意：`OPENAI_API_KEY` 不应该写死在代码里，也不要提交到 GitHub。

## 本地开发部署

### 1. 克隆项目

```bash
git clone https://github.com/zxw6/ai-web.git
cd ai-web
```

### 2. 准备 MySQL

创建数据库：

```sql
CREATE DATABASE gpt_mirror DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

后续项目会在 `sql/` 目录补充完整建表脚本。第一版核心表包括：

- `users`
- `conversations`
- `messages`

### 3. 启动后端

进入后端目录：

```bash
cd backend
```

启动项目：

```bash
mvn spring-boot:run
```

默认后端地址：

```text
http://localhost:8080
```

### 4. 启动前端

前端目录会在后续开发中补充。规划启动方式如下：

```bash
cd frontend
npm install
npm run dev
```

默认前端地址：

```text
http://localhost:5173
```

## 生产部署思路

推荐部署结构：

```text
Browser
  -> Nginx
    -> React 静态文件
    -> /api 反向代理到 Spring Boot
  -> MySQL
  -> OpenAI 兼容模型接口
```

部署步骤：

1. 使用 `npm run build` 构建前端静态文件。
2. 使用 `mvn clean package` 构建后端 Jar。
3. 在服务器上准备 MySQL 数据库。
4. 配置后端环境变量，尤其是数据库连接、JWT 密钥和 OpenAI API Key。
5. 使用 `java -jar`、systemd 或 Docker 启动后端。
6. 使用 Nginx 托管前端静态文件，并将 `/api` 代理到后端服务。

后端 Jar 启动示例：

```bash
java -jar backend/target/gpt-backend-0.0.1-SNAPSHOT.jar
```

Nginx 代理示例：

```nginx
location /api/ {
    proxy_pass http://127.0.0.1:8080/api/;
    proxy_set_header Host $host;
    proxy_set_header X-Real-IP $remote_addr;
    proxy_set_header Authorization $http_authorization;
}
```

SSE 流式接口部署时需要关闭代理缓冲：

```nginx
proxy_buffering off;
proxy_cache off;
```

## 文档

详细实现计划见：

```text
docs/implementation-plan.md
```

## 当前进度

- 已完成：Git 仓库初始化
- 已完成：Spring Boot 2.7 后端基础骨架
- 已完成：JWT、OpenAI、CORS 等基础配置类
- 已完成：项目实现计划文档
- 待完成：用户实体、登录接口、JWT 过滤器
- 待完成：会话和消息接口
- 待完成：OpenAI WebClient 流式调用
- 待完成：React 前端页面
- 待完成：MySQL 建表脚本

## 安全说明

- 不要提交 `.env`、数据库密码、JWT 密钥或 OpenAI API Key。
- 密码只保存 BCrypt 哈希，不保存明文。
- 所有聊天记录查询都必须基于当前登录用户过滤。
- 前端只保存 JWT，不保存模型接口密钥。
