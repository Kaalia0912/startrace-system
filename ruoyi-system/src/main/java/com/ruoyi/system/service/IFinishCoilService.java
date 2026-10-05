package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.FinishCoil;

/**
 * 成品卷Service接口
 * 
 * @author yuanwenbo
 */
public interface IFinishCoilService
{
    /**
     * 查询成品卷列表
     */
    public List<FinishCoil> selectFinishCoilList(FinishCoil finishCoil);

    /**
     * 根据主键查询成品卷
     */
    public FinishCoil selectFinishCoilByCoilId(Long coilId);

    /**
     * 校验卷号是否唯一
     */
    public boolean checkCoilNoUnique(FinishCoil finishCoil);

    /**
     * 新增成品卷
     */
    public int insertFinishCoil(FinishCoil finishCoil);

    /**
     * 修改成品卷
     */
    public int updateFinishCoil(FinishCoil finishCoil);

    /**
     * 批量删除成品卷
     */
    public int deleteFinishCoilByCoilIds(Long[] coilIds);
}
