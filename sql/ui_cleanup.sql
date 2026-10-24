-- ============================================
-- 企业版界面收敛
-- 1. 隐藏 RuoYi 开发工具
-- 2. 移除已废弃的企业统计看板菜单
-- 已有数据库执行：mysql -u root -p ry_vue < ui_cleanup.sql
-- 脚本可重复执行
-- ============================================

START TRANSACTION;

UPDATE sys_menu
SET visible = '1',
    update_by = 'admin',
    update_time = sysdate()
WHERE menu_id IN (3, 115, 116, 117)
   OR parent_id IN (3, 115, 116, 117);

DELETE FROM sys_role_menu WHERE menu_id = 2120;
DELETE FROM sys_menu WHERE menu_id = 2120;

COMMIT;
