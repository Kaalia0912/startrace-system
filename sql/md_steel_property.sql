-- ============================================
-- 基础数据 2310 段：钢种性能标准（钢种 × 状态）
-- 数据来源：宝浦电子样本第 8 页「常用钢种各状态下的力学性能」
-- 全新部署：mysql -u root -p ry_vue < md_steel_property.sql
-- ============================================

CREATE TABLE IF NOT EXISTS md_steel_property (
  property_id    BIGINT(20)    NOT NULL AUTO_INCREMENT COMMENT '主键',
  steel_grade    VARCHAR(32)   NOT NULL COMMENT '钢种',
  state          VARCHAR(16)   NOT NULL COMMENT '状态（ANN/1-4H/FH/EH/SH）',
  yield_min      DECIMAL(10,2) DEFAULT NULL COMMENT '屈服强度下限(MPa)',
  tensile_min    DECIMAL(10,2) DEFAULT NULL COMMENT '抗拉强度下限(MPa)',
  elongation_min DECIMAL(10,2) DEFAULT NULL COMMENT '延伸率下限(%)',
  hv_min         DECIMAL(10,2) DEFAULT NULL COMMENT '维氏硬度下限HV',
  hv_max         DECIMAL(10,2) DEFAULT NULL COMMENT '维氏硬度上限HV',
  status         CHAR(1)       DEFAULT '0' COMMENT '状态（0正常 1停用）',
  create_by      VARCHAR(64)   DEFAULT '' COMMENT '创建者',
  create_time    DATETIME      DEFAULT NULL COMMENT '创建时间',
  update_by      VARCHAR(64)   DEFAULT '' COMMENT '更新者',
  update_time    DATETIME      DEFAULT NULL COMMENT '更新时间',
  remark         VARCHAR(500)  DEFAULT '' COMMENT '备注',
  PRIMARY KEY (property_id),
  UNIQUE KEY uk_grade_state (steel_grade, state)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='钢种性能标准表';

INSERT INTO md_steel_property (steel_grade, state, yield_min, tensile_min, elongation_min, hv_min, hv_max, status, remark) VALUES
('SUS301', 'ANN',  205,  520,  40, NULL, 218, '0', NULL),
('SUS301', '1/4H', 470,  780,  25, 250, 310, '0', NULL),
('SUS301', '1/2H', 510,  930,  18, 310, 370, '0', NULL),
('SUS301', '3/4H', 745, 1130,   5, 370, 430, '0', NULL),
('SUS301', 'FH',  1030, 1320,   4, 430, 490, '0', NULL),
('SUS301', 'EH',  1275, 1570,   3, 490, 550, '0', NULL),
('SUS301', 'SH',  1550, 1750,   1, 550, NULL, '0', NULL),
('SUS304', 'ANN',  205,  520,  40, NULL, 200, '0', NULL),
('SUS304', '1/4H', 450,  700,  20, 220, 250, '0', NULL),
('SUS304', '1/2H', 700,  850,   8, 250, 310, '0', NULL),
('SUS304', '3/4H', 880, 1000,   4, 310, 370, '0', NULL),
('SUS304', 'FH',  1020, 1150,   1, 370, NULL, '0', NULL),
('SUS430', 'ANN',  205,  420,  22, NULL, 200, '0', NULL),
('SUS430', '1/4H', 500,  550,   5, 160, 200, '0', NULL),
('SUS430', '1/2H', 600,  650,   3, 200, 240, '0', NULL),
('SUS430', '3/4H', 680,  750,   2, 240, 280, '0', NULL),
('SUS430', 'H',    800,  850,   1, 280, NULL, '0', NULL);

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(2310, '钢种性能标准', 2300, 2, 'steelProperty', 'basedata/steelProperty/index', NULL, '', 1, 0, 'C', '0', '0', 'basedata:property:list', 'table', 'admin', sysdate(), '', NULL, '钢种性能标准菜单');

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(2311, '性能查询', 2310, 1, '', '', NULL, '', 1, 0, 'F', '0', '0', 'basedata:property:query', '#', 'admin', sysdate(), '', NULL, NULL),
(2312, '性能新增', 2310, 2, '', '', NULL, '', 1, 0, 'F', '0', '0', 'basedata:property:add', '#', 'admin', sysdate(), '', NULL, NULL),
(2313, '性能修改', 2310, 3, '', '', NULL, '', 1, 0, 'F', '0', '0', 'basedata:property:edit', '#', 'admin', sysdate(), '', NULL, NULL),
(2314, '性能删除', 2310, 4, '', '', NULL, '', 1, 0, 'F', '0', '0', 'basedata:property:remove', '#', 'admin', sysdate(), '', NULL, NULL),
(2315, '性能导出', 2310, 5, '', '', NULL, '', 1, 0, 'F', '0', '0', 'basedata:property:export', '#', 'admin', sysdate(), '', NULL, NULL);

INSERT INTO sys_role_menu (role_id, menu_id) VALUES
(1, 2310), (1, 2311), (1, 2312), (1, 2313), (1, 2314), (1, 2315),
(2, 2310), (2, 2311), (2, 2312), (2, 2313), (2, 2314), (2, 2315);
