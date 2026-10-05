package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.Process;

/**
 * 工序库Mapper接口
 * 
 * @author yuanwenbo
 */
public interface ProcessMapper
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
     * 按工序号查询（唯一性校验用）
     */
    public Process checkProcessNoUnique(Integer processNo);

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
