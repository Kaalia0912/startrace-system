package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.EntContact;

/**
 * 企业联系人Mapper接口
 * 
 * @author yuanwenbo
 */
public interface EntContactMapper 
{
    /**
     * 查询企业联系人
     * 
     * @param contactId 企业联系人主键
     * @return 企业联系人
     */
    public EntContact selectEntContactByContactId(Long contactId);

    /**
     * 查询企业联系人列表
     * 
     * @param entContact 企业联系人
     * @return 企业联系人集合
     */
    public List<EntContact> selectEntContactList(EntContact entContact);

    /**
     * 查询某企业的联系人列表
     * 
     * @param entId 企业ID
     * @return 企业联系人集合
     */
    public List<EntContact> selectEntContactByEntId(Long entId);

    /**
     * 新增企业联系人
     * 
     * @param entContact 企业联系人
     * @return 结果
     */
    public int insertEntContact(EntContact entContact);

    /**
     * 修改企业联系人
     * 
     * @param entContact 企业联系人
     * @return 结果
     */
    public int updateEntContact(EntContact entContact);

    /**
     * 批量删除企业联系人
     * 
     * @param contactIds 需要删除的企业联系人主键集合
     * @return 结果
     */
    public int deleteEntContactByContactIds(Long[] contactIds);

    /**
     * 删除某企业的全部联系人
     * 
     * @param entId 企业ID
     * @return 结果
     */
    public int deleteEntContactByEntId(Long entId);
}
