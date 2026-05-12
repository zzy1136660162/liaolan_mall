# 客户案例模块 → case 命名重构计划

## 背景分析

当前客户案例模块的代码**嵌入在文章（Article）系统内部**，与新闻模块共享同一套文件：
- 后台管理前端：`admin/src/api/article.js`、`admin/src/views/content/article/edit.vue`、`admin/src/views/content/article/list.vue` 中混合了案例特有代码
- Java 后端：`ArticleController`（admin 和 front）中混合了案例接口
- 这导致两个模块相互耦合，产生冲突

**目标**：将客户案例模块从文章系统中**完整剥离**，创建独立的、以 `case` 命名的文件体系。原文件暂不删除。

**好消息**：App 端（前台 uni-app）已经使用 `case` 命名（`pages/case/`、`getCaseList` 等），无需大改。

---

## 一、需要新建的文件清单

### 1.1 后台管理前端（Admin）

| 序号 | 新建文件路径 | 说明 |
|------|-------------|------|
| 1 | `admin/src/api/case.js` | 从 `article.js` 中提取案例相关 API |
| 2 | `admin/src/views/content/case/list.vue` | 从 `article/list.vue` 中提取案例列表页 |
| 3 | `admin/src/views/content/case/edit.vue` | 从 `article/edit.vue` 中提取案例编辑页 |

### 1.2 Java 后端 - Admin Controller

| 序号 | 新建文件路径 | 说明 |
|------|-------------|------|
| 4 | `crmeb/crmeb-admin/src/main/java/com/zbkj/admin/controller/CaseController.java` | 从 `ArticleController` 中提取案例相关接口 |

### 1.3 Java 后端 - Front Controller

| 序号 | 新建文件路径 | 说明 |
|------|-------------|------|
| 5 | `crmeb/crmeb-front/src/main/java/com/zbkj/front/controller/CaseController.java` | 从 `ArticleController` 中提取案例相关接口 |

---

## 二、详细实施步骤

### 步骤 1：创建 `admin/src/api/case.js`

**内容来源**：从 [admin/src/api/article.js](file:///e:/liaolan/admin/src/api/article.js) 中提取案例相关方法。

需要包含的方法：
- `getCaseList(params)` — 案例列表（含行业/区域筛选），对应 `GET /admin/article/list`
- `getCaseDetail(id)` — 案例详情（含扩展信息），对应 `GET /admin/article/info/case`
- `saveCase(data)` — 新增案例，对应 `POST /admin/article/save`
- `updateCase(data)` — 更新案例，对应 `POST /admin/article/update`
- `deleteCase(id)` — 删除案例，对应 `GET /admin/article/delete`
- `bindCaseProduct(data)` — 绑定商品，对应 `POST /admin/article/bind/product`

> 注意：后端接口路径（`/admin/article/*`）暂时保持不变，仅在前端封装层使用 `case` 命名。如果后续后端也新建独立接口，再同步更新。

### 步骤 2：创建 `admin/src/views/content/case/list.vue`

**内容来源**：从 [admin/src/views/content/article/list.vue](file:///e:/liaolan/admin/src/views/content/article/list.vue) 复制，然后：

- 移除文章分类筛选逻辑（案例固定使用案例分类）
- 保留行业分类、区域分类筛选
- 将 API 调用改为引用 `@/api/case`
- 列表列保留：标题、行业分类、区域分类、项目类型、排序、状态、操作
- 新增按钮跳转到 `case/edit` 路由
- 面包屑导航更新为案例管理

路由名称建议：`caseManager`

### 步骤 3：创建 `admin/src/views/content/case/edit.vue`

**内容来源**：从 [admin/src/views/content/article/edit.vue](file:///e:/liaolan/admin/src/views/content/article/edit.vue) 复制，然后：

- 固定文章分类为案例分类（不显示分类选择器，或只显示案例相关分类）
- 保留完整的案例扩展表单（项目名称、行业分类、区域分类、项目类型、项目地点、项目周期、项目背景、供货产品、实施效果、关联商品）
- 将 API 调用改为引用 `@/api/case`
- 表单提交使用新封装的 `saveCase`/`updateCase`
- 面包屑导航更新

### 步骤 4：注册后台路由

**文件**：[admin/src/router/modules/content.js](file:///e:/liaolan/admin/src/router/modules/content.js)

在 `contentRouter.children` 数组中，紧接 `articleCreat` 路由之后，新增案例路由：

```javascript
{
  path: 'caseManager',
  name: 'caseManager',
  component: () => import('@/views/content/case/list'),
  meta: {
    title: '案例管理',
    icon: 'clipboard',
  },
},
{
  path: 'caseCreat/:id?',
  name: 'caseCreat',
  component: () => import('@/views/content/case/edit'),
  meta: {
    title: '添加案例',
    noCache: true,
    activeMenu: `/content/caseManager`,
  },
},
```

> 路由放在 `/content` 下面，与文章管理平级，菜单中会显示为"内容 > 案例管理"。

### 步骤 5：创建 `CaseController.java`（Admin 端）

**文件路径**：`crmeb/crmeb-admin/src/main/java/com/zbkj/admin/controller/CaseController.java`

**内容来源**：从 [ArticleController.java (admin)](file:///e:/liaolan/crmeb/crmeb-admin/src/main/java/com/zbkj/admin/controller/ArticleController.java) 复制，然后：

- 类名改为 `CaseController`
- `@RequestMapping` 改为 `api/admin/case`
- `@Api(tags = "案例管理")`
- 保留案例相关方法：列表（含行业/区域筛选）、详情（含扩展信息）、新增、更新、删除、绑定商品
- 移除纯文章相关方法（如文章分类管理等非案例方法）
- 注入 `ArticleService` 和 `ArticleCaseExtendService` 保持不变

### 步骤 6：创建 `CaseController.java`（Front 端）

**文件路径**：`crmeb/crmeb-front/src/main/java/com/zbkj/front/controller/CaseController.java`

**内容来源**：从 [ArticleController.java (front)](file:///e:/liaolan/crmeb/crmeb-front/src/main/java/com/zbkj/front/controller/ArticleController.java) 复制，然后：

- 类名改为 `CaseController`，Bean 名改为 `"CaseFrontController"`
- `@RequestMapping` 改为 `api/front/case`
- `@Api(tags = "案例")`
- 保留案例方法：`getCaseList`、`caseInfo`、`getCaseIndustryCategories`
- 移除纯文章方法（`getList`、`getHotList`、`getBannerList`、`categoryList`、`info`）

> 注意：由于使用 `@RestController("CaseFrontController")` 作为 Bean 名，不会与 Admin 端的 `CaseController` 冲突。

### 步骤 7：更新 App 端 API 调用路径

如果步骤 6 中 Front 端 Controller 的路径从 `api/front/article/case/*` 改为 `api/front/case/*`，则需同步更新：

**文件**：[app/api/api.js](file:///e:/liaolan/app/api/api.js)

将：
- `article/case/list/` → `case/list/`
- `article/case/info` → `case/info`
- `article/case/industry/categories` → `case/industry/categories`

### 步骤 8：Admin 端添加路由配置

找到 Admin 路由文件，添加案例管理的路由条目。

---

## 三、文件改动影响范围汇总

### 新建文件（6个）

| 端 | 文件 | 类型 |
|----|------|------|
| Admin 前端 | `admin/src/api/case.js` | API 封装 |
| Admin 前端 | `admin/src/views/content/case/list.vue` | 页面组件 |
| Admin 前端 | `admin/src/views/content/case/edit.vue` | 页面组件 |
| Java Admin | `crmeb/crmeb-admin/.../controller/CaseController.java` | Controller |
| Java Front | `crmeb/crmeb-front/.../controller/CaseController.java` | Controller |

### 可能需要修改的现有文件（2个）

| 文件 | 修改内容 |
|------|---------|
| `admin/src/router/...`（路由配置） | 新增 case 路由 |
| `app/api/api.js` | 如接口路径变更则更新（如不变则无需改动）|

### 不修改的文件（保持不变）

- 所有 Common 模块文件（`ArticleCaseExtend*` 等）— 保持原命名
- 所有 Service 层文件 — 保持不变
- App 端页面文件（`pages/case/*`）— 已使用 case 命名
- 原 `ArticleController`、`article.js`、`edit.vue`、`list.vue` — 暂不删除，保留

---

## 四、执行顺序

1. 创建 `admin/src/api/case.js`
2. 创建 `admin/src/views/content/case/list.vue`
3. 创建 `admin/src/views/content/case/edit.vue`
4. 添加 Admin 路由配置
5. 创建 Admin 端 `CaseController.java`
6. 创建 Front 端 `CaseController.java`
7. 更新 `app/api/api.js`（如路径变更）

---

## 五、注意事项

- 原文件**暂不删除**，仅新建 case 命名的新文件
- Java Service 层和 Common 层的 `ArticleCaseExtend*` 类名保持不变（仅 Controller 层新建）
- 如果后端新 Controller 使用新的 URL 路径（`/admin/case/*`），需同步更新前端 API 调用
- 如果保持原 URL 路径（`/admin/article/*`），则前端 API 调用路径无需变更，仅封装层函数名更新
- App 端 `pages.json` 中的 `pages/case` 子包配置无需修改
