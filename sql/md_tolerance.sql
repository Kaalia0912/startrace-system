-- ============================================
-- 基础数据 2320 段：宽度允许偏差标准
-- 数据来源：宝浦电子样本第 8 页「钢带宽度允许偏差」
-- 全新部署：mysql -u root -p ry_vue < md_tolerance.sql
-- 说明：偏差按"下限/上限"两个字段存，便于将来录入非对称公差
-- ============================================

CREATE TABLE IF NOT EXISTS md_tolerance (
  tolerance_id  BIGINT(20)    NOT NULL AUTO_INCREMENT COMMENT '主键',
  standard      VARCHAR(32)   NOT NULL COMMENT '标准体系',
  thickness_min DECIMAL(10,3) DEFAULT NULL COMMENT '厚度下限(mm，含)',
  thickness_max DECIMAL(10,3) DEFAULT NULL COMMENT '厚度上限(mm，不含)',
  width_min     DECIMAL(10,2) DEFAULT NULL COMMENT '宽度下限(mm，含)',
  width_max     DECIMAL(10,2) DEFAULT NULL COMMENT '宽度上限(mm，不含)',
  dev_lower     DECIMAL(10,3) DEFAULT NULL COMMENT '下偏差(mm)',
  dev_upper     DECIMAL(10,3) DEFAULT NULL COMMENT '上偏差(mm)',
  status        CHAR(1)       DEFAULT '0' COMMENT '状态（0正常 1停用）',
  create_by     VARCHAR(64)   DEFAULT '' COMMENT '创建者',
  create_time   DATETIME      DEFAULT NULL COMMENT '创建时间',
  update_by     VARCHAR(64)   DEFAULT '' COMMENT '更新者',
  update_time   DATETIME      DEFAULT NULL COMMENT '更新时间',
  remark        VARCHAR(500)  DEFAULT '' COMMENT '备注',
  PRIMARY KEY (tolerance_id),
  KEY idx_std_thick (standard, thickness_min, thickness_max)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='宽度允许偏差标准表';

-- JIS G4305
INSERT INTO md_tolerance (standard, thickness_min, thickness_max, width_min, width_max, dev_lower, dev_upper) VALUES
('JIS G4305', 0.000, 0.600,  10.00,  80.00, -0.15, 0.15),
('JIS G4305', 0.600, 1.000,  10.00,  80.00, -0.20, 0.20),
('JIS G4305', 0.000, 0.600,  80.00, 160.00, -0.15, 0.15),
('JIS G4305', 0.600, 1.000,  80.00, 160.00, -0.20, 0.20),
('JIS G4305', 0.000, 0.600, 160.00, 260.00, -0.20, 0.20),
('JIS G4305', 0.600, 1.000, 160.00, 260.00, -0.25, 0.25),
('JIS G4305', 0.000, 0.600, 260.00, 400.00, -0.25, 0.25),
('JIS G4305', 0.600, 1.000, 260.00, 400.00, -0.25, 0.25),
('JIS G4305', 0.000, 0.600, 400.00, 600.00, -0.30, 0.30),
('JIS G4305', 0.600, 1.000, 400.00, 600.00, -0.30, 0.30);

-- 无锡宝浦（内控标准）
INSERT INTO md_tolerance (standard, thickness_min, thickness_max, width_min, width_max, dev_lower, dev_upper) VALUES
('无锡宝浦', 0.030, 0.150,  10.00,  80.00, -0.10, 0.10),
('无锡宝浦', 0.150, 0.500,  10.00,  80.00, -0.15, 0.15),
('无锡宝浦', 0.500, 1.000,  10.00,  80.00, -0.20, 0.20),
('无锡宝浦', 0.030, 0.150,  80.00, 160.00, -0.10, 0.10),
('无锡宝浦', 0.150, 0.500,  80.00, 160.00, -0.15, 0.15),
('无锡宝浦', 0.500, 1.000,  80.00, 160.00, -0.20, 0.20),
('无锡宝浦', 0.030, 0.150, 160.00, 260.00, -0.10, 0.10),
('无锡宝浦', 0.150, 0.500, 160.00, 260.00, -0.15, 0.15),
('无锡宝浦', 0.500, 1.000, 160.00, 260.00, -0.20, 0.20),
('无锡宝浦', 0.030, 0.150, 260.00, 400.00, -0.10, 0.10),
('无锡宝浦', 0.150, 0.500, 260.00, 400.00, -0.15, 0.15),
('无锡宝浦', 0.500, 1.000, 260.00, 400.00, -0.20, 0.20),
('无锡宝浦', 0.030, 0.150, 400.00, 600.00, -0.10, 0.10),
('无锡宝浦', 0.150, 0.500, 400.00, 600.00, -0.15, 0.15),
('无锡宝浦', 0.500, 1.000, 400.00, 600.00, -0.20, 0.20);

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(2320, '宽度公差标准', 2300, 3, 'tolerance', 'basedata/tolerance/index', NULL, '', 1, 0, 'C', '0', '0', 'basedata:tolerance:list', 'size', 'admin', sysdate(), '', NULL, '宽度公差标准菜单');

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(2321, '公差查询', 2320, 1, '', '', NULL, '', 1, 0, 'F', '0', '0', 'basedata:tolerance:query', '#', 'admin', sysdate(), '', NULL, NULL),
(2322, '公差新增', 2320, 2, '', '', NULL, '', 1, 0, 'F', '0', '0', 'basedata:tolerance:add', '#', 'admin', sysdate(), '', NULL, NULL),
(2323, '公差修改', 2320, 3, '', '', NULL, '', 1, 0, 'F', '0', '0', 'basedata:tolerance:edit', '#', 'admin', sysdate(), '', NULL, NULL),
(2324, '公差删除', 2320, 4, '', '', NULL, '', 1, 0, 'F', '0', '0', 'basedata:tolerance:remove', '#', 'admin', sysdate(), '', NULL, NULL),
(2325, '公差导出', 2320, 5, '', '', NULL, '', 1, 0, 'F', '0', '0', 'basedata:tolerance:export', '#', 'admin', sysdate(), '', NULL, NULL);

INSERT INTO sys_role_menu (role_id, menu_id) VALUES
(1, 2320), (1, 2321), (1, 2322), (1, 2323), (1, 2324), (1, 2325),
(2, 2320), (2, 2321), (2, 2322), (2, 2323), (2, 2324), (2, 2325);
