package com.ruoyi.system.domain;

import java.math.BigDecimal;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 物料主数据对象 md_material
 * 
 * @author yuanwenbo
 */
public class Material extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 物料ID */
    private Long materialId;

    /** 物料编码 */
    @Excel(name = "物料编码", width = 22)
    private String materialCode;

    /** 产品名称 */
    @Excel(name = "产品名称", width = 20)
    private String productName;

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

    /** 卷内径(mm) */
    @Excel(name = "卷内径(mm)", width = 12)
    private BigDecimal coilInnerDia;

    /** 单位 */
    @Excel(name = "单位", width = 8)
    private String unit;

    /** 执行标准 */
    @Excel(name = "执行标准", width = 14)
    private String standard;

    /** 硬度(HV) */
    @Excel(name = "硬度(HV)", width = 12)
    private String hardness;

    /** 抗拉(MPa) */
    @Excel(name = "抗拉(MPa)", width = 12)
    private String tensileStrength;

    /** 表面精度Ra */
    @Excel(name = "表面精度Ra", width = 12)
    private String surfaceRa;

    /** 单卷重下限(kg) */
    @Excel(name = "单卷重下限(kg)", width = 14)
    private BigDecimal coilWeightMin;

    /** 单卷重上限(kg) */
    @Excel(name = "单卷重上限(kg)", width = 14)
    private BigDecimal coilWeightMax;

    /** 包装方式 */
    @Excel(name = "包装方式", width = 12)
    private String packageCode;

    /** 原料单价(元/kg)，毛利统计用 */
    @Excel(name = "原料单价(元/kg)", width = 16)
    private BigDecimal rawPrice;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 备注 */
    @Excel(name = "备注", width = 30)
    private String remark;

    public Long getMaterialId()
    {
        return materialId;
    }

    public void setMaterialId(Long materialId)
    {
        this.materialId = materialId;
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

    public BigDecimal getCoilInnerDia()
    {
        return coilInnerDia;
    }

    public void setCoilInnerDia(BigDecimal coilInnerDia)
    {
        this.coilInnerDia = coilInnerDia;
    }

    public String getUnit()
    {
        return unit;
    }

    public void setUnit(String unit)
    {
        this.unit = unit;
    }

    public String getStandard()
    {
        return standard;
    }

    public void setStandard(String standard)
    {
        this.standard = standard;
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

    public String getSurfaceRa()
    {
        return surfaceRa;
    }

    public void setSurfaceRa(String surfaceRa)
    {
        this.surfaceRa = surfaceRa;
    }

    public BigDecimal getCoilWeightMin()
    {
        return coilWeightMin;
    }

    public void setCoilWeightMin(BigDecimal coilWeightMin)
    {
        this.coilWeightMin = coilWeightMin;
    }

    public BigDecimal getCoilWeightMax()
    {
        return coilWeightMax;
    }

    public void setCoilWeightMax(BigDecimal coilWeightMax)
    {
        this.coilWeightMax = coilWeightMax;
    }

    public String getPackageCode()
    {
        return packageCode;
    }

    public void setPackageCode(String packageCode)
    {
        this.packageCode = packageCode;
    }

    public BigDecimal getRawPrice()
    {
        return rawPrice;
    }

    public void setRawPrice(BigDecimal rawPrice)
    {
        this.rawPrice = rawPrice;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
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
