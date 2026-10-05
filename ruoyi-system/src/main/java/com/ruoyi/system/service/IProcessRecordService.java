package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.ProcessRecord;

/** 工序报工记录Service接口 */
public interface IProcessRecordService
{
    public List<ProcessRecord> selectProcessRecordList(ProcessRecord processRecord);

    public ProcessRecord selectProcessRecordByRecordId(Long recordId);

    public int insertProcessRecord(ProcessRecord processRecord);

    public int updateProcessRecord(ProcessRecord processRecord);

    public int deleteProcessRecordByRecordIds(Long[] recordIds);
}
