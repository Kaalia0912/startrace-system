-- ============================================
-- 企业档案模块：字典 + 菜单 + 角色绑定
-- ============================================

-- 1. 字典类型
INSERT INTO sys_dict_type (dict_id, dict_name, dict_type, status, create_by, create_time, remark) VALUES
(11, '企业状态', 'enterprise_status', '0', 'admin', sysdate(), '企业档案-状态'),
(12, '所属行业', 'enterprise_industry', '0', 'admin', sysdate(), '企业档案-行业'),
(13, '企业类型', 'enterprise_type', '0', 'admin', sysdate(), '企业档案-类型'),
(14, '企业规模', 'enterprise_scale', '0', 'admin', sysdate(), '企业档案-规模');

-- 2. 字典数据
INSERT INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) VALUES
-- 企业状态
(1, '在营', '0', 'enterprise_status', '', 'success', 'Y', '0', 'admin', sysdate(), NULL),
(2, '注销', '1', 'enterprise_status', '', 'danger', 'N', '0', 'admin', sysdate(), NULL),
-- 所属行业
(1, '互联网/软件', 'it', 'enterprise_industry', '', '', 'N', '0', 'admin', sysdate(), NULL),
(2, '智能制造', 'manufacturing', 'enterprise_industry', '', '', 'N', '0', 'admin', sysdate(), NULL),
(3, '生物医药', 'biomedicine', 'enterprise_industry', '', '', 'N', '0', 'admin', sysdate(), NULL),
(4, '新能源', 'new_energy', 'enterprise_industry', '', '', 'N', '0', 'admin', sysdate(), NULL),
(5, '新材料', 'new_material', 'enterprise_industry', '', '', 'N', '0', 'admin', sysdate(), NULL),
(6, '金融服务', 'finance', 'enterprise_industry', '', '', 'N', '0', 'admin', sysdate(), NULL),
(7, '节能环保', 'env_protect', 'enterprise_industry', '', '', 'N', '0', 'admin', sysdate(), NULL),
(8, '其他', 'other', 'enterprise_industry', '', '', 'N', '0', 'admin', sysdate(), NULL),
-- 企业类型
(1, '高新技术企业', 'high_tech', 'enterprise_type', '', 'success', 'N', '0', 'admin', sysdate(), NULL),
(2, '三类企业', 'three_type', 'enterprise_type', '', 'warning', 'N', '0', 'admin', sysdate(), NULL),
(3, '一般企业', 'normal', 'enterprise_type', '', 'info', 'N', '0', 'admin', sysdate(), NULL),
-- 企业规模
(1, '大型', 'large', 'enterprise_scale', '', '', 'N', '0', 'admin', sysdate(), NULL),
(2, '中型', 'medium', 'enterprise_scale', '', '', 'N', '0', 'admin', sysdate(), NULL),
(3, '小型', 'small', 'enterprise_scale', '', '', 'N', '0', 'admin', sysdate(), NULL),
(4, '微型', 'micro', 'enterprise_scale', '', '', 'N', '0', 'admin', sysdate(), NULL);

-- 3. 菜单：目录
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(2100, '企业档案', 0, 6, 'enterprise', NULL, NULL, '', 1, 0, 'M', '0', '0', '', 'building', 'admin', sysdate(), '', NULL, '企业档案目录');

-- 4. 菜单：页面
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(2101, '企业档案', 2100, 1, 'enterprise', 'enterprise/index', NULL, '', 1, 0, 'C', '0', '0', 'enterprise:enterprise:list', 'building', 'admin', sysdate(), '', NULL, '企业档案菜单');

-- 5. 菜单：按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(2102, '企业档案查询', 2101, 1, '', '', NULL, '', 1, 0, 'F', '0', '0', 'enterprise:enterprise:query', '#', 'admin', sysdate(), '', NULL, NULL),
(2103, '企业档案新增', 2101, 2, '', '', NULL, '', 1, 0, 'F', '0', '0', 'enterprise:enterprise:add', '#', 'admin', sysdate(), '', NULL, NULL),
(2104, '企业档案修改', 2101, 3, '', '', NULL, '', 1, 0, 'F', '0', '0', 'enterprise:enterprise:edit', '#', 'admin', sysdate(), '', NULL, NULL),
(2105, '企业档案删除', 2101, 4, '', '', NULL, '', 1, 0, 'F', '0', '0', 'enterprise:enterprise:remove', '#', 'admin', sysdate(), '', NULL, NULL),
(2106, '企业档案导出', 2101, 5, '', '', NULL, '', 1, 0, 'F', '0', '0', 'enterprise:enterprise:export', '#', 'admin', sysdate(), '', NULL, NULL);

-- 6. 角色绑定（超级管理员 + 普通角色）
INSERT INTO sys_role_menu (role_id, menu_id) VALUES
(1, 2100), (1, 2101), (1, 2102), (1, 2103), (1, 2104), (1, 2105), (1, 2106),
(2, 2100), (2, 2101), (2, 2102), (2, 2103), (2, 2104), (2, 2105), (2, 2106);
