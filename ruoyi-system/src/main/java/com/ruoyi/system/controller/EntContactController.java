package com.ruoyi.system.controller;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.EntContact;
import com.ruoyi.system.service.IEntContactService;

/**
 * 企业联系人Controller
 * 
 * @author yuanwenbo
 */
@RestController
@RequestMapping("/enterprise/contact")
public class EntContactController extends BaseController
{
    @Autowired
    private IEntContactService entContactService;

    /**
     * 查询企业联系人列表
     */
    @PreAuthorize("@ss.hasPermi('enterprise:contact:list')")
    @GetMapping("/list")
    public TableDataInfo list(EntContact entContact)
    {
        startPage();
        List<EntContact> list = entContactService.selectEntContactList(entContact);
        return getDataTable(list);
    }

    /**
     * 查询某企业的联系人列表（不分页，详情页用）
     */
    @PreAuthorize("@ss.hasPermi('enterprise:contact:list')")
    @GetMapping("/listByEnt/{entId}")
    public AjaxResult listByEnt(@PathVariable("entId") Long entId)
    {
        return success(entContactService.selectEntContactByEntId(entId));
    }

    /**
     * 导出企业联系人列表
     */
    @PreAuthorize("@ss.hasPermi('enterprise:contact:export')")
    @Log(title = "企业联系人", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, EntContact entContact)
    {
        List<EntContact> list = entContactService.selectEntContactList(entContact);
        ExcelUtil<EntContact> util = new ExcelUtil<EntContact>(EntContact.class);
        util.exportExcel(response, list, "企业联系人数据");
    }

    /**
     * 获取企业联系人详细信息
     */
    @PreAuthorize("@ss.hasPermi('enterprise:contact:query')")
    @GetMapping(value = "/{contactId}")
    public AjaxResult getInfo(@PathVariable("contactId") Long contactId)
    {
        return success(entContactService.selectEntContactByContactId(contactId));
    }

    /**
     * 新增企业联系人
     */
    @PreAuthorize("@ss.hasPermi('enterprise:contact:add')")
    @Log(title = "企业联系人", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody EntContact entContact)
    {
        entContact.setCreateBy(getUsername());
        return toAjax(entContactService.insertEntContact(entContact));
    }

    /**
     * 修改企业联系人
     */
    @PreAuthorize("@ss.hasPermi('enterprise:contact:edit')")
    @Log(title = "企业联系人", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody EntContact entContact)
    {
        entContact.setUpdateBy(getUsername());
        return toAjax(entContactService.updateEntContact(entContact));
    }

    /**
     * 删除企业联系人
     */
    @PreAuthorize("@ss.hasPermi('enterprise:contact:remove')")
    @Log(title = "企业联系人", businessType = BusinessType.DELETE)
    @DeleteMapping("/{contactIds}")
    public AjaxResult remove(@PathVariable Long[] contactIds)
    {
        return toAjax(entContactService.deleteEntContactByContactIds(contactIds));
    }
}
