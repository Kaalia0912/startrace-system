package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.ProcessRecord;

/** 工序报工记录Mapper接口 */
public interface ProcessRecordMapper
{
    public List<ProcessRecord> selectProcessRecordList(ProcessRecord processRecord);

    public ProcessRecord selectProcessRecordByRecordId(Long recordId);

    public int insertProcessRecord(ProcessRecord processRecord);

    public int updateProcessRecord(ProcessRecord processRecord);

    public int deleteProcessRecordByRecordIds(Long[] recordIds);
}
