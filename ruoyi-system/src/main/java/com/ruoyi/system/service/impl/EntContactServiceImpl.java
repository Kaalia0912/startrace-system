package com.ruoyi.system.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.domain.EntContact;
import com.ruoyi.system.mapper.EntContactMapper;
import com.ruoyi.system.service.IEntContactService;

/**
 * 企业联系人Service业务层处理
 * 
 * @author yuanwenbo
 */
@Service
public class EntContactServiceImpl implements IEntContactService 
{
    @Autowired
    private EntContactMapper entContactMapper;

    @Override
    public EntContact selectEntContactByContactId(Long contactId)
    {
        return entContactMapper.selectEntContactByContactId(contactId);
    }

    @Override
    public List<EntContact> selectEntContactList(EntContact entContact)
    {
        return entContactMapper.selectEntContactList(entContact);
    }

    @Override
    public List<EntContact> selectEntContactByEntId(Long entId)
    {
        return entContactMapper.selectEntContactByEntId(entId);
    }

    @Override
    public int insertEntContact(EntContact entContact)
    {
        entContact.setCreateTime(new Date());
        return entContactMapper.insertEntContact(entContact);
    }

    @Override
    public int updateEntContact(EntContact entContact)
    {
        entContact.setUpdateTime(new Date());
        return entContactMapper.updateEntContact(entContact);
    }

    @Override
    public int deleteEntContactByContactIds(Long[] contactIds)
    {
        return entContactMapper.deleteEntContactByContactIds(contactIds);
    }

    @Override
    public int deleteEntContactByEntId(Long entId)
    {
        return entContactMapper.deleteEntContactByEntId(entId);
    }
}
