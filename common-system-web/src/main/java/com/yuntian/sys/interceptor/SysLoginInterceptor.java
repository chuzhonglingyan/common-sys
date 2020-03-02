package com.yuntian.sys.interceptor;


import com.yuntian.architecture.data.ResultCode;
import com.yuntian.architecture.data.exception.BusinessException;
import com.yuntian.architecture.redis.config.RedisManage;
import com.yuntian.architecture.util.IPUtil;
import com.yuntian.cache.SysRedisKey;
import com.yuntian.sys.model.entity.Operator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Administrator
 * @Auther: yuntian
 * @Date: 2018/8/19 21:02
 * @Description: 后台登录拦截器
 */
public class SysLoginInterceptor implements HandlerInterceptor {


    public final static String ACCESS_TOKEN = "X-Token";
    private final static int NOVALID_TOKEN_CODE = ResultCode.UN_LOGIN.code();

    @Resource
    private RedisManage redisManage;

    /**
     * 在请求处理之前进行调用（Controller方法调用之前）
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        // 判断Header是否存在令牌信息，如果存在，则允许登录
        String accessToken = request.getHeader(ACCESS_TOKEN);
        String useInfoKey = SysRedisKey.getOperatorInfoKey(accessToken, IPUtil.getClientIpAddress(request));
        if (StringUtils.isBlank(accessToken)) {
            throw new BusinessException(NOVALID_TOKEN_CODE, "token不存在，请重新登录");
        }
        //此处应该用缓存取
        Operator operator = redisManage.getValue(useInfoKey);
        if (operator == null || operator.getId() <= 0) {
            throw new BusinessException(NOVALID_TOKEN_CODE, "token已经失效，请重新登录");
        }
        String orignToken = redisManage.getValue(SysRedisKey.getBackendTokenkey(String.valueOf(operator.getId())));
        if (!StringUtils.equals(accessToken, orignToken)) {
            throw new BusinessException(NOVALID_TOKEN_CODE, "token已经失效，请重新登录");
        }
        return true;
    }

    /**
     * 请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    /**
     * 在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作）
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @param e
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
