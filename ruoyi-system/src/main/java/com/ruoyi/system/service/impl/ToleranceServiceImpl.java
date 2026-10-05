package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.domain.Tolerance;
import com.ruoyi.system.mapper.ToleranceMapper;
import com.ruoyi.system.service.IToleranceService;

/**
 * 宽度允许偏差标准Service业务层处理
 * 
 * @author yuanwenbo
 */
@Service
public class ToleranceServiceImpl implements IToleranceService
{
    @Autowired
    private ToleranceMapper toleranceMapper;

    /**
     * 查询宽度允许偏差标准列表
     */
    @Override
    public List<Tolerance> selectToleranceList(Tolerance tolerance)
    {
        return toleranceMapper.selectToleranceList(tolerance);
    }

    /**
     * 根据主键查询宽度允许偏差标准
     */
    @Override
    public Tolerance selectToleranceByToleranceId(Long toleranceId)
    {
        return toleranceMapper.selectToleranceByToleranceId(toleranceId);
    }

    /**
     * 新增宽度允许偏差标准
     */
    @Override
    public int insertTolerance(Tolerance tolerance)
    {
        return toleranceMapper.insertTolerance(tolerance);
    }

    /**
     * 修改宽度允许偏差标准
     */
    @Override
    public int updateTolerance(Tolerance tolerance)
    {
        return toleranceMapper.updateTolerance(tolerance);
    }

    /**
     * 批量删除宽度允许偏差标准
     */
    @Override
    public int deleteToleranceByToleranceIds(Long[] toleranceIds)
    {
        return toleranceMapper.deleteToleranceByToleranceIds(toleranceIds);
    }
}
