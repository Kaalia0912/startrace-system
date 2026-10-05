package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SteelProperty;

/**
 * 钢种性能标准Service接口
 * 
 * @author yuanwenbo
 */
public interface ISteelPropertyService
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
     * 校验钢种+状态是否唯一
     */
    public boolean checkGradeStateUnique(SteelProperty steelProperty);

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
