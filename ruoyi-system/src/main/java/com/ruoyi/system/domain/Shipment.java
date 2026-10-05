package com.ruoyi.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 发货单对象 inv_shipment
 * 
 * @author yuanwenbo
 */
public class Shipment extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long shipmentId;

    /** 发货单号 */
    @Excel(name = "发货单号", width = 22)
    private String shipmentNo;

    /** 客户名称 */
    @Excel(name = "客户名称", width = 28)
    private String customerName;

    /** 关联订单号 */
    @Excel(name = "订单号", width = 20)
    private String orderNo;

    /** 送货地址 */
    @Excel(name = "送货地址", width = 30)
    private String address;

    /** 收货人 */
    @Excel(name = "收货人", width = 12)
    private String receiver;

    /** 联系电话 */
    @Excel(name = "联系电话", width = 15)
    private String phone;

    /** 发货日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "发货日期", width = 12, dateFormat = "yyyy-MM-dd")
    private Date shipmentDate;

    /** 发货总重(kg) */
    @Excel(name = "发货总重(kg)", width = 14)
    private BigDecimal totalWeight;

    /** 发货总件数 */
    @Excel(name = "发货总件数", width = 12)
    private Integer totalPieces;

    /** 状态（0已发货 1已签收） */
    @Excel(name = "状态", readConverterExp = "0=已发货,1=已签收")
    private String status;

    /** 发货明细 */
    private List<ShipmentItem> items;

    /** 待发货的成品卷ID（仅用于发货请求，不入库） */
    private Long[] coilIds;

    /** 备注 */
    @Excel(name = "备注", width = 30)
    private String remark;

    public Long getShipmentId()
    {
        return shipmentId;
    }

    public void setShipmentId(Long shipmentId)
    {
        this.shipmentId = shipmentId;
    }

    public String getShipmentNo()
    {
        return shipmentNo;
    }

    public void setShipmentNo(String shipmentNo)
    {
        this.shipmentNo = shipmentNo;
    }

    public String getCustomerName()
    {
        return customerName;
    }

    public void setCustomerName(String customerName)
    {
        this.customerName = customerName;
    }

    public String getOrderNo()
    {
        return orderNo;
    }

    public void setOrderNo(String orderNo)
    {
        this.orderNo = orderNo;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getReceiver()
    {
        return receiver;
    }

    public void setReceiver(String receiver)
    {
        this.receiver = receiver;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public Date getShipmentDate()
    {
        return shipmentDate;
    }

    public void setShipmentDate(Date shipmentDate)
    {
        this.shipmentDate = shipmentDate;
    }

    public BigDecimal getTotalWeight()
    {
        return totalWeight;
    }

    public void setTotalWeight(BigDecimal totalWeight)
    {
        this.totalWeight = totalWeight;
    }

    public Integer getTotalPieces()
    {
        return totalPieces;
    }

    public void setTotalPieces(Integer totalPieces)
    {
        this.totalPieces = totalPieces;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public List<ShipmentItem> getItems()
    {
        return items;
    }

    public void setItems(List<ShipmentItem> items)
    {
        this.items = items;
    }

    public Long[] getCoilIds()
    {
        return coilIds;
    }

    public void setCoilIds(Long[] coilIds)
    {
        this.coilIds = coilIds;
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
