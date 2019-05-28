package xyz.zaijushou.zhx.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import xyz.zaijushou.zhx.common.entity.CommonEntity;
import xyz.zaijushou.zhx.sys.entity.DataBatchEntity;
import xyz.zaijushou.zhx.sys.entity.DataCaseEntity;
import xyz.zaijushou.zhx.sys.entity.SysDictionaryEntity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class RedisUtils {

    private static Logger logger = LoggerFactory.getLogger(RedisUtils.class);

    private static StringRedisTemplate stringRedisTemplate;

    public static void deleteKeysWihtPrefix(String redisKeyPrefix) {
        Set<String> keys = listAllKeyWithKeyPrefix(redisKeyPrefix);
        stringRedisTemplate.delete(keys);
    }

    public static void refreshCommonEntityWithId(List list, String redisKeyPrefix) {
        try {
            RedisUtils.deleteKeysWihtPrefix(redisKeyPrefix);
            for (Object object : list) {
                CommonEntity commonEntity = (CommonEntity) object;
                stringRedisTemplate.opsForValue().set(redisKeyPrefix + commonEntity.getId(), JSONObject.toJSONString(object));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void refreshBatchEntity(List list, String redisKeyPrefix) {
        //RedisUtils.deleteKeysWihtPrefix(redisKeyPrefix);
        try {
            for (Object object : list) {
                DataBatchEntity dataBatchEntity = (DataBatchEntity) object;
                stringRedisTemplate.opsForValue().set(redisKeyPrefix + dataBatchEntity.getBatchNo(), JSONObject.toJSONString(object));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void refreshDicEntity(List list, String redisKeyPrefix) {
        try {
            RedisUtils.deleteKeysWihtPrefix(redisKeyPrefix);
            for (Object object : list) {
                SysDictionaryEntity bean = (SysDictionaryEntity) object;

                stringRedisTemplate.opsForValue().set(redisKeyPrefix + bean.getId(), JSONObject.toJSONString(object));
                stringRedisTemplate.opsForValue().set(redisKeyPrefix + bean.getName(), JSONObject.toJSONString(object));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void refreshCaseEntity(List list, String redisKeyPrefix) {
        //RedisUtils.deleteKeysWihtPrefix(redisKeyPrefix);
        try {
            for (Object object : list) {
                DataCaseEntity dataCaseEntity = (DataCaseEntity) object;
                //logger.info(redisKeyPrefix + dataCaseEntity.getCardNo()+"@"+dataCaseEntity.getCaseDate());
                //logger.info(JSONObject.toJSONString(object));
                stringRedisTemplate.opsForValue().set(redisKeyPrefix + dataCaseEntity.getSeqNo(), JSONObject.toJSONString(object));
                stringRedisTemplate.opsForValue().set(redisKeyPrefix + dataCaseEntity.getCardNo() + "@" + dataCaseEntity.getCaseDate(), JSONObject.toJSONString(object));
            }
        }catch (Exception e){
            e.printStackTrace();
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

    public static <T extends CommonEntity> List<T> scanEntityWithKeys(Set<String> idSet, Class<T> clazz) {
        List<T> list = new ArrayList<>();
        List<String> values = stringRedisTemplate.opsForValue().multiGet(idSet);
        if(CollectionUtils.isEmpty(values)) {
            return list;
        }
        for(String value : values) {
            list.add(JSON.parseObject(value, clazz));
        }
        return list;
    }

    public static <T> T entityGet(String key, Class<T> clazz) {
        String value = stringRedisTemplate.opsForValue().get(key);
        if(value == null) {
            return null;
        }
        try {
            return JSONObject.parseObject(value, clazz);
        } catch (Exception e) {
            logger.error("redis转entity出错：{}", e);
            return null;
        }
    }

    @Autowired
    public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
         RedisUtils.stringRedisTemplate = stringRedisTemplate;
    }

}
