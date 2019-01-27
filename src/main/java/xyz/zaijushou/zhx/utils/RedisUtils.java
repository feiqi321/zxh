package xyz.zaijushou.zhx.utils;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import xyz.zaijushou.zhx.common.entity.CommonEntity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class RedisUtils {

    private static StringRedisTemplate stringRedisTemplate;

    public static void deleteKeysWihtPrefix(String redisKeyPrefix) {
        Set<String> keys = listAllKeyWithKeyPrefix(redisKeyPrefix);
        stringRedisTemplate.delete(keys);
    }

    public static void refreshCommonEntityWithId(List list, String redisKeyPrefix) {
        RedisUtils.deleteKeysWihtPrefix(redisKeyPrefix);
        for (Object object : list) {
            CommonEntity commonEntity = (CommonEntity) object;
            stringRedisTemplate.opsForValue().set(redisKeyPrefix + commonEntity.getId(), JSONObject.toJSONString(object));
        }
    }

    public static <T> List<T> scanEntityWithKeyPrefix(String redisKeyPrefix, Class<T> clazz) {
        List<T> list = new ArrayList<>();
        List<String> values = scanValuesWithKeyPrefix(redisKeyPrefix);
        if(CollectionUtils.isEmpty(values)) {
            return list;
        }
        for(String value : values) {
            T t = JSONObject.parseObject(value, clazz);
            list.add(t);
        }
        return list;
    }

    public static List<String> scanValuesWithKeyPrefix (String redisKeyPrefix) {
        Set<String> keys = listAllKeyWithKeyPrefix(redisKeyPrefix);
        return stringRedisTemplate.opsForValue().multiGet(keys);
    }

    public static Set<String> listAllKeyWithKeyPrefix(String redisKeyPrefix) {
        Set<String> keys = new HashSet<>();
        ScanOptions options = ScanOptions.scanOptions().match(redisKeyPrefix + "*").count(Integer.MAX_VALUE).build();
        Cursor cursor = stringRedisTemplate.getConnectionFactory().getConnection().scan(options);
        while (cursor.hasNext()) {
            keys.add(new String((byte[]) cursor.next()));
        }
        return keys;
    }

    @Autowired
    public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
         RedisUtils.stringRedisTemplate = stringRedisTemplate;
    }

}
