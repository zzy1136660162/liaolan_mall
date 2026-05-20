# Codex多分支自动开发与验证方案

生成日期：2026-05-08  
适用项目：`E:\ZZY_PROJECT\crmeb_java`

## 1. 文档目标

本方案用于指导如何让 Codex 在当前项目中，对多个 Git 功能分支进行相对自动化的开发、验证与合并控制，同时解决以下问题：

- 多个模块并行开发时，AI 上下文长度不够
- 一个会话跨多个模块开发，容易串上下文
- 切分支开发时容易改错文件、提错分支
- AI 只会“说已经完成”，但没有可靠验证证据
- 中途会话中断后，后续无法低成本恢复
- 多模块开发后无法判断哪些分支已经达到可合并标准

本文件不是 AI 产品介绍，而是本项目的实际执行方案。

## 2. 适用分支范围

当前项目分支：

- `develop`
- `feature/mall-dev1`
- `feature/case-dev2`
- `feature/news-dev3`
- `feature/download-dev4`
- `feature/about-dev5`
- `feature/contact-dev6`

对应模块建议：

- `feature/mall-dev1`：产品商城模块
- `feature/case-dev2`：客户案例模块
- `feature/news-dev3`：公司新闻模块
- `feature/download-dev4`：资料下载模块
- `feature/about-dev5`：关于辽缆模块
- `feature/contact-dev6`：联系我们模块

## 3. 总体原则

Codex 多分支开发必须遵守以下原则：

1. 一个执行会话只负责一个模块分支
2. 一个模块开发过程拆成多个短阶段执行
3. 每个模块使用独立工作目录，不在同一个目录里来回切分支
4. 每个模块必须有中间状态文件，不依赖模型长记忆
5. 每个模块必须有验证报告，不允许只口头说明“已完成”
6. 所有合并动作必须以验证结果为依据，而不是以 AI 自我判断为依据

## 4. 为什么不能让一个 Codex 会话直接做完全部模块

如果让一个 Codex 会话连续开发 6 个模块，通常会出现以下问题：

- 前一个模块上下文污染后一个模块
- 忘记当前处于哪个分支
- 忘记哪些文件已改、哪些文件禁止改
- 验证结论和代码归属混乱
- 一旦会话压缩，早期关键细节被丢失
- 后续补改时，模型会用错误假设继续修改

因此，本项目不建议：

- 一个会话同时负责全部模块
- 一个会话长期保存所有模块细节
- 在一个目录内频繁 `checkout` 不同模块分支来回开发

## 5. 推荐架构

推荐采用“三层结构”：

### 5.1 主控会话

职责：

- 负责分配模块任务
- 负责选择执行顺序
- 负责检查模块状态文件和验证报告
- 负责决定是否允许进入 `develop`

主控会话不负责长期写所有模块代码。

### 5.2 模块执行会话

职责：

- 只负责一个模块
- 只操作一个功能分支
- 只修改允许范围内的文件
- 输出中间状态文件和验证报告

示例：

- 商城模块会话只负责 `feature/mall-dev1`
- 新闻模块会话只负责 `feature/news-dev3`

### 5.3 验证层

职责：

- 编译验证
- 接口 smoke test
- 页面打开和关键流程验证
- 风险归档

验证层可以由主控会话触发，也可以由模块执行会话按统一模板执行。

## 6. 用 `git worktree` 做物理隔离

## 6.1 为什么必须用 worktree

多人模块并行开发场景下，不建议只在一个目录里切换分支。  
更稳的方式是给每个分支单独目录。

这样可以避免：

- 改错分支
- 未提交代码切换分支导致混乱
- 不同模块文件状态相互干扰
- 主控会话与执行会话相互污染

## 6.2 推荐目录结构

建议在 `E:\ZZY_PROJECT` 下创建以下工作目录：

- `E:\ZZY_PROJECT\wt-develop`
- `E:\ZZY_PROJECT\wt-mall`
- `E:\ZZY_PROJECT\wt-case`
- `E:\ZZY_PROJECT\wt-news`
- `E:\ZZY_PROJECT\wt-download`
- `E:\ZZY_PROJECT\wt-about`
- `E:\ZZY_PROJECT\wt-contact`

## 6.3 创建命令

在仓库根目录执行：

```bash
git worktree add ..\wt-develop develop
git worktree add ..\wt-mall feature/mall-dev1
git worktree add ..\wt-case feature/case-dev2
git worktree add ..\wt-news feature/news-dev3
git worktree add ..\wt-download feature/download-dev4
git worktree add ..\wt-about feature/about-dev5
git worktree add ..\wt-contact feature/contact-dev6
```

## 6.4 管理命令

查看 worktree：

```bash
git worktree list
```

删除已不用的 worktree：

```bash
git worktree remove ..\wt-news
```

## 7. 每个模块的固定输入上下文

每个 Codex 模块执行会话，不应读取整个项目所有文档，而应只加载最小必要上下文。

建议固定输入包括：

1. 当前模块任务书
2. UI 统一规范
3. Git 使用规范
4. 当前模块代码相关目录
5. 明确的允许修改范围
6. 明确的验证命令

示例：

### 商城模块固定上下文

- [01-产品商城模块任务书.md](E:\ZZY_PROJECT\crmeb_java\docs\01-产品商城模块任务书.md)
- [00-UI设计Token风格指南.md](E:\ZZY_PROJECT\crmeb_java\docs\00-UI设计Token风格指南.md)
- [07-Git分支使用与IDEA操作指南.md](E:\ZZY_PROJECT\crmeb_java\docs\07-Git分支使用与IDEA操作指南.md)

### 新闻模块固定上下文

- [03-公司新闻模块任务书.md](E:\ZZY_PROJECT\crmeb_java\docs\03-公司新闻模块任务书.md)
- [00-UI设计Token风格指南.md](E:\ZZY_PROJECT\crmeb_java\docs\00-UI设计Token风格指南.md)
- [07-Git分支使用与IDEA操作指南.md](E:\ZZY_PROJECT\crmeb_java\docs\07-Git分支使用与IDEA操作指南.md)

## 8. 上下文长度控制方案

## 8.1 一个模块拆成四个阶段

每个模块不要一口气做完，应拆成以下 4 个阶段：

### 阶段 A：代码定位与方案确认

只做：

- 读任务书
- 读现有代码
- 确认修改文件
- 确认验证入口

输出：

- 模块状态文件初稿

### 阶段 B：编码实现

只做：

- 按范围编码
- 不处理其他模块问题
- 不随意改全局结构

输出：

- 已修改文件列表
- 本阶段完成点

### 阶段 C：验证

只做：

- 执行编译
- 执行接口验证
- 执行页面 smoke test
- 记录失败项

输出：

- 验证报告

### 阶段 D：收口与提交

只做：

- 修复验证失败项
- 更新状态文件
- 生成提交说明
- 推送到远程

## 8.2 为什么要阶段化

原因：

- 降低单次上下文长度
- 让模型每次只关注一个问题面
- 中断后容易恢复
- 更容易发现“编码已完成但验证未完成”的情况

## 8.3 单次会话时长控制

建议：

- 一个会话只做一个阶段
- 单个阶段控制在 30 到 45 分钟逻辑量
- 阶段结束必须先落状态文件，再进入下一轮

## 9. 中间状态文件机制

## 9.1 必须生成的状态文件

建议在仓库中增加目录：

- `docs/agent-status`
- `docs/verification`

每个模块至少生成：

- `docs/agent-status/mall.md`
- `docs/agent-status/case.md`
- `docs/agent-status/news.md`
- `docs/agent-status/download.md`
- `docs/agent-status/about.md`
- `docs/agent-status/contact.md`

## 9.2 状态文件固定结构

每个状态文件建议包含：

```markdown
# 模块状态

- 当前分支：
- 当前工作目录：
- 当前阶段：
- 已修改文件：
- 已完成功能：
- 未完成功能：
- 已知风险：
- 下次继续前必须检查：
- 推荐下一步命令：
```

## 9.3 作用

状态文件用于解决：

- 会话压缩后记忆丢失
- 不同 Codex 会话接力
- 负责人检查进展
- 后续验证和合并追踪

## 10. 验证报告机制

## 10.1 验证报告文件

每个模块必须生成一份验证报告：

- `docs/verification/mall-verification.md`
- `docs/verification/case-verification.md`
- `docs/verification/news-verification.md`
- `docs/verification/download-verification.md`
- `docs/verification/about-verification.md`
- `docs/verification/contact-verification.md`

## 10.2 验证报告固定结构

建议模板：

```markdown
# 模块验证报告

- 模块名称：
- 当前分支：
- 当前提交：
- 验证时间：

## 执行命令

-
-
-

## 验证结果

- 编译是否通过：
- 接口是否通过：
- 页面是否通过：
- 控制台是否报错：

## 失败项

- 无 / 具体问题列表

## 结论

- 可合并 develop / 不可合并 develop
```

## 10.3 验证结论规则

如果出现以下任一情况，不允许进入 `develop`：

- 编译失败
- 核心页面打不开
- 核心接口报错
- 改动范围越界
- 修改了不允许改的全局文件但未说明原因
- 状态文件和验证报告缺失

## 11. 验证维度设计

每个模块至少做四类验证：

### 11.1 编译验证

后端相关模块至少验证：

```bash
mvn -pl crmeb -am package -DskipTests
```

前端后台相关模块至少验证：

```bash
cd admin
npm install
npm run build
```

如涉及 UniApp/H5，还应补充对应前端构建或运行验证。

### 11.2 接口验证

至少验证：

- 列表接口
- 详情接口
- 新增接口
- 编辑接口
- 状态修改接口

验证方式可选：

- `curl` 命令行验证
- `fetch` 脚本验证
- Postman / Apifox / 浏览器开发者工具等可用工具

执行要求：

- 优先使用可复现的命令行或脚本方式验证，首选 `curl`
- 如果接口需要复杂 Header、Token、Cookie，也应尽量整理成可重复执行的 `curl` 命令
- 不允许只写“接口已测通过”而不保留验证命令
- 验证报告中必须记录实际执行过的请求命令或脚本摘要

推荐示例：

```bash
curl --location 'http://127.0.0.1:20510/api/front/sendCode' \
  --header 'Content-Type: application/x-www-form-urlencoded' \
  --data-urlencode 'phone=18840016119'
```

```bash
curl --location 'http://127.0.0.1:20500/api/admin/login' \
  --header 'Content-Type: application/json' \
  --data '{"account":"admin","pwd":"admin123"}'
```

### 11.3 页面验证

至少验证：

- 路由可进入
- 列表可展示
- 表单可提交
- 空状态与异常提示正常
- UI 风格符合 Token 规范

### 11.4 回归验证

至少确认未破坏：

- 登录
- 上传
- 基础列表页
- 基础详情页
- 公共富文本或附件能力

## 12. 模块执行顺序建议

不建议一开始 6 个模块全部并发。  
建议分两批执行：

### 第一批：低耦合内容模块

- `feature/case-dev2`
- `feature/news-dev3`
- `feature/about-dev5`
- `feature/contact-dev6`

用途：

- 先验证 Codex 多分支协作流程是否顺畅
- 先跑通状态文件、验证报告、合并流程

### 第二批：高耦合业务模块

- `feature/download-dev4`
- `feature/mall-dev1`

原因：

- 更可能涉及上传、权限、富文本、前后端联调
- 更容易影响公共模块

## 13. 主控会话职责清单

主控会话只做以下事情：

1. 指定当前要推进的模块
2. 明确当前模块的工作目录和分支
3. 下发固定输入文档
4. 检查状态文件是否更新
5. 检查验证报告是否完整
6. 判断是否允许合并到 `develop`

主控会话不要负责：

- 长时间记住 6 个模块的全部细节
- 在一个会话中来回改多个模块
- 无验证证据就判定“完成”

## 14. 模块执行会话标准 Prompt 模板

建议每个模块都使用类似输入：

```text
你只负责 feature/mall-dev1 分支。
工作目录只允许在 E:\ZZY_PROJECT\wt-mall。

严格依据以下文档执行：
1. E:\ZZY_PROJECT\crmeb_java\docs\01-产品商城模块任务书.md
2. E:\ZZY_PROJECT\crmeb_java\docs\00-UI设计Token风格指南.md
3. E:\ZZY_PROJECT\crmeb_java\docs\07-Git分支使用与IDEA操作指南.md

要求：
- 只修改商城模块相关文件
- 不修改其他模块
- 每完成一个阶段更新 docs/agent-status/mall.md
- 完成验证后输出 docs/verification/mall-verification.md
- 最终输出：
  1. 修改文件列表
  2. 执行过的验证命令
  3. 失败项
  4. 是否可合并 develop
```

## 15. 自动化准确性保障机制

AI 自动化开发时，准确性不能只靠“模型认真”。  
必须靠以下约束：

### 15.1 分支隔离

- 一个模块一个 worktree
- 一个模块一个分支
- 一个模块一个状态文件

### 15.2 输入收敛

- 只给模块相关任务书
- 只给统一 UI 规范
- 只给当前模块代码范围
- 不把全项目杂乱信息都塞进来

### 15.3 输出留痕

- 必须有状态文件
- 必须有验证报告
- 必须列出修改文件
- 必须列出执行命令

### 15.4 合并门禁

没有验证报告，不允许进入 `develop`。  
验证不通过，不允许进入 `develop`。

## 16. 失败与中断恢复方案

## 16.1 会话中断

如果 Codex 会话中断：

1. 打开对应模块状态文件
2. 读取当前阶段和未完成项
3. 从上一次的工作目录继续
4. 先验证现有代码状态，再继续改动

## 16.2 编码完成但验证失败

处理规则：

1. 不合并到 `develop`
2. 把失败项写入验证报告
3. 回到阶段 D 修复
4. 重新生成验证报告

## 16.3 改动越界

如果某模块改了不该改的公共文件：

1. 在状态文件中说明原因
2. 标记为高风险
3. 由主控会话决定是否允许保留

## 17. develop 合并策略

建议合并顺序：

1. 先合低耦合内容模块
2. 再合下载模块
3. 最后合商城模块

每次只合一个模块到 `develop`，不要一次性把多个模块混合合入。

标准流程：

```text
模块分支完成
-> 生成验证报告
-> 主控审核
-> 合并到 develop
-> 在 develop 做联调验证
-> 修复联调问题
-> 准备进入 master
```

## 18. 实际落地建议

对当前项目，建议采用以下落地步骤：

1. 为 6 个模块分支创建独立 worktree
2. 在 `docs` 下建立 `agent-status` 和 `verification` 目录
3. 每个模块先跑阶段 A，产出状态文件初稿
4. 先选择 1 到 2 个低耦合模块试运行
5. 跑通整套“编码 -> 验证 -> 合并”流程
6. 再扩大到剩余模块

## 19. 最终执行标准

判断 Codex 多分支自动开发是否可用，不看“是否写了代码”，而看是否满足以下标准：

1. 每个模块都有独立分支与独立 worktree
2. 每个模块都有状态文件
3. 每个模块都有验证报告
4. 每个模块都有明确的修改文件边界
5. 每个模块都能在短上下文下恢复执行
6. 每个模块都能提供是否可合并 `develop` 的客观依据

如果以上 6 条不能满足，就不应认为该模块已经进入可自动化开发状态。

## 20. 本方案适合当前项目的原因

当前项目已经具备：

- 模块分支已拆分
- 模块任务书已拆分
- UI 统一规范已拆分
- Git 分支使用规范已整理

因此最适合采用“模块独立执行 + 中间状态文件 + 验证报告 + develop 门禁”的方式，而不是让单个长会话把所有事情一次做完。
