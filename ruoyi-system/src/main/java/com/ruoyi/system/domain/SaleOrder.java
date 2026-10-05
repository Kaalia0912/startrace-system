package com.ruoyi.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 销售订单对象 ord_sale_order
 * 
 * @author yuanwenbo
 */
public class SaleOrder extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 订单ID */
    private Long orderId;

    /** 订单号 */
    @Excel(name = "订单号", width = 20)
    private String orderNo;

    /** 日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "日期", width = 12, dateFormat = "yyyy-MM-dd")
    private Date orderDate;

    /** 客户名称 */
    @Excel(name = "客户名称", width = 30)
    private String customerName;

    /** 联系人 */
    @Excel(name = "联系人", width = 12)
    private String contactPerson;

    /** 联系电话 */
    @Excel(name = "联系电话", width = 15)
    private String contactPhone;

    /** 联系地址 */
    @Excel(name = "联系地址", width = 30)
    private String contactAddress;

    /** 物料编码 */
    @Excel(name = "物料编码", width = 15)
    private String materialCode;

    /** 产品名称 */
    @Excel(name = "产品名称", width = 25)
    private String productName;

    /** 材质 */
    @Excel(name = "材质", width = 12)
    private String material;

    /** 规格型号(mm) */
    @Excel(name = "规格型号(mm)", width = 18)
    private String spec;

    /** 硬度(HV) */
    @Excel(name = "硬度(HV)", width = 12)
    private String hardness;

    /** 抗拉(MPa) */
    @Excel(name = "抗拉(MPa)", width = 12)
    private String tensileStrength;

    /** 单位 */
    @Excel(name = "单位", width = 8)
    private String unit;

    /** 数量 */
    @Excel(name = "数量", width = 12)
    private BigDecimal quantity;

    /** 单价 */
    @Excel(name = "单价", width = 12)
    private BigDecimal unitPrice;

    /** 金额 */
    @Excel(name = "金额", width = 14)
    private BigDecimal amount;

    /** 交期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "交期", width = 12, dateFormat = "yyyy-MM-dd")
    private Date deliveryDate;

    /** 备注 */
    @Excel(name = "备注", width = 30)
    private String remark;

    public Long getOrderId()
    {
        return orderId;
    }

    public void setOrderId(Long orderId)
    {
        this.orderId = orderId;
    }

    public String getOrderNo()
    {
        return orderNo;
    }

    public void setOrderNo(String orderNo)
    {
        this.orderNo = orderNo;
    }

    public Date getOrderDate()
    {
        return orderDate;
    }

    public void setOrderDate(Date orderDate)
    {
        this.orderDate = orderDate;
    }

    public String getCustomerName()
    {
        return customerName;
    }

    public void setCustomerName(String customerName)
    {
        this.customerName = customerName;
    }

    public String getContactPerson()
    {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson)
    {
        this.contactPerson = contactPerson;
    }

    public String getContactPhone()
    {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone)
    {
        this.contactPhone = contactPhone;
    }

    public String getContactAddress()
    {
        return contactAddress;
    }

    public void setContactAddress(String contactAddress)
    {
        this.contactAddress = contactAddress;
    }

    public String getMaterialCode()
    {
        return materialCode;
    }

    public void setMaterialCode(String materialCode)
    {
        this.materialCode = materialCode;
    }

    public String getProductName()
    {
        return productName;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    public String getMaterial()
    {
        return material;
    }

    public void setMaterial(String material)
    {
        this.material = material;
    }

    public String getSpec()
    {
        return spec;
    }

    public void setSpec(String spec)
    {
        this.spec = spec;
    }

    public String getHardness()
    {
        return hardness;
    }

    public void setHardness(String hardness)
    {
        this.hardness = hardness;
    }

    public String getTensileStrength()
    {
        return tensileStrength;
    }

    public void setTensileStrength(String tensileStrength)
    {
        this.tensileStrength = tensileStrength;
    }

    public String getUnit()
    {
        return unit;
    }

    public void setUnit(String unit)
    {
        this.unit = unit;
    }

    public BigDecimal getQuantity()
    {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity)
    {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice()
    {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice)
    {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getAmount()
    {
        return amount;
    }

    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
    }

    public Date getDeliveryDate()
    {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate)
    {
        this.deliveryDate = deliveryDate;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }
}
