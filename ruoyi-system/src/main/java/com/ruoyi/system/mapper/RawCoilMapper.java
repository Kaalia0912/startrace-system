package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.RawCoil;

/**
 * 原料卷料Mapper接口
 * 
 * @author yuanwenbo
 */
public interface RawCoilMapper
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
     * 按卷号查询（唯一性校验用）
     */
    public RawCoil checkCoilNoUnique(String coilNo);

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
