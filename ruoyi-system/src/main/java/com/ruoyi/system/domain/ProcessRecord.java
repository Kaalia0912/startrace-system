package com.ruoyi.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/** 工序报工记录对象 prod_process_record */
public class ProcessRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long recordId;

    private Long workOrderId;

    @Excel(name = "工单号", width = 22)
    private String workOrderNo;

    @Excel(name = "工序号", width = 10)
    private Integer processNo;

    @Excel(name = "工序名称", width = 16)
    private String processName;

    @Excel(name = "机台", width = 14)
    private String machine;

    @Excel(name = "操作人", width = 12)
    private String operator;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "开始时间", width = 20, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "结束时间", width = 20, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @Excel(name = "投入重量(kg)", width = 14)
    private BigDecimal inputWeight;

    @Excel(name = "产出重量(kg)", width = 14)
    private BigDecimal outputWeight;

    @Excel(name = "损耗重量(kg)", width = 14)
    private BigDecimal lossWeight;

    @Excel(name = "备注", width = 30)
    private String remark;

    public Long getRecordId() { return recordId; }
    public void setRecordId(Long recordId) { this.recordId = recordId; }
    public Long getWorkOrderId() { return workOrderId; }
    public void setWorkOrderId(Long workOrderId) { this.workOrderId = workOrderId; }
    public String getWorkOrderNo() { return workOrderNo; }
    public void setWorkOrderNo(String workOrderNo) { this.workOrderNo = workOrderNo; }
    public Integer getProcessNo() { return processNo; }
    public void setProcessNo(Integer processNo) { this.processNo = processNo; }
    public String getProcessName() { return processName; }
    public void setProcessName(String processName) { this.processName = processName; }
    public String getMachine() { return machine; }
    public void setMachine(String machine) { this.machine = machine; }
    public String getOperator() { return operator; }
    public void setOperator(String operator) { this.operator = operator; }
    public Date getStartTime() { return startTime; }
    public void setStartTime(Date startTime) { this.startTime = startTime; }
    public Date getEndTime() { return endTime; }
    public void setEndTime(Date endTime) { this.endTime = endTime; }
    public BigDecimal getInputWeight() { return inputWeight; }
    public void setInputWeight(BigDecimal inputWeight) { this.inputWeight = inputWeight; }
    public BigDecimal getOutputWeight() { return outputWeight; }
    public void setOutputWeight(BigDecimal outputWeight) { this.outputWeight = outputWeight; }
    public BigDecimal getLossWeight() { return lossWeight; }
    public void setLossWeight(BigDecimal lossWeight) { this.lossWeight = lossWeight; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
