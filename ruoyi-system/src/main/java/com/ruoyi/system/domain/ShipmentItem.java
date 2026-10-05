package com.ruoyi.system.domain;

import java.math.BigDecimal;
import com.ruoyi.common.annotation.Excel;

/**
 * 发货单明细对象 inv_shipment_item
 * 
 * @author yuanwenbo
 */
public class ShipmentItem
{
    /** 主键 */
    private Long itemId;

    /** 发货单ID */
    private Long shipmentId;

    /** 卷号 */
    @Excel(name = "卷号", width = 20)
    private String coilNo;

    /** 物料编码 */
    @Excel(name = "物料编码", width = 20)
    private String materialCode;

    /** 产品名称 */
    @Excel(name = "产品名称", width = 20)
    private String productName;

    /** 规格 */
    @Excel(name = "规格", width = 16)
    private String spec;

    /** 净重(kg) */
    @Excel(name = "净重(kg)", width = 12)
    private BigDecimal netWeight;

    /** 件数 */
    @Excel(name = "件数", width = 10)
    private Integer pieces;

    public Long getItemId()
    {
        return itemId;
    }

    public void setItemId(Long itemId)
    {
        this.itemId = itemId;
    }

    public Long getShipmentId()
    {
        return shipmentId;
    }

    public void setShipmentId(Long shipmentId)
    {
        this.shipmentId = shipmentId;
    }

    public String getCoilNo()
    {
        return coilNo;
    }

    public void setCoilNo(String coilNo)
    {
        this.coilNo = coilNo;
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

    public String getSpec()
    {
        return spec;
    }

    public void setSpec(String spec)
    {
        this.spec = spec;
    }

    public BigDecimal getNetWeight()
    {
        return netWeight;
    }

    public void setNetWeight(BigDecimal netWeight)
    {
        this.netWeight = netWeight;
    }

    public Integer getPieces()
    {
        return pieces;
    }

    public void setPieces(Integer pieces)
    {
        this.pieces = pieces;
    }
}
