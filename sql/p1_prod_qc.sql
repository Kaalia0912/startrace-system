-- ============================================
-- P1：生产工单 / 工序报工 / 质量检验
-- 数据来源：宝浦电子样本第 7 页工序链（10~90）、第 8 页性能表与公差表
-- 全新部署：mysql -u root -p ry_vue < p1_prod_qc.sql
-- ============================================

-- 1. 生产工单
CREATE TABLE IF NOT EXISTS prod_work_order (
  work_order_id   BIGINT(20)    NOT NULL AUTO_INCREMENT COMMENT '主键',
  work_order_no   VARCHAR(64)   NOT NULL COMMENT '工单号',
  order_no        VARCHAR(64)   DEFAULT '' COMMENT '来源订单号',
  material_code   VARCHAR(64)   DEFAULT '' COMMENT '物料编码',
  product_name    VARCHAR(128)  DEFAULT '' COMMENT '产品名称',
  spec            VARCHAR(64)   DEFAULT '' COMMENT '规格',
  quantity        DECIMAL(18,3) DEFAULT NULL COMMENT '计划数量',
  plan_start_date DATE          DEFAULT NULL COMMENT '计划开工',
  plan_end_date   DATE          DEFAULT NULL COMMENT '计划完工',
  status          CHAR(1)       DEFAULT '0' COMMENT '状态（0待生产 1生产中 2已完成）',
  create_by       VARCHAR(64)   DEFAULT '' COMMENT '创建者',
  create_time     DATETIME      DEFAULT NULL COMMENT '创建时间',
  update_by       VARCHAR(64)   DEFAULT '' COMMENT '更新者',
  update_time     DATETIME      DEFAULT NULL COMMENT '更新时间',
  remark          VARCHAR(500)  DEFAULT '' COMMENT '备注',
  PRIMARY KEY (work_order_id),
  UNIQUE KEY uk_work_order_no (work_order_no),
  KEY idx_prod_order_no (order_no)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='生产工单';

-- 2. 工序报工记录（一行一道工序）
CREATE TABLE IF NOT EXISTS prod_process_record (
  record_id     BIGINT(20)    NOT NULL AUTO_INCREMENT COMMENT '主键',
  work_order_id BIGINT(20)    DEFAULT NULL COMMENT '工单ID',
  work_order_no VARCHAR(64)   DEFAULT '' COMMENT '工单号',
  process_no    INT(4)        DEFAULT NULL COMMENT '工序号',
  process_name  VARCHAR(64)   DEFAULT '' COMMENT '工序名称',
  machine       VARCHAR(64)   DEFAULT '' COMMENT '机台',
  operator      VARCHAR(32)   DEFAULT '' COMMENT '操作人',
  start_time    DATETIME      DEFAULT NULL COMMENT '开始时间',
  end_time      DATETIME      DEFAULT NULL COMMENT '结束时间',
  input_weight  DECIMAL(12,2) DEFAULT NULL COMMENT '投入重量(kg)',
  output_weight DECIMAL(12,2) DEFAULT NULL COMMENT '产出重量(kg)',
  loss_weight   DECIMAL(12,2) DEFAULT NULL COMMENT '损耗重量(kg)',
  create_by     VARCHAR(64)   DEFAULT '' COMMENT '创建者',
  create_time   DATETIME      DEFAULT NULL COMMENT '创建时间',
  update_by     VARCHAR(64)   DEFAULT '' COMMENT '更新者',
  update_time   DATETIME      DEFAULT NULL COMMENT '更新时间',
  remark        VARCHAR(500)  DEFAULT '' COMMENT '备注',
  PRIMARY KEY (record_id),
  KEY idx_record_work_order (work_order_no),
  KEY idx_record_process_no (process_no)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='工序报工记录';

-- 3. 质量检验单（实测值 + 自动判定）
CREATE TABLE IF NOT EXISTS qc_inspection (
  inspection_id  BIGINT(20)    NOT NULL AUTO_INCREMENT COMMENT '主键',
  inspection_no  VARCHAR(64)   NOT NULL COMMENT '检验单号',
  source         CHAR(1)       DEFAULT '2' COMMENT '检验来源（0来料 1过程 2成品）',
  order_no       VARCHAR(64)   DEFAULT '' COMMENT '订单号',
  coil_no        VARCHAR(64)   DEFAULT '' COMMENT '卷号',
  material_code  VARCHAR(64)   DEFAULT '' COMMENT '物料编码',
  steel_grade    VARCHAR(32)   DEFAULT '' COMMENT '钢种',
  state          VARCHAR(16)   DEFAULT '' COMMENT '状态（ANN/1-4H/FH/EH/SH）',
  standard       VARCHAR(32)   DEFAULT '无锡宝浦' COMMENT '宽度公差判定标准',
  thickness_val  DECIMAL(10,3) DEFAULT NULL COMMENT '实测厚度(mm)',
  nominal_width  DECIMAL(10,2) DEFAULT NULL COMMENT '名义宽度(mm)',
  width_val      DECIMAL(10,2) DEFAULT NULL COMMENT '实测宽度(mm)',
  yield_val      DECIMAL(10,2) DEFAULT NULL COMMENT '实测屈服(MPa)',
  tensile_val    DECIMAL(10,2) DEFAULT NULL COMMENT '实测抗拉(MPa)',
  elongation_val DECIMAL(10,2) DEFAULT NULL COMMENT '实测延伸率(%)',
  hv_val         DECIMAL(10,2) DEFAULT NULL COMMENT '实测硬度HV',
  surface_ra_val VARCHAR(32)   DEFAULT '' COMMENT '实测表面精度',
  judge_result   CHAR(1)       DEFAULT NULL COMMENT '判定（0合格 1不合格 2让步接收）',
  judge_detail   VARCHAR(1000) DEFAULT '' COMMENT '判定明细',
  inspector      VARCHAR(32)   DEFAULT '' COMMENT '检验员',
  inspect_date   DATE          DEFAULT NULL COMMENT '检验日期',
  create_by      VARCHAR(64)   DEFAULT '' COMMENT '创建者',
  create_time    DATETIME      DEFAULT NULL COMMENT '创建时间',
  update_by      VARCHAR(64)   DEFAULT '' COMMENT '更新者',
  update_time    DATETIME      DEFAULT NULL COMMENT '更新时间',
  remark         VARCHAR(500)  DEFAULT '' COMMENT '备注',
  PRIMARY KEY (inspection_id),
  UNIQUE KEY uk_inspection_no (inspection_no),
  KEY idx_insp_coil_no (coil_no),
  KEY idx_insp_order_no (order_no)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='质量检验单';

-- 4. 菜单与权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(2500, '生产管理', 0, 7, 'prod', NULL, NULL, '', 1, 0, 'M', '0', '0', '', 'job', 'admin', sysdate(), '', NULL, '生产管理菜单'),
(2510, '生产工单', 2500, 1, 'workOrder', 'prod/workOrder/index', NULL, '', 1, 0, 'C', '0', '0', 'prod:workorder:list', 'form', 'admin', sysdate(), '', NULL, '生产工单'),
(2520, '工序报工', 2500, 2, 'processRecord', 'prod/processRecord/index', NULL, '', 1, 0, 'C', '0', '0', 'prod:record:list', 'list', 'admin', sysdate(), '', NULL, '工序报工'),
(2600, '质量管理', 0, 8, 'qc', NULL, NULL, '', 1, 0, 'M', '0', '0', '', 'star', 'admin', sysdate(), '', NULL, '质量管理菜单'),
(2610, '质量检验单', 2600, 1, 'inspection', 'qc/inspection/index', NULL, '', 1, 0, 'C', '0', '0', 'qc:inspection:list', 'clipboard', 'admin', sysdate(), '', NULL, '质量检验单'),
(2700, '追溯查询', 0, 9, 'trace', NULL, NULL, '', 1, 0, 'M', '0', '0', '', 'search', 'admin', sysdate(), '', NULL, '追溯查询菜单'),
(2710, '卷号追溯', 2700, 1, 'coil', 'trace/coil/index', NULL, '', 1, 0, 'C', '0', '0', 'trace:coil:query', 'eye-open', 'admin', sysdate(), '', NULL, '卷号追溯');

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(2511, '工单查询', 2510, 1, '', '', NULL, '', 1, 0, 'F', '0', '0', 'prod:workorder:query', '#', 'admin', sysdate(), '', NULL, NULL),
(2512, '工单新增', 2510, 2, '', '', NULL, '', 1, 0, 'F', '0', '0', 'prod:workorder:add', '#', 'admin', sysdate(), '', NULL, NULL),
(2513, '工单修改', 2510, 3, '', '', NULL, '', 1, 0, 'F', '0', '0', 'prod:workorder:edit', '#', 'admin', sysdate(), '', NULL, NULL),
(2514, '工单删除', 2510, 4, '', '', NULL, '', 1, 0, 'F', '0', '0', 'prod:workorder:remove', '#', 'admin', sysdate(), '', NULL, NULL),
(2515, '工单导出', 2510, 5, '', '', NULL, '', 1, 0, 'F', '0', '0', 'prod:workorder:export', '#', 'admin', sysdate(), '', NULL, NULL),
(2521, '报工查询', 2520, 1, '', '', NULL, '', 1, 0, 'F', '0', '0', 'prod:record:query', '#', 'admin', sysdate(), '', NULL, NULL),
(2522, '报工新增', 2520, 2, '', '', NULL, '', 1, 0, 'F', '0', '0', 'prod:record:add', '#', 'admin', sysdate(), '', NULL, NULL),
(2523, '报工修改', 2520, 3, '', '', NULL, '', 1, 0, 'F', '0', '0', 'prod:record:edit', '#', 'admin', sysdate(), '', NULL, NULL),
(2524, '报工删除', 2520, 4, '', '', NULL, '', 1, 0, 'F', '0', '0', 'prod:record:remove', '#', 'admin', sysdate(), '', NULL, NULL),
(2525, '报工导出', 2520, 5, '', '', NULL, '', 1, 0, 'F', '0', '0', 'prod:record:export', '#', 'admin', sysdate(), '', NULL, NULL),
(2611, '检验查询', 2610, 1, '', '', NULL, '', 1, 0, 'F', '0', '0', 'qc:inspection:query', '#', 'admin', sysdate(), '', NULL, NULL),
(2612, '检验新增', 2610, 2, '', '', NULL, '', 1, 0, 'F', '0', '0', 'qc:inspection:add', '#', 'admin', sysdate(), '', NULL, NULL),
(2613, '检验修改', 2610, 3, '', '', NULL, '', 1, 0, 'F', '0', '0', 'qc:inspection:edit', '#', 'admin', sysdate(), '', NULL, NULL),
(2614, '检验删除', 2610, 4, '', '', NULL, '', 1, 0, 'F', '0', '0', 'qc:inspection:remove', '#', 'admin', sysdate(), '', NULL, NULL),
(2615, '检验导出', 2610, 5, '', '', NULL, '', 1, 0, 'F', '0', '0', 'qc:inspection:export', '#', 'admin', sysdate(), '', NULL, NULL),
(2616, '质保书打印', 2610, 6, '', '', NULL, '', 1, 0, 'F', '0', '0', 'qc:inspection:report', '#', 'admin', sysdate(), '', NULL, NULL),
(2711, '追溯查询', 2710, 1, '', '', NULL, '', 1, 0, 'F', '0', '0', 'trace:coil:query', '#', 'admin', sysdate(), '', NULL, NULL);

INSERT INTO sys_role_menu (role_id, menu_id) VALUES
(1, 2500), (1, 2510), (1, 2511), (1, 2512), (1, 2513), (1, 2514), (1, 2515),
(1, 2520), (1, 2521), (1, 2522), (1, 2523), (1, 2524), (1, 2525),
(1, 2600), (1, 2610), (1, 2611), (1, 2612), (1, 2613), (1, 2614), (1, 2615), (1, 2616),
(1, 2700), (1, 2710), (1, 2711),
(2, 2500), (2, 2510), (2, 2511), (2, 2512), (2, 2513), (2, 2514), (2, 2515),
(2, 2520), (2, 2521), (2, 2522), (2, 2523), (2, 2524), (2, 2525),
(2, 2600), (2, 2610), (2, 2611), (2, 2612), (2, 2613), (2, 2614), (2, 2615), (2, 2616),
(2, 2700), (2, 2710), (2, 2711);
