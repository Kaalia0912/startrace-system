package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.Material;

/**
 * 物料主数据Service接口
 * 
 * @author yuanwenbo
 */
public interface IMaterialService
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
     * 校验物料编码是否唯一
     *
     * @param material 物料主数据
     * @return 结果（true 唯一）
     */
    public boolean checkMaterialCodeUnique(Material material);

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
}
