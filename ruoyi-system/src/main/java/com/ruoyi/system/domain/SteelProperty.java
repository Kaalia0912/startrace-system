package com.ruoyi.system.domain;

import java.math.BigDecimal;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 钢种性能标准对象 md_steel_property
 * 
 * @author yuanwenbo
 */
public class SteelProperty extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long propertyId;

    /** 钢种 */
    @Excel(name = "钢种", width = 12)
    private String steelGrade;

    /** 状态 */
    @Excel(name = "状态", width = 10)
    private String state;

    /** 屈服强度下限(MPa) */
    @Excel(name = "屈服下限(MPa)", width = 14)
    private BigDecimal yieldMin;

    /** 抗拉强度下限(MPa) */
    @Excel(name = "抗拉下限(MPa)", width = 14)
    private BigDecimal tensileMin;

    /** 延伸率下限(%) */
    @Excel(name = "延伸率下限(%)", width = 14)
    private BigDecimal elongationMin;

    /** 维氏硬度下限HV */
    @Excel(name = "硬度下限HV", width = 12)
    private BigDecimal hvMin;

    /** 维氏硬度上限HV */
    @Excel(name = "硬度上限HV", width = 12)
    private BigDecimal hvMax;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 备注 */
    @Excel(name = "备注", width = 30)
    private String remark;

    public Long getPropertyId()
    {
        return propertyId;
    }

    public void setPropertyId(Long propertyId)
    {
        this.propertyId = propertyId;
    }

    public String getSteelGrade()
    {
        return steelGrade;
    }

    public void setSteelGrade(String steelGrade)
    {
        this.steelGrade = steelGrade;
    }

    public String getState()
    {
        return state;
    }

    public void setState(String state)
    {
        this.state = state;
    }

    public BigDecimal getYieldMin()
    {
        return yieldMin;
    }

    public void setYieldMin(BigDecimal yieldMin)
    {
        this.yieldMin = yieldMin;
    }

    public BigDecimal getTensileMin()
    {
        return tensileMin;
    }

    public void setTensileMin(BigDecimal tensileMin)
    {
        this.tensileMin = tensileMin;
    }

    public BigDecimal getElongationMin()
    {
        return elongationMin;
    }

    public void setElongationMin(BigDecimal elongationMin)
    {
        this.elongationMin = elongationMin;
    }

    public BigDecimal getHvMin()
    {
        return hvMin;
    }

    public void setHvMin(BigDecimal hvMin)
    {
        this.hvMin = hvMin;
    }

    public BigDecimal getHvMax()
    {
        return hvMax;
    }

    public void setHvMax(BigDecimal hvMax)
    {
        this.hvMax = hvMax;
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
