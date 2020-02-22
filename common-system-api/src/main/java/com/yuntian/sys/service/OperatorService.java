package com.yuntian.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yuntian.architecture.data.IBaseService;
import java.util.Collection;
import java.util.List;

import com.yuntian.sys.model.dto.LoginDTO;
import com.yuntian.sys.model.dto.OperatorQueryDTO;
import com.yuntian.sys.model.dto.OperatorSaveDTO;
import com.yuntian.sys.model.dto.OperatorUpdateDTO;
import com.yuntian.sys.model.dto.RegisterDTO;
import com.yuntian.sys.model.entity.Menu;
import com.yuntian.sys.model.entity.Operator;
import com.yuntian.sys.model.vo.MenuComponentVo;
import com.yuntian.sys.model.vo.MenuTreeVO;
import com.yuntian.sys.model.vo.OperatorVO;

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

    IPage<Operator> queryListByPage(OperatorQueryDTO dto);

    List<MenuTreeVO> getMenuTreeVoListByOperator(Long operatorId);


    List<MenuComponentVo> getMenuComponentTreeVoListByOperator(Long operatorId);

    void deleteBatchByDTO(Collection<Operator> entityList);

    OperatorVO login(LoginDTO dto);

    Operator register(RegisterDTO dto);

    Operator getUserByAccount(String account) ;

    OperatorVO getInfo(Long userId);

    OperatorVO getInfo(String account);

    List<Menu> getEnableMenuListByOperatorId(Long operatorId);
}
