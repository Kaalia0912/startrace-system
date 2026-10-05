package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.Process;

/**
 * 工序库Service接口
 * 
 * @author yuanwenbo
 */
public interface IProcessService
{
    /**
     * 查询工序库列表
     */
    public List<Process> selectProcessList(Process process);

    /**
     * 根据主键查询工序
     */
    public Process selectProcessByProcessId(Long processId);

    /**
     * 校验工序号是否唯一
     */
    public boolean checkProcessNoUnique(Process process);

    /**
     * 新增工序
     */
    public int insertProcess(Process process);

    /**
     * 修改工序
     */
    public int updateProcess(Process process);

    /**
     * 批量删除工序
     */
    public int deleteProcessByProcessIds(Long[] processIds);
}
