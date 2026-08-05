package com.ruoyi.system.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.EntEnterprise;
import com.ruoyi.system.mapper.EntEnterpriseMapper;
import com.ruoyi.system.service.IEntEnterpriseService;

/**
 * 企业档案Service业务层处理
 * 
 * @author yuanwenbo
 */
@Service
public class EntEnterpriseServiceImpl implements IEntEnterpriseService 
{
    @Autowired
    private EntEnterpriseMapper entEnterpriseMapper;

    /**
     * 查询企业档案
     * 
     * @param entId 企业档案主键
     * @return 企业档案
     */
    @Override
    public EntEnterprise selectEntEnterpriseByEntId(Long entId)
    {
        return entEnterpriseMapper.selectEntEnterpriseByEntId(entId);
    }

    /**
     * 查询企业档案列表
     * 
     * @param entEnterprise 企业档案
     * @return 企业档案
     */
    @Override
    public List<EntEnterprise> selectEntEnterpriseList(EntEnterprise entEnterprise)
    {
        return entEnterpriseMapper.selectEntEnterpriseList(entEnterprise);
    }

    /**
     * 新增企业档案
     * 
     * @param entEnterprise 企业档案
     * @return 结果
     */
    @Override
    public int insertEntEnterprise(EntEnterprise entEnterprise)
    {
        entEnterprise.setCreateTime(new Date());
        return entEnterpriseMapper.insertEntEnterprise(entEnterprise);
    }

    /**
     * 修改企业档案
     * 
     * @param entEnterprise 企业档案
     * @return 结果
     */
    @Override
    public int updateEntEnterprise(EntEnterprise entEnterprise)
    {
        entEnterprise.setUpdateTime(new Date());
        return entEnterpriseMapper.updateEntEnterprise(entEnterprise);
    }

    /**
     * 批量删除企业档案
     * 
     * @param entIds 需要删除的企业档案主键
     * @return 结果
     */
    @Override
    public int deleteEntEnterpriseByEntIds(Long[] entIds)
    {
        return entEnterpriseMapper.deleteEntEnterpriseByEntIds(entIds);
    }

    /**
     * 校验统一社会信用代码是否唯一
     * 
     * @param entEnterprise 企业档案
     * @return 结果
     */
    @Override
    public boolean checkEntCodeUnique(EntEnterprise entEnterprise)
    {
        EntEnterprise info = entEnterpriseMapper.checkEntCodeUnique(entEnterprise.getEntCode());
        if (info != null && !info.getEntId().equals(entEnterprise.getEntId()))
        {
            return false;
        }
        return true;
    }

    /**
     * 导入企业档案数据
     * 
     * @param list 企业档案列表
     * @param updateSupport 是否更新已存在数据
     * @return 结果
     */
    @Override
    public String importEnterprise(List<EntEnterprise> list, boolean updateSupport)
    {
        if (StringUtils.isNull(list) || list.size() == 0)
        {
            throw new ServiceException("导入数据不能为空");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (EntEnterprise ent : list)
        {
            try
            {
                EntEnterprise exist = entEnterpriseMapper.checkEntCodeUnique(ent.getEntCode());
                if (StringUtils.isNull(exist))
                {
                    ent.setCreateTime(new Date());
                    entEnterpriseMapper.insertEntEnterprise(ent);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、企业 ").append(ent.getEntName()).append(" 导入成功");
                }
                else if (updateSupport)
                {
                    ent.setEntId(exist.getEntId());
                    ent.setUpdateTime(new Date());
                    entEnterpriseMapper.updateEntEnterprise(ent);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、企业 ").append(ent.getEntName()).append(" 更新成功");
                }
                else
                {
                    failureNum++;
                    failureMsg.append("<br/>").append(failureNum).append("、企业 ").append(ent.getEntName()).append(" 已存在");
                }
            }
            catch (Exception e)
            {
                failureNum++;
                failureMsg.append("<br/>").append(failureNum).append("、企业 ").append(ent.getEntName()).append(" 导入失败：").append(e.getMessage());
            }
        }
        if (failureNum > 0)
        {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        }
        else
        {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }
}
