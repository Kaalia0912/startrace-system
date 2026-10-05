-- ============================================
-- 基础数据 2300 段：物料主数据
-- 用途：给销售订单的"物料编码/材质/规格"提供源头（原来靠手填）
-- 全新部署：mysql -u root -p ry_vue < md_material.sql
-- ============================================

-- 1. 物料主数据表
CREATE TABLE IF NOT EXISTS md_material (
  material_id       BIGINT(20)    NOT NULL AUTO_INCREMENT COMMENT '物料ID',
  material_code     VARCHAR(64)   NOT NULL COMMENT '物料编码',
  product_name      VARCHAR(128)  DEFAULT '' COMMENT '产品名称',
  material          VARCHAR(64)   DEFAULT '' COMMENT '材质',
  spec              VARCHAR(64)   DEFAULT '' COMMENT '规格型号(mm)',
  thickness         DECIMAL(10,3) DEFAULT NULL COMMENT '厚度(mm)',
  width             DECIMAL(10,2) DEFAULT NULL COMMENT '宽度(mm)',
  coil_inner_dia    DECIMAL(10,2) DEFAULT NULL COMMENT '卷内径(mm)',
  unit              VARCHAR(16)   DEFAULT '' COMMENT '单位',
  standard          VARCHAR(32)   DEFAULT '' COMMENT '执行标准',
  hardness          VARCHAR(32)   DEFAULT '' COMMENT '硬度(HV)',
  tensile_strength  VARCHAR(32)   DEFAULT '' COMMENT '抗拉(MPa)',
  surface_ra        VARCHAR(32)   DEFAULT '' COMMENT '表面精度Ra',
  coil_weight_min   DECIMAL(10,2) DEFAULT NULL COMMENT '单卷重下限(kg)',
  coil_weight_max   DECIMAL(10,2) DEFAULT NULL COMMENT '单卷重上限(kg)',
  package_code      VARCHAR(16)   DEFAULT '' COMMENT '包装方式',
  status            CHAR(1)       DEFAULT '0' COMMENT '状态（0正常 1停用）',
  create_by         VARCHAR(64)   DEFAULT '' COMMENT '创建者',
  create_time       DATETIME      DEFAULT NULL COMMENT '创建时间',
  update_by         VARCHAR(64)   DEFAULT '' COMMENT '更新者',
  update_time       DATETIME      DEFAULT NULL COMMENT '更新时间',
  remark            VARCHAR(500)  DEFAULT '' COMMENT '备注',
  PRIMARY KEY (material_id),
  UNIQUE KEY uk_material_code (material_code),
  KEY idx_product_name (product_name),
  KEY idx_material (material)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='物料主数据表';

-- 2. 菜单与权限（基础数据 2300 段）
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(2300, '基础数据', 0, 5, 'basedata', NULL, NULL, '', 1, 0, 'M', '0', '0', '', 'build', 'admin', sysdate(), '', NULL, '基础数据菜单');

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(2301, '物料主数据', 2300, 1, 'material', 'basedata/material/index', NULL, '', 1, 0, 'C', '0', '0', 'basedata:material:list', 'list', 'admin', sysdate(), '', NULL, '物料主数据菜单');

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(2302, '物料查询', 2301, 1, '', '', NULL, '', 1, 0, 'F', '0', '0', 'basedata:material:query', '#', 'admin', sysdate(), '', NULL, NULL),
(2303, '物料新增', 2301, 2, '', '', NULL, '', 1, 0, 'F', '0', '0', 'basedata:material:add', '#', 'admin', sysdate(), '', NULL, NULL),
(2304, '物料修改', 2301, 3, '', '', NULL, '', 1, 0, 'F', '0', '0', 'basedata:material:edit', '#', 'admin', sysdate(), '', NULL, NULL),
(2305, '物料删除', 2301, 4, '', '', NULL, '', 1, 0, 'F', '0', '0', 'basedata:material:remove', '#', 'admin', sysdate(), '', NULL, NULL),
(2306, '物料导出', 2301, 5, '', '', NULL, '', 1, 0, 'F', '0', '0', 'basedata:material:export', '#', 'admin', sysdate(), '', NULL, NULL);

-- 3. 角色绑定（超级管理员 + 普通角色）
INSERT INTO sys_role_menu (role_id, menu_id) VALUES
(1, 2300), (1, 2301), (1, 2302), (1, 2303), (1, 2304), (1, 2305), (1, 2306),
(2, 2300), (2, 2301), (2, 2302), (2, 2303), (2, 2304), (2, 2305), (2, 2306);

-- 4. 初始基础数据（宝浦常用钢种，来自电子样本第 6/8 页）
INSERT INTO md_material (material_code, product_name, material, spec, thickness, width, coil_inner_dia, unit, standard, hardness, tensile_strength, surface_ra, coil_weight_min, coil_weight_max, package_code, status, remark) VALUES
('WL-SUS304-0100-0400', '不锈钢钢带', 'SUS304', '0.1*400', 0.100, 400.00, 500.00, 'kg', 'JIS G4305', '270~300', '520~620', 'Ra0.3-0.4', 800.00, 1000.00, 'B01', '0', '样本第 11 页示例规格'),
('WL-SUS304-0230-0213', '不锈钢钢带', 'SUS304', '0.23*213', 0.230, 213.00, 508.00, 'kg', 'JIS G4305', '250~280', '≥485', 'Ra0.2-0.4', 800.00, 1000.00, 'B01', '0', '样本第 7 页轧制/分剪规格'),
('WL-SUS301-0230-0213', '不锈钢钢带', 'SUS301', '0.23*213', 0.230, 213.00, 508.00, 'kg', 'JIS G4305', '250~310', '≥700', 'Ra0.2-0.4', 800.00, 1000.00, 'B01', '0', 'SUS301 常用状态'),
('WL-SUS430-0230-0213', '不锈钢钢带', 'SUS430', '0.23*213', 0.230, 213.00, 508.00, 'kg', 'JIS G4305', '160~200', '≥550', 'Ra0.2-0.4', 800.00, 1000.00, 'B01', '0', '1/4H 状态');
