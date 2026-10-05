package com.ruoyi.system.service.impl;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.domain.ProcessRecord;
import com.ruoyi.system.mapper.ProcessRecordMapper;
import com.ruoyi.system.service.IProcessRecordService;

/** 工序报工记录Service业务层处理 */
@Service
public class ProcessRecordServiceImpl implements IProcessRecordService
{
    @Autowired
    private ProcessRecordMapper processRecordMapper;

    @Override
    public List<ProcessRecord> selectProcessRecordList(ProcessRecord processRecord)
    {
        return processRecordMapper.selectProcessRecordList(processRecord);
    }

    @Override
    public ProcessRecord selectProcessRecordByRecordId(Long recordId)
    {
        return processRecordMapper.selectProcessRecordByRecordId(recordId);
    }

    @Override
    public int insertProcessRecord(ProcessRecord processRecord)
    {
        fillLoss(processRecord);
        return processRecordMapper.insertProcessRecord(processRecord);
    }

    @Override
    public int updateProcessRecord(ProcessRecord processRecord)
    {
        fillLoss(processRecord);
        return processRecordMapper.updateProcessRecord(processRecord);
    }

    @Override
    public int deleteProcessRecordByRecordIds(Long[] recordIds)
    {
        return processRecordMapper.deleteProcessRecordByRecordIds(recordIds);
    }

    /** 损耗 = 投入 - 产出（前端没手填损耗时自动算） */
    private void fillLoss(ProcessRecord processRecord)
    {
        if (processRecord.getLossWeight() == null
                && processRecord.getInputWeight() != null
                && processRecord.getOutputWeight() != null)
        {
            BigDecimal loss = processRecord.getInputWeight().subtract(processRecord.getOutputWeight());
            processRecord.setLossWeight(loss);
        }
    }
}
