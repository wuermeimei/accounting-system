# SpringBoot 记账系统 Demo

基于 Spring Boot 2.7.x + MyBatis Plus + Spring Security + H2 的记账系统（REST API）。

## 技术栈

- **Spring Boot**: 2.7.15
- **MyBatis Plus**: 3.5.4
- **Spring Security**: 5.7.x
- **数据库**: H2 (内存数据库)
- **认证**: JWT (Token)
- **Java**: 11+

## 功能特性

- **用户认证**: 注册、登录 (JWT)
- **权限管理**: 基于角色的权限控制 (RBAC)
- **菜单管理**: 树形菜单结构，支持动态权限过滤
- **账单管理**: CRUD 操作，支持条件查询
- **统计分析**: 按时间范围、分类统计收入/支出
- **数据导出**: 支持 Excel/CSV 导出（功能预留）
- **角色权限**: ADMIN 拥有全部权限，USER 仅有账单管理权限

## 项目结构

```
src/main/java/com/example/accounting/
├── config/          # 配置类
│   ├── SecurityConfig.java
│   └── MybatisPlusConfig.java
├── controller/      # 控制器
│   ├── AuthController.java
│   ├── ApiController.java
│   └── AdminController.java
├── entity/          # 实体类
│   ├── User.java
│   ├── Role.java
│   ├── Menu.java
│   ├── Record.java
│   ├── UserRole.java
│   └── RoleMenu.java
├── mapper/          # Mapper接口
│   ├── UserMapper.java
│   ├── RoleMapper.java
│   ├── MenuMapper.java
│   ├── RecordMapper.java
│   ├── UserRoleMapper.java
│   └── RoleMenuMapper.java
├── service/         # 业务服务层
│   ├── AuthService.java
│   ├── UserService.java
│   ├── MenuService.java
│   ├── RecordService.java
│   ├── StatisticsService.java
│   └── RoleService.java
├── dto/             # 数据传输对象
│   ├── LoginRequest.java
│   ├── RegisterRequest.java
│   ├── RecordCreateRequest.java
│   ├── RecordUpdateRequest.java
│   └── RecordQueryRequest.java
├── vo/              # 视图对象
│   ├── RecordVO.java
│   └── StatisticVO.java
├── security/        # 安全相关
│   ├── JwtTokenProvider.java
│   ├── JwtAuthenticationFilter.java
│   └── CustomUserDetailsService.java
├── util/            # 工具类
│   └── SecurityUtils.java
└── handler/         # 异常处理
    └── GlobalExceptionHandler.java

src/main/resources/
├── mapper/          # MyBatis XML映射文件
│   ├── UserMapper.xml
│   ├── RoleMapper.xml
│   ├── MenuMapper.xml
│   └── RecordMapper.xml
├── schema.sql       # 建表语句
├── data.sql         # 初始化数据
└── application.yml  # 配置文件
```

## 快速启动

### 环境要求

- JDK 11 或更高版本
- Maven 3.6+

### 运行步骤

1. **编译打包**
   ```bash
   mvn clean package
   ```

2. **运行应用**
   ```bash
   java -jar target/accounting-system-1.0.0.jar
   ```

   或使用Maven：
   ```bash
   mvn spring-boot:run
   ```

3. **访问H2控制台** (可选，用于调试)
   - URL: http://localhost:8080/h2-console
   - JDBC URL: `jdbc:h2:mem:accounting_db`
   - 用户名: `sa`
   - 密码: (留空)

### API 接口

#### 1. 用户认证

**注册**
```
POST /api/auth/register
Content-Type: application/json

{
  "username": "testuser",
  "password": "test123",
  "confirmPassword": "test123",
  "email": "test@example.com"
}
```

**登录**
```
POST /api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}
```

响应：
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "username": "admin"
}
```

#### 2. 账单管理 (需要认证)

**创建账单**
```
POST /api/records
Authorization: Bearer {token}
Content-Type: application/json

{
  "amount": 100.50,
  "type": "EXPENSE",  // INCOME 或 EXPENSE
  "category": "餐饮",
  "description": "午餐",
  "createDate": "2026-03-12"  // 可选
}
```

**查询账单**
```
GET /api/records?type=EXPENSE&startDate=2026-03-01&endDate=2026-03-12
Authorization: Bearer {token}
```

**更新账单**
```
PUT /api/records/{id}
Authorization: Bearer {token}
Content-Type: application/json

{
  "amount": 150.00,
  "type": "EXPENSE",
  "category": "交通",
  "description": "打车"
}
```

**删除账单**
```
DELETE /api/records/{id}
Authorization: Bearer {token}
```

#### 3. 统计分析 (需要认证)

**汇总统计**
```
GET /api/statistics/summary?startDate=2026-03-01&endDate=2026-03-12
Authorization: Bearer {token}
```

**按分类统计**
```
GET /api/statistics/category?type=EXPENSE&startDate=2026-03-01&endDate=2026-03-12
Authorization: Bearer {token}
```

**月度统计**
```
GET /api/statistics/monthly?year=2026
Authorization: Bearer {token}
```

#### 4. 菜单管理 (需要认证)

```
GET /api/menus/tree
Authorization: Bearer {token}
```

返回当前用户的菜单树（基于权限过滤）。

### 初始账户

| 角色 | 用户名 | 密码 | 权限 |
|------|--------|------|------|
| ADMIN | admin | admin123 | 全部 |
| USER | user | user123 | 账单管理 |

## 数据库设计

### 实体关系图

```
User (用户)
  ↑   ↓
UserRole (用户角色关联)
  ↑   ↓
Role (角色)
  ↑   ↓
RoleMenu (角色菜单关联)
  ↑   ↓
Menu (菜单)

User (用户)
  ↑   ↓
Record (账单)
```

### 主要表结构

- **user**: 用户表
- **role**: 角色表 (ADMIN, USER)
- **menu**: 菜单表
- **record**: 账单表
- **user_role**: 用户-角色关联表
- **role_menu**: 角色-菜单关联表

## 扩展建议

1. **添加前端**: 使用 Vue/React + Element UI/Ant Design
2. **数据导出**: 实现 Excel/CSV 导出功能（使用 Apache POI）
3. **分页查询**: 添加 PageHelper 分页支持
4. **文件上传**: 支持账单截图上传
5. **多数据源**: 支持 MySQL/PostgreSQL 生产环境
6. **缓存**: 添加 Redis 缓存菜单和权限
7. **API文档**: 集成 Swagger/OpenAPI 3
8. **单元测试**: 完善 Service/Controller 测试

## 注意事项

- 本项目为 Demo，生产环境需要：
  - 更换更强的密钥
  - 启用 HTTPS
  - 添加 CSRF 防护
  - 完善异常处理和日志
  - 添加API限流
  - 数据库连接池优化
- H2 数据库为内存数据库，重启后数据丢失
- JWT Token 默认有效期 24 小时

## 许可证

MIT License

---

创建时间: 2026-03-12