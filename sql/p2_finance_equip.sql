-- ============================================
-- P2：客户档案扩字段 / 设备台账 / 收款登记与应收
-- 说明：本脚本含 ALTER TABLE，只执行一次（重复执行会报列已存在）
-- 全新部署：mysql -u root -p ry_vue < p2_finance_equip.sql
-- ============================================

-- 1. 客户档案（企业档案）扩展：开票与账期
ALTER TABLE ent_enterprise
  ADD COLUMN invoice_title   VARCHAR(128)  DEFAULT '' COMMENT '开票抬头',
  ADD COLUMN tax_no          VARCHAR(64)   DEFAULT '' COMMENT '税号',
  ADD COLUMN invoice_address VARCHAR(255)  DEFAULT '' COMMENT '开票地址',
  ADD COLUMN invoice_phone   VARCHAR(32)   DEFAULT '' COMMENT '开票电话',
  ADD COLUMN bank_name       VARCHAR(128)  DEFAULT '' COMMENT '开户行',
  ADD COLUMN bank_account    VARCHAR(64)   DEFAULT '' COMMENT '银行账号',
  ADD COLUMN payment_days    INT(11)       DEFAULT NULL COMMENT '账期(天)',
  ADD COLUMN credit_limit    DECIMAL(18,2) DEFAULT NULL COMMENT '信用额度(元)',
  ADD COLUMN default_address VARCHAR(255)  DEFAULT '' COMMENT '默认收货地址';

-- 2. 设备台账
CREATE TABLE IF NOT EXISTS equip_ledger (
  equip_id        BIGINT(20)    NOT NULL AUTO_INCREMENT COMMENT '主键',
  equip_code      VARCHAR(64)   NOT NULL COMMENT '设备编号',
  equip_name      VARCHAR(128)  DEFAULT '' COMMENT '设备名称',
  equip_type      CHAR(1)       DEFAULT '0' COMMENT '设备类型（0生产设备 1表面处理 2检测设备）',
  model           VARCHAR(64)   DEFAULT '' COMMENT '型号',
  location        VARCHAR(64)   DEFAULT '' COMMENT '存放位置',
  buy_date        DATE          DEFAULT NULL COMMENT '购置日期',
  check_cycle_days INT(6)       DEFAULT NULL COMMENT '校准周期(天)',
  last_check_date DATE          DEFAULT NULL COMMENT '上次校准日期',
  status          CHAR(1)       DEFAULT '0' COMMENT '状态（0正常 1停用）',
  create_by       VARCHAR(64)   DEFAULT '' COMMENT '创建者',
  create_time     DATETIME      DEFAULT NULL COMMENT '创建时间',
  update_by       VARCHAR(64)   DEFAULT '' COMMENT '更新者',
  update_time     DATETIME      DEFAULT NULL COMMENT '更新时间',
  remark          VARCHAR(500)  DEFAULT '' COMMENT '备注',
  PRIMARY KEY (equip_id),
  UNIQUE KEY uk_equip_code (equip_code)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='设备台账';

-- 3. 收款登记
CREATE TABLE IF NOT EXISTS fin_receipt (
  receipt_id     BIGINT(20)    NOT NULL AUTO_INCREMENT COMMENT '主键',
  receipt_no     VARCHAR(64)   NOT NULL COMMENT '收款单号',
  customer_name  VARCHAR(128)  DEFAULT '' COMMENT '客户名称',
  order_no       VARCHAR(64)   DEFAULT '' COMMENT '关联订单号',
  receipt_date   DATE          DEFAULT NULL COMMENT '收款日期',
  amount         DECIMAL(18,2) DEFAULT NULL COMMENT '收款金额(元)',
  pay_method     VARCHAR(32)   DEFAULT '' COMMENT '收款方式',
  handler        VARCHAR(32)   DEFAULT '' COMMENT '经手人',
  create_by      VARCHAR(64)   DEFAULT '' COMMENT '创建者',
  create_time    DATETIME      DEFAULT NULL COMMENT '创建时间',
  update_by      VARCHAR(64)   DEFAULT '' COMMENT '更新者',
  update_time    DATETIME      DEFAULT NULL COMMENT '更新时间',
  remark         VARCHAR(500)  DEFAULT '' COMMENT '备注',
  PRIMARY KEY (receipt_id),
  UNIQUE KEY uk_receipt_no (receipt_no),
  KEY idx_receipt_customer (customer_name)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='收款登记';

-- 4. 菜单与权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(2210, '订单执行进度', 2200, 2, 'progress', 'order/progress/index', NULL, '', 1, 0, 'C', '0', '0', 'order:progress:list', 'chart', 'admin', sysdate(), '', NULL, '订单执行进度看板'),
(2530, '设备台账', 2500, 3, 'equip', 'prod/equip/index', NULL, '', 1, 0, 'C', '0', '0', 'prod:equip:list', 'tool', 'admin', sysdate(), '', NULL, '设备台账'),
(2800, '财务管理', 0, 10, 'fin', NULL, NULL, '', 1, 0, 'M', '0', '0', '', 'money', 'admin', sysdate(), '', NULL, '财务管理菜单'),
(2810, '收款登记', 2800, 1, 'receipt', 'fin/receipt/index', NULL, '', 1, 0, 'C', '0', '0', 'fin:receipt:list', 'form', 'admin', sysdate(), '', NULL, '收款登记'),
(2820, '应收汇总', 2800, 2, 'receivable', 'fin/receivable/index', NULL, '', 1, 0, 'C', '0', '0', 'fin:receivable:list', 'chart', 'admin', sysdate(), '', NULL, '应收汇总');

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(2211, '进度查询', 2210, 1, '', '', NULL, '', 1, 0, 'F', '0', '0', 'order:progress:list', '#', 'admin', sysdate(), '', NULL, NULL),
(2531, '设备查询', 2530, 1, '', '', NULL, '', 1, 0, 'F', '0', '0', 'prod:equip:query', '#', 'admin', sysdate(), '', NULL, NULL),
(2532, '设备新增', 2530, 2, '', '', NULL, '', 1, 0, 'F', '0', '0', 'prod:equip:add', '#', 'admin', sysdate(), '', NULL, NULL),
(2533, '设备修改', 2530, 3, '', '', NULL, '', 1, 0, 'F', '0', '0', 'prod:equip:edit', '#', 'admin', sysdate(), '', NULL, NULL),
(2534, '设备删除', 2530, 4, '', '', NULL, '', 1, 0, 'F', '0', '0', 'prod:equip:remove', '#', 'admin', sysdate(), '', NULL, NULL),
(2535, '设备导出', 2530, 5, '', '', NULL, '', 1, 0, 'F', '0', '0', 'prod:equip:export', '#', 'admin', sysdate(), '', NULL, NULL),
(2811, '收款查询', 2810, 1, '', '', NULL, '', 1, 0, 'F', '0', '0', 'fin:receipt:query', '#', 'admin', sysdate(), '', NULL, NULL),
(2812, '收款新增', 2810, 2, '', '', NULL, '', 1, 0, 'F', '0', '0', 'fin:receipt:add', '#', 'admin', sysdate(), '', NULL, NULL),
(2813, '收款修改', 2810, 3, '', '', NULL, '', 1, 0, 'F', '0', '0', 'fin:receipt:edit', '#', 'admin', sysdate(), '', NULL, NULL),
(2814, '收款删除', 2810, 4, '', '', NULL, '', 1, 0, 'F', '0', '0', 'fin:receipt:remove', '#', 'admin', sysdate(), '', NULL, NULL),
(2815, '收款导出', 2810, 5, '', '', NULL, '', 1, 0, 'F', '0', '0', 'fin:receipt:export', '#', 'admin', sysdate(), '', NULL, NULL),
(2821, '应收查询', 2820, 1, '', '', NULL, '', 1, 0, 'F', '0', '0', 'fin:receivable:list', '#', 'admin', sysdate(), '', NULL, NULL);

INSERT INTO sys_role_menu (role_id, menu_id) VALUES
(1, 2210), (1, 2211), (1, 2530), (1, 2531), (1, 2532), (1, 2533), (1, 2534), (1, 2535),
(1, 2800), (1, 2810), (1, 2811), (1, 2812), (1, 2813), (1, 2814), (1, 2815), (1, 2820), (1, 2821),
(2, 2210), (2, 2211), (2, 2530), (2, 2531), (2, 2532), (2, 2533), (2, 2534), (2, 2535),
(2, 2800), (2, 2810), (2, 2811), (2, 2812), (2, 2813), (2, 2814), (2, 2815), (2, 2820), (2, 2821);

-- 5. 设备台账初始数据（样本第 9 页）
INSERT INTO equip_ledger (equip_code, equip_name, equip_type, model, location, check_cycle_days, status, remark) VALUES
('EQ-P01', '轧机',           '0', '', '生产车间', NULL, '0', '样本第 9 页 生产设备'),
('EQ-P02', '拉矫机',         '0', '', '生产车间', NULL, '0', '样本第 9 页 生产设备'),
('EQ-P03', '分剪机',         '0', '', '生产车间', NULL, '0', '样本第 9 页 生产设备'),
('EQ-S01', '表面处理产线',   '1', '', '表面处理车间', NULL, '0', '样本第 9 页 表面处理'),
('EQ-T01', '维氏硬度计',     '2', '', '检测室', 365, '0', '检测设备，建议每年校准'),
('EQ-T02', '万能材料试验机', '2', '', '检测室', 365, '0', '检测设备，建议每年校准'),
('EQ-T03', '金相显微镜',     '2', '', '检测室', 365, '0', '检测设备，建议每年校准');
