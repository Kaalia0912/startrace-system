package com.ruoyi.system.domain;

import java.math.BigDecimal;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 宽度允许偏差标准对象 md_tolerance
 * 
 * @author yuanwenbo
 */
public class Tolerance extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long toleranceId;

    /** 标准体系 */
    @Excel(name = "标准体系", width = 14)
    private String standard;

    /** 厚度下限(mm，含) */
    @Excel(name = "厚度下限(mm)", width = 14)
    private BigDecimal thicknessMin;

    /** 厚度上限(mm，不含) */
    @Excel(name = "厚度上限(mm)", width = 14)
    private BigDecimal thicknessMax;

    /** 宽度下限(mm，含) */
    @Excel(name = "宽度下限(mm)", width = 14)
    private BigDecimal widthMin;

    /** 宽度上限(mm，不含) */
    @Excel(name = "宽度上限(mm)", width = 14)
    private BigDecimal widthMax;

    /** 下偏差(mm) */
    @Excel(name = "下偏差(mm)", width = 12)
    private BigDecimal devLower;

    /** 上偏差(mm) */
    @Excel(name = "上偏差(mm)", width = 12)
    private BigDecimal devUpper;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 备注 */
    @Excel(name = "备注", width = 30)
    private String remark;

    public Long getToleranceId()
    {
        return toleranceId;
    }

    public void setToleranceId(Long toleranceId)
    {
        this.toleranceId = toleranceId;
    }

    public String getStandard()
    {
        return standard;
    }

    public void setStandard(String standard)
    {
        this.standard = standard;
    }

    public BigDecimal getThicknessMin()
    {
        return thicknessMin;
    }

    public void setThicknessMin(BigDecimal thicknessMin)
    {
        this.thicknessMin = thicknessMin;
    }

    public BigDecimal getThicknessMax()
    {
        return thicknessMax;
    }

    public void setThicknessMax(BigDecimal thicknessMax)
    {
        this.thicknessMax = thicknessMax;
    }

    public BigDecimal getWidthMin()
    {
        return widthMin;
    }

    public void setWidthMin(BigDecimal widthMin)
    {
        this.widthMin = widthMin;
    }

    public BigDecimal getWidthMax()
    {
        return widthMax;
    }

    public void setWidthMax(BigDecimal widthMax)
    {
        this.widthMax = widthMax;
    }

    public BigDecimal getDevLower()
    {
        return devLower;
    }

    public void setDevLower(BigDecimal devLower)
    {
        this.devLower = devLower;
    }

    public BigDecimal getDevUpper()
    {
        return devUpper;
    }

    public void setDevUpper(BigDecimal devUpper)
    {
        this.devUpper = devUpper;
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
