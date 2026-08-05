package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 企业联系人对象 ent_contact
 * 
 * @author yuanwenbo
 */
public class EntContact extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 联系人ID */
    private Long contactId;

    /** 所属企业ID */
    private Long entId;

    /** 所属企业名称（非表字段，用于列表展示） */
    @Excel(name = "所属企业", width = 25)
    private String entName;

    /** 联系人姓名 */
    @Excel(name = "联系人姓名", width = 15)
    private String contactName;

    /** 职务 */
    @Excel(name = "职务", width = 15)
    private String position;

    /** 手机号 */
    @Excel(name = "手机号", width = 15)
    private String phone;

    /** 邮箱 */
    @Excel(name = "邮箱", width = 20)
    private String email;

    /** 微信号 */
    @Excel(name = "微信号", width = 15)
    private String wechat;

    /** 是否主要对接人（0否 1是） */
    @Excel(name = "是否主要对接人", readConverterExp = "0=否,1=是")
    private String isMain;

    public void setContactId(Long contactId) 
    {
        this.contactId = contactId;
    }

    public Long getContactId() 
    {
        return contactId;
    }

    public void setEntId(Long entId) 
    {
        this.entId = entId;
    }

    public Long getEntId() 
    {
        return entId;
    }

    public String getEntName() 
    {
        return entName;
    }

    public void setEntName(String entName) 
    {
        this.entName = entName;
    }

    public void setContactName(String contactName) 
    {
        this.contactName = contactName;
    }

    public String getContactName() 
    {
        return contactName;
    }

    public void setPosition(String position) 
    {
        this.position = position;
    }

    public String getPosition() 
    {
        return position;
    }

    public void setPhone(String phone) 
    {
        this.phone = phone;
    }

    public String getPhone() 
    {
        return phone;
    }

    public void setEmail(String email) 
    {
        this.email = email;
    }

    public String getEmail() 
    {
        return email;
    }

    public void setWechat(String wechat) 
    {
        this.wechat = wechat;
    }

    public String getWechat() 
    {
        return wechat;
    }

    public void setIsMain(String isMain) 
    {
        this.isMain = isMain;
    }

    public String getIsMain() 
    {
        return isMain;
    }
}
