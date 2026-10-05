package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.Inspection;

/** 质量检验单Mapper接口 */
public interface InspectionMapper
{
    public List<Inspection> selectInspectionList(Inspection inspection);

    public Inspection selectInspectionByInspectionId(Long inspectionId);

    public Inspection checkInspectionNoUnique(String inspectionNo);

    public int insertInspection(Inspection inspection);

    public int updateInspection(Inspection inspection);

    public int deleteInspectionByInspectionIds(Long[] inspectionIds);
}
