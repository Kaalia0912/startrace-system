-- ============================================
-- 基础数据 2330 段：工序库
-- 数据来源：宝浦电子样本第 7 页「品质管理」工序流程图（编号即工序号）
-- 全新部署：mysql -u root -p ry_vue < md_process.sql
-- ============================================

CREATE TABLE IF NOT EXISTS md_process (
  process_id   BIGINT(20)   NOT NULL AUTO_INCREMENT COMMENT '主键',
  process_no   INT(4)       NOT NULL COMMENT '工序号',
  process_name VARCHAR(64)  NOT NULL COMMENT '工序名称',
  is_qc        CHAR(1)      DEFAULT '0' COMMENT '是否质检点（1是 0否）',
  status       CHAR(1)      DEFAULT '0' COMMENT '状态（0正常 1停用）',
  create_by    VARCHAR(64)  DEFAULT '' COMMENT '创建者',
  create_time  DATETIME     DEFAULT NULL COMMENT '创建时间',
  update_by    VARCHAR(64)  DEFAULT '' COMMENT '更新者',
  update_time  DATETIME     DEFAULT NULL COMMENT '更新时间',
  remark       VARCHAR(500) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (process_id),
  UNIQUE KEY uk_process_no (process_no)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='工序库表';

INSERT INTO md_process (process_no, process_name, is_qc, status, remark) VALUES
(10, '原材料检验',     '1', '0', '来料检验：物理性能、带材规格、材质、表面'),
(20, '轧制',           '0', '0', '厚度、表面（Ra0.2-0.4）'),
(30, '固溶处理',       '0', '0', '硬度HV≤165、延伸率≥55%、表面光亮无划伤'),
(40, '清洗',           '0', '0', '无油污'),
(50, '拉矫',           '0', '0', '不平度≤1.5mm、性能HV250-280、延伸率≥55%'),
(60, '分剪',           '0', '0', '宽度213±0.10mm、毛刺高度≤0.03mm'),
(70, '包装',           '0', '0', '卷重要求、内孔 c508mm'),
(80, '总检查员抽检',   '1', '0', '各项尺寸和性能抽检'),
(90, '成品仓库',       '0', '0', '储存条件：防止损伤和淋湿');

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(2330, '工序库', 2300, 4, 'process', 'basedata/process/index', NULL, '', 1, 0, 'C', '0', '0', 'basedata:process:list', 'job', 'admin', sysdate(), '', NULL, '工序库菜单');

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(2331, '工序查询', 2330, 1, '', '', NULL, '', 1, 0, 'F', '0', '0', 'basedata:process:query', '#', 'admin', sysdate(), '', NULL, NULL),
(2332, '工序新增', 2330, 2, '', '', NULL, '', 1, 0, 'F', '0', '0', 'basedata:process:add', '#', 'admin', sysdate(), '', NULL, NULL),
(2333, '工序修改', 2330, 3, '', '', NULL, '', 1, 0, 'F', '0', '0', 'basedata:process:edit', '#', 'admin', sysdate(), '', NULL, NULL),
(2334, '工序删除', 2330, 4, '', '', NULL, '', 1, 0, 'F', '0', '0', 'basedata:process:remove', '#', 'admin', sysdate(), '', NULL, NULL),
(2335, '工序导出', 2330, 5, '', '', NULL, '', 1, 0, 'F', '0', '0', 'basedata:process:export', '#', 'admin', sysdate(), '', NULL, NULL);

INSERT INTO sys_role_menu (role_id, menu_id) VALUES
(1, 2330), (1, 2331), (1, 2332), (1, 2333), (1, 2334), (1, 2335),
(2, 2330), (2, 2331), (2, 2332), (2, 2333), (2, 2334), (2, 2335);
