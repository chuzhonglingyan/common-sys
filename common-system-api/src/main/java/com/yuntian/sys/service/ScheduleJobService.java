package com.yuntian.sys.service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yuntian.sys.model.dto.ScheduleJobDTO;
import com.yuntian.sys.model.entity.ScheduleJob;
import com.yuntian.architecture.data.IBaseService;
import java.util.Collection;

/**
 * <p>
 * 系统-定时任务 服务类
 * </p>
 *
 * @author yuntian
 * @since 2020-02-27
 */
public interface ScheduleJobService extends IBaseService<ScheduleJob> {

   IPage<ScheduleJob> queryListByPage(ScheduleJobDTO dto);


   void updateByDTO(ScheduleJob dto);


   void deleteByDTO(ScheduleJob dto);


   void deleteBatchByDTO(Collection<ScheduleJob> entityList);

}
