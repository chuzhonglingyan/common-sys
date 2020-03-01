package com.yuntian.sys.quartz;

import com.yuntian.architecture.redis.lock.RedissLockUtil;
import com.yuntian.architecture.util.ThrowableUtil;
import com.yuntian.sys.enums.ScheduleStatus;
import com.yuntian.sys.model.entity.ScheduleJob;
import com.yuntian.sys.model.entity.ScheduleJobLog;
import com.yuntian.sys.service.ScheduleJobLogService;
import com.yuntian.sys.service.ScheduleJobService;
import com.yuntian.sys.util.IPUtil;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

/**
 * @Auther: yuntian
 * @Date: 2020/2/29 0029 22:14
 * @Description:
 */
@Slf4j
public class QzTaskExecutor {

    private static final String LOCK_KEY_PRE = "qz:";

    private static final Integer MIN_OUT_TIME = 1;
    private static final Integer MAX_OUT_TIME = 60 * 30;
    private static final Integer DEFALUT_OUT_TIME = 60;

    private static Integer getLockTime(Integer outTime) {
        if (outTime < 0) {
            return DEFALUT_OUT_TIME;
        }
        if (outTime == 0) {
            return MIN_OUT_TIME;
        }
        if (outTime > MAX_OUT_TIME) {
            return MAX_OUT_TIME;
        }
        return outTime;
    }

    @Resource
    private ScheduleJobService scheduleJobService;

    @Resource
    private ScheduleJobLogService scheduleJobLogService;

    @Resource
    private SchedulerUtil schedulerUtil;

    @Resource
    private ThreadPoolTaskExecutor qzServiceExecutor;

    public void executeAuto(final Long jobId) {
        executeAuto(jobId, null);
    }

    public void executeAuto(final Long jobId, final Long executeId) {
        long startTime = System.currentTimeMillis();
        ScheduleJob scheduleJob = scheduleJobService.getById(jobId);
        if (scheduleJob == null) {
            return;
        }
        qzServiceExecutor.submit(() -> executeTask(false, scheduleJob, startTime, executeId));
    }

    public Integer executeManual(ScheduleJob scheduleJob, Long startTime, final Long executeId) {
        try {
            Future<Integer> future = qzServiceExecutor.submit(() -> executeTask(true, scheduleJob, startTime, executeId));
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getMessage(), e);
            saveLog(scheduleJob, true, IPUtil.getLocalIP(), executeId, 0L, 0L, 0, ThrowableUtil.getStackTrace(e));
        }
        return 0;
    }


    private Integer executeTask(boolean manual, ScheduleJob scheduleJob, Long startTime, final Long executeId) {
        String ip = IPUtil.getLocalIP();
        String lockKey = LOCK_KEY_PRE + ip;
        RedissLockUtil.lock(lockKey, getLockTime(scheduleJob.getTimeOut()));

        log.info("定时器:ip=" + ip + "," + "group=" + scheduleJob.getGroupName() + ",name=" + scheduleJob.getJobName() + ",id=" + scheduleJob.getId() + ",state=开始执行");
        int result = 0;
        String errMsg = "";
        long taskTimes = 0;
        long qzTimes;
        try {
            schedulerUtil.updateTrigger(scheduleJob);
            if (scheduleJob.getStatus() == ScheduleStatus.NORMAL.getValue() || manual) {
                ScheduleTaskBean callTask = new ScheduleTaskBean(scheduleJob.getBeanName(), scheduleJob.getMethodName(), scheduleJob.getParams());
                taskTimes = callTask.invoke();
                result = 1;
                log.info("定时器:ip=" + ip + "," + "group=" + scheduleJob.getGroupName() + ",name=" + scheduleJob.getJobName() + ",id=" + scheduleJob.getId() + ",state=执行成功");
            } else {
                log.info("定时器:ip=" + ip + "," + "group=" + scheduleJob.getGroupName() + ",name=" + scheduleJob.getJobName() + ",id=" + scheduleJob.getId() + ",state=暂停");
            }
        } catch (Exception e) {
            log.error("定时器:ip=" + ip + "," + "group=" + scheduleJob.getGroupName() + ",name=" + scheduleJob.getJobName() + ",id=" + scheduleJob.getId() + ",state=执行失败,errMsg=" + e.getMessage());
            errMsg = ThrowableUtil.getStackTrace(e);
        } finally {
            qzTimes = System.currentTimeMillis() - startTime;
            RedissLockUtil.unlock(lockKey);
            saveLog(scheduleJob, manual, ip, executeId, qzTimes, taskTimes, result, errMsg);
        }
        log.info("定时器:ip=" + ip + "," + "group=" + scheduleJob.getGroupName() + ",name=" + scheduleJob.getJobName() + ",id=" + scheduleJob.getId() + ",state=开始完毕,qzTimes=" + qzTimes + "ms");
        return result;
    }

    public void saveLog(ScheduleJob scheduleJob, boolean manual, String ip, Long executeId, Long qzTimes, Long taskTimes, Integer result, String errMsg) {
        ScheduleJobLog scheduleJobLog = new ScheduleJobLog();
        scheduleJobLog.setIp(ip);
        scheduleJobLog.setJobId(scheduleJob.getId());
        scheduleJobLog.setJobName(scheduleJob.getJobName());
        scheduleJobLog.setBeanName(scheduleJob.getBeanName());
        scheduleJobLog.setMethodName(scheduleJob.getMethodName());
        scheduleJobLog.setParams(scheduleJob.getParams());
        scheduleJobLog.setCronExpression(scheduleJob.getCronExpression());
        scheduleJobLog.setManual(manual ? 1 : 0);
        scheduleJobLog.setQzTimes(qzTimes);
        scheduleJobLog.setTaskTimes(taskTimes);
        scheduleJobLog.setError(errMsg);
        scheduleJobLog.setStatus(result);
        if (executeId == null) {
            scheduleJobLog.setCreateId(1L);
            scheduleJobLog.setUpdateId(1L);
        } else {
            scheduleJobLog.setCreateId(executeId);
            scheduleJobLog.setUpdateId(executeId);
        }
        scheduleJobLogService.save(scheduleJobLog);
    }

}
