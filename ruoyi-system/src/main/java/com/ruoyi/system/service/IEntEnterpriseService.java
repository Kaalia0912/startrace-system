package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.EntEnterprise;

/**
 * 企业档案Service接口
 * 
 * @author yuanwenbo
 */
public interface IEntEnterpriseService 
{
    /**
     * 查询企业档案
     * 
     * @param entId 企业档案主键
     * @return 企业档案
     */
    public EntEnterprise selectEntEnterpriseByEntId(Long entId);

    /**
     * 查询企业档案列表
     * 
     * @param entEnterprise 企业档案
     * @return 企业档案集合
     */
    public List<EntEnterprise> selectEntEnterpriseList(EntEnterprise entEnterprise);

    /**
     * 新增企业档案
     * 
     * @param entEnterprise 企业档案
     * @return 结果
     */
    public int insertEntEnterprise(EntEnterprise entEnterprise);

    /**
     * 修改企业档案
     * 
     * @param entEnterprise 企业档案
     * @return 结果
     */
    public int updateEntEnterprise(EntEnterprise entEnterprise);

    /**
     * 批量删除企业档案
     * 
     * @param entIds 需要删除的企业档案主键集合
     * @return 结果
     */
    public int deleteEntEnterpriseByEntIds(Long[] entIds);

    /**
     * 校验统一社会信用代码是否唯一
     * 
     * @param entEnterprise 企业档案
     * @return 结果
     */
    public boolean checkEntCodeUnique(EntEnterprise entEnterprise);

    /**
     * 导入企业档案数据
     * 
     * @param list 企业档案列表
     * @param updateSupport 是否更新已存在数据
     * @return 结果
     */
    public String importEnterprise(List<EntEnterprise> list, boolean updateSupport);
}
