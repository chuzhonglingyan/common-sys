package com.yuntian.quartz;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Objects;

import javax.annotation.Nullable;
import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Administrator
 * @Auther: yuntian
 * @Date: 2019/3/17 0017 14:28
 * @Description:
 */
@Slf4j
public class TaskQuartzJobBean extends QuartzJobBean {


    @Resource
    private QzTaskExecutor qzTaskExecutor;

    @Override
    protected void executeInternal(@Nullable JobExecutionContext jobExecutionContext) {
        JobDetail jobDetail = Objects.requireNonNull(jobExecutionContext).getJobDetail();
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        qzTaskExecutor.executeAuto(jobDataMap.getLong(QuartJobTask.JOB_ID));
    }

}
