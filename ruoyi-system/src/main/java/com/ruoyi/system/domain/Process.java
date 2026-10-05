package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 工序库对象 md_process
 * 
 * @author yuanwenbo
 */
public class Process extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long processId;

    /** 工序号 */
    @Excel(name = "工序号", width = 10)
    private Integer processNo;

    /** 工序名称 */
    @Excel(name = "工序名称", width = 18)
    private String processName;

    /** 是否质检点（1是 0否） */
    @Excel(name = "是否质检点", readConverterExp = "1=是,0=否")
    private String isQc;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 备注 */
    @Excel(name = "备注", width = 40)
    private String remark;

    public Long getProcessId()
    {
        return processId;
    }

    public void setProcessId(Long processId)
    {
        this.processId = processId;
    }

    public Integer getProcessNo()
    {
        return processNo;
    }

    public void setProcessNo(Integer processNo)
    {
        this.processNo = processNo;
    }

    public String getProcessName()
    {
        return processName;
    }

    public void setProcessName(String processName)
    {
        this.processName = processName;
    }

    public String getIsQc()
    {
        return isQc;
    }

    public void setIsQc(String isQc)
    {
        this.isQc = isQc;
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
