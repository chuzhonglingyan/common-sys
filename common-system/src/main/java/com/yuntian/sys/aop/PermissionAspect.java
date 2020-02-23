package com.yuntian.sys.aop;

import com.yuntian.architecture.data.ResultCode;
import com.yuntian.architecture.data.exception.BusinessException;
import com.yuntian.architecture.redis.config.RedisManage;
import com.yuntian.sys.annotation.Permission;
import com.yuntian.sys.model.entity.Menu;
import com.yuntian.sys.model.entity.Operator;
import com.yuntian.sys.service.MenuService;
import com.yuntian.sys.service.OperatorService;

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

import static com.yuntian.sys.interceptor.BackendLoginInterceptor.ACCESS_TOKEN;

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
        String token = request.getHeader(ACCESS_TOKEN);
        Operator operator = redisManage.getValue(token);
        List<Menu> menuList = menuService.getEnableMenuListByOperatorId(operator.getId());
        List<String> userPermissions = menuList.stream().map(Menu::getPermissionCode).collect(Collectors.toList());
        if (!userPermissions.contains(permissionCode)) {
            BusinessException.throwMessage(ResultCode.UNAUTHORIZED.code(), "无权限访问");
        }
    }


}
