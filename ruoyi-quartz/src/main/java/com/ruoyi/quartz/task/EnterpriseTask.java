package com.ruoyi.quartz.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * 企业系统自定义定时任务
 * 
 * @author yuanwenbo
 */
@Component("enterpriseTask")
public class EnterpriseTask
{
    private static final Logger log = LoggerFactory.getLogger(EnterpriseTask.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 清理90天前的操作日志
     */
    public void cleanOperLog()
    {
        int deleted = jdbcTemplate.update(
                "DELETE FROM sys_oper_log WHERE oper_time < DATE_SUB(NOW(), INTERVAL 90 DAY)");
        log.info("定时任务[清理操作日志]执行完成，共清理 {} 条", deleted);
    }

    /**
     * 清理90天前的登录日志
     */
    public void cleanLogininfor()
    {
        int deleted = jdbcTemplate.update(
                "DELETE FROM sys_logininfor WHERE login_time < DATE_SUB(NOW(), INTERVAL 90 DAY)");
        log.info("定时任务[清理登录日志]执行完成，共清理 {} 条", deleted);
    }
}
