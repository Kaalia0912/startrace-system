package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/** 设备台账对象 equip_ledger */
public class EquipLedger extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long equipId;

    @Excel(name = "设备编号", width = 16)
    private String equipCode;

    @Excel(name = "设备名称", width = 20)
    private String equipName;

    @Excel(name = "设备类型", readConverterExp = "0=生产设备,1=表面处理,2=检测设备")
    private String equipType;

    @Excel(name = "型号", width = 16)
    private String model;

    @Excel(name = "存放位置", width = 16)
    private String location;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "购置日期", width = 12, dateFormat = "yyyy-MM-dd")
    private Date buyDate;

    @Excel(name = "校准周期(天)", width = 14)
    private Integer checkCycleDays;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "上次校准", width = 12, dateFormat = "yyyy-MM-dd")
    private Date lastCheckDate;

    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    @Excel(name = "备注", width = 30)
    private String remark;

    public Long getEquipId() { return equipId; }
    public void setEquipId(Long equipId) { this.equipId = equipId; }
    public String getEquipCode() { return equipCode; }
    public void setEquipCode(String equipCode) { this.equipCode = equipCode; }
    public String getEquipName() { return equipName; }
    public void setEquipName(String equipName) { this.equipName = equipName; }
    public String getEquipType() { return equipType; }
    public void setEquipType(String equipType) { this.equipType = equipType; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public Date getBuyDate() { return buyDate; }
    public void setBuyDate(Date buyDate) { this.buyDate = buyDate; }
    public Integer getCheckCycleDays() { return checkCycleDays; }
    public void setCheckCycleDays(Integer checkCycleDays) { this.checkCycleDays = checkCycleDays; }
    public Date getLastCheckDate() { return lastCheckDate; }
    public void setLastCheckDate(Date lastCheckDate) { this.lastCheckDate = lastCheckDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
