package com.yuntian.sys.service;

import com.yuntian.architecture.data.IBaseService;
import com.yuntian.sys.model.dto.DownQueryDTO;
import com.yuntian.sys.model.dto.LoginDTO;
import com.yuntian.sys.model.dto.OperatorQueryDTO;
import com.yuntian.sys.model.dto.OperatorSaveDTO;
import com.yuntian.sys.model.dto.OperatorUpdateDTO;
import com.yuntian.sys.model.dto.RegisterDTO;
import com.yuntian.sys.model.entity.Operator;
import com.yuntian.sys.model.vo.OperatorVO;
import com.yuntian.sys.model.vo.PageVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 后台系统-用户表 服务类
 * </p>
 *
 * @author yuntian
 * @since 2020-01-31
 */
public interface OperatorService extends IBaseService<Operator> {


    void saveByDTO(OperatorSaveDTO dto);

    void updateByDTO(OperatorUpdateDTO dto);

    void deleteByDTO(Operator dto);

    void enable(Operator dto);


    void disEnable(Operator dto);

    void changeState(Operator dto);

    PageVO<OperatorVO> queryListByPage(OperatorQueryDTO dto);

    void deleteBatchByDTO(Long operatorId, List<Long> idList);

    OperatorVO login(LoginDTO dto);

    Operator register(RegisterDTO dto);

    Operator getUserByName(String userName);

    OperatorVO getInfo(Long userId);

    OperatorVO getInfo(String userName);

    List<Map<String, Object>> getDownLoadData(DownQueryDTO dto);

}
