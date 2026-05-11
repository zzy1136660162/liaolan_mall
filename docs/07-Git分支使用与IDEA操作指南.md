origin/feature/contact-dev6
# Git分支使用与IDEA操作指南

生成日期：2026-05-08  
适用项目：`crmeb_java`

## 1. 文档目标

本指南用于统一项目组成员对 Git 分支的使用方式，避免出现以下问题：

- 多个人直接在 `master` 上开发
- 开发人员改错分支
- 功能还没完成就直接合入主分支
- IDEA 中不会切分支、拉最新代码、提交代码、解决冲突
- 联调代码和正式代码混在一起，后期难以管理

本文件不是 Git 原理说明，而是本项目的实际操作规范。

## 2. 本项目分支结构

当前项目建议使用以下分支结构：

```text
master
└── develop
    ├── feature/mall-dev1
    ├── feature/case-dev2
    ├── feature/news-dev3
    ├── feature/download-dev4
    ├── feature/about-dev5
    └── feature/contact-dev6
```

## 3. 各分支用途说明

### 3.1 `master`

用途：

- 生产主分支
- 保存稳定、可交付、可上线代码

规则：

- 禁止开发人员直接在 `master` 上写功能
- 禁止未经联调验证直接合入
- 只有项目负责人或主程允许最终合并到 `master`

### 3.2 `develop`

用途：

- 公共开发联调分支
- 各模块开发完成后，先合入 `develop` 做联调验证

规则：

- 每个模块功能完成后先合入 `develop`
- `develop` 稳定后，再统一合到 `master`

### 3.3 `feature/*`

用途：

- 每个开发人员的个人功能分支
- 用于独立开发某一个模块

当前命名：

- `feature/mall-dev1`
- `feature/case-dev2`
- `feature/news-dev3`
- `feature/download-dev4`
- `feature/about-dev5`
- `feature/contact-dev6`

规则：

- 每个人只在自己的 `feature/*` 分支开发
- 不要多人共用同一个 `feature/*` 分支
- 一个分支尽量只负责一个模块，不要混改多个模块

## 4. 分支流转关系

本项目代码流转规则如下：

```text
feature/*  -->  develop  -->  master
```

说明：

1. 开发人员先在自己的 `feature/*` 分支编码
2. 功能开发完成后，合并到 `develop`
3. 在 `develop` 上完成联调、自测、修复问题
4. `develop` 稳定后，再合并到 `master`

## 5. 每位开发人员应该怎么用

### 5.1 第一次开发前

先拉取远程最新代码：

```bash
git pull origin master
git pull origin develop
```

然后切到自己的功能分支：

```bash
git checkout feature/mall-dev1
```

如果本地还没有这个分支：

```bash
git checkout -b feature/mall-dev1 origin/feature/mall-dev1
```

### 5.2 日常开发

开发人员每天开始工作前：

1. 先确认当前分支是不是自己的分支
2. 拉取远程最新代码
3. 再开始写代码

命令如下：

```bash
git branch --show-current
git pull origin feature/mall-dev1
```

### 5.3 开发中同步公共代码

如果 `develop` 上有别人合入的新代码，你需要同步到自己分支：

```bash
git checkout develop
git pull origin develop
git checkout feature/mall-dev1
git merge develop
```

说明：

- 本项目优先建议用 `merge`
- 不要求所有开发人员使用 `rebase`
- 这样更稳，也更容易理解

### 5.4 提交代码

开发完成一部分后提交：

```bash
git add .
git commit -m "feat: 商城模块新增分类列表"
git push origin feature/mall-dev1
```

提交说明规范建议：

- `feat:` 新功能
- `fix:` 修复问题
- `refactor:` 重构
- `docs:` 文档修改
- `style:` 样式调整

示例：

```bash
git commit -m "feat: 客户案例模块新增案例分类管理"
git commit -m "fix: 修复新闻详情发布时间显示错误"
```

## 6. 合并规则

### 6.1 开发人员不能直接做的事

禁止：

- 直接在 `master` 上开发
- 未经验证直接把 `feature/*` 合入 `master`
- 在自己分支上随意合并别人未确认的代码
- 强制推送覆盖别人远程分支

### 6.2 正确合并顺序

正确顺序：

```text
feature/* -> develop -> master
```

推荐做法：

1. 开发人员功能完成后提交到自己的 `feature/*`
2. 由负责人或指定人员合并到 `develop`
3. 联调完成后再由负责人合并到 `master`

## 7. 冲突处理原则

### 7.1 哪些文件最容易冲突

常见高冲突文件：

- 全局配置文件
- 全局路由文件
- 公共组件入口文件
- 公共样式文件
- 同一个页面文件
- SQL 汇总脚本

### 7.2 如何降低冲突

建议：

- 每个人主负责一个模块
- 公共文件修改前先沟通
- 数据库脚本按模块分文件或按日期命名
- 公共组件调整前先确定负责人

### 7.3 冲突时处理原则

冲突时不要直接无脑选自己的版本。  
必须确认：

- 别人的代码功能是否已经生效
- 自己的修改是否会覆盖别人逻辑
- 配置项是否被误删

如果不会处理冲突，先找负责人，不要乱提交。

## 8. 命令行常用操作

### 8.1 查看当前分支

```bash
git branch --show-current
```

### 8.2 查看所有分支

```bash
git branch -a
```

### 8.3 切换分支

```bash
git checkout develop
git checkout feature/mall-dev1
```

### 8.4 创建本地分支并关联远程

```bash
git checkout -b feature/mall-dev1 origin/feature/mall-dev1
```

### 8.5 提交代码

```bash
git add .
git commit -m "feat: 新增下载资料列表页"
git push origin feature/download-dev4
```

### 8.6 合并 `develop` 到自己分支

```bash
git checkout develop
git pull origin develop
git checkout feature/news-dev3
git merge develop
```

## 9. 在 IDEA 中如何使用 Git 分支

## 9.1 查看当前分支

在 IDEA 右下角可以看到当前 Git 分支名称。  
例如：

- `master`
- `develop`
- `feature/mall-dev1`

开发前先确认当前是不是自己的分支。

## 9.2 切换分支

操作路径：

1. 点击 IDEA 右下角分支名称
2. 打开 Git Branches 弹窗
3. 在 `Local Branches` 中找到目标分支
4. 点击对应分支
5. 选择 `Checkout`

示例：

- 切换到 `feature/mall-dev1`
- 点击右下角分支
- 选择 `feature/mall-dev1`
- 点击 `Checkout`

## 9.3 第一次检出远程分支

如果本地没有该分支：

1. 点击 IDEA 右下角分支名称
2. 在 `Remote Branches` 中找到 `origin/feature/mall-dev1`
3. 点击该远程分支
4. 选择 `Checkout As...`
5. 本地分支名保持为 `feature/mall-dev1`
6. 确认创建

这样 IDEA 会创建本地分支并自动跟踪远程分支。

## 9.4 拉取最新代码

操作路径：

1. 菜单栏 `Git`
2. 选择 `Pull...`
3. 确认远程为 `origin`
4. 确认当前分支正确
5. 点击 `Pull`

建议：

- 每天开始工作前先 `Pull`
- 合并前先 `Pull`
- 提交前先确认是否要同步 `develop`

## 9.5 提交代码

操作路径：

1. 修改代码后，按 `Ctrl + K`
2. 打开 Commit 窗口
3. 勾选本次要提交的文件
4. 填写提交说明
5. 点击 `Commit` 或 `Commit and Push`

提交说明示例：

- `feat: 商城模块新增商品详情页`
- `fix: 修复联系我们表单校验错误`

建议：

- 每次提交只提交与当前任务有关的文件
- 不要把无关改动一起提交

## 9.6 推送代码

操作路径：

1. 菜单栏 `Git`
2. 选择 `Push...`
3. 检查目标远程和分支名
4. 点击 `Push`

注意：

- 推送前确认当前分支正确
- 不要把个人代码推到 `master`

## 9.7 合并 `develop` 到自己分支

操作步骤：

1. 先切到 `develop`
2. 执行 `Pull`
3. 再切回自己的 `feature/*`
4. 点击右下角分支名称
5. 选择 `Merge into Current`
6. 选择 `develop`

这样会把最新公共代码合并到你的当前分支。

## 9.8 处理冲突

如果 IDEA 提示冲突：

1. 打开冲突文件列表
2. 逐个点击 `Merge`
3. 查看左侧、本地、右侧内容
4. 确认保留哪些代码
5. 完成后点击 `Apply`
6. 再次提交

原则：

- 不理解的冲突不要直接全部选左边或右边
- 先理解逻辑再处理

## 9.9 查看修改记录

常用方式：

1. 右键文件
2. 选择 `Git`
3. 查看：
   - `Show History`
   - `Annotate`
   - `Compare with Branch`

用途：

- 看谁改过
- 看什么时候改的
- 看改动内容

## 10. 本项目推荐的 IDEA 使用流程

每位开发人员建议按下面顺序工作：

1. 打开项目
2. 确认当前分支是自己的 `feature/*`
3. 先 `Pull`
4. 开发代码
5. 本地自测
6. `Commit`
7. `Push`
8. 提交给负责人合并到 `develop`

如果中途需要同步公共代码：

1. 切到 `develop`
2. `Pull`
3. 切回自己的分支
4. `Merge develop into current`
5. 处理冲突
6. 继续开发

## 11. 开发人员操作禁止项

以下行为禁止：

- 在 `master` 上直接编码
- 在错误分支上提交代码
- 未看清分支就 `Push`
- 把多个模块不相关代码混在一次提交里
- 冲突没看懂就强行提交
- 用自己的代码覆盖别人已经联调通过的代码

## 12. 负责人管理建议

项目负责人建议执行以下管理规则：

1. 限制 `master` 只做稳定代码合并
2. 统一要求开发人员只使用自己的 `feature/*`
3. 所有模块先合 `develop`
4. `develop` 联调通过后再进入 `master`
5. 公共文件安排固定负责人
6. 每个模块合并前先自测再提合并

## 13. 最终执行标准

所有开发人员必须遵守以下规则：

1. 自己的代码只写在自己的 `feature/*` 分支
2. 联调代码统一进 `develop`
3. 正式稳定代码最终才进 `master`
4. 不会操作分支时，先问负责人，不要乱提交

本文件的目标不是讲复杂 Git 原理，而是让项目组所有人用同一套分支方式稳定协作。
