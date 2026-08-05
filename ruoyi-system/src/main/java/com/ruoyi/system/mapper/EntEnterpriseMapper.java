package com.ruoyi.system.mapper;

import java.util.List;
import java.util.Map;
import com.ruoyi.system.domain.EntEnterprise;

/**
 * 企业档案Mapper接口
 * 
 * @author yuanwenbo
 */
public interface EntEnterpriseMapper 
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
     * @param entCode 统一社会信用代码
     * @return 结果
     */
    public EntEnterprise checkEntCodeUnique(String entCode);

    /**
     * 按企业类型统计
     * 
     * @return 统计结果
     */
    public List<Map<String, Object>> selectCountByEntType();

    /**
     * 按行业统计
     * 
     * @return 统计结果
     */
    public List<Map<String, Object>> selectCountByIndustry();

    /**
     * 按规模统计
     * 
     * @return 统计结果
     */
    public List<Map<String, Object>> selectCountByScale();

    /**
     * 按状态统计
     * 
     * @return 统计结果
     */
    public List<Map<String, Object>> selectCountByStatus();

    /**
     * 汇总统计
     * 
     * @return 汇总结果
     */
    public Map<String, Object> selectSummary();
}
