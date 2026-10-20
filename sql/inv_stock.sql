-- ============================================
-- 库存管理 2400 段：原料卷料 / 成品卷 / 发货单 / 标签打印
-- 设计说明：按"卷"为最小管理单位，一卷一行记录，靠状态流转表达生命周期
--           （原料卷：在库→已开卷→已用完；成品卷：在库→已发货）
--           因此不再单建库存流水表，出库历史由发货明细承担
-- 全新部署：mysql -u root -p ry_vue < inv_stock.sql
-- ============================================

-- 1. 原料卷料台账
CREATE TABLE IF NOT EXISTS inv_raw_coil (
  coil_id       BIGINT(20)    NOT NULL AUTO_INCREMENT COMMENT '主键',
  coil_no       VARCHAR(64)   NOT NULL COMMENT '卷号',
  supplier      VARCHAR(128)  DEFAULT '' COMMENT '供应商',
  material      VARCHAR(64)   DEFAULT '' COMMENT '材质',
  spec          VARCHAR(64)   DEFAULT '' COMMENT '规格型号(mm)',
  thickness     DECIMAL(10,3) DEFAULT NULL COMMENT '厚度(mm)',
  width         DECIMAL(10,2) DEFAULT NULL COMMENT '宽度(mm)',
  net_weight    DECIMAL(12,2) DEFAULT NULL COMMENT '净重(kg)',
  hardness      VARCHAR(32)   DEFAULT '' COMMENT '硬度(HV)',
  inner_dia     DECIMAL(10,2) DEFAULT NULL COMMENT '卷内径(mm)',
  arrive_date   DATE          DEFAULT NULL COMMENT '到货日期',
  location      VARCHAR(64)   DEFAULT '' COMMENT '库位',
  qc_status     CHAR(1)       DEFAULT '0' COMMENT '来料检验状态（0待检 1合格 2不合格）',
  coil_status   CHAR(1)       DEFAULT '0' COMMENT '卷状态（0在库 1已开卷 2已用完）',
  create_by     VARCHAR(64)   DEFAULT '' COMMENT '创建者',
  create_time   DATETIME      DEFAULT NULL COMMENT '创建时间',
  update_by     VARCHAR(64)   DEFAULT '' COMMENT '更新者',
  update_time   DATETIME      DEFAULT NULL COMMENT '更新时间',
  remark        VARCHAR(500)  DEFAULT '' COMMENT '备注',
  PRIMARY KEY (coil_id),
  UNIQUE KEY uk_raw_coil_no (coil_no),
  KEY idx_raw_material (material)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='原料卷料台账';

-- 2. 成品卷台账（字段可直接支撑标签打印）
CREATE TABLE IF NOT EXISTS inv_finish_coil (
  coil_id       BIGINT(20)    NOT NULL AUTO_INCREMENT COMMENT '主键',
  coil_no       VARCHAR(64)   NOT NULL COMMENT '卷号',
  label_no      VARCHAR(64)   DEFAULT '' COMMENT '卡号',
  order_no      VARCHAR(64)   DEFAULT '' COMMENT '订单号',
  customer_name VARCHAR(128)  DEFAULT '' COMMENT '客户名称',
  material_code VARCHAR(64)   DEFAULT '' COMMENT '物料编码',
  product_name  VARCHAR(128)  DEFAULT '' COMMENT '产品名称',
  material      VARCHAR(64)   DEFAULT '' COMMENT '钢号',
  spec          VARCHAR(64)   DEFAULT '' COMMENT '规格',
  hardness      VARCHAR(32)   DEFAULT '' COMMENT '性能(硬度HV)',
  surface_ra    VARCHAR(32)   DEFAULT '' COMMENT '表面精度',
  net_weight    DECIMAL(12,2) DEFAULT NULL COMMENT '净重(kg)',
  pieces        INT(11)       DEFAULT NULL COMMENT '件数',
  produce_date  DATE          DEFAULT NULL COMMENT '生产日期',
  checker       VARCHAR(32)   DEFAULT '' COMMENT '检验员',
  location      VARCHAR(64)   DEFAULT '' COMMENT '库位',
  in_date       DATE          DEFAULT NULL COMMENT '入库日期',
  coil_status   CHAR(1)       DEFAULT '0' COMMENT '卷状态（0在库 1已发货）',
  create_by     VARCHAR(64)   DEFAULT '' COMMENT '创建者',
  create_time   DATETIME      DEFAULT NULL COMMENT '创建时间',
  update_by     VARCHAR(64)   DEFAULT '' COMMENT '更新者',
  update_time   DATETIME      DEFAULT NULL COMMENT '更新时间',
  remark        VARCHAR(500)  DEFAULT '' COMMENT '备注',
  PRIMARY KEY (coil_id),
  UNIQUE KEY uk_finish_coil_no (coil_no),
  KEY idx_finish_order_no (order_no),
  KEY idx_finish_customer (customer_name)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='成品卷台账';

-- 3. 发货单（单头）
CREATE TABLE IF NOT EXISTS inv_shipment (
  shipment_id   BIGINT(20)    NOT NULL AUTO_INCREMENT COMMENT '主键',
  shipment_no   VARCHAR(64)   NOT NULL COMMENT '发货单号',
  customer_name VARCHAR(128)  DEFAULT '' COMMENT '客户名称',
  order_no      VARCHAR(64)   DEFAULT '' COMMENT '关联订单号',
  address       VARCHAR(255)  DEFAULT '' COMMENT '送货地址',
  receiver      VARCHAR(32)   DEFAULT '' COMMENT '收货人',
  phone         VARCHAR(32)   DEFAULT '' COMMENT '联系电话',
  shipment_date DATE          DEFAULT NULL COMMENT '发货日期',
  total_weight  DECIMAL(12,2) DEFAULT NULL COMMENT '发货总重(kg)',
  total_pieces  INT(11)       DEFAULT NULL COMMENT '发货总件数',
  status        CHAR(1)       DEFAULT '0' COMMENT '状态（0已发货 1已签收）',
  create_by     VARCHAR(64)   DEFAULT '' COMMENT '创建者',
  create_time   DATETIME      DEFAULT NULL COMMENT '创建时间',
  update_by     VARCHAR(64)   DEFAULT '' COMMENT '更新者',
  update_time   DATETIME      DEFAULT NULL COMMENT '更新时间',
  remark        VARCHAR(500)  DEFAULT '' COMMENT '备注',
  PRIMARY KEY (shipment_id),
  UNIQUE KEY uk_shipment_no (shipment_no),
  KEY idx_shipment_customer (customer_name)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='发货单';

-- 4. 发货单明细（一行一卷）
CREATE TABLE IF NOT EXISTS inv_shipment_item (
  item_id       BIGINT(20)    NOT NULL AUTO_INCREMENT COMMENT '主键',
  shipment_id   BIGINT(20)    NOT NULL COMMENT '发货单ID',
  coil_no       VARCHAR(64)   DEFAULT '' COMMENT '卷号',
  material_code VARCHAR(64)   DEFAULT '' COMMENT '物料编码',
  product_name  VARCHAR(128)  DEFAULT '' COMMENT '产品名称',
  spec          VARCHAR(64)   DEFAULT '' COMMENT '规格',
  net_weight    DECIMAL(12,2) DEFAULT NULL COMMENT '净重(kg)',
  pieces        INT(11)       DEFAULT NULL COMMENT '件数',
  PRIMARY KEY (item_id),
  KEY idx_item_shipment (shipment_id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='发货单明细';

-- 5. 菜单与权限（库存管理 2400 段）
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(2400, '库存管理', 0, 6, 'inv', NULL, NULL, '', 1, 0, 'M', '0', '0', '', 'shopping', 'admin', sysdate(), '', NULL, '库存管理菜单');

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(2410, '原料卷料', 2400, 1, 'rawCoil', 'inv/rawCoil/index', NULL, '', 1, 0, 'C', '0', '0', 'inv:rawcoil:list', 'list', 'admin', sysdate(), '', NULL, '原料卷料台账'),
(2420, '成品卷', 2400, 2, 'finishCoil', 'inv/finishCoil/index', NULL, '', 1, 0, 'C', '0', '0', 'inv:finishcoil:list', 'list', 'admin', sysdate(), '', NULL, '成品卷台账'),
(2430, '发货单', 2400, 3, 'shipment', 'inv/shipment/index', NULL, '', 1, 0, 'C', '0', '0', 'inv:shipment:list', 'form', 'admin', sysdate(), '', NULL, '发货单'),
(2440, '标签打印', 2400, 4, 'label', 'inv/label/index', NULL, '', 1, 0, 'C', '0', '0', 'inv:label:list', 'clipboard', 'admin', sysdate(), '', NULL, '产品标签打印');

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(2411, '原料查询', 2410, 1, '', '', NULL, '', 1, 0, 'F', '0', '0', 'inv:rawcoil:query', '#', 'admin', sysdate(), '', NULL, NULL),
(2412, '原料新增', 2410, 2, '', '', NULL, '', 1, 0, 'F', '0', '0', 'inv:rawcoil:add', '#', 'admin', sysdate(), '', NULL, NULL),
(2413, '原料修改', 2410, 3, '', '', NULL, '', 1, 0, 'F', '0', '0', 'inv:rawcoil:edit', '#', 'admin', sysdate(), '', NULL, NULL),
(2414, '原料删除', 2410, 4, '', '', NULL, '', 1, 0, 'F', '0', '0', 'inv:rawcoil:remove', '#', 'admin', sysdate(), '', NULL, NULL),
(2415, '原料导出', 2410, 5, '', '', NULL, '', 1, 0, 'F', '0', '0', 'inv:rawcoil:export', '#', 'admin', sysdate(), '', NULL, NULL),
(2421, '成品查询', 2420, 1, '', '', NULL, '', 1, 0, 'F', '0', '0', 'inv:finishcoil:query', '#', 'admin', sysdate(), '', NULL, NULL),
(2422, '成品新增', 2420, 2, '', '', NULL, '', 1, 0, 'F', '0', '0', 'inv:finishcoil:add', '#', 'admin', sysdate(), '', NULL, NULL),
(2423, '成品修改', 2420, 3, '', '', NULL, '', 1, 0, 'F', '0', '0', 'inv:finishcoil:edit', '#', 'admin', sysdate(), '', NULL, NULL),
(2424, '成品删除', 2420, 4, '', '', NULL, '', 1, 0, 'F', '0', '0', 'inv:finishcoil:remove', '#', 'admin', sysdate(), '', NULL, NULL),
(2425, '成品导出', 2420, 5, '', '', NULL, '', 1, 0, 'F', '0', '0', 'inv:finishcoil:export', '#', 'admin', sysdate(), '', NULL, NULL),
(2426, '成品发货', 2420, 6, '', '', NULL, '', 1, 0, 'F', '0', '0', 'inv:finishcoil:ship', '#', 'admin', sysdate(), '', NULL, NULL),
(2431, '发货查询', 2430, 1, '', '', NULL, '', 1, 0, 'F', '0', '0', 'inv:shipment:query', '#', 'admin', sysdate(), '', NULL, NULL),
(2432, '发货删除', 2430, 2, '', '', NULL, '', 1, 0, 'F', '0', '0', 'inv:shipment:remove', '#', 'admin', sysdate(), '', NULL, NULL),
(2433, '发货导出', 2430, 3, '', '', NULL, '', 1, 0, 'F', '0', '0', 'inv:shipment:export', '#', 'admin', sysdate(), '', NULL, NULL),
(2441, '标签打印', 2440, 1, '', '', NULL, '', 1, 0, 'F', '0', '0', 'inv:label:print', '#', 'admin', sysdate(), '', NULL, NULL);
(2442, '发货修改', 2430, 4, '', '', NULL, '', 1, 0, 'F', '0', '0', 'inv:shipment:edit', '#', 'admin', sysdate(), '', NULL, NULL);

INSERT INTO sys_role_menu (role_id, menu_id) VALUES
(1, 2400), (1, 2410), (1, 2411), (1, 2412), (1, 2413), (1, 2414), (1, 2415),
(1, 2420), (1, 2421), (1, 2422), (1, 2423), (1, 2424), (1, 2425), (1, 2426),
(1, 2430), (1, 2431), (1, 2432), (1, 2433), (1, 2440), (1, 2441), (1, 2442),
(2, 2400), (2, 2410), (2, 2411), (2, 2412), (2, 2413), (2, 2414), (2, 2415),
(2, 2420), (2, 2421), (2, 2422), (2, 2423), (2, 2424), (2, 2425), (2, 2426),
(2, 2430), (2, 2431), (2, 2432), (2, 2433), (2, 2440), (2, 2441), (2, 2442);
