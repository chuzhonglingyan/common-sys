package com.yuntian.sys.service;

import com.yuntian.architecture.data.IBaseService;
import com.yuntian.sys.model.dto.LoginDTO;
import com.yuntian.sys.model.dto.OperatorQueryDTO;
import com.yuntian.sys.model.dto.OperatorSaveDTO;
import com.yuntian.sys.model.dto.OperatorUpdateDTO;
import com.yuntian.sys.model.dto.RegisterDTO;
import com.yuntian.sys.model.entity.Operator;
import com.yuntian.sys.model.vo.OperatorVO;
import com.yuntian.sys.model.vo.PageVO;

import java.util.Collection;

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

    void isEnable(Operator dto);

    void isDisEnable(Operator dto);

    PageVO<OperatorVO> queryListByPage(OperatorQueryDTO dto);


    void deleteBatchByDTO(Collection<Operator> entityList);

    OperatorVO login(LoginDTO dto);

    Operator register(RegisterDTO dto);

    Operator getUserByAccount(String account) ;

    OperatorVO getInfo(Long userId);

    OperatorVO getInfo(String account);

}
