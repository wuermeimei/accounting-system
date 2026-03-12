# 详细API文档

## 基础信息

- 基础URL: `http://localhost:8080/api`
- 请求格式: JSON
- 响应格式: JSON
- 认证方式: JWT Token (Bearer)

## 1. 用户认证

### 1.1 用户注册

注册新用户账号。

**接口地址**: `POST /api/auth/register`

**请求头**: `Content-Type: application/json`

**请求体**:
```json
{
  "username": "testuser",        // 必填，3-50字符
  "password": "test123",         // 必填，至少6位
  "confirmPassword": "test123",  // 必填，需与password一致
  "email": "test@example.com"    // 可选，邮箱格式
}
```

**响应示例**:
```json
{
  "success": true,
  "message": "注册成功"
}
```

**错误响应**:
```json
{
  "success": false,
  "message": "用户名已存在"
}
```

**注意事项**:
- 用户名必须唯一
- 密码至少6位
- 注册成功后用户为普通USER角色

---

### 1.2 用户登录

用户登录并获取JWT Token。

**接口地址**: `POST /api/auth/login`

**请求头**: `Content-Type: application/json`

**请求体**:
```json
{
  "username": "admin",
  "password": "admin123"
}
```

**响应示例**:
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "username": "admin"
}
```

**使用Token**:
在后续请求的Header中添加:
```
Authorization: Bearer {token}
```

**错误响应 (401)**:
```json
{
  "success": false,
  "message": "用户名或密码错误"
}
```

**初始账户**:
| 用户名 | 密码 | 角色 |
|--------|------|------|
| admin  | admin123 | ADMIN |
| user   | user123  | USER |

---

## 2. 菜单管理

### 2.1 获取菜单树

获取当前用户的权限菜单树（动态过滤）。

**接口地址**: `GET /api/menus/tree`

**请求头**: `Authorization: Bearer {token}`

**响应示例**:
```json
[
  {
    "id": 1,
    "menuName": "系统管理",
    "parentId": 0,
    "path": "/system",
    "component": "Layout",
    "icon": "setting",
    "sortOrder": 1,
    "permission": null,
    "type": "MENU",
    "children": [
      {
        "id": 2,
        "menuName": "用户管理",
        "parentId": 1,
        "path": "/system/users",
        "component": "system/user/index",
        "icon": "user",
        "sortOrder": 1,
        "permission": "system:user:query",
        "type": "MENU",
        "children": []
      }
    ]
  }
]
```

**字段说明**:
- `id`: 菜单ID
- `menuName`: 菜单名称
- `parentId`: 父菜单ID (0表示根菜单)
- `path`: 路由路径
- `component`: 组件路径
- `icon`: 图标名称
- `sortOrder`: 排序号
- `permission`: 权限标识
- `type`: 类型 (MENU目录, BUTTON按钮)
- `children`: 子菜单数组

**权限说明**:
- ADMIN: 返回全部菜单（系统管理+账单管理）
- USER: 仅返回账单管理相关菜单

---

## 3. 账单管理

### 3.1 创建账单

创建一条新的收入或支出记录。

**接口地址**: `POST /api/records`

**请求头**: `Authorization: Bearer {token}`

**请求体**:
```json
{
  "amount": 100.50,                 // 必填，金额，大于0
  "type": "EXPENSE",                // 必填，INCOME收入/EXPENSE支出
  "category": "餐饮",                // 必填，分类名称
  "description": "午餐",             // 可选，描述
  "createDate": "2026-03-12"        // 可选，创建日期，默认当前时间
}
```

**响应示例**:
```json
{
  "id": 1,
  "success": true,
  "message": "创建成功"
}
```

**类型枚举**:
- `INCOME`: 收入
- `EXPENSE`: 支出

**常见分类示例**:
- 收入: 工资、奖金、投资、其他收入
- 支出: 餐饮、交通、购物、娱乐、医疗、教育、住房、其他支出

---

### 3.2 查询账单列表

分页查询当前用户的账单记录。

**接口地址**: `GET /api/records`

**请求头**: `Authorization: Bearer {token}`

**查询参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| type | String | 否 | 类型: INCOME/EXPENSE |
| category | String | 否 | 分类名称 |
| startDate | LocalDate | 否 | 开始日期 (yyyy-MM-dd) |
| endDate | LocalDate | 否 | 结束日期 (yyyy-MM-dd) |

**响应示例**:
```json
[
  {
    "id": 1,
    "amount": 100.50,
    "type": "EXPENSE",
    "category": "餐饮",
    "description": "午餐",
    "userId": 1,
    "createTime": "2026-03-12T10:30:00",
    "updateTime": "2026-03-12T10:30:00"
  }
]
```

---

### 3.3 更新账单

修改指定ID的账单记录。

**接口地址**: `PUT /api/records/{id}`

**请求头**: `Authorization: Bearer {token}`

**路径参数**:
- `id`: 账单ID

**请求体**:
```json
{
  "amount": 150.00,
  "type": "EXPENSE",
  "category": "交通",
  "description": "打车"
}
```

**响应示例**:
```json
{
  "success": true,
  "message": "更新成功"
}
```

**权限说明**:
- 只能更新自己的账单
- ADMIN可以更新任何用户的账单

---

### 3.4 删除账单

逻辑删除指定ID的账单记录。

**接口地址**: `DELETE /api/records/{id}`

**请求头**: `Authorization: Bearer {token}`

**路径参数**:
- `id`: 账单ID

**响应示例**:
```json
{
  "success": true,
  "message": "删除成功"
}
```

**说明**:
- 逻辑删除 (deleted=1)，数据仍在数据库中
- 只能删除自己的账单

---

## 4. 统计分析

### 4.1 汇总统计

统计指定时间范围内的收入、支出和结余。

**接口地址**: `GET /api/statistics/summary`

**请求头**: `Authorization: Bearer {token}`

**查询参数**:
| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| startDate | LocalDate | 否 | 前30天 | 开始日期 |
| endDate | LocalDate | 否 | 今天 | 结束日期 |

**响应示例**:
```json
{
  "income": 15000.00,
  "expense": 8000.00,
  "balance": 7000.00
}
```

**计算逻辑**:
- `balance = income - expense`

---

### 4.2 按分类统计

统计指定类型、时间范围内各分类的金额汇总。

**接口地址**: `GET /api/statistics/category`

**请求头**: `Authorization: Bearer {token}`

**查询参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| type | String | 是 | INCOME或EXPENSE |
| startDate | LocalDate | 否 | 开始日期，默认前30天 |
| endDate | LocalDate | 否 | 结束日期，默认今天 |

**响应示例**:
```json
[
  {
    "category": "餐饮",
    "total_amount": 2500.00
  },
  {
    "category": "交通",
    "total_amount": 800.00
  }
]
```

**说明**:
- 结果按总金额降序排列

---

### 4.3 月度统计

统计指定年份每个月的收支金额。

**接口地址**: `GET /api/statistics/monthly`

**请求头**: `Authorization: Bearer {token}`

**查询参数**:
| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| year | Integer | 否 | 当前年份 | 年份，如2026 |

**响应示例**:
```json
[
  {
    "month": 1,
    "total_amount": 5000.00
  },
  {
    "month": 2,
    "total_amount": 4500.00
  }
]
```

**说明**:
- 只返回有账单记录的月份
- 按月份升序排列

---

## 5. 管理员接口

### 5.1 获取所有角色

**接口地址**: `GET /api/admin/roles`

**请求头**: `Authorization: Bearer {token}`

**权限**: 仅ADMIN角色

**响应示例**:
```json
{
  "success": true,
  "message": "角色管理功能待完善"
}
```

---

### 5.2 获取所有菜单

**接口地址**: `GET /api/admin/menus`

**请求头**: `Authorization: Bearer {token}`

**权限**: 仅ADMIN角色

**响应示例**:
```json
{
  "success": true,
  "message": "菜单管理功能待完善"
}
```

---

## 6. 错误码说明

| HTTP状态码 | 错误信息 | 说明 |
|-----------|---------|------|
| 200 | success: true | 请求成功 |
| 200 | success: false | 业务逻辑错误（如用户名重复） |
| 401 | 用户名或密码错误 | 认证失败 |
| 401 | Token无效 | JWT Token过期或非法 |
| 403 | 权限不足 | 无权访问该接口 |
| 400 | 参数验证失败 | 请求参数不符合要求 |

---

## 7. 数据示例

### 7.1 初始数据

系统初始化时会自动创建：
- 2个用户 (admin, user)
- 2个角色 (ADMIN, USER)
- 8个菜单 (系统管理4个 + 账单管理4个)
- 角色权限关联

### 7.2 账单表字段

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键，自增 |
| amount | DECIMAL(15,2) | 金额 |
| type | VARCHAR(20) | 类型: INCOME/EXPENSE |
| category | VARCHAR(50) | 分类 |
| description | VARCHAR(500) | 描述 |
| user_id | BIGINT | 用户ID (外键) |
| create_time | TIMESTAMP | 创建时间 |
| update_time | TIMESTAMP | 更新时间 |
| deleted | INT | 删除标记 (0正常, 1删除) |

---

## 8. 使用提示

1. **测试流程**:
   - 注册新用户
   - 登录获取Token
   - 创建几条账单数据
   - 查询账单列表
   - 统计汇总数据
   - 尝试更新/删除

2. **权限测试**:
   - 使用user登录，只能访问账单相关接口
   - 使用admin登录，可以访问所有接口

3. **H2控制台**:
   - 访问: http://localhost:8080/h2-console
   - JDBC URL: `jdbc:h2:mem:accounting_db`
   - 用户名: `sa`
   - 密码: (空)
   - 可查看 `user`, `record`, `role`, `menu` 等表数据

---

## 9. 扩展开发建议

### 9.1 分页查询改造

当前账单查询返回全部数据，生产环境应添加分页：

```java
// RecordQueryRequest 添加
private Integer page = 1;
private Integer size = 20;

// Controller返回 Page<RecordVO>
// Service使用 MP 分页插件
```

### 9.2 数据导出

添加Excel导出接口:

```java
@GetMapping("/records/export/excel")
public ResponseEntity<byte[]> exportExcel(...) {
    // 使用 Apache POI 生成Excel
}
```

### 9.3 文件上传

支持账单截图上传:

```java
@PostMapping("/records/{id}/attachment")
public ResponseEntity<?> uploadAttachment(...) {
    // 使用 OSS 或本地文件存储
}
```

### 9.4 多条件组合查询

增强RecordQueryRequest，支持更多查询条件:
- 金额区间
- 关键词模糊匹配
- 多分类选择

---

**文档版本**: v1.0
**最后更新**: 2026-03-12