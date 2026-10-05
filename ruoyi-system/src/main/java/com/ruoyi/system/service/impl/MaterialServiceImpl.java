package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.Material;
import com.ruoyi.system.mapper.MaterialMapper;
import com.ruoyi.system.service.IMaterialService;

/**
 * 物料主数据Service业务层处理
 * 
 * @author yuanwenbo
 */
@Service
public class MaterialServiceImpl implements IMaterialService
{
    @Autowired
    private MaterialMapper materialMapper;

    /**
     * 查询物料主数据列表
     */
    @Override
    public List<Material> selectMaterialList(Material material)
    {
        return materialMapper.selectMaterialList(material);
    }

    /**
     * 根据物料ID查询物料主数据
     */
    @Override
    public Material selectMaterialByMaterialId(Long materialId)
    {
        return materialMapper.selectMaterialByMaterialId(materialId);
    }

    /**
     * 校验物料编码是否唯一
     */
    @Override
    public boolean checkMaterialCodeUnique(Material material)
    {
        Long materialId = StringUtils.isNull(material.getMaterialId()) ? -1L : material.getMaterialId();
        Material info = materialMapper.checkMaterialCodeUnique(material.getMaterialCode());
        if (StringUtils.isNotNull(info) && info.getMaterialId().longValue() != materialId.longValue())
        {
            return false;
        }
        return true;
    }

    /**
     * 新增物料主数据
     */
    @Override
    public int insertMaterial(Material material)
    {
        if (!checkMaterialCodeUnique(material))
        {
            throw new ServiceException("新增物料失败，物料编码 " + material.getMaterialCode() + " 已存在");
        }
        return materialMapper.insertMaterial(material);
    }

    /**
     * 修改物料主数据
     */
    @Override
    public int updateMaterial(Material material)
    {
        if (!checkMaterialCodeUnique(material))
        {
            throw new ServiceException("修改物料失败，物料编码 " + material.getMaterialCode() + " 已存在");
        }
        return materialMapper.updateMaterial(material);
    }

    /**
     * 批量删除物料主数据
     */
    @Override
    public int deleteMaterialByMaterialIds(Long[] materialIds)
    {
        return materialMapper.deleteMaterialByMaterialIds(materialIds);
    }
}
