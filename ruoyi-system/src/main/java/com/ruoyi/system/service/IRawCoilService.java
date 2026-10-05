package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.RawCoil;

/**
 * 原料卷料Service接口
 * 
 * @author yuanwenbo
 */
public interface IRawCoilService
{
    /**
     * 查询原料卷料列表
     */
    public List<RawCoil> selectRawCoilList(RawCoil rawCoil);

    /**
     * 根据主键查询原料卷料
     */
    public RawCoil selectRawCoilByCoilId(Long coilId);

    /**
     * 校验卷号是否唯一
     */
    public boolean checkCoilNoUnique(RawCoil rawCoil);

    /**
     * 新增原料卷料
     */
    public int insertRawCoil(RawCoil rawCoil);

    /**
     * 修改原料卷料
     */
    public int updateRawCoil(RawCoil rawCoil);

    /**
     * 批量删除原料卷料
     */
    public int deleteRawCoilByCoilIds(Long[] coilIds);
}
