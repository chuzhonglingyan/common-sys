package com.yuntian.sys.service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yuntian.sys.model.dto.ScheduleJobLogDTO;
import com.yuntian.sys.model.entity.ScheduleJobLog;
import com.yuntian.architecture.data.IBaseService;
import java.util.Collection;

/**
 * <p>
 * 系统-定时任务日志 服务类
 * </p>
 *
 * @author yuntian
 * @since 2020-02-27
 */
public interface ScheduleJobLogService extends IBaseService<ScheduleJobLog> {

   IPage<ScheduleJobLog> queryListByPage(ScheduleJobLogDTO dto);


   void updateByDTO(ScheduleJobLog dto);


   void deleteByDTO(ScheduleJobLog dto);


   void deleteBatchByDTO(Collection<ScheduleJobLog> entityList);

}
