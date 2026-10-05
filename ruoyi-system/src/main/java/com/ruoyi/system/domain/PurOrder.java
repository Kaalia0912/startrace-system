package com.ruoyi.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/** 采购单对象 pur_order */
public class PurOrder extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long purchaseId;

    @Excel(name = "采购单号", width = 22)
    private String purchaseNo;

    @Excel(name = "供应商", width = 24)
    private String supplier;

    @Excel(name = "物料编码", width = 20)
    private String materialCode;

    @Excel(name = "材质", width = 12)
    private String material;

    @Excel(name = "规格", width = 14)
    private String spec;

    @Excel(name = "数量", width = 12)
    private BigDecimal quantity;

    @Excel(name = "重量(kg)", width = 12)
    private BigDecimal weight;

    @Excel(name = "单价(元/kg)", width = 12)
    private BigDecimal unitPrice;

    @Excel(name = "金额(元)", width = 14)
    private BigDecimal amount;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "采购日期", width = 12, dateFormat = "yyyy-MM-dd")
    private Date purchaseDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "预计到货", width = 12, dateFormat = "yyyy-MM-dd")
    private Date expectDate;

    @Excel(name = "状态", readConverterExp = "0=待收货,1=已收货")
    private String status;

    @Excel(name = "备注", width = 30)
    private String remark;

    public Long getPurchaseId() { return purchaseId; }
    public void setPurchaseId(Long purchaseId) { this.purchaseId = purchaseId; }
    public String getPurchaseNo() { return purchaseNo; }
    public void setPurchaseNo(String purchaseNo) { this.purchaseNo = purchaseNo; }
    public String getSupplier() { return supplier; }
    public void setSupplier(String supplier) { this.supplier = supplier; }
    public String getMaterialCode() { return materialCode; }
    public void setMaterialCode(String materialCode) { this.materialCode = materialCode; }
    public String getMaterial() { return material; }
    public void setMaterial(String material) { this.material = material; }
    public String getSpec() { return spec; }
    public void setSpec(String spec) { this.spec = spec; }
    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }
    public BigDecimal getWeight() { return weight; }
    public void setWeight(BigDecimal weight) { this.weight = weight; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public Date getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(Date purchaseDate) { this.purchaseDate = purchaseDate; }
    public Date getExpectDate() { return expectDate; }
    public void setExpectDate(Date expectDate) { this.expectDate = expectDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
