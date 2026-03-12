# 项目结构总览

## 目录树

```
accounting-system/
├── pom.xml
├── README.md
├── API_DOCS.md
├── postman_collection.json
├── .gitignore
└── src/
    ├── main/
    │   ├── java/com/example/accounting/
    │   │   ├── AccountingApplication.java      # 启动类
    │   │   ├── config/
    │   │   │   ├── SecurityConfig.java        # Spring Security配置
    │   │   │   ├── MybatisPlusConfig.java     # MyBatis Plus配置
    │   │   │   └── PasswordConfig.java        # 密码编码器配置
    │   │   ├── controller/
    │   │   │   ├── ApiController.java         # API主控制器
    │   │   │   └── AdminController.java       # 管理员控制器
    │   │   ├── entity/
    │   │   │   ├── User.java                  # 用户实体
    │   │   │   ├── Role.java                  # 角色实体
    │   │   │   ├── Menu.java                  # 菜单实体
    │   │   │   ├── Record.java                # 账单实体
    │   │   │   ├── UserRole.java              # 用户角色关联
    │   │   │   └── RoleMenu.java              # 角色菜单关联
    │   │   ├── mapper/
    │   │   │   ├── UserMapper.java            # 用户Mapper
    │   │   │   ├── RoleMapper.java            # 角色Mapper
    │   │   │   ├── MenuMapper.java            # 菜单Mapper
    │   │   │   ├── RecordMapper.java          # 账单Mapper
    │   │   │   ├── UserRoleMapper.java        # 用户角色Mapper
    │   │   │   └── RoleMenuMapper.java        # 角色菜单Mapper
    │   │   ├── service/
    │   │   │   ├── AuthService.java           # 认证服务
    │   │   │   ├── UserService.java           # 用户服务
    │   │   │   ├── MenuService.java           # 菜单服务
    │   │   │   ├── RecordService.java         # 账单服务
    │   │   │   ├── StatisticsService.java     # 统计服务
    │   │   │   └── RoleService.java           # 角色服务
    │   │   ├── dto/
    │   │   │   ├── LoginRequest.java          # 登录请求
    │   │   │   ├── RegisterRequest.java       # 注册请求
    │   │   │   ├── RecordCreateRequest.java   # 创建账单请求
    │   │   │   ├── RecordUpdateRequest.java   # 更新账单请求
    │   │   │   └── RecordQueryRequest.java    # 查询账单请求
    │   │   ├── vo/
    │   │   │   ├── RecordVO.java              # 账单视图对象
    │   │   │   └── StatisticVO.java           # 统计视图对象
    │   │   ├── security/
    │   │   │   ├── JwtTokenProvider.java      # JWT Token工具
    │   │   │   ├── JwtAuthenticationFilter.java  # JWT过滤器
    │   │   │   └── CustomUserDetailsService.java # 用户详情服务
    │   │   ├── util/
    │   │   │   └── SecurityUtils.java         # 安全工具类
    │   │   └── handler/
    │   │       └── GlobalExceptionHandler.java  # 全局异常处理器
    │   └── resources/
    │       ├── mapper/
    │       │   ├── UserMapper.xml
    │       │   ├── RoleMapper.xml
    │       │   ├── MenuMapper.xml
    │       │   └── RecordMapper.xml
    │       ├── schema.sql                      # 建表SQL
    │       ├── data.sql                        # 初始化数据
    │       └── application.yml                # 配置文件
    └── test/
        └── java/com/example/accounting/
            └── AccountingApplicationTests.java  # 基础测试

## 文件统计

- Java文件: 37个
- XML文件: 5个 (Mapper + pom.xml)
- SQL文件: 2个
- 配置文件: 2个 (application.yml, .gitignore)
- 文档文件: 3个 (README.md, API_DOCS.md, postman_collection.json)

## 核心类职责

### 配置层 (config)
- **SecurityConfig**: 配置Spring Security, JWT过滤器, 权限规则
- **MybatisPlusConfig**: 配置MyBatis Plus分页插件
- **PasswordConfig**: 密码编码器Bean

### 控制器层 (controller)
- **ApiController**: 主要API接口 (登录、注册、账单、统计、菜单)
- **AdminController**: 管理员接口预留 (角色、菜单管理)

### 实体层 (entity)
- **User**: 用户 (id, username, password, email, enabled, deleted, create_time, update_time)
- **Role**: 角色 (id, name, code, description)
- **Menu**: 菜单 (id, menuName, parentId, path, component, icon, sortOrder, permission, type, status)
- **Record**: 账单 (id, amount, type, category, description, user_id, create_time, update_time, deleted)
- **UserRole**: 用户角色关联 (user_id, role_id)
- **RoleMenu**: 角色菜单关联 (role_id, menu_id)

### 数据层 (mapper)
- **BaseMapper<T>**: MyBatis Plus基础接口，提供CRUD
- 自定义XML: 复杂查询（递归CTE构建菜单树、聚合统计）

### 服务层 (service)
- **AuthService**: 登录、注册业务逻辑
- **UserService**: 用户管理
- **MenuService**: 菜单树构建
- **RecordService**: 账单CRUD + 条件查询
- **StatisticsService**: 统计计算 (收入/支出/分类/月度)
- **RoleService**: 角色权限分配

### 安全层 (security)
- **JwtTokenProvider**: JWT token生成、验证、解析
- **JwtAuthenticationFilter**: 请求过滤器，验证Token并设置SecurityContext
- **CustomUserDetailsService**: Spring Security UserDetailsService实现，从数据库加载用户

### 数据传输 (dto/vo)
- **DTO**: 请求数据传输对象 (入参校验)
- **VO**: 视图对象 (出参封装)

### 异常处理 (handler)
- **GlobalExceptionHandler**: 统一异常处理，返回标准JSON格式

## 技术亮点

1. **RBAC权限模型**: 用户-角色-菜单三层权限控制
2. **JWT认证**: Token无状态认证，适合前后端分离
3. **MyBatis Plus**: 简化CRUD，支持分页、逻辑删除
4. **递归菜单树**: 使用H2 CTE (Common Table Expressions) 高效构建无限层级菜单
5. **动态菜单过滤**: 只返回当前用户有权访问的菜单
6. **Spring Security集成**: @PreAuthorize注解声明式权限控制
7. **H2内存数据库**: 零配置，即插即用
8. **RESTful API**: 标准HTTP方法，JSON响应

## 部署方式

### 方式一: 可执行JAR
```bash
mvn clean package
java -jar target/accounting-system-1.0.0.jar
```

### 方式二: Maven运行
```bash
mvn spring-boot:run
```

### 方式三: IDE运行
直接运行 `AccountingApplication.java` 的main方法

## 访问地址

- **应用API**: http://localhost:8080/api
- **H2控制台**: http://localhost:8080/h2-console
- **健康检查**: http://localhost:8080/actuator/health (需添加actuator依赖)

## 快速测试

1. 启动应用
2. 访问 http://localhost:8080/api/auth/login (POST)
   ```json
   {"username": "admin", "password": "admin123"}
   ```
3. 复制返回的token
4. 访问 http://localhost:8080/api/menus/tree (GET)
   Header: `Authorization: Bearer <token>`
5. 创建账单测试

详细API见 `API_DOCS.md`

---

**创建时间**: 2026-03-12
**技术栈**: SpringBoot 2.7.15 + MyBatis Plus 3.5.4 + H2 + Spring Security