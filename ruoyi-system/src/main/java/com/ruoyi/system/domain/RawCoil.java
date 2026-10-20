package com.ruoyi.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 原料卷料台账对象 inv_raw_coil
 * 
 * @author yuanwenbo
 */
public class RawCoil extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long coilId;

    /** 卷号 */
    @Excel(name = "卷号", width = 20)
    private String coilNo;

    /** 供应商 */
    @Excel(name = "供应商", width = 20)
    private String supplier;

    /** 材质 */
    @Excel(name = "材质", width = 12)
    private String material;

    /** 规格型号(mm) */
    @Excel(name = "规格型号(mm)", width = 16)
    private String spec;

    /** 厚度(mm) */
    @Excel(name = "厚度(mm)", width = 12)
    private BigDecimal thickness;

    /** 宽度(mm) */
    @Excel(name = "宽度(mm)", width = 12)
    private BigDecimal width;

    /** 净重(kg) */
    @Excel(name = "净重(kg)", width = 12)
    private BigDecimal netWeight;

    /** 硬度(HV) */
    @Excel(name = "硬度(HV)", width = 12)
    private String hardness;

    /** 卷内径(mm) */
    @Excel(name = "卷内径(mm)", width = 12)
    private BigDecimal innerDia;

    /** 到货日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "到货日期", width = 12, dateFormat = "yyyy-MM-dd")
    private Date arriveDate;

    /** 库位 */
    @Excel(name = "库位", width = 12)
    private String location;

    /** 来料检验状态（0待检 1合格 2不合格） */
    @Excel(name = "检验状态", readConverterExp = "0=待检,1=合格,2=不合格")
    private String qcStatus;

    /** 卷状态（0在库 1已开卷 2已用完） */
    @Excel(name = "卷状态", readConverterExp = "0=在库,1=已开卷,2=已用完")
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

    public String getSupplier()
    {
        return supplier;
    }

    public void setSupplier(String supplier)
    {
        this.supplier = supplier;
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

    public BigDecimal getThickness()
    {
        return thickness;
    }

    public void setThickness(BigDecimal thickness)
    {
        this.thickness = thickness;
    }

    public BigDecimal getWidth()
    {
        return width;
    }

    public void setWidth(BigDecimal width)
    {
        this.width = width;
    }

    public BigDecimal getNetWeight()
    {
        return netWeight;
    }

    public void setNetWeight(BigDecimal netWeight)
    {
        this.netWeight = netWeight;
    }

    public BigDecimal getInnerDia()
    {
        return innerDia;
    }

    public void setInnerDia(BigDecimal innerDia)
    {
        this.innerDia = innerDia;
    }

    public String getHardness()
    {
        return hardness;
    }

    public void setHardness(String hardness)
    {
        this.hardness = hardness;
    }

    public Date getArriveDate()
    {
        return arriveDate;
    }

    public void setArriveDate(Date arriveDate)
    {
        this.arriveDate = arriveDate;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public String getQcStatus()
    {
        return qcStatus;
    }

    public void setQcStatus(String qcStatus)
    {
        this.qcStatus = qcStatus;
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
