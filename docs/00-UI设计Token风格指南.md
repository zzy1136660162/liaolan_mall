# UI设计Token风格指南

生成日期：2026-05-07  
适用项目：`E:\ZZY_PROJECT\crmeb_java`  
适用范围：`admin` 后台管理端、`app` UniApp/H5 前台

## 1. 文档目标

本指南用于统一不同模块开发人员的页面视觉语言，确保多人并行开发时：

- 使用同一套颜色语义，不出现“每个模块一个主题色”。
- 使用同一套字号、字重、行高，不出现标题和正文层级混乱。
- 使用同一套按钮、表单、表格、卡片、标签样式，不出现组件风格漂移。
- 使用同一套间距、圆角、边框、阴影，不出现页面观感不一致。
- 后台管理端与前台 H5/UniApp 虽然品牌主色可以不同，但语义层必须一致。

本文件不是建议稿，而是模块开发必须遵守的统一 UI 基线。

## 2. 设计原则

### 2.1 统一语义，不统一像素表象

允许：

- 后台主色为蓝色系，前台主色为红色系。
- 后台页面偏工具型，前台页面偏业务展示型。

不允许：

- 后台把“保存”做成蓝色，另一个后台模块把“保存”做成绿色。
- 前台商城把警告态做成橙色，下载模块把警告态做成红色。
- 某个模块自定义一套新的卡片圆角、阴影、表单边框。

### 2.2 先语义 Token，再组件样式

所有页面样式必须先落到 Token，再落到组件。  
禁止在业务页面里直接散落硬编码颜色、字号、间距。

错误示例：

```scss
color: #0256ff;
border-radius: 13px;
padding: 18px 22px;
```

正确示例：

```scss
color: $color-primary;
border-radius: $radius-sm;
padding: $space-4 $space-5;
```

### 2.3 新模块不得发明新风格

开发人员只负责按规范编码与自测，不负责：

- 重新定义品牌色
- 重新定义表单样式
- 重新定义卡片规范
- 重新定义后台/前台页面结构
- 重新设计 UI 主题

如确需新增 Token，只允许新增“同语义别名”或“现有层级补充项”，不得破坏现有命名体系。

## 3. 项目现状与统一策略

### 3.1 当前项目已有主题基础

后台管理端已有主题入口：

- `admin/src/theme/element-variables.scss`
- `admin/src/styles/index.scss`
- `admin/src/styles/styles.scss`

当前后台主色：

- `$--color-primary: #0256ff`

前台 UniApp/H5 已有主题入口：

- `app/uni.scss`

当前前台主色：

- `$theme-color: #E93323`
- `$theme-color-opacity: rgba(233,51,35,.6)`

### 3.2 统一策略

本项目采用：

- 后台端：工具型主题，主色蓝
- 前台端：业务型主题，主色红
- 两端共享同一套语义 Token 命名

即：

- `primary`
- `success`
- `warning`
- `danger`
- `text-primary`
- `text-secondary`
- `border-default`
- `bg-page`
- `radius-sm`
- `space-4`

这些名字和语义必须完全一致，只允许最终映射值因端不同而不同。

## 4. Token 分层模型

推荐采用三层结构：

### 4.1 基础层 Base Token

基础颜色、尺寸、圆角、阴影、字号。

示例：

- `blue-6`
- `red-6`
- `gray-1`
- `gray-9`
- `radius-6`
- `space-16`

### 4.2 语义层 Semantic Token

给业务和组件使用的语义名称。

示例：

- `color-primary`
- `color-success`
- `text-primary`
- `bg-surface`
- `border-default`
- `button-height-md`

### 4.3 组件层 Component Token

组件最终消费的具体变量。

示例：

- `button-primary-bg`
- `button-primary-hover-bg`
- `input-border-color`
- `table-header-bg`
- `card-padding`

本项目开发要求：  
业务代码只允许直接使用语义层和组件层，不允许大面积直接使用基础层值。

## 5. 颜色 Token 规范

## 5.1 品牌与状态色

| Token 名称 | 语义说明 | admin 建议值 | app 建议值 | 使用范围 |
|---|---|---|---|---|
| `color-primary` | 主操作色 | `#0256FF` | `#E93323` | 保存、提交、确认、主链接、激活态 |
| `color-primary-hover` | 主操作悬停色 | `#1D67FF` | `#F04A3A` | hover、按下前一层 |
| `color-primary-active` | 主操作按下色 | `#0047DB` | `#D92C1D` | active |
| `color-primary-soft` | 主色浅底 | `#EAF1FF` | `#FFF1EE` | 浅色标签、信息块、筛选高亮底 |
| `color-success` | 成功色 | `#19BE6B` | `#19BE6B` | 启用、成功、完成 |
| `color-success-soft` | 成功浅底 | `#E8F8F0` | `#E8F8F0` | 成功提示块、成功标签 |
| `color-warning` | 警告色 | `#FF7D00` | `#FF7D00` | 待处理、提醒、风险提示 |
| `color-warning-soft` | 警告浅底 | `#FFF4E8` | `#FFF4E8` | 警告提示块 |
| `color-danger` | 危险色 | `#F56464` | `#F56464` | 删除、失败、禁用业务 |
| `color-danger-soft` | 危险浅底 | `#FDEDED` | `#FDEDED` | 失败提示块、删除确认提示 |
| `color-info` | 中性信息色 | `#909399` | `#909399` | 次级提示、普通状态 |

### 5.2 强制使用规则

- “保存 / 提交 / 确认 / 去处理 / 查看详情主入口”统一使用 `color-primary`
- “删除 / 作废 / 移除 / 拒绝”统一使用 `color-danger`
- “已完成 / 启用 / 已上架 / 已支付”统一使用 `color-success`
- “待审核 / 待处理 / 库存紧张 / 需关注”统一使用 `color-warning`

禁止：

- 保存按钮使用绿色
- 删除按钮使用灰色
- 同一页面出现两个不同主按钮色
- 某模块自行加紫色、青色作为新的业务主态

## 5.3 中性色

| Token 名称 | 建议值 | 说明 |
|---|---|---|
| `gray-0` | `#FFFFFF` | 纯白 |
| `gray-1` | `#FAFAFA` | 很浅背景 |
| `gray-2` | `#F7F8FA` | 页面背景 |
| `gray-3` | `#F2F3F5` | 弱分隔背景 |
| `gray-4` | `#EAECEF` | 轻边框 |
| `gray-5` | `#DCDFE6` | 标准边框 |
| `gray-6` | `#C0C4CC` | 禁用边框/文本 |
| `gray-7` | `#999999` | 三级文本 |
| `gray-8` | `#666666` | 二级文本 |
| `gray-9` | `#333333` | 一级文本 |
| `gray-10` | `#1F2D3D` | 深标题，仅少量强调 |

## 6. 文本 Token 规范

| Token 名称 | 建议值 | 使用说明 |
|---|---|---|
| `text-primary` | `#333333` | 页面主标题、正文、重点信息 |
| `text-secondary` | `#666666` | 表单说明、次级信息、列表描述 |
| `text-tertiary` | `#999999` | 占位、提示、补充说明 |
| `text-disabled` | `#C0C4CC` | 禁用态文本 |
| `text-inverse` | `#FFFFFF` | 深色背景上的文字 |
| `text-link` | admin:`#0256FF` / app:`#E93323` | 链接文本 |
| `text-danger` | `#F56464` | 错误文案、危险提示 |
| `text-success` | `#19BE6B` | 成功文案、正向结果 |
| `text-price` | `#FD502F` | 价格和关键数字，仅前台重点展示使用 |

### 6.1 文本强制规则

- 页面主标题统一使用 `text-primary`
- 次级说明统一使用 `text-secondary`
- 辅助文案统一使用 `text-tertiary`
- 禁止大面积使用纯黑 `#000000`
- 价格只能用 `text-price` 或 `color-primary`，不能随意定义橙红色

## 7. 背景、边框、阴影 Token

## 7.1 背景

| Token 名称 | 建议值 | 使用范围 |
|---|---|---|
| `bg-page` | `#F7F8FA` | 页面大背景 |
| `bg-surface` | `#FFFFFF` | 卡片、面板、容器背景 |
| `bg-surface-soft` | `#FAFAFA` | 浅色信息区、说明块 |
| `bg-disabled` | `#F5F5F5` | 禁用态区域 |
| `bg-overlay` | `rgba(0,0,0,.4)` | 弹窗遮罩 |

## 7.2 边框

| Token 名称 | 建议值 | 使用范围 |
|---|---|---|
| `border-default` | `#DCDFE6` | 默认输入框、卡片、表格边框 |
| `border-light` | `#EAECEF` | 分割线、弱边框 |
| `border-strong` | `#D0D7DE` | 强调边框 |
| `border-danger` | `#F56464` | 错误校验边框 |

## 7.3 阴影

| Token 名称 | 建议值 | 使用范围 |
|---|---|---|
| `shadow-none` | `none` | 默认 |
| `shadow-sm` | `0 1px 2px rgba(0,0,0,.04)` | 轻悬浮 |
| `shadow-md` | `0 4px 12px rgba(0,0,0,.08)` | 下拉层、浮层、弹层 |

### 7.4 视觉约束

- 后台以边框分层为主，阴影只用于弹层、抽屉、悬浮面板
- 前台卡片允许轻阴影，但不得使用营销页重阴影
- 同一页面不得混用三四种相近灰色背景

## 8. 圆角与间距 Token

## 8.1 圆角

| Token 名称 | 值 | 用途 |
|---|---|---|
| `radius-xs` | `4px` | 小标签、小输入块 |
| `radius-sm` | `6px` | 按钮、输入框、缩略图 |
| `radius-md` | `8px` | 卡片、容器、弹窗内部块 |
| `radius-lg` | `12px` | 前台较大展示卡片 |
| `radius-pill` | `999px` | 胶囊标签、筛选项 |

### 8.2 圆角强制规则

- 后台按钮、输入框、表格图片统一 `radius-sm`
- 后台卡片、筛选面板统一 `radius-md`
- 前台商品卡片、案例卡片不超过 `radius-md`
- 禁止随意使用 `16px`、`20px` 这种明显更圆的风格

## 8.3 间距

统一采用 4 的倍数体系。

| Token 名称 | 值 | 用途 |
|---|---|---|
| `space-1` | `4px` | 极小间距 |
| `space-2` | `8px` | 图标与文字 |
| `space-3` | `12px` | 小组件间距 |
| `space-4` | `16px` | 表单项、卡片内边距基础值 |
| `space-5` | `20px` | 中型容器内边距 |
| `space-6` | `24px` | 区块上下间距 |
| `space-8` | `32px` | 大区块间距 |
| `space-10` | `40px` | 页面段落级间距 |

### 8.4 间距强制规则

- 后台筛选区、表格区、详情区常规内边距统一使用 `16px` 或 `20px`
- 前台列表区、详情区统一使用 `16px`、`24px`
- 禁止出现 `13px`、`18px`、`22px` 这类无体系值，除非旧组件兼容不可避免

## 9. 字体层级规范

## 9.1 后台字体层级

| Token 名称 | 字号 | 字重 | 行高 | 使用范围 |
|---|---|---|---|---|
| `font-page-title` | `20px` | `600` | `28px` | 页面标题 |
| `font-section-title` | `16px` | `600` | `24px` | 区块标题 |
| `font-body` | `14px` | `400` | `22px` | 正文、表单、表格 |
| `font-caption` | `12px` | `400` | `18px` | 辅助文案 |

## 9.2 前台字体层级

| Token 名称 | 字号 | 字重 | 行高 | 使用范围 |
|---|---|---|---|---|
| `font-page-title` | `18px / 36rpx` | `600` | `26px` | 页面主标题 |
| `font-section-title` | `16px / 32rpx` | `600` | `24px` | 区块标题 |
| `font-body` | `14px / 28rpx` | `400` | `22px` | 正文 |
| `font-caption` | `12px / 24rpx` | `400` | `18px` | 辅助文案 |
| `font-price` | `18px / 36rpx` | `600` | `24px` | 价格和关键数字 |

### 9.3 字体规则

- 不允许模块之间标题字号漂移过大
- 不允许用超大字做普通分组标题
- 不允许用 `vw`、`vh` 做字号缩放
- 字重只使用 `400`、`500`、`600`

## 10. 组件级 Token 与样式约束

## 10.1 按钮 Button

### 主按钮

- 背景：`color-primary`
- 悬停：`color-primary-hover`
- 按下：`color-primary-active`
- 文字：`text-inverse`
- 圆角：`radius-sm`
- 边框：同背景色

尺寸建议：

- 后台：`32px` / `36px`
- 前台：`40px` / `44px`

### 次按钮

- 背景：`bg-surface`
- 文字：`text-primary`
- 边框：`border-default`
- 悬停文字：`color-primary`
- 悬停边框：`color-primary`

### 危险按钮

- 背景：`color-danger`
- 文字：`text-inverse`
- 悬停：略深于 `color-danger`

强制规则：

- 同一操作区仅允许一个视觉主按钮
- “取消”默认用次按钮，不要做成红色
- 后台列表行内操作优先使用文本按钮或小按钮，不使用大面积色块按钮

## 10.2 输入框 Input / Select / Textarea

- 高度：后台 `32px` / `36px`，前台 `40px` / `44px`
- 文字：`text-primary`
- 占位：`text-tertiary`
- 边框：`border-default`
- hover 边框：`border-strong`
- focus 边框：`color-primary`
- 错误态边框：`border-danger`
- 圆角：`radius-sm`
- 背景：`bg-surface`

强制规则：

- 输入框禁止使用过深阴影
- 校验错误必须有 `danger` 文案和边框，不能只改提示文案

## 10.3 表格 Table（admin）

- 表头背景：`bg-surface-soft`
- 表头文字：`text-secondary`
- 表格正文：`text-primary`
- 边框：`border-light`
- 行 hover：`color-primary-soft`
- 操作列文本按钮：默认 `text-link`

强制规则：

- 表头不得使用深色填充
- 表格分页、筛选、批量操作区风格必须复用现有后台样式

## 10.4 卡片 Card / Panel

- 背景：`bg-surface`
- 圆角：后台 `radius-md`，前台 `radius-md`
- 边框：`border-light`
- 阴影：默认 `shadow-none` 或 `shadow-sm`
- 内边距：`space-4` 或 `space-5`

强制规则：

- 页面区块不要套多层卡片
- 不要把整个页面做成“很多悬浮卡片”

## 10.5 标签 Tag / Status

| 状态 | 文字色 | 背景色 |
|---|---|---|
| 主态 | `color-primary` | `color-primary-soft` |
| 成功 | `color-success` | `color-success-soft` |
| 警告 | `color-warning` | `color-warning-soft` |
| 危险 | `color-danger` | `color-danger-soft` |
| 中性 | `text-secondary` | `bg-surface-soft` |

## 10.6 弹窗 Dialog / Drawer

- 标题：`font-section-title` + `text-primary`
- 内容：`font-body`
- 遮罩：`bg-overlay`
- 圆角：`radius-md`
- 阴影：`shadow-md`
- 底部按钮区：右对齐，主次清晰

## 11. 页面级布局规范

## 11.1 后台页面

统一结构建议：

1. 页面标题区
2. 查询筛选区
3. 表格或内容区
4. 分页区
5. 弹窗/抽屉详情区

页面规则：

- 标题区与内容区间距 `space-6`
- 筛选区与表格区间距 `space-4`
- 页面主背景使用 `bg-page`
- 内容块统一 `bg-surface`

## 11.2 前台页面

统一结构建议：

1. 顶部标题或筛选区
2. 内容列表区
3. 空状态 / 加载状态
4. 底部操作栏

页面规则：

- 左右内边距统一 `16px`
- 区块上下间距统一 `16px` 或 `24px`
- 列表卡片布局统一，不允许一个模块 8px 圆角，一个模块 16px 圆角

## 12. 建议的代码落点

## 12.1 后台 admin

建议新增：

- `admin/src/styles/token.scss`

建议职责：

- 承载后台业务语义 Token
- 供页面与业务组件统一引用
- 与 `admin/src/theme/element-variables.scss` 做映射

建议示例：

```scss
$color-primary: #0256FF;
$color-primary-hover: #1D67FF;
$color-success: #19BE6B;
$color-warning: #FF7D00;
$color-danger: #F56464;

$text-primary: #333333;
$text-secondary: #666666;
$text-tertiary: #999999;

$bg-page: #F7F8FA;
$bg-surface: #FFFFFF;
$border-default: #DCDFE6;

$radius-sm: 6px;
$radius-md: 8px;

$space-4: 16px;
$space-5: 20px;
$space-6: 24px;
```

## 12.2 前台 app

建议在：

- `app/uni.scss`

中补充语义别名，不改变现有变量兼容关系，只增加统一语义命名。

建议示例：

```scss
$color-primary: $theme-color;
$color-primary-soft: #FFF1EE;
$color-success: #19BE6B;
$color-warning: #FF7D00;
$color-danger: #F56464;

$text-primary: $crmeb-font-color;
$text-secondary: $crmeb-font-color-subtitle;
$text-tertiary: $crmeb-font-color-assist;
$text-disabled: $crmeb-font-color-disable;

$bg-page: $crmeb-bg-color-grey;
$bg-surface: $crmeb-bg-color;
$border-default: $uni-border-color;
```

## 13. API 与 UI 联动展示约束

开发人员在接口联调时，必须统一以下状态显示规则：

| 接口字段语义 | UI 展示要求 |
|---|---|
| `status=0/1` | 统一映射为禁用/启用标签，颜色用 `danger/success` 或中性/成功 |
| `is_show=0/1` | 统一映射为下架/上架或隐藏/显示 |
| `is_del=0/1` | 删除状态只用于后台逻辑，不直接做主展示状态 |
| 空列表 | 统一使用项目空状态组件或空样式，不允许裸文本 |
| 加载中 | 统一使用现有 loading 组件，不允许每页自造 loading 风格 |
| 接口错误 | 统一 toast/message 风格，不允许页面内随意拼红字 |

## 14. 模块开发验收清单

所有模块开发人员提交前必须逐项自查：

### 14.1 颜色检查

- 是否仍然只使用主色/成功/警告/危险四类业务色
- 是否存在页面内硬编码十六进制颜色
- 是否存在“这个模块自己定义的新主题色”

### 14.2 字号检查

- 页面标题、分组标题、正文、辅助文案是否落在规定层级内
- 是否存在 15px、17px、19px 这类无规则字号

### 14.3 间距检查

- 是否采用 4 的倍数间距
- 是否存在随意写死的 `18px 22px 14px`

### 14.4 组件检查

- 按钮主次是否清晰
- 输入框风格是否与现有系统一致
- 卡片、表格、筛选区样式是否统一

### 14.5 页面结构检查

- 是否复用项目已有页面结构
- 是否存在一个模块明显更花、更重阴影、更大圆角

## 15. 开发禁止项

以下行为一律禁止：

- 在业务页面直接写大量十六进制颜色
- 在不同模块里随意定义新的按钮样式
- 为了“好看”擅自放大圆角、增加重阴影、增加渐变
- 把后台页面做成营销风格卡片流
- 把前台业务页做成完全不同的视觉体系
- 页面同类元素使用多个接近但不一致的灰色值

## 16. 与 6 份模块任务书的关系

后续所有模块任务书默认引用本指南。  
涉及以下模块时，前端编码必须以本文件为唯一 UI 主题基线：

- 产品商城模块
- 客户案例模块
- 公司新闻模块
- 资料下载模块
- 关于辽缆模块
- 联系我们模块

如果模块任务书与本文件局部描述冲突，以本文件的 Token 约束和组件约束为准。

## 17. 最终执行要求

所有开发人员只做三件事：

1. 按既定 API、数据库、页面结构开发
2. 按本 Token 规范统一实现 UI
3. 按验收清单完成自查

不做：

- 自行设计主题
- 自行创造新视觉系统
- 自行决定新的组件规范
- 自行修改全局品牌风格

本文件用于压缩多人协作时的 UI 决策成本，让不同模块最终产出的页面风格保持同一套主题语言。
