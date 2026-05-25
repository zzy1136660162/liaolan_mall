# 联系我们模块说明

## 1. 配置项

后台页面：`内容 -> 联系我们`

对应系统配置键：

- `consumer_hotline`：联系电话
- `contact_company_address`：公司地址
- `contact_map_link`：地图导航链接
- `yzf_h5_url`：在线客服链接
- `contact_service_qrcode`：客服二维码图片地址
- `site_name`：公司名称，直接复用现有站点名称

## 2. 留言表字段

表名：`eb_contact_message`

- `company_name`：公司名称，前台选填
- `name`：联系人姓名，前台必填
- `mobile`：手机号，前台必填
- `content`：留言内容，前台必填
- `source_page`：来源页面
  - `contact_page`：联系我们页
  - `product_detail`：商品详情页
  - `user_center`：用户中心入口
- `status`：处理状态
  - `0`：待处理
  - `1`：已联系
  - `2`：无效线索
  - `3`：转商机
- `handle_remark`：处理备注
- `handle_user_id` / `handle_user_name`：处理人信息
- `handle_time`：处理时间
- `created_at` / `updated_at`：创建与更新时间

## 3. 前台页面流程

1. 用户从用户中心或商品详情进入 `pages/contact/index`
2. 页面加载 `GET /api/front/contact/info`
3. 页面展示四个快捷入口：
   - 在线留言：滚动到表单
   - 在线客服：跳转客服 H5 链接
   - 联系电话：直接拨号
   - 地图导航：跳转地图 H5 链接
4. 用户提交留言时调用 `POST /api/front/contact/message`
5. 后台默认按“待处理”进入列表

## 4. 后台页面流程

1. 进入 `内容 -> 联系我们`
2. 顶部维护联系方式配置，保存后直接写入 `eb_system_config`
3. 下方留言列表支持按联系人、手机号、来源页面、处理状态筛选
4. 运营点击“处理”后可更新状态并填写备注
5. 处理后回填处理人、处理时间
