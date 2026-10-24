package com.ruoyi.system.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.Material;
import com.ruoyi.system.service.IMaterialService;

/**
 * 物料主数据Controller
 * 
 * @author yuanwenbo
 */
@RestController
@RequestMapping("/basedata/material")
public class MaterialController extends BaseController
{
    @Autowired
    private IMaterialService materialService;

    /**
     * 查询物料主数据列表
     */
    @PreAuthorize("@ss.hasPermi('basedata:material:list')")
    @GetMapping("/list")
    public TableDataInfo list(Material material)
    {
        startPage();
        List<Material> list = materialService.selectMaterialList(material);
        return getDataTable(list);
    }

    /**
     * 获取物料主数据详细信息
     */
    @PreAuthorize("@ss.hasPermi('basedata:material:query')")
    @GetMapping(value = "/{materialId}")
    public AjaxResult getInfo(@PathVariable("materialId") Long materialId)
    {
        return success(materialService.selectMaterialByMaterialId(materialId));
    }

    /**
     * 导出物料主数据列表
     */
    @PreAuthorize("@ss.hasPermi('basedata:material:export')")
    @Log(title = "物料主数据", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Material material)
    {
        List<Material> list = materialService.selectMaterialList(material);
        ExcelUtil<Material> util = new ExcelUtil<Material>(Material.class);
        util.exportExcel(response, list, "物料主数据");
    }

    /**
     * 导入物料主数据
     */
    @PreAuthorize("@ss.hasPermi('basedata:material:import')")
    @Log(title = "物料主数据", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception
    {
        validateImportFile(file);
        ExcelUtil<Material> util = new ExcelUtil<Material>(Material.class);
        List<Material> list = util.importExcel(file.getInputStream());
        return success(materialService.importMaterial(list, updateSupport));
    }

    /**
     * 下载物料主数据导入模板
     */
    @PreAuthorize("@ss.hasPermi('basedata:material:import')")
    @GetMapping("/importTemplate")
    public AjaxResult importTemplate()
    {
        ExcelUtil<Material> util = new ExcelUtil<Material>(Material.class);
        List<Material> demoList = new ArrayList<Material>();
        Material demo = new Material();
        demo.setMaterialCode("WL-DEMO-001");
        demo.setProductName("304不锈钢冷轧卷");
        demo.setMaterial("SUS304");
        demo.setSpec("0.8×1219×C");
        demo.setThickness(new BigDecimal("0.8"));
        demo.setWidth(new BigDecimal("1219"));
        demo.setCoilInnerDia(new BigDecimal("508"));
        demo.setUnit("kg");
        demo.setStandard("JIS G4305");
        demo.setHardness("180~220");
        demo.setTensileStrength("520~620");
        demo.setSurfaceRa("Ra0.3-0.4");
        demo.setCoilWeightMin(new BigDecimal("800"));
        demo.setCoilWeightMax(new BigDecimal("1000"));
        demo.setPackageCode("B01");
        demo.setRawPrice(new BigDecimal("13.50"));
        demo.setStatus("0");
        demo.setRemark("示例数据行，正式导入前请删除本行");
        demoList.add(demo);
        AjaxResult result = util.exportExcel(demoList, "物料主数据");
        return AjaxResult.success("操作成功", Constants.RESOURCE_PREFIX + "/download/" + result.get(AjaxResult.MSG_TAG));
    }

    /**
     * 新增物料主数据
     */
    @PreAuthorize("@ss.hasPermi('basedata:material:add')")
    @Log(title = "物料主数据", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Material material)
    {
        material.setCreateBy(getUsername());
        material.setCreateTime(new Date());
        return toAjax(materialService.insertMaterial(material));
    }

    /**
     * 修改物料主数据
     */
    @PreAuthorize("@ss.hasPermi('basedata:material:edit')")
    @Log(title = "物料主数据", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Material material)
    {
        return toAjax(materialService.updateMaterial(material));
    }

    /**
     * 删除物料主数据
     */
    @PreAuthorize("@ss.hasPermi('basedata:material:remove')")
    @Log(title = "物料主数据", businessType = BusinessType.DELETE)
    @DeleteMapping("/{materialIds}")
    public AjaxResult remove(@PathVariable Long[] materialIds)
    {
        return toAjax(materialService.deleteMaterialByMaterialIds(materialIds));
    }

    private void validateImportFile(MultipartFile file)
    {
        if (file == null || file.isEmpty())
        {
            throw new ServiceException("请选择要导入的Excel文件");
        }
        String fileName = StringUtils.lowerCase(file.getOriginalFilename());
        if (fileName == null || (!fileName.endsWith(".xls") && !fileName.endsWith(".xlsx")))
        {
            throw new ServiceException("仅支持导入 .xls 或 .xlsx 文件");
        }
        if (file.getSize() > 10 * 1024 * 1024)
        {
            throw new ServiceException("导入文件不能超过 10MB");
        }
    }
}
