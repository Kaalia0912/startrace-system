-- 已有数据库升级：物料主数据批量导入权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(2307, '物料导入', 2301, 6, '', '', NULL, '', 1, 0, 'F', '0', '0', 'basedata:material:import', '#', 'admin', sysdate(), '', NULL, NULL)
ON DUPLICATE KEY UPDATE menu_name = VALUES(menu_name), perms = VALUES(perms), status = VALUES(status);

INSERT IGNORE INTO sys_role_menu (role_id, menu_id) VALUES
(1, 2307),
(2, 2307);
