package com.yuntian.sys.quartz;

import com.yuntian.architecture.data.exception.BusinessException;
import com.yuntian.sys.enums.ScheduleStatus;
import com.yuntian.sys.model.entity.ScheduleJob;

import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.stereotype.Component;
import java.text.ParseException;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import static com.yuntian.sys.quartz.QuartJobTask.JOB_ID;


/**
 * @author Administrator
 * @Date: 2019/3/17 0017 15:32
 * @Description:
 */
@Component
@Slf4j
public class SchedulerUtil {

    @Resource(name = "customScheduler")
    private Scheduler scheduler;

    public void start(ScheduleJob scheduleJob) throws SchedulerException {
        JobDetail jobDetail = QuartJobTask.createTaskDetail(scheduleJob);
        Trigger trigger = QuartJobTask.createTrigger(scheduleJob, jobDetail);
        start(scheduleJob, jobDetail, trigger);
    }

    private void start(ScheduleJob scheduleJob, JobDetail jobDetail, Trigger trigger) throws SchedulerException {
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        log.info("启动定时任务:group=" + jobDetail.getKey().getGroup() + ",name=" + jobDetail.getKey().getName() + ",id:" + jobDataMap.get(JOB_ID));
        scheduler.scheduleJob(jobDetail, trigger);
        // 启动
        if (!scheduler.isStarted()) {
            scheduler.start();
        }
        if (scheduleJob.getStatus() != ScheduleStatus.NORMAL.getValue()) {
            pause(scheduleJob);
        }
    }


    public void resume(ScheduleJob scheduleJob) throws SchedulerException {
        log.info("恢复定时任务:group=" + scheduleJob.getGroupName() + ",id=" + scheduleJob.getId());
        JobKey jobKey = new JobKey(QuartJobTask.JOB_TAG + scheduleJob.getId(), scheduleJob.getGroupName());
        List<? extends Trigger> triggersOfJob = scheduler.getTriggersOfJob(jobKey);
        if (triggersOfJob.size() > 0) {
            for (Trigger t : triggersOfJob) {
                if (t instanceof SimpleTrigger || t instanceof CronTrigger) {
                    scheduler.resumeJob(jobKey);
                }
            }
        }
    }


    public void updateTrigger(ScheduleJob scheduleJob) throws SchedulerException, ParseException {
        TriggerKey triggerKey = new TriggerKey(QuartJobTask.JOB_TAG + scheduleJob.getId(), scheduleJob.getGroupName());
        CronTriggerImpl trigger = (CronTriggerImpl) scheduler.getTrigger(triggerKey);
        if (trigger == null) {
            BusinessException.throwMessage("cron表达式为空");
        }
        String oldExpression = trigger.getCronExpression();
        if (!oldExpression.equalsIgnoreCase(scheduleJob.getCronExpression())) {
            // 修改时间
            trigger.setCronExpression(scheduleJob.getCronExpression());
            // 方式一 ：修改一个任务的触发时间
            scheduler.rescheduleJob(triggerKey, trigger);
            log.info("更新定时任务的cron:group=" + scheduleJob.getGroupName() + ",id=" + scheduleJob.getId() + ",cronExpression:" + scheduleJob.getCronExpression());
            if (scheduleJob.getStatus() != ScheduleStatus.NORMAL.getValue()) {
                pause(scheduleJob);
            }
        }
    }


    public void removeJob(ScheduleJob scheduleJob) {
        JobKey jobKey = new JobKey(QuartJobTask.JOB_TAG + scheduleJob.getId(), scheduleJob.getGroupName());
        log.info("移除定时任务:group=" + scheduleJob.getGroupName() + ",id=" + scheduleJob.getId() + "成功");
        TriggerKey triggerKey = new TriggerKey(QuartJobTask.JOB_TAG + scheduleJob.getId(), scheduleJob.getGroupName());
        try {
            // 停止触发器
            scheduler.pauseTrigger(triggerKey);
            // 移除触发器
            scheduler.unscheduleJob(triggerKey);
            // 删除任务
            scheduler.deleteJob(jobKey);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            BusinessException.throwMessage("删除定时任务失败");
        }
    }


    public void pause(ScheduleJob scheduleJob) throws SchedulerException {
        log.info("暂停定时任务:group=" + scheduleJob.getGroupName() + ",id=" + scheduleJob.getId());
        JobKey jobKey = new JobKey(QuartJobTask.JOB_TAG + scheduleJob.getId(), scheduleJob.getGroupName());
        scheduler.pauseJob(jobKey);
    }


    public void shutdown() throws SchedulerException {
        log.info("结束所有定时任务");
        scheduler.shutdown();
    }
}
