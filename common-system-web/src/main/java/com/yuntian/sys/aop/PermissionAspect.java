package com.yuntian.sys.aop;

import com.yuntian.architecture.util.IPUtil;
import com.yuntian.sys.annotation.Permission;
import com.yuntian.architecture.data.ResultCode;
import com.yuntian.architecture.data.exception.BusinessException;
import com.yuntian.architecture.redis.config.RedisManage;
import com.yuntian.cache.SysRedisKey;
import com.yuntian.constant.SysConstants;
import com.yuntian.sys.model.entity.Menu;
import com.yuntian.sys.model.entity.Operator;
import com.yuntian.sys.service.MenuService;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.yuntian.sys.interceptor.SysLoginInterceptor.ACCESS_TOKEN;

/**
 * @Auther: yuntian
 * @Date: 2020/2/5 0005 21:54
 * @Description:
 */
@Aspect
@Component
public class PermissionAspect {

    @Resource
    private RedisManage redisManage;

    @Resource
    private MenuService menuService;


    @Pointcut("@annotation(com.yuntian.sys.annotation.Permission)")
    public void verifyPointCut() {

    }

    @Before("verifyPointCut()&&@annotation(permission)")
    public void doBefore(JoinPoint joinPoint, Permission permission) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder
                .getRequestAttributes())).getRequest();
        String permissionCode = permission.value();
        // 判断Header是否存在令牌信息，如果存在，则允许登录
        String accessToken = request.getHeader(ACCESS_TOKEN);
        String useInfoKey = SysRedisKey.getOperatorInfoKey(accessToken, IPUtil.getClientIpAddress(request));
        Operator operator = redisManage.getValue(useInfoKey);
        if (StringUtils.equals(SysConstants.AMDIN_USER, operator.getUserName())) {
            return;
        }
        List<Menu> menuList = menuService.getEnableMenuListByOperatorId(operator.getId());
        List<String> userPermissions = menuList.stream().map(Menu::getPermission).collect(Collectors.toList());
        if (!userPermissions.contains(permissionCode)) {
            BusinessException.throwMessage(ResultCode.UN_AUTHORITY.code(), ResultCode.UN_AUTHORITY.msg());
        }
    }


}
