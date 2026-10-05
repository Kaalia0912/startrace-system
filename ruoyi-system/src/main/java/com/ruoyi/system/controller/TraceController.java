package com.ruoyi.system.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.service.ITraceService;

/** 卷号追溯Controller */
@RestController
@RequestMapping("/trace/coil")
public class TraceController extends BaseController
{
    @Autowired
    private ITraceService traceService;

    /**
     * 按卷号追溯：卷 → 订单 → 工序 → 检验 → 发货
     */
    @PreAuthorize("@ss.hasPermi('trace:coil:query')")
    @GetMapping(value = "/{coilNo}")
    public AjaxResult trace(@PathVariable("coilNo") String coilNo)
    {
        Map<String, Object> result = traceService.traceByCoilNo(coilNo);
        return success(result);
    }
}
