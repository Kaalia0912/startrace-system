package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.Process;
import com.ruoyi.system.mapper.ProcessMapper;
import com.ruoyi.system.service.IProcessService;

/**
 * 工序库Service业务层处理
 * 
 * @author yuanwenbo
 */
@Service
public class ProcessServiceImpl implements IProcessService
{
    @Autowired
    private ProcessMapper processMapper;

    /**
     * 查询工序库列表
     */
    @Override
    public List<Process> selectProcessList(Process process)
    {
        return processMapper.selectProcessList(process);
    }

    /**
     * 根据主键查询工序
     */
    @Override
    public Process selectProcessByProcessId(Long processId)
    {
        return processMapper.selectProcessByProcessId(processId);
    }

    /**
     * 校验工序号是否唯一
     */
    @Override
    public boolean checkProcessNoUnique(Process process)
    {
        Long processId = StringUtils.isNull(process.getProcessId()) ? -1L : process.getProcessId();
        Process info = processMapper.checkProcessNoUnique(process.getProcessNo());
        if (StringUtils.isNotNull(info) && info.getProcessId().longValue() != processId.longValue())
        {
            return false;
        }
        return true;
    }

    /**
     * 新增工序
     */
    @Override
    public int insertProcess(Process process)
    {
        if (!checkProcessNoUnique(process))
        {
            throw new ServiceException("新增失败，工序号 " + process.getProcessNo() + " 已存在");
        }
        return processMapper.insertProcess(process);
    }

    /**
     * 修改工序
     */
    @Override
    public int updateProcess(Process process)
    {
        if (!checkProcessNoUnique(process))
        {
            throw new ServiceException("修改失败，工序号 " + process.getProcessNo() + " 已存在");
        }
        return processMapper.updateProcess(process);
    }

    /**
     * 批量删除工序
     */
    @Override
    public int deleteProcessByProcessIds(Long[] processIds)
    {
        return processMapper.deleteProcessByProcessIds(processIds);
    }
}
