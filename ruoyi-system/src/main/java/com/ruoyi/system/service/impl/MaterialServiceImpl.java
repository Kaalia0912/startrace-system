package com.ruoyi.system.service.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
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

    /**
     * 导入物料主数据，以物料编码作为新增或更新的唯一依据
     */
    @Override
    public String importMaterial(List<Material> list, boolean updateSupport)
    {
        if (StringUtils.isNull(list) || list.isEmpty())
        {
            throw new ServiceException("导入数据不能为空");
        }

        int insertNum = 0;
        int updateNum = 0;
        int failureNum = 0;
        Set<String> importedCodes = new HashSet<>();
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();

        for (int i = 0; i < list.size(); i++)
        {
            Material material = list.get(i);
            int rowNo = i + 1;
            try
            {
                String materialCode = StringUtils.trim(material.getMaterialCode());
                if (StringUtils.isBlank(materialCode))
                {
                    throw new ServiceException("物料编码为空");
                }
                material.setMaterialCode(materialCode);
                if (!importedCodes.add(materialCode.toLowerCase(Locale.ROOT)))
                {
                    throw new ServiceException("文件内物料编码重复");
                }

                Material existing = materialMapper.checkMaterialCodeUnique(materialCode);
                if (existing == null)
                {
                    if (StringUtils.isBlank(material.getStatus()))
                    {
                        material.setStatus("0");
                    }
                    validateStatus(material.getStatus());
                    material.setCreateTime(new Date());
                    materialMapper.insertMaterial(material);
                    insertNum++;
                    successMsg.append("<br/>").append(insertNum).append("、第 ").append(rowNo).append(" 行（物料编码 ")
                            .append(materialCode).append("）导入成功");
                }
                else if (updateSupport)
                {
                    fillBlankFields(material, existing);
                    material.setMaterialId(existing.getMaterialId());
                    if (StringUtils.isBlank(material.getStatus()))
                    {
                        material.setStatus("0");
                    }
                    validateStatus(material.getStatus());
                    material.setUpdateTime(new Date());
                    materialMapper.updateMaterial(material);
                    updateNum++;
                    successMsg.append("<br/>").append(updateNum).append("、第 ").append(rowNo).append(" 行（物料编码 ")
                            .append(materialCode).append("）更新成功");
                }
                else
                {
                    failureNum++;
                    failureMsg.append("<br/>").append(failureNum).append("、第 ").append(rowNo).append(" 行（物料编码 ")
                            .append(materialCode).append("）已存在");
                }
            }
            catch (Exception e)
            {
                failureNum++;
                failureMsg.append("<br/>").append(failureNum).append("、第 ").append(rowNo).append(" 行导入失败：")
                        .append(e.getMessage());
            }
        }

        if (failureNum > 0)
        {
            failureMsg.insert(0, "导入未全部完成，共 " + failureNum + " 条数据有错误，错误如下：");
            throw new ServiceException(failureMsg.toString());
        }
        successMsg.insert(0, "导入成功：新增 " + insertNum + " 条，更新 " + updateNum + " 条。");
        return successMsg.toString();
    }

    private void validateStatus(String status)
    {
        if (!"0".equals(status) && !"1".equals(status))
        {
            throw new ServiceException("状态只能填写 0（正常）或 1（停用）");
        }
    }

    /**
     * 导入更新时保留 Excel 中未填写的字段，避免把已有资料清空
     */
    private void fillBlankFields(Material target, Material existing)
    {
        if (StringUtils.isBlank(target.getProductName())) target.setProductName(existing.getProductName());
        if (StringUtils.isBlank(target.getMaterial())) target.setMaterial(existing.getMaterial());
        if (StringUtils.isBlank(target.getSpec())) target.setSpec(existing.getSpec());
        if (target.getThickness() == null) target.setThickness(existing.getThickness());
        if (target.getWidth() == null) target.setWidth(existing.getWidth());
        if (target.getCoilInnerDia() == null) target.setCoilInnerDia(existing.getCoilInnerDia());
        if (StringUtils.isBlank(target.getUnit())) target.setUnit(existing.getUnit());
        if (StringUtils.isBlank(target.getStandard())) target.setStandard(existing.getStandard());
        if (StringUtils.isBlank(target.getHardness())) target.setHardness(existing.getHardness());
        if (StringUtils.isBlank(target.getTensileStrength())) target.setTensileStrength(existing.getTensileStrength());
        if (StringUtils.isBlank(target.getSurfaceRa())) target.setSurfaceRa(existing.getSurfaceRa());
        if (target.getCoilWeightMin() == null) target.setCoilWeightMin(existing.getCoilWeightMin());
        if (target.getCoilWeightMax() == null) target.setCoilWeightMax(existing.getCoilWeightMax());
        if (StringUtils.isBlank(target.getPackageCode())) target.setPackageCode(existing.getPackageCode());
        if (target.getRawPrice() == null) target.setRawPrice(existing.getRawPrice());
        if (StringUtils.isBlank(target.getStatus())) target.setStatus(existing.getStatus());
        if (StringUtils.isBlank(target.getRemark())) target.setRemark(existing.getRemark());
    }
}
