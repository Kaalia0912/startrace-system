-- ============================================
-- 销售订单模块（v2：客户信息 + 物料条目，一行一条）
-- 全新部署：mysql -u root -p ry_vue < sale_order.sql
-- 已有 v1 结构升级：先把旧表改名备份（RENAME TABLE ord_sale_order TO ord_sale_order_v1_backup;）
-- 再执行本脚本
-- ============================================

-- 1. 销售订单表（一条订单 = 客户 + 一条产品物料）
CREATE TABLE IF NOT EXISTS ord_sale_order (
  order_id          BIGINT(20)    NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  order_no          VARCHAR(64)   DEFAULT '' COMMENT '订单号',
  order_date        DATE          DEFAULT NULL COMMENT '日期',
  customer_name     VARCHAR(128)  DEFAULT '' COMMENT '客户名称',
  contact_person    VARCHAR(32)   DEFAULT '' COMMENT '联系人',
  contact_phone     VARCHAR(32)   DEFAULT '' COMMENT '联系电话',
  contact_address   VARCHAR(255)  DEFAULT '' COMMENT '联系地址',
  material_code     VARCHAR(64)   DEFAULT '' COMMENT '物料编码',
  product_name      VARCHAR(128)  DEFAULT '' COMMENT '产品名称',
  material          VARCHAR(64)   DEFAULT '' COMMENT '材质',
  spec              VARCHAR(64)   DEFAULT '' COMMENT '规格型号(mm)',
  hardness          VARCHAR(32)   DEFAULT '' COMMENT '硬度(HV)',
  tensile_strength  VARCHAR(32)   DEFAULT '' COMMENT '抗拉(MPa)',
  unit              VARCHAR(16)   DEFAULT '' COMMENT '单位',
  quantity          DECIMAL(18,3) DEFAULT NULL COMMENT '数量',
  unit_price        DECIMAL(18,2) DEFAULT NULL COMMENT '单价',
  amount            DECIMAL(18,2) DEFAULT NULL COMMENT '金额',
  delivery_date     DATE          DEFAULT NULL COMMENT '交期',
  remark            VARCHAR(500)  DEFAULT '' COMMENT '备注',
  create_by         VARCHAR(64)   DEFAULT '' COMMENT '创建者',
  create_time       DATETIME      DEFAULT NULL COMMENT '创建时间',
  update_by         VARCHAR(64)   DEFAULT '' COMMENT '更新者',
  update_time       DATETIME      DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (order_id),
  KEY idx_order_no (order_no),
  KEY idx_customer_name (customer_name),
  KEY idx_order_date (order_date)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='销售订单表';

-- 2. 菜单与权限（订单管理 2200 段，仅全新库需要）
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(2200, '订单管理', 0, 4, 'order', NULL, NULL, '', 1, 0, 'M', '0', '0', '', 'shopping', 'admin', sysdate(), '', NULL, '订单管理菜单');

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(2201, '销售订单', 2200, 1, 'sale', 'order/sale/index', NULL, '', 1, 0, 'C', '0', '0', 'order:sale:list', 'list', 'admin', sysdate(), '', NULL, '销售订单菜单');

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(2202, '销售订单查询', 2201, 1, '', '', NULL, '', 1, 0, 'F', '0', '0', 'order:sale:query', '#', 'admin', sysdate(), '', NULL, NULL),
(2203, '销售订单新增', 2201, 2, '', '', NULL, '', 1, 0, 'F', '0', '0', 'order:sale:add', '#', 'admin', sysdate(), '', NULL, NULL),
(2204, '销售订单修改', 2201, 3, '', '', NULL, '', 1, 0, 'F', '0', '0', 'order:sale:edit', '#', 'admin', sysdate(), '', NULL, NULL),
(2205, '销售订单删除', 2201, 4, '', '', NULL, '', 1, 0, 'F', '0', '0', 'order:sale:remove', '#', 'admin', sysdate(), '', NULL, NULL),
(2206, '销售订单导出', 2201, 5, '', '', NULL, '', 1, 0, 'F', '0', '0', 'order:sale:export', '#', 'admin', sysdate(), '', NULL, NULL),
(2207, '销售订单导入', 2201, 6, '', '', NULL, '', 1, 0, 'F', '0', '0', 'order:sale:import', '#', 'admin', sysdate(), '', NULL, NULL);

-- 3. 角色绑定（超级管理员 + 普通角色）
INSERT INTO sys_role_menu (role_id, menu_id) VALUES
(1, 2200), (1, 2201), (1, 2202), (1, 2203), (1, 2204), (1, 2205),
(1, 2206), (1, 2207),
(2, 2200), (2, 2201), (2, 2202), (2, 2203), (2, 2204), (2, 2205),
(2, 2206), (2, 2207);
