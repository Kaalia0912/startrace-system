package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.Inspection;

/** 质量检验单Service接口 */
public interface IInspectionService
{
    public List<Inspection> selectInspectionList(Inspection inspection);

    public Inspection selectInspectionByInspectionId(Long inspectionId);

    public boolean checkInspectionNoUnique(Inspection inspection);

    /** 新增（自动判定） */
    public int insertInspection(Inspection inspection);

    /** 修改（自动重新判定） */
    public int updateInspection(Inspection inspection);

    public int deleteInspectionByInspectionIds(Long[] inspectionIds);
}
