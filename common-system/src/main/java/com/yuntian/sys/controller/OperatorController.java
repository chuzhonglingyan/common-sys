package com.yuntian.sys.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yuntian.architecture.data.Result;
import com.yuntian.architecture.data.ResultGenerator;
import com.yuntian.sys.annotation.Valid;
import com.yuntian.sys.common.BaseBackendController;
import com.yuntian.sys.common.constant.RedisKey;
import com.yuntian.sys.model.dto.LoginDTO;
import com.yuntian.sys.model.dto.OperatorQueryDTO;
import com.yuntian.sys.model.dto.OperatorSaveDTO;
import com.yuntian.sys.model.dto.OperatorUpdateDTO;
import com.yuntian.sys.model.dto.RegisterDTO;
import com.yuntian.sys.model.entity.Operator;
import com.yuntian.sys.model.vo.OperatorVO;
import com.yuntian.sys.service.OperatorService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 后台系统-用户表 前端控制器
 * </p>
 *
 * @author yuntian
 * @since 2020-01-31
 */
@RestController
@RequestMapping("/sys/operator")
public class OperatorController extends BaseBackendController {


    @Resource
    private OperatorService operatorService;


    @PostMapping("/save")
    public Result save(OperatorSaveDTO dto) {
        dto.setCreateId(getUserId());
        dto.setUpdateId(getUserId());
        operatorService.saveByDTO(dto);
        return ResultGenerator.genSuccessResult();
    }


    @PostMapping("/update")
    public Result update(OperatorUpdateDTO dto) {
        dto.setCreateId(getUserId());
        dto.setUpdateId(getUserId());
        operatorService.updateByDTO(dto);
        return ResultGenerator.genSuccessResult();
    }


    @PostMapping("/delete")
    public Result delete(Operator dto) {
        dto.setUpdateId(getUserId());
        operatorService.deleteByDTO(dto);
        return ResultGenerator.genSuccessResult();
    }


    @PostMapping("/detail")
    public Result detail() {
        OperatorVO entity = operatorService.getInfo(getUserId());
        return ResultGenerator.genSuccessResult(entity);
    }


    @PostMapping("/list")
    public IPage<Operator> list(OperatorQueryDTO dto) {
        return operatorService.queryListByPage(dto);
    }


    /**
     * 登录，使用POST，传输数据
     *
     * @param dto
     * @return
     */
    @Valid
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result login(@RequestBody @Valid LoginDTO dto) {
        Operator operator = operatorService.login(dto);
        return ResultGenerator.genSuccessResult(operator);
    }

    /**
     * 注册
     *
     * @param dto
     * @return
     */
    @Valid
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Result register(@RequestBody @Valid  RegisterDTO dto) {
        return ResultGenerator.genSuccessResult(operatorService.register(dto));
    }


    /**
     * 退出登录
     *
     * @return
     */
    @RequestMapping(value = "/loginOut", method = RequestMethod.POST)
    public Result loginOut() {
        //清除token
        String token = getToken();
        String useIdKey = RedisKey.getBackendTokenkey(String.valueOf(getUserId()));
        redisManage.del(token);
        redisManage.del(useIdKey);
        return ResultGenerator.genSuccessResult();
    }

}
