package com.yuntian.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yuntian.architecture.data.IBaseService;
import com.yuntian.sys.model.dto.ScheduleJobQueryDTO;
import com.yuntian.sys.model.dto.ScheduleJobSaveDTO;
import com.yuntian.sys.model.dto.ScheduleJobUpdateDTO;
import com.yuntian.sys.model.entity.ScheduleJob;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 系统-定时任务 服务类
 * </p>
 *
 * @author yuntian
 * @since 2020-02-27
 */
public interface ScheduleJobService extends IBaseService<ScheduleJob> {


    void saveByDTO(ScheduleJobSaveDTO dto);

    void updateByDTO(ScheduleJobUpdateDTO dto);

    void deleteByDTO(ScheduleJob dto);


    void deleteBatchByDTO(Long operatorId, List<Long> idList);

    IPage<ScheduleJob> queryListByPage(ScheduleJobQueryDTO dto);


    void exec(ScheduleJob dto);

    void resume(ScheduleJob dto);


    void pause(ScheduleJob dto);

    void changeState(ScheduleJob dto);

}
