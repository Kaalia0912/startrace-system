package com.ruoyi.system.controller;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.mapper.EntEnterpriseMapper;

/**
 * 企业统计看板Controller
 * 
 * @author yuanwenbo
 */
@RestController
@RequestMapping("/enterprise/dashboard")
public class EntDashboardController extends BaseController
{
    @Autowired
    private EntEnterpriseMapper entEnterpriseMapper;

    /**
     * 获取统计看板数据
     */
    @PreAuthorize("@ss.hasPermi('enterprise:dashboard:list')")
    @GetMapping("/stats")
    public AjaxResult stats()
    {
        List<Map<String, Object>> entType = entEnterpriseMapper.selectCountByEntType();
        List<Map<String, Object>> industry = entEnterpriseMapper.selectCountByIndustry();
        List<Map<String, Object>> scale = entEnterpriseMapper.selectCountByScale();
        List<Map<String, Object>> status = entEnterpriseMapper.selectCountByStatus();
        Map<String, Object> summary = entEnterpriseMapper.selectSummary();
        AjaxResult ajax = AjaxResult.success();
        ajax.put("entType", entType);
        ajax.put("industry", industry);
        ajax.put("scale", scale);
        ajax.put("status", status);
        ajax.put("summary", summary);
        return ajax;
    }
}
