package com.yuntian.sys.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wf.captcha.ArithmeticCaptcha;
import com.yuntian.architecture.data.Result;
import com.yuntian.architecture.data.ResultGenerator;
import com.yuntian.architecture.util.UUIDUitl;
import com.yuntian.sys.common.BaseBackendController;
import com.yuntian.sys.common.constant.RedisKey;
import com.yuntian.sys.model.dto.LoginDTO;
import com.yuntian.sys.model.dto.OperatorQueryDTO;
import com.yuntian.sys.model.dto.OperatorSaveDTO;
import com.yuntian.sys.model.dto.OperatorUpdateDTO;
import com.yuntian.sys.model.dto.RegisterDTO;
import com.yuntian.sys.model.entity.Operator;
import com.yuntian.sys.model.vo.CodeImageVO;
import com.yuntian.sys.model.vo.MenuComponentVo;
import com.yuntian.sys.model.vo.OperatorVO;
import com.yuntian.sys.service.OperatorService;
import com.yuntian.sys.util.IPUtil;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public Result save(@RequestBody @Validated OperatorSaveDTO dto) {
        dto.setCreateId(getUserId());
        dto.setUpdateId(getUserId());
        operatorService.saveByDTO(dto);
        return ResultGenerator.genSuccessResult();
    }


    @PostMapping("/update")
    public Result update(@RequestBody @Validated OperatorUpdateDTO dto) {
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



    @PostMapping("/deleteList")
    public Result deleteList(@RequestBody List<Long> idList) {
        operatorService.deleteBatchByDTO(getUserId(),idList);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail() {
        OperatorVO entity = operatorService.getInfo(getUserId());
        return ResultGenerator.genSuccessResult(entity);
    }


    @PostMapping("/list")
    public  Result list(@RequestBody OperatorQueryDTO dto) {
        return ResultGenerator.genSuccessResult(operatorService.queryListByPage(dto));
    }



    /**
     * 登录，使用POST，传输数据
     *
     * @param dto
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result login(@RequestBody @Validated LoginDTO dto) {
        dto.setClientIp(IPUtil.getClientIpAddress(request));
        Operator operator = operatorService.login(dto);
        return ResultGenerator.genSuccessResult(operator);
    }

    /**
     * 注册
     *
     * @param dto
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Result register(@RequestBody @Validated RegisterDTO dto) {
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
        String userInfoKey = RedisKey.getOperatorInfoKey(getToken(), IPUtil.getClientIpAddress(request));
        String tokenKey = RedisKey.getBackendTokenkey(String.valueOf(getUserId()));
        redisManage.del(userInfoKey);
        redisManage.del(tokenKey);
        return ResultGenerator.genSuccessResult();
    }


    /**
     * 获取验证码
     *
     * @return
     */
    @GetMapping(value = "/code")
    public Result getCode() {
        // 算术类型 https://gitee.com/whvse/EasyCaptcha
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(111, 36);
        // 几位数运算，默认是两位
        captcha.setLen(2);
        // 获取运算的结果
        String result = captcha.text();
        String uuid = UUIDUitl.getUUIDName();
        // 保存
        redisManage.set(uuid, result, 60);
        // 验证码信息
        CodeImageVO codeImage = new CodeImageVO();
        codeImage.setImg(captcha.toBase64());
        codeImage.setUuid(uuid);
        return ResultGenerator.genSuccessResult(codeImage);
    }
}
