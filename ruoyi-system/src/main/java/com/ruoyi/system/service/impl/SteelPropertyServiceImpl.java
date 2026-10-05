package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SteelProperty;
import com.ruoyi.system.mapper.SteelPropertyMapper;
import com.ruoyi.system.service.ISteelPropertyService;

/**
 * 钢种性能标准Service业务层处理
 * 
 * @author yuanwenbo
 */
@Service
public class SteelPropertyServiceImpl implements ISteelPropertyService
{
    @Autowired
    private SteelPropertyMapper steelPropertyMapper;

    /**
     * 查询钢种性能标准列表
     */
    @Override
    public List<SteelProperty> selectSteelPropertyList(SteelProperty steelProperty)
    {
        return steelPropertyMapper.selectSteelPropertyList(steelProperty);
    }

    /**
     * 根据主键查询钢种性能标准
     */
    @Override
    public SteelProperty selectSteelPropertyByPropertyId(Long propertyId)
    {
        return steelPropertyMapper.selectSteelPropertyByPropertyId(propertyId);
    }

    /**
     * 校验钢种+状态是否唯一
     */
    @Override
    public boolean checkGradeStateUnique(SteelProperty steelProperty)
    {
        Long propertyId = StringUtils.isNull(steelProperty.getPropertyId()) ? -1L : steelProperty.getPropertyId();
        SteelProperty info = steelPropertyMapper.checkGradeStateUnique(steelProperty);
        if (StringUtils.isNotNull(info) && info.getPropertyId().longValue() != propertyId.longValue())
        {
            return false;
        }
        return true;
    }

    /**
     * 新增钢种性能标准
     */
    @Override
    public int insertSteelProperty(SteelProperty steelProperty)
    {
        if (!checkGradeStateUnique(steelProperty))
        {
            throw new ServiceException("新增失败，" + steelProperty.getSteelGrade() + " 的 " + steelProperty.getState() + " 状态已存在");
        }
        return steelPropertyMapper.insertSteelProperty(steelProperty);
    }

    /**
     * 修改钢种性能标准
     */
    @Override
    public int updateSteelProperty(SteelProperty steelProperty)
    {
        if (!checkGradeStateUnique(steelProperty))
        {
            throw new ServiceException("修改失败，" + steelProperty.getSteelGrade() + " 的 " + steelProperty.getState() + " 状态已存在");
        }
        return steelPropertyMapper.updateSteelProperty(steelProperty);
    }

    /**
     * 批量删除钢种性能标准
     */
    @Override
    public int deleteSteelPropertyByPropertyIds(Long[] propertyIds)
    {
        return steelPropertyMapper.deleteSteelPropertyByPropertyIds(propertyIds);
    }
}
