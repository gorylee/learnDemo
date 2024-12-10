package example.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.beans.BeanMap;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @Author GoryLee
 * @Date 2024/12/10
 */
public class XBeanUtils extends BeanUtils {

    /**
     * 将javabean对象转换为map
     *
     * @param bean            Bean对象
     */
    public static <T> Map<String, Object> beanToMap(T bean){

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = objectMapper.convertValue(bean, Map.class);
        return map;
    }
}
