-- ============================================
-- P2 收尾：采购单 + 原料单价 + 加工费参数 + 毛利统计菜单
-- 说明：含 ALTER TABLE，只执行一次（重复执行会报列已存在）
-- 全新部署：mysql -u root -p ry_vue < p2b_purchase_profit.sql
-- ============================================

-- 1. 物料主数据补"原料单价"（标准成本用）
ALTER TABLE md_material
  ADD COLUMN raw_price DECIMAL(10,2) DEFAULT NULL COMMENT '原料单价(元/kg)';

-- 2. 采购单
CREATE TABLE IF NOT EXISTS pur_order (
  purchase_id   BIGINT(20)    NOT NULL AUTO_INCREMENT COMMENT '主键',
  purchase_no   VARCHAR(64)   NOT NULL COMMENT '采购单号',
  supplier      VARCHAR(128)  DEFAULT '' COMMENT '供应商',
  material_code VARCHAR(64)   DEFAULT '' COMMENT '物料编码',
  material      VARCHAR(64)   DEFAULT '' COMMENT '材质',
  spec          VARCHAR(64)   DEFAULT '' COMMENT '规格',
  quantity      DECIMAL(18,3) DEFAULT NULL COMMENT '数量',
  weight        DECIMAL(12,2) DEFAULT NULL COMMENT '重量(kg)',
  unit_price    DECIMAL(18,2) DEFAULT NULL COMMENT '单价(元/kg)',
  amount        DECIMAL(18,2) DEFAULT NULL COMMENT '金额(元)',
  purchase_date DATE          DEFAULT NULL COMMENT '采购日期',
  expect_date   DATE          DEFAULT NULL COMMENT '预计到货',
  status        CHAR(1)       DEFAULT '0' COMMENT '状态（0待收货 1已收货）',
  create_by     VARCHAR(64)   DEFAULT '' COMMENT '创建者',
  create_time   DATETIME      DEFAULT NULL COMMENT '创建时间',
  update_by     VARCHAR(64)   DEFAULT '' COMMENT '更新者',
  update_time   DATETIME      DEFAULT NULL COMMENT '更新时间',
  remark        VARCHAR(500)  DEFAULT '' COMMENT '备注',
  PRIMARY KEY (purchase_id),
  UNIQUE KEY uk_purchase_no (purchase_no),
  KEY idx_purchase_supplier (supplier)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='采购单';

-- 3. 加工费单价参数（毛利统计用）
INSERT INTO sys_config (config_name, config_key, config_value, config_type, create_by, create_time, remark) VALUES
('加工费单价(元/kg)', 'erp.process.cost.per.kg', '2.50', 'Y', 'admin', sysdate(), '毛利统计用：每公斤加工成本（含轧制、固溶、拉矫、分剪等）');

-- 4. 菜单与权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(2460, '采购单', 2400, 5, 'purchase', 'pur/purchase/index', NULL, '', 1, 0, 'C', '0', '0', 'pur:order:list', 'shopping', 'admin', sysdate(), '', NULL, '采购单'),
(2830, '毛利统计', 2800, 3, 'profit', 'fin/profit/index', NULL, '', 1, 0, 'C', '0', '0', 'fin:profit:list', 'chart', 'admin', sysdate(), '', NULL, '毛利统计');

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(2461, '采购查询', 2460, 1, '', '', NULL, '', 1, 0, 'F', '0', '0', 'pur:order:query', '#', 'admin', sysdate(), '', NULL, NULL),
(2462, '采购新增', 2460, 2, '', '', NULL, '', 1, 0, 'F', '0', '0', 'pur:order:add', '#', 'admin', sysdate(), '', NULL, NULL),
(2463, '采购修改', 2460, 3, '', '', NULL, '', 1, 0, 'F', '0', '0', 'pur:order:edit', '#', 'admin', sysdate(), '', NULL, NULL),
(2464, '采购删除', 2460, 4, '', '', NULL, '', 1, 0, 'F', '0', '0', 'pur:order:remove', '#', 'admin', sysdate(), '', NULL, NULL),
(2465, '采购导出', 2460, 5, '', '', NULL, '', 1, 0, 'F', '0', '0', 'pur:order:export', '#', 'admin', sysdate(), '', NULL, NULL),
(2466, '采购收货', 2460, 6, '', '', NULL, '', 1, 0, 'F', '0', '0', 'pur:order:receive', '#', 'admin', sysdate(), '', NULL, NULL),
(2831, '毛利查询', 2830, 1, '', '', NULL, '', 1, 0, 'F', '0', '0', 'fin:profit:list', '#', 'admin', sysdate(), '', NULL, NULL);

INSERT INTO sys_role_menu (role_id, menu_id) VALUES
(1, 2460), (1, 2461), (1, 2462), (1, 2463), (1, 2464), (1, 2465), (1, 2466),
(1, 2830), (1, 2831),
(2, 2460), (2, 2461), (2, 2462), (2, 2463), (2, 2464), (2, 2465), (2, 2466),
(2, 2830), (2, 2831);
