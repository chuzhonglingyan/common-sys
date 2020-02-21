package com.yuntian.sys.common;

import com.yuntian.architecture.redis.config.RedisManage;
import com.yuntian.sys.model.entity.Operator;

import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.yuntian.sys.interceptor.BackendLoginInterceptor.ACCESS_TOKEN;


/**
 * @Auther: yuntian
 * @Date: 2019/6/18 0018 20:57
 * @Description:
 */
public abstract class BaseBackendController {


    @Resource
    protected HttpServletRequest request;

    protected Long getUserId() {
        if (getUser() != null) {
            return getUser().getId();
        }
        return 0L;
    }

    @Resource
    protected RedisManage redisManage;



    protected String getToken() {
        return request.getHeader(ACCESS_TOKEN);
    }


    public Operator getUser() {
        String token = request.getHeader(ACCESS_TOKEN);
        if (StringUtils.isNotBlank(token)) {
            return redisManage.getValue(token);
        }
        return null;
    }



}
