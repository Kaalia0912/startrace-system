package com.ruoyi.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/** 收款登记对象 fin_receipt */
public class FinReceipt extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long receiptId;

    @Excel(name = "收款单号", width = 22)
    private String receiptNo;

    @Excel(name = "客户名称", width = 28)
    private String customerName;

    @Excel(name = "订单号", width = 20)
    private String orderNo;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "收款日期", width = 12, dateFormat = "yyyy-MM-dd")
    private Date receiptDate;

    @Excel(name = "收款金额(元)", width = 14)
    private BigDecimal amount;

    @Excel(name = "收款方式", width = 14)
    private String payMethod;

    @Excel(name = "经手人", width = 12)
    private String handler;

    @Excel(name = "备注", width = 30)
    private String remark;

    public Long getReceiptId() { return receiptId; }
    public void setReceiptId(Long receiptId) { this.receiptId = receiptId; }
    public String getReceiptNo() { return receiptNo; }
    public void setReceiptNo(String receiptNo) { this.receiptNo = receiptNo; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    public Date getReceiptDate() { return receiptDate; }
    public void setReceiptDate(Date receiptDate) { this.receiptDate = receiptDate; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getPayMethod() { return payMethod; }
    public void setPayMethod(String payMethod) { this.payMethod = payMethod; }
    public String getHandler() { return handler; }
    public void setHandler(String handler) { this.handler = handler; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
