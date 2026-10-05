package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SteelProperty;

/**
 * 钢种性能标准Mapper接口
 * 
 * @author yuanwenbo
 */
public interface SteelPropertyMapper
{
    /**
     * 查询钢种性能标准列表
     */
    public List<SteelProperty> selectSteelPropertyList(SteelProperty steelProperty);

    /**
     * 根据主键查询钢种性能标准
     */
    public SteelProperty selectSteelPropertyByPropertyId(Long propertyId);

    /**
     * 按钢种+状态查询（唯一性校验用）
     */
    public SteelProperty checkGradeStateUnique(SteelProperty steelProperty);

    /**
     * 新增钢种性能标准
     */
    public int insertSteelProperty(SteelProperty steelProperty);

    /**
     * 修改钢种性能标准
     */
    public int updateSteelProperty(SteelProperty steelProperty);

    /**
     * 批量删除钢种性能标准
     */
    public int deleteSteelPropertyByPropertyIds(Long[] propertyIds);
}
