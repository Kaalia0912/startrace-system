package com.ruoyi.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/** 生产工单对象 prod_work_order */
public class WorkOrder extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long workOrderId;

    @Excel(name = "工单号", width = 22)
    private String workOrderNo;

    @Excel(name = "订单号", width = 20)
    private String orderNo;

    @Excel(name = "物料编码", width = 20)
    private String materialCode;

    @Excel(name = "产品名称", width = 20)
    private String productName;

    @Excel(name = "规格", width = 14)
    private String spec;

    @Excel(name = "计划数量", width = 12)
    private BigDecimal quantity;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "计划开工", width = 12, dateFormat = "yyyy-MM-dd")
    private Date planStartDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "计划完工", width = 12, dateFormat = "yyyy-MM-dd")
    private Date planEndDate;

    @Excel(name = "状态", readConverterExp = "0=待生产,1=生产中,2=已完成")
    private String status;

    @Excel(name = "备注", width = 30)
    private String remark;

    public Long getWorkOrderId() { return workOrderId; }
    public void setWorkOrderId(Long workOrderId) { this.workOrderId = workOrderId; }
    public String getWorkOrderNo() { return workOrderNo; }
    public void setWorkOrderNo(String workOrderNo) { this.workOrderNo = workOrderNo; }
    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    public String getMaterialCode() { return materialCode; }
    public void setMaterialCode(String materialCode) { this.materialCode = materialCode; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getSpec() { return spec; }
    public void setSpec(String spec) { this.spec = spec; }
    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }
    public Date getPlanStartDate() { return planStartDate; }
    public void setPlanStartDate(Date planStartDate) { this.planStartDate = planStartDate; }
    public Date getPlanEndDate() { return planEndDate; }
    public void setPlanEndDate(Date planEndDate) { this.planEndDate = planEndDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
