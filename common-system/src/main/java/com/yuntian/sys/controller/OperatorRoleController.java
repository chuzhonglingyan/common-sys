package com.yuntian.sys.controller;

import com.yuntian.architecture.data.Result;
import com.yuntian.architecture.data.ResultGenerator;
import com.yuntian.sys.common.BaseBackendController;
import com.yuntian.sys.model.dto.OperatorRoleDTO;
import com.yuntian.sys.model.vo.RoleVO;
import com.yuntian.sys.service.OperatorRoleService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import javax.annotation.Resource;


/**
 * <p>
 * 后台系统-用户角色表 前端控制器
 * </p>
 *
 * @author yuntian
 * @since 2020-01-31
 */
@RestController
@RequestMapping("sys/operator/role")
public class OperatorRoleController extends BaseBackendController {

    @Resource
    private OperatorRoleService operatorRoleService;




    @PostMapping("/saveRoleListByOperatorId")
    public Result saveRoleListByoperatorId(@RequestBody OperatorRoleDTO dto) {
        dto.setCreateId(getUserId());
        dto.setUpdateId(getUserId());
        operatorRoleService.saveRoleListByOperatorId(dto);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/getRoleListByOperatorId")
    public List<RoleVO> getRoleListByoperatorId(@RequestParam Long operatorId) {
        return operatorRoleService.getRoleListByOperatorId(operatorId);
    }


}
