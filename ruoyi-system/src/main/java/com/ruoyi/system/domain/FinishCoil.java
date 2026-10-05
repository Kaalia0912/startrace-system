package com.ruoyi.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 成品卷台账对象 inv_finish_coil
 * 
 * @author yuanwenbo
 */
public class FinishCoil extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long coilId;

    /** 卷号 */
    @Excel(name = "卷号", width = 20)
    private String coilNo;

    /** 卡号 */
    @Excel(name = "卡号", width = 14)
    private String labelNo;

    /** 订单号 */
    @Excel(name = "订单号", width = 20)
    private String orderNo;

    /** 客户名称 */
    @Excel(name = "客户名称", width = 28)
    private String customerName;

    /** 物料编码 */
    @Excel(name = "物料编码", width = 20)
    private String materialCode;

    /** 产品名称 */
    @Excel(name = "产品名称", width = 20)
    private String productName;

    /** 钢号 */
    @Excel(name = "钢号", width = 12)
    private String material;

    /** 规格 */
    @Excel(name = "规格", width = 16)
    private String spec;

    /** 性能(硬度HV) */
    @Excel(name = "性能(硬度HV)", width = 14)
    private String hardness;

    /** 表面精度 */
    @Excel(name = "表面精度", width = 14)
    private String surfaceRa;

    /** 净重(kg) */
    @Excel(name = "净重(kg)", width = 12)
    private BigDecimal netWeight;

    /** 件数 */
    @Excel(name = "件数", width = 10)
    private Integer pieces;

    /** 生产日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "生产日期", width = 12, dateFormat = "yyyy-MM-dd")
    private Date produceDate;

    /** 检验员 */
    @Excel(name = "检验员", width = 12)
    private String checker;

    /** 库位 */
    @Excel(name = "库位", width = 12)
    private String location;

    /** 入库日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "入库日期", width = 12, dateFormat = "yyyy-MM-dd")
    private Date inDate;

    /** 卷状态（0在库 1已发货） */
    @Excel(name = "卷状态", readConverterExp = "0=在库,1=已发货")
    private String coilStatus;

    /** 备注 */
    @Excel(name = "备注", width = 30)
    private String remark;

    public Long getCoilId()
    {
        return coilId;
    }

    public void setCoilId(Long coilId)
    {
        this.coilId = coilId;
    }

    public String getCoilNo()
    {
        return coilNo;
    }

    public void setCoilNo(String coilNo)
    {
        this.coilNo = coilNo;
    }

    public String getLabelNo()
    {
        return labelNo;
    }

    public void setLabelNo(String labelNo)
    {
        this.labelNo = labelNo;
    }

    public String getOrderNo()
    {
        return orderNo;
    }

    public void setOrderNo(String orderNo)
    {
        this.orderNo = orderNo;
    }

    public String getCustomerName()
    {
        return customerName;
    }

    public void setCustomerName(String customerName)
    {
        this.customerName = customerName;
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

    public String getSurfaceRa()
    {
        return surfaceRa;
    }

    public void setSurfaceRa(String surfaceRa)
    {
        this.surfaceRa = surfaceRa;
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

    public Date getProduceDate()
    {
        return produceDate;
    }

    public void setProduceDate(Date produceDate)
    {
        this.produceDate = produceDate;
    }

    public String getChecker()
    {
        return checker;
    }

    public void setChecker(String checker)
    {
        this.checker = checker;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public Date getInDate()
    {
        return inDate;
    }

    public void setInDate(Date inDate)
    {
        this.inDate = inDate;
    }

    public String getCoilStatus()
    {
        return coilStatus;
    }

    public void setCoilStatus(String coilStatus)
    {
        this.coilStatus = coilStatus;
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
