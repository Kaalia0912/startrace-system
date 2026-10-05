package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.Tolerance;

/**
 * 宽度允许偏差标准Mapper接口
 * 
 * @author yuanwenbo
 */
public interface ToleranceMapper
{
    /**
     * 查询宽度允许偏差标准列表
     */
    public List<Tolerance> selectToleranceList(Tolerance tolerance);

    /**
     * 根据主键查询宽度允许偏差标准
     */
    public Tolerance selectToleranceByToleranceId(Long toleranceId);

    /**
     * 新增宽度允许偏差标准
     */
    public int insertTolerance(Tolerance tolerance);

    /**
     * 修改宽度允许偏差标准
     */
    public int updateTolerance(Tolerance tolerance);

    /**
     * 批量删除宽度允许偏差标准
     */
    public int deleteToleranceByToleranceIds(Long[] toleranceIds);
}
