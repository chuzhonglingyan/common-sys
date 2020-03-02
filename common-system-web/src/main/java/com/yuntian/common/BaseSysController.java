package com.yuntian.common;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.yuntian.architecture.redis.config.RedisManage;
import com.yuntian.architecture.util.IPUtil;
import com.yuntian.cache.SysRedisKey;
import com.yuntian.sys.model.entity.Operator;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import static com.yuntian.sys.interceptor.SysLoginInterceptor.ACCESS_TOKEN;


/**
 * @Auther: yuntian
 * @Date: 2019/6/18 0018 20:57
 * @Description:
 */
public abstract class BaseSysController {


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
            String useInfoKey = SysRedisKey.getOperatorInfoKey(token, IPUtil.getClientIpAddress(request));
            return redisManage.getValue(useInfoKey);
        }
        return null;
    }


}
