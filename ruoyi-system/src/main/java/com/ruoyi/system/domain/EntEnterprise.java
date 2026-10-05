package com.ruoyi.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 企业档案对象 ent_enterprise
 * 
 * @author yuanwenbo
 */
public class EntEnterprise extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 企业ID */
    private Long entId;

    /** 统一社会信用代码 */
    @Excel(name = "统一社会信用代码", width = 25)
    private String entCode;

    /** 企业名称 */
    @Excel(name = "企业名称", width = 30)
    private String entName;

    /** 所属行业 */
    @Excel(name = "所属行业", dictType = "enterprise_industry")
    private String industry;

    /** 企业类型（高新/三类/一般） */
    @Excel(name = "企业类型", dictType = "enterprise_type")
    private String entType;

    /** 企业规模 */
    @Excel(name = "企业规模", dictType = "enterprise_scale")
    private String scale;

    /** 注册地址 */
    @Excel(name = "注册地址", width = 30)
    private String address;

    /** 法定代表人 */
    @Excel(name = "法定代表人", width = 15)
    private String legalPerson;

    /** 联系电话 */
    @Excel(name = "联系电话", width = 15)
    private String phone;

    /** 邮箱 */
    @Excel(name = "邮箱", width = 20)
    private String email;

    /** 成立日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "成立日期", width = 15, dateFormat = "yyyy-MM-dd")
    private Date establishDate;

    /** 注册资本(万元) */
    @Excel(name = "注册资本(万元)", width = 18)
    private BigDecimal registeredCapital;

    /** 员工人数 */
    @Excel(name = "员工人数", width = 12)
    private Integer employeeCount;

    /** 状态（0在营 1注销） */
    @Excel(name = "状态", readConverterExp = "0=在营,1=注销")
   private String status;

    /** 开票抬头 */
    private String invoiceTitle;

    /** 税号 */
    private String taxNo;

    /** 开票地址 */
    private String invoiceAddress;

    /** 开票电话 */
    private String invoicePhone;

    /** 开户行 */
    private String bankName;

    /** 银行账号 */
    private String bankAccount;

    /** 账期(天) */
    private Integer paymentDays;

    /** 信用额度(元) */
    private java.math.BigDecimal creditLimit;

    /** 默认收货地址 */
    private String defaultAddress;

    public void setEntId(Long entId) 
    {
        this.entId = entId;
    }

    public Long getEntId() 
    {
        return entId;
    }

    public void setEntCode(String entCode) 
    {
        this.entCode = entCode;
    }

    public String getEntCode() 
    {
        return entCode;
    }

    public void setEntName(String entName) 
    {
        this.entName = entName;
    }

    public String getEntName() 
    {
        return entName;
    }

    public void setIndustry(String industry) 
    {
        this.industry = industry;
    }

    public String getIndustry() 
    {
        return industry;
    }

    public void setEntType(String entType) 
    {
        this.entType = entType;
    }

    public String getEntType() 
    {
        return entType;
    }

    public void setScale(String scale) 
    {
        this.scale = scale;
    }

    public String getScale() 
    {
        return scale;
    }

    public void setAddress(String address) 
    {
        this.address = address;
    }

    public String getAddress() 
    {
        return address;
    }

    public void setLegalPerson(String legalPerson) 
    {
        this.legalPerson = legalPerson;
    }

    public String getLegalPerson() 
    {
        return legalPerson;
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

    public void setEstablishDate(Date establishDate) 
    {
        this.establishDate = establishDate;
    }

    public Date getEstablishDate() 
    {
        return establishDate;
    }

    public void setRegisteredCapital(BigDecimal registeredCapital) 
    {
        this.registeredCapital = registeredCapital;
    }

    public BigDecimal getRegisteredCapital() 
    {
        return registeredCapital;
    }

    public void setEmployeeCount(Integer employeeCount) 
    {
        this.employeeCount = employeeCount;
    }

    public Integer getEmployeeCount() 
    {
        return employeeCount;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public String getInvoiceTitle() 
    {
        return invoiceTitle;
    }

    public void setInvoiceTitle(String invoiceTitle) 
    {
        this.invoiceTitle = invoiceTitle;
    }

    public String getTaxNo() 
    {
        return taxNo;
    }

    public void setTaxNo(String taxNo) 
    {
        this.taxNo = taxNo;
    }

    public String getInvoiceAddress() 
    {
        return invoiceAddress;
    }

    public void setInvoiceAddress(String invoiceAddress) 
    {
        this.invoiceAddress = invoiceAddress;
    }

    public String getInvoicePhone() 
    {
        return invoicePhone;
    }

    public void setInvoicePhone(String invoicePhone) 
    {
        this.invoicePhone = invoicePhone;
    }

    public String getBankName() 
    {
        return bankName;
    }

    public void setBankName(String bankName) 
    {
        this.bankName = bankName;
    }

    public String getBankAccount() 
    {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) 
    {
        this.bankAccount = bankAccount;
    }

    public Integer getPaymentDays() 
    {
        return paymentDays;
    }

    public void setPaymentDays(Integer paymentDays) 
    {
        this.paymentDays = paymentDays;
    }

    public java.math.BigDecimal getCreditLimit() 
    {
        return creditLimit;
    }

    public void setCreditLimit(java.math.BigDecimal creditLimit) 
    {
        this.creditLimit = creditLimit;
    }

    public String getDefaultAddress() 
    {
        return defaultAddress;
    }

    public void setDefaultAddress(String defaultAddress) 
    {
        this.defaultAddress = defaultAddress;
    }
}
