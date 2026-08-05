-- 企业档案表
CREATE TABLE ent_enterprise (
  ent_id            BIGINT(20)    NOT NULL AUTO_INCREMENT COMMENT '企业ID',
  ent_code          VARCHAR(18)   NOT NULL COMMENT '统一社会信用代码',
  ent_name          VARCHAR(128)  NOT NULL COMMENT '企业名称',
  industry          VARCHAR(32)   DEFAULT '' COMMENT '所属行业',
  ent_type          VARCHAR(20)   DEFAULT '' COMMENT '企业类型（高新/三类/一般）',
  scale             VARCHAR(20)   DEFAULT '' COMMENT '企业规模',
  address           VARCHAR(255)  DEFAULT '' COMMENT '注册地址',
  legal_person      VARCHAR(32)   DEFAULT '' COMMENT '法定代表人',
  phone             VARCHAR(20)   DEFAULT '' COMMENT '联系电话',
  email             VARCHAR(64)   DEFAULT '' COMMENT '邮箱',
  establish_date    DATE          DEFAULT NULL COMMENT '成立日期',
  registered_capital DECIMAL(18,2) DEFAULT NULL COMMENT '注册资本(万元)',
  employee_count    INT(11)       DEFAULT NULL COMMENT '员工人数',
  status            CHAR(1)       DEFAULT '0' COMMENT '状态（0在营 1注销）',
  create_by         VARCHAR(64)   DEFAULT '' COMMENT '创建者',
  create_time       DATETIME      DEFAULT NULL COMMENT '创建时间',
  update_by         VARCHAR(64)   DEFAULT '' COMMENT '更新者',
  update_time       DATETIME      DEFAULT NULL COMMENT '更新时间',
  remark            VARCHAR(500)  DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (ent_id),
  UNIQUE KEY uk_ent_code (ent_code)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='企业档案表';
