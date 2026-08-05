-- ============================================
-- 企业档案扩展：联系人 + 看板 + 详情页菜单
-- ============================================

-- 企业联系人菜单（C）
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(2110, '企业联系人', 2100, 2, 'contact', 'enterprise/contact/index', NULL, '', 1, 0, 'C', '0', '0', 'enterprise:contact:list', 'people', 'admin', sysdate(), '', NULL, '企业联系人菜单');

-- 联系人按钮权限（F）
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(2111, '联系人查询', 2110, 1, '', '', NULL, '', 1, 0, 'F', '0', '0', 'enterprise:contact:query', '#', 'admin', sysdate(), '', NULL, NULL),
(2112, '联系人新增', 2110, 2, '', '', NULL, '', 1, 0, 'F', '0', '0', 'enterprise:contact:add', '#', 'admin', sysdate(), '', NULL, NULL),
(2113, '联系人修改', 2110, 3, '', '', NULL, '', 1, 0, 'F', '0', '0', 'enterprise:contact:edit', '#', 'admin', sysdate(), '', NULL, NULL),
(2114, '联系人删除', 2110, 4, '', '', NULL, '', 1, 0, 'F', '0', '0', 'enterprise:contact:remove', '#', 'admin', sysdate(), '', NULL, NULL),
(2115, '联系人导出', 2110, 5, '', '', NULL, '', 1, 0, 'F', '0', '0', 'enterprise:contact:export', '#', 'admin', sysdate(), '', NULL, NULL);

-- 统计看板菜单（C）
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(2120, '统计看板', 2100, 3, 'dashboard', 'enterprise/dashboard/index', NULL, '', 1, 0, 'C', '0', '0', 'enterprise:dashboard:list', 'chart', 'admin', sysdate(), '', NULL, '统计看板菜单');

-- 企业详情页（隐藏菜单，visible=1 只注册路由不显示在侧边栏）
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(2130, '企业详情', 2100, 4, 'detail', 'enterprise/detail/index', NULL, '', 1, 0, 'C', '1', '0', 'enterprise:enterprise:query', 'edit', 'admin', sysdate(), '', NULL, '企业详情页（隐藏）');

-- 角色绑定（超级管理员 + 普通角色）
INSERT INTO sys_role_menu (role_id, menu_id) VALUES
(1, 2110), (1, 2111), (1, 2112), (1, 2113), (1, 2114), (1, 2115), (1, 2120), (1, 2130),
(2, 2110), (2, 2111), (2, 2112), (2, 2113), (2, 2114), (2, 2115), (2, 2120), (2, 2130);
