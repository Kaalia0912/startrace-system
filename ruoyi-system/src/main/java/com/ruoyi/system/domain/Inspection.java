package com.ruoyi.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/** 质量检验单对象 qc_inspection */
public class Inspection extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long inspectionId;

    @Excel(name = "检验单号", width = 22)
    private String inspectionNo;

    @Excel(name = "检验来源", readConverterExp = "0=来料,1=过程,2=成品")
    private String source;

    @Excel(name = "订单号", width = 20)
    private String orderNo;

    @Excel(name = "卷号", width = 20)
    private String coilNo;

    @Excel(name = "物料编码", width = 20)
    private String materialCode;

    @Excel(name = "钢种", width = 12)
    private String steelGrade;

    @Excel(name = "状态", width = 10)
    private String state;

    @Excel(name = "公差标准", width = 14)
    private String standard;

    @Excel(name = "实测厚度(mm)", width = 14)
    private BigDecimal thicknessVal;

    @Excel(name = "名义宽度(mm)", width = 14)
    private BigDecimal nominalWidth;

    @Excel(name = "实测宽度(mm)", width = 14)
    private BigDecimal widthVal;

    @Excel(name = "实测屈服(MPa)", width = 14)
    private BigDecimal yieldVal;

    @Excel(name = "实测抗拉(MPa)", width = 14)
    private BigDecimal tensileVal;

    @Excel(name = "实测延伸率(%)", width = 14)
    private BigDecimal elongationVal;

    @Excel(name = "实测硬度HV", width = 12)
    private BigDecimal hvVal;

    @Excel(name = "实测表面精度", width = 14)
    private String surfaceRaVal;

    @Excel(name = "判定结果", readConverterExp = "0=合格,1=不合格,2=让步接收")
    private String judgeResult;

    @Excel(name = "判定明细", width = 60)
    private String judgeDetail;

    @Excel(name = "检验员", width = 12)
    private String inspector;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "检验日期", width = 12, dateFormat = "yyyy-MM-dd")
    private Date inspectDate;

    @Excel(name = "备注", width = 30)
    private String remark;

    public Long getInspectionId() { return inspectionId; }
    public void setInspectionId(Long inspectionId) { this.inspectionId = inspectionId; }
    public String getInspectionNo() { return inspectionNo; }
    public void setInspectionNo(String inspectionNo) { this.inspectionNo = inspectionNo; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    public String getCoilNo() { return coilNo; }
    public void setCoilNo(String coilNo) { this.coilNo = coilNo; }
    public String getMaterialCode() { return materialCode; }
    public void setMaterialCode(String materialCode) { this.materialCode = materialCode; }
    public String getSteelGrade() { return steelGrade; }
    public void setSteelGrade(String steelGrade) { this.steelGrade = steelGrade; }
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    public String getStandard() { return standard; }
    public void setStandard(String standard) { this.standard = standard; }
    public BigDecimal getThicknessVal() { return thicknessVal; }
    public void setThicknessVal(BigDecimal thicknessVal) { this.thicknessVal = thicknessVal; }
    public BigDecimal getNominalWidth() { return nominalWidth; }
    public void setNominalWidth(BigDecimal nominalWidth) { this.nominalWidth = nominalWidth; }
    public BigDecimal getWidthVal() { return widthVal; }
    public void setWidthVal(BigDecimal widthVal) { this.widthVal = widthVal; }
    public BigDecimal getYieldVal() { return yieldVal; }
    public void setYieldVal(BigDecimal yieldVal) { this.yieldVal = yieldVal; }
    public BigDecimal getTensileVal() { return tensileVal; }
    public void setTensileVal(BigDecimal tensileVal) { this.tensileVal = tensileVal; }
    public BigDecimal getElongationVal() { return elongationVal; }
    public void setElongationVal(BigDecimal elongationVal) { this.elongationVal = elongationVal; }
    public BigDecimal getHvVal() { return hvVal; }
    public void setHvVal(BigDecimal hvVal) { this.hvVal = hvVal; }
    public String getSurfaceRaVal() { return surfaceRaVal; }
    public void setSurfaceRaVal(String surfaceRaVal) { this.surfaceRaVal = surfaceRaVal; }
    public String getJudgeResult() { return judgeResult; }
    public void setJudgeResult(String judgeResult) { this.judgeResult = judgeResult; }
    public String getJudgeDetail() { return judgeDetail; }
    public void setJudgeDetail(String judgeDetail) { this.judgeDetail = judgeDetail; }
    public String getInspector() { return inspector; }
    public void setInspector(String inspector) { this.inspector = inspector; }
    public Date getInspectDate() { return inspectDate; }
    public void setInspectDate(Date inspectDate) { this.inspectDate = inspectDate; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
