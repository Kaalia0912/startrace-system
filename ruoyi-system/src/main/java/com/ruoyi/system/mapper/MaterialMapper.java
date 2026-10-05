package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.Material;

/**
 * 物料主数据Mapper接口
 * 
 * @author yuanwenbo
 */
public interface MaterialMapper
{
    /**
     * 查询物料主数据列表
     */
    public List<Material> selectMaterialList(Material material);

    /**
     * 根据物料ID查询物料主数据
     */
    public Material selectMaterialByMaterialId(Long materialId);

    /**
     * 根据物料编码查询物料主数据（唯一性校验用）
     */
    public Material checkMaterialCodeUnique(String materialCode);

    /**
     * 新增物料主数据
     */
    public int insertMaterial(Material material);

    /**
     * 修改物料主数据
     */
    public int updateMaterial(Material material);

    /**
     * 批量删除物料主数据
     */
    public int deleteMaterialByMaterialIds(Long[] materialIds);

    /**
     * 删除物料主数据
     */
    public int deleteMaterialByMaterialId(Long materialId);
}
