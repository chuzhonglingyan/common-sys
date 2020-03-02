package com.yuntian.sys.model.vo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yuntian.util.BeanCopyUtil;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import lombok.Data;


/**
 * @Auther: yuntian
 * @Date: 2020/2/23 0023 11:02
 * @Description:
 */
@Data
public class PageVO<V> {

    private List<V> records;

    private Long total;
    private Long size;
    private Long current;

    private Boolean isSearchCount;

    public PageVO() {

    }

    public <T> PageVO(IPage<T> page) {
        //获得运行期的泛型类型
        Type superclass = getClass().getGenericSuperclass();
        ParameterizedType parameterized = (ParameterizedType)superclass;
        Class<V> clazz= (Class<V>) parameterized.getActualTypeArguments()[0];
        records = convertData(clazz, page.getRecords());
        total = page.getTotal();
        size = page.getSize();
        current = page.getCurrent();
        isSearchCount = page.isSearchCount();
    }


    private <T> List<V> convertData(Class<V> clazz, List<T> orgList) {
        return BeanCopyUtil.copyListProperties(orgList, clazz);
    }

}
