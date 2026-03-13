-- 初始化数据

-- 管理员用户 (密码: admin123, 已BCrypt加密)
INSERT INTO users (username, password, email, enabled, create_time, update_time) VALUES
('admin', '$2b$10$skkiDNo1wxHCDtYUG2ZSK.o8nUbr.HcgaCUe52IcVrNoWHEXry3WG', 'admin@example.com', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 普通用户 (密码: user123)
INSERT INTO users (username, password, email, enabled, create_time, update_time) VALUES
('user', '$2b$10$YOWPqWxilHrGMjZ6jtIWse3t0n0x0etfRgBnp90y5qi7vx1Jb/wUO', 'user@example.com', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 角色
INSERT INTO roles (name, code, description, create_time) VALUES
('超级管理员', 'ADMIN', '拥有所有权限', CURRENT_TIMESTAMP),
('普通用户', 'USER', '仅账单管理权限', CURRENT_TIMESTAMP);

-- 用户角色关联
INSERT INTO user_roles (user_id, role_id, create_time) VALUES
(1, 1, CURRENT_TIMESTAMP),  -- admin -> ADMIN
(2, 2, CURRENT_TIMESTAMP);  -- user -> USER

-- 菜单数据 (按照设计层级)
INSERT INTO menus (menu_name, parent_id, path, component, icon, sort_order, permission, type, status, create_time) VALUES
-- 系统管理 (parent_id=0)
('系统管理', 0, '/system', 'Layout', 'setting', 1, NULL, 'MENU', 0, CURRENT_TIMESTAMP),
('用户管理', 1, '/system/users', 'system/user/index', 'user', 1, 'system:user:query', 'MENU', 0, CURRENT_TIMESTAMP),
('角色管理', 1, '/system/roles', 'system/role/index', 'role', 2, 'system:role:query', 'MENU', 0, CURRENT_TIMESTAMP),
('菜单管理', 1, '/system/menus', 'system/menu/index', 'menu', 3, 'system:menu:query', 'MENU', 0, CURRENT_TIMESTAMP),

-- 账单管理 (parent_id=0)
('账单管理', 0, '/accounting', 'Layout', 'wallet', 2, NULL, 'MENU', 0, CURRENT_TIMESTAMP),
('账单列表', 5, '/accounting/records', 'accounting/record/index', 'list', 1, 'accounting:record:query', 'MENU', 0, CURRENT_TIMESTAMP),
('统计分析', 5, '/accounting/statistics', 'accounting/statistics/index', 'chart', 2, 'accounting:record:statistics', 'MENU', 0, CURRENT_TIMESTAMP),
('数据导出', 5, '/accounting/export', 'accounting/export/index', 'download', 3, 'accounting:record:export', 'MENU', 0, CURRENT_TIMESTAMP);

-- 角色菜单关联
-- ADMIN 角色拥有所有菜单 (1-8)
INSERT INTO role_menus (role_id, menu_id, create_time) VALUES
(1, 1, CURRENT_TIMESTAMP),
(1, 2, CURRENT_TIMESTAMP),
(1, 3, CURRENT_TIMESTAMP),
(1, 4, CURRENT_TIMESTAMP),
(1, 5, CURRENT_TIMESTAMP),
(1, 6, CURRENT_TIMESTAMP),
(1, 7, CURRENT_TIMESTAMP),
(1, 8, CURRENT_TIMESTAMP);

-- USER 角色仅拥有账单管理菜单 (5-8)
INSERT INTO role_menus (role_id, menu_id, create_time) VALUES
(2, 5, CURRENT_TIMESTAMP),
(2, 6, CURRENT_TIMESTAMP),
(2, 7, CURRENT_TIMESTAMP),
(2, 8, CURRENT_TIMESTAMP);
