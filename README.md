<div align="center">

# 🤖 Healthy Life AI Agent

**基于 Spring AI、ReAct、Tool Calling 与 MCP 的健康生活智能体平台**

[![Java](https://img.shields.io/badge/Java-21-blue.svg?style=for-the-badge&logo=openjdk)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.9-6DB33F.svg?style=for-the-badge&logo=springboot)](https://spring.io/projects/spring-boot)
[![Build](https://img.shields.io/badge/Build-Maven-C71A22.svg?style=for-the-badge&logo=apache-maven)](https://maven.apache.org/)
[![Tech](https://img.shields.io/badge/Architecture-RAG_%7C_ReAct_%7C_MCP-success.svg?style=for-the-badge)](https://github.com/yuanyu1016/ai-agent)
[![Stars](https://img.shields.io/github/stars/yuanyu1016/ai-agent?style=for-the-badge&color=yellow)](https://github.com/yuanyu1016/ai-agent/stargazers)
[![Issues](https://img.shields.io/github/issues/yuanyu1016/ai-agent?style=for-the-badge&color=red)](https://github.com/yuanyu1016/ai-agent/issues)

[描述](#-描述) · [核心功能](#-核心功能) · [目录结构](#-目录结构) · [安装](#-安装) · [使用](#-使用) · [接口](#-接口) · [已知限制](#-已知限制)

</div>

---

## 📝 描述

**Healthy Life AI Agent** 是一个基于 Java 21、Spring Boot 和 Spring AI 构建的智能体应用，包含健康生活顾问和通用超级智能体两类对话入口，并配套 Vue 3 前端界面。

项目重点实践了以下能力：

- 使用 Spring AI `ChatClient` 实现模型对话、流式输出和多轮会话记忆。
- 使用 ReAct 循环组织“思考 → 工具调用 → 观察结果 → 下一步行动”。
- 使用 Spring AI Tool Calling 注册文件、网页、PDF、邮件和终止等工具。
- 集成 MCP 客户端能力，并提供独立的图片搜索 MCP Server 子项目。
- 预留本地 Markdown 知识库、文本切分、关键词增强和向量数据库接入能力。

仓库地址：<https://github.com/yuanyu1016/ai-agent>

> ⚠️ 本项目提供的是健康科普和生活方式辅助建议，不能替代医生诊断、治疗或紧急医疗服务。

---

## 🎯 核心功能

- **🧘 AI 健康生活顾问**：围绕身体健康、心理健康和社交健康提供多轮对话建议，并通过文件记忆保存会话上下文。
- **🤖 AI 超级智能体 YuManus**：基于 `ToolCallAgent` 和 ReAct 机制，最多执行 20 个步骤，按任务选择并组合工具完成复杂操作。
- **🛠️ Tool Calling 工具链**：支持文件读写、百度网页搜索、网页内容抓取、资源下载、PDF 生成、邮件发送和终止任务。
- **📡 SSE 流式对话**：后端提供多种同步/流式输出方式，前端通过 `EventSource` 接收实时回复。
- **🔌 MCP 集成**：根项目预留 MCP Client 配置；`image-search-mcp-server` 提供基于 Pexels API 的图片搜索工具，可通过 SSE 或 stdio 暴露给 MCP 客户端。
- **📚 RAG 实验能力**：包含健康 Markdown 文档加载、文本切分、关键词增强、内存向量库和 PgVector 配置代码。
- **🖥️ Vue 3 前端**：提供首页、健康生活顾问和超级智能体三个路由页面，使用 Axios/SSE 与后端通信。

## 🧰 技术栈

| 模块 | 技术 |
| --- | --- |
| 后端运行时 | Java 21、Spring Boot 3.4.9 |
| AI 编排 | Spring AI Alibaba 1.0.0-M6.1、Spring AI、DashScope |
| Agent | ReAct、Tool Calling、自定义 `BaseAgent` / `ToolCallAgent` |
| RAG | Spring AI Document、Markdown Reader、SimpleVectorStore、PgVector |
| 数据与记忆 | PostgreSQL、PgVector、文件型 Chat Memory、Kryo |
| MCP | Spring AI MCP Client；独立 MCP Server 使用 Spring AI MCP Server |
| 前端 | Vue 3、Vue Router、Axios、Vite、SSE |
| 其他 | Hutool、Jsoup、iText PDF、JavaMail、Knife4j |

---

## 📂 目录结构

```text
ai-agent/
├── src/main/java/com/yuanyu/aiagent/
│   ├── agent/             # Agent 抽象、ReAct 循环、Tool Calling Agent、YuManus
│   ├── advisor/           # 日志和对话增强 Advisor
│   ├── app/               # 健康生活顾问 ChatClient 与会话能力
│   ├── chatmemory/        # 基于文件的多轮会话记忆
│   ├── config/            # Web/CORS 等应用配置
│   ├── controller/        # HTTP/SSE 接口：健康检查和 AI 对话
│   ├── demo/invoke/       # 不同 AI 调用方式的示例代码
│   ├── rag/               # 文档加载、切分、关键词增强和向量库配置
│   └── tools/             # Tool Calling 工具及工具注册
├── src/main/resources/
│   ├── document/          # 身体、心理、社交健康 Markdown 知识文档
│   ├── mcp-servers.json   # MCP stdio Server 配置示例
│   ├── application.yml    # 通用配置
│   └── application-local.yml # 本地敏感配置（被 Git 忽略）
├── ai-agent-frontend/     # Vue 3 + Vite 前端
├── image-search-mcp-server/ # 独立图片搜索 MCP Server
├── Dockerfile             # 后端 Docker 构建文件
├── pom.xml                # 后端 Maven 配置
├── mvnw / mvnw.cmd        # Maven Wrapper
└── README.md
```

---

## 📚 知识库与 Agent 能力

### 健康知识文档

`src/main/resources/document/` 当前包含三类 Markdown 文档：

1. **身体健康**：运动、饮食和常见健康问题。
2. **心理健康**：情绪、压力和心理相关问题。
3. **社交健康**：人际关系和社交场景相关问题。

`AppDocumentLoader` 会按 `classpath:document/*.md` 扫描文档，并为文档添加文件名和类型元数据。

### 内置工具

当前默认注册的工具包括：

- 文件读取和写入
- 百度网页搜索
- 网页内容抓取
- URL 资源下载
- PDF 文件生成
- QQ 邮箱 SMTP 邮件发送
- 终止 Agent 执行

终端命令工具代码仍在项目中，但在 `ToolRegistration` 中默认关闭，避免让模型直接执行本地命令。

---

## ⚙️ 安装

### 环境要求

- JDK 21
- Maven 3.9+（也可以使用项目内的 Maven Wrapper）
- Node.js 16+ 和 npm 7+
- 如果启用 PgVector RAG，需要 PostgreSQL 和 pgvector 扩展
- 如果启用 MCP 图片搜索，需要 Pexels API Key

### 获取项目

```bash
git clone https://github.com/yuanyu1016/ai-agent.git
cd ai-agent
```

### 配置本地参数

项目默认激活 `local` profile。请创建或修改 `src/main/resources/application-local.yml`，该文件已被 `.gitignore` 忽略，不应提交真实密钥。

常用配置项包括：

```yaml
spring:
  ai:
    dashscope:
      api-key: your_dashscope_api_key
  datasource:
    url: jdbc:postgresql://localhost:5432/ai_agent
    username: your_postgres_user
    password: your_postgres_password

search-api:
  api-key: your_search_api_key

email:
  from-email: your_email@example.com
  auth-code: your_smtp_auth_code
```

只有在启用对应功能时才需要配置 PostgreSQL、搜索 API 或邮箱参数。不要将 API Key、数据库密码或邮箱授权码写入 Git。

### 安装依赖

后端：

```bash
# Linux/macOS
./mvnw clean package

# Windows
./mvnw.cmd clean package
```

前端：

```bash
cd ai-agent-frontend
npm install
```

---

## 🚀 使用

### 启动后端

```bash
# Linux/macOS
./mvnw spring-boot:run

# Windows
./mvnw.cmd spring-boot:run
```

后端默认监听 `8123` 端口，并使用 `/api` 作为上下文路径：

```text
健康检查：http://localhost:8123/api/health
Knife4j：http://localhost:8123/api/doc.html
```

也可以运行打包后的 JAR：

```bash
java -jar target/ai-agent-0.0.1-SNAPSHOT.jar
```

### 启动前端

另开终端：

```bash
cd ai-agent-frontend
npm run dev
```

前端包含以下页面：

- `/`：平台首页
- `/master`：AI 健康生活顾问
- `/super-agent`：AI 超级智能体

前端开发环境默认请求 `http://localhost:8123/api`，生产构建使用相对路径 `/api`。

### 启动图片搜索 MCP Server

MCP 子项目位于 `image-search-mcp-server/`，默认使用 SSE 配置，监听 `8127` 端口。先配置 `IMAGE_SEARCH_API_KEY` 环境变量，再运行：

```bash
cd image-search-mcp-server

# Windows
./mvnw.cmd spring-boot:run

# Linux/macOS
./mvnw spring-boot:run
```

stdio 模式可使用：

```bash
./mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=stdio
```

根项目的 `src/main/resources/application.yml` 中 MCP Client 配置目前是注释状态；如需让主 Agent 调用 MCP Server，需要根据本地环境启用并完善对应配置。

---

## 🔌 接口

所有后端 HTTP 接口默认带 `/api` 前缀：

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| GET | `/api/health` | 健康检查 |
| GET | `/api/ai/app/chat/sync` | 健康生活顾问同步回复 |
| GET | `/api/ai/app/chat/sse` | 健康生活顾问 SSE 流式回复 |
| GET | `/api/ai/app/chat/sse/server_sent_event` | `ServerSentEvent` 类型流式回复 |
| GET | `/api/ai/app/chat/sse/emitter` | `SseEmitter` 类型流式回复 |
| GET | `/api/ai/manus/chat` | YuManus 超级智能体 SSE 回复 |

聊天接口使用查询参数：

```text
message=用户问题
chatId=会话 ID（健康生活顾问接口使用）
```

例如：

```text
http://localhost:8123/api/ai/app/chat/sse?message=如何安排一周运动计划&chatId=demo-001
```

---

## 🧪 测试

运行后端测试：

```bash
./mvnw test
```

Windows：

```bash
./mvnw.cmd test
```

部分测试会访问外部搜索、Redis/数据库或模型相关配置。执行完整测试前，请确认本地 profile 已准备好对应依赖；不具备外部服务时，可先运行不依赖外部服务的单元测试。

前端构建校验：

```bash
cd ai-agent-frontend
npm run build
```

---

## ⚠️ 已知限制

- RAG 相关向量库 Bean 当前默认未启用：`AppVectorStoreConfig`、`PgVectorVectorStoreConfig` 中的 Bean 方法以及 `App` 中的 RAG Advisor 均处于注释/实验状态，不能把当前应用描述为默认已启用 RAG。
- 根项目的 MCP Client 配置目前主要是注释示例；`image-search-mcp-server` 可以独立运行，但主 Agent 是否连接它取决于本地 MCP 配置。
- `application-local.yml` 未纳入版本控制，需要每个开发环境自行创建。
- 文件、终端和邮件工具会产生本地文件或外部副作用，接入真实模型前应限制可访问目录、命令和收件人范围。
- 生产 Dockerfile只构建后端；前端需要单独使用 `ai-agent-frontend/Dockerfile` 构建并通过 Nginx 提供静态文件。
- 当前仓库未发现 `LICENSE` 文件，因此 README 不声明具体开源许可证。

---

## 🤝 贡献

1. Fork 本仓库。
2. 创建特性分支：`git checkout -b feature/new-tool`。
3. 完成修改并运行相关测试。
4. 提交更改：`git commit -m "Add new tool"`。
5. 推送分支并创建 Pull Request。

提交代码时请勿包含本地密钥、构建产物、`tmp/`、`npm_cache/` 或 `node_modules/`。

---

## 👨‍💻 作者

**何元玉 (@yuanyu1016)**

- GitHub：<https://github.com/yuanyu1016>
- 方向：AI 应用开发、Java 研发和复杂系统架构设计

---

<p align="center">
  <i>如果这个 AI Agent 项目对你的开发或学习有帮助，欢迎点一个 ⭐ 支持一下！</i>
</p>
