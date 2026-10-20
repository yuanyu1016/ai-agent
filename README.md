<div align="center">

# 🤖 Healthy Life AI Agent

**基于先进 Agent 架构 (ReAct/CoT/MCP) 的全方位健康生活智能体**

[![Java](https://img.shields.io/badge/Language-Java_100%25-blue.svg?style=for-the-badge&logo=java)](https://github.com/yuanyu-1016/ai-agent)
[![Maven](https://img.shields.io/badge/Build-Maven-C71A22.svg?style=for-the-badge&logo=apache-maven)](https://github.com/yuanyu-1016/ai-agent)
[![Tech](https://img.shields.io/badge/Architecture-RAG_%7C_ReAct_%7C_MCP-success.svg?style=for-the-badge)](https://github.com/yuanyu-1016/ai-agent)
[![Stars](https://img.shields.io/github/stars/yuanyu-1016/ai-agent?style=for-the-badge&color=yellow)](https://github.com/yuanyu-1016/ai-agent/stargazers)
[![Issues](https://img.shields.io/github/issues/yuanyu-1016/ai-agent?style=for-the-badge&color=red)](https://github.com/yuanyu-1016/ai-agent/issues)
[![License](https://img.shields.io/github/license/yuanyu-1016/ai-agent?style=for-the-badge&color=blue)](https://github.com/yuanyu-1016/ai-agent/blob/master/LICENSE)

[描述](#-描述) • [核心功能](#-核心功能) • [目录结构](#-目录结构) • [知识库覆盖](#-知识库覆盖) • [安装](#-安装) • [使用](#-使用) • [贡献](#-贡献) • [作者](#-作者)

</div>

---

## 📝 描述

**Healthy Life AI Agent** 是一个基于纯 Java 栈构建的高阶健康生活智能体应用，并自带开箱即用的前端交互界面。

本项目超越了传统的单轮问答机器人，深度集成了 **RAG (检索增强生成)** 与现代 Agent 编排模式。通过引入 **CoT (Chain of Thought, 思维链)** 和 **ReAct (Reasoning and Acting)** 框架，智能体能够像人类一样“先思考、后行动”。此外，项目拥抱了最新的 **MCP (Model Context Protocol)** 标准与 **Tool Calling (工具调用)** 能力，使其能够动态调用外部健康计算工具或实时检索最新医学资讯，为用户提供科学、严谨、个性化的全方位健康指导。

---

## 🎯 核心功能

- **🧠 深度推理与行动 (ReAct & CoT)**：
  摒弃简单的 Prompt 回答，引擎内部采用 ReAct 模式循环（思考 -> 观察 -> 行动）。结合 CoT 思维链设计，强迫大模型在给出健康建议前，输出完整的诊断逻辑与推理步骤，极大提升了建议的安全性和准确性。
- **🛠️ 动态工具调用 (Tool Calling)**：
  智能体不再受限于预训练数据，可通过 Tool Calling 动态调用外部 Java 方法。例如：调用 `BMICalculator` 计算体脂率，调用 `WeatherAPI` 结合当地天气给出当日运动建议。
- **🔌 标准化模型上下文协议 (MCP)**：
  拥抱 MCP 架构，实现工具、上下文与提示词的标准化解耦，轻松对接不同的 LLM 供应商，提升了系统扩展外部数据源和工具链的敏捷性。
- **📖 智能 RAG 架构**：
  内置向量数据库对接能力，实现对本地/云端健康知识文档的高效分块、向量化与语义检索，精准召回专业健康资料，消除 AI 幻觉。
- **🖥️ 开箱即用的可视化界面**：
  项目内含完善的前端 UI 代码，提供类似 ChatGPT 的对话交互体验，并能直观展示 Agent 的思考过程与工具调用状态。

---

## 📂 目录结构

本项目的代码结构清晰分离了 Agent 核心引擎、前端界面、工具链与 RAG 模块，前后端协同，便于全栈扩展与维护：

```text
ai-agent/
├── frontend/           # 🌐 前端 UI 目录：包含用户交互界面源码，提供聊天对话、思考过程展示等视图
├── src/main/java/com/yuanyu/agent/
│   ├── core/           # 核心引擎层：包含 ReAct 循环调度器、状态机与 CoT 提示词编排器
│   ├── mcp/            # MCP 协议层：Model Context Protocol 的 Java 实现，标准化资源与工具的注册发现
│   ├── tools/          # Tool Calling 工具库：定义智能体可调用的外部能力 (如联网搜索、BMI计算等)
│   ├── rag/            # RAG 模块：文档解析、文本切割、向量存储与检索逻辑
│   ├── domain/         # 业务领域层：处理身体、心理、社交等健康知识的具体业务逻辑
│   └── AgentApplication.java # Spring Boot 主启动类
├── src/main/resources/
│   ├── prompts/        # 集中管理的 Prompt 模板库 (System Prompts, CoT 模板, ReAct 模板)
│   └── application.yml # 全局配置文件 (大模型 API 密钥、向量数据库连接配置)
└── pom.xml             # Maven 依赖与构建配置
```

---

## 📚 知识库覆盖 (RAG 领域)

目前的 RAG 知识库与工具链主要涵盖以下三大健康维度：

1. **🏃 身体健康 (Physical Health)**
   - 运动科学、健身计划推荐及慢病（如三高）膳食管理。
   - 结合实时天气工具给出出行与锻炼建议。
2. **🧘 心理健康 (Mental Well-being)**
   - 压力管理、情绪调节、常见心理困扰科普与睡眠质量优化。
3. **🤝 社交健康 (Social Wellness)**
   - 人际关系处理、职场社交边界感建立与积极社交圈层建立建议。

---

## ⚙️ 安装

### 1. 环境准备
- **后端**: JDK 1.8+ (推荐 JDK 17), Maven 3.6+
- **前端**: Node.js 16+

### 2. 获取项目
```bash
git clone [https://github.com/yuanyu-1016/ai-agent.git](https://github.com/yuanyu-1016/ai-agent.git)
cd ai-agent
```

### 3. 构建与安装
**后端依赖：**
```bash
mvn clean install
```
**前端依赖：**
```bash
cd frontend
npm install
```

---

## 🚀 使用

1. **配置大模型与向量库**：
   打开 `src/main/resources/application.yml`，填入您的 LLM API Key（如 OpenAI / 智谱 等）以及向量数据库连接信息。
2. **启动后端 Agent 服务**：
   退回项目根目录，通过 Maven 启动后端：
   ```bash
   mvn spring-boot:run
   ```
3. **启动前端 UI 服务**：
   新开一个终端窗口，进入前端目录并启动开发服务器：
   ```bash
   cd frontend
   npm run dev
   ```
4. **开始对话**：
   打开浏览器访问前端控制台输出的本地地址（通常为 `http://localhost:5173` 或 `3000`），即可开始与您的专属健康 AI 助手进行对话，并在界面中直观查看思维链 (CoT) 的推理过程！

---

## 🤝 贡献

健康是一个宏大且持续发展的领域，我们非常欢迎开发者、医学科普工作者加入！

1. Fork 本仓库。
2. 创建特性分支：`git checkout -b feature/NewHealthTool`
3. 提交更改：`git commit -m 'Add new MCP tool for diet tracking'`
4. 推送分支：`git push origin feature/NewHealthTool`
5. 提交 Pull Request。

---

## 👨‍💻 作者

**何元玉 (@yuanyu-1016)**

- **GitHub**: [https://github.com/yuanyu-1016](https://github.com/yuanyu-1016)
- **专注领域**: AI 应用开发、Java 研发、复杂系统架构设计

---

## 📄 认证

本项目采用 [MIT License](https://opensource.org/licenses/MIT) 进行开源认证。详细信息请参阅 `LICENSE` 文件。

> **⚠️ 医疗免责声明**：本 AI 智能体及其产生的推理结果仅作为健康科普与生活方式优化的辅助参考，**不能**替代专业医生的诊断与治疗。遇到实质性躯体或严重心理疾病，请务必及时就医。

---
<p align="center">
  <i>如果这个 AI Agent 项目对您的开发有启发，请给一个 ⭐️ 鼓励一下！</i>
</p>
