package com.nft.cn.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RedisUtils {


    @Autowired
    private RedisTemplate redisTemplate;

    public boolean setNx(String key, String value,Integer expireTime) {
        return redisTemplate.opsForValue().setIfAbsent(key, value, expireTime, TimeUnit.SECONDS);
    }

    public <T> T getCacheObject(final String key)
    {
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        return operation.get(key);
    }

    public <T> void setCacheObject(final String key, final T value, final Integer timeout, final TimeUnit timeUnit)
    {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    public void remove(final String... keys) {
            for (String key : keys) {
                remove(key);
            }
    }

    public void removePattern(final String pattern) {

        try {
            Set<Object> keys = redisTemplate.keys(pattern);
            if (keys.size() > 0)
                redisTemplate.delete(keys);
        }catch (Exception e){
              log.error("redisee---{}---{}---{}",e.getMessage(),e.getCause(),e.getStackTrace());
        }finally {
            if(redisTemplate.getConnectionFactory()!=null){
                RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
            }
        }
    }

    public boolean hasKey(String key){
        try {
            return redisTemplate.hasKey(key);
        }catch (Exception e){
            log.error("redisee---{}---{}---{}",e.getMessage(),e.getCause(),e.getStackTrace());
        }finally {
            if(redisTemplate.getConnectionFactory()!=null){
                RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
            }
        }
        return false;

    }

    public void remove(final String key) {

        try {
            if (exists(key)) {
                redisTemplate.delete(key);
            }
        }catch (Exception e){
            log.error("redisee---{}---{}---{}",e.getMessage(),e.getCause(),e.getStackTrace());
        }finally {
            if(redisTemplate.getConnectionFactory()!=null){
                RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
            }
        }
    }

    public boolean listPush(String key, Object value) {
        try {

            if(key == null || value == null){
                return false;
            }
            long i=redisTemplate.opsForList().rightPush(key,value);
            return i!=0;
        }catch (Exception e){
            log.error("redisee---{}---{}---{}",e.getMessage(),e.getCause(),e.getStackTrace());
        }finally {
            if(redisTemplate.getConnectionFactory()!=null){
                RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
            }
        }
        return false;

    }

    public long listSize(String key){

        try {
            return redisTemplate.opsForList().size(key);
        }catch (Exception e){
            log.error("redisee---{}---{}---{}",e.getMessage(),e.getCause(),e.getStackTrace());
        }finally {
            if(redisTemplate.getConnectionFactory()!=null){
                RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
            }
        }

        return 0;
    }


    public List<Object> getList(String key){

        try {
            return redisTemplate.opsForList().range(key,0,redisTemplate.opsForList().size(key));
        }catch (Exception e){
            log.error("redisee---{}---{}---{}",e.getMessage(),e.getCause(),e.getStackTrace());
        }finally {
            if(redisTemplate.getConnectionFactory()!=null){
                RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
            }
        }
        return null;
    }



    public Object listPop(String key) {

        try {
            return redisTemplate.opsForList().leftPop(key);
        }catch (Exception e){
            log.error("redisee---{}---{}---{}",e.getMessage(),e.getCause(),e.getStackTrace());
        }finally {
            if(redisTemplate.getConnectionFactory()!=null){
                RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
            }
        }
        return null;

    }

    public Map<String, Object> getCacheMap(String cacheKey) {

        try {
            BoundHashOperations<Object, String, Object> bound = redisTemplate.boundHashOps(cacheKey);
            return bound.entries();
        }catch (Exception e){
            log.error("redisee---{}---{}---{}",e.getMessage(),e.getCause(),e.getStackTrace());
        }finally {
            if(redisTemplate.getConnectionFactory()!=null){
                RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
            }
        }
        return null;


    }

    public Object getDataFromCacheMap(String cacheKey, Object key) {
        BoundHashOperations<Object, Object, Object> bound = redisTemplate.boundHashOps(cacheKey);
        return bound.get(key);
    }

    public void setDataFromCacheMap(String cacheKey, Object key, Object value) {
        BoundHashOperations<Object, Object, Object> bound = redisTemplate.boundHashOps(cacheKey);
        bound.put(key, value);
    }

    public void removeDataFromCacheMap(String cacheKey, Object key) {
        BoundHashOperations<Object, Object, Object> bound = redisTemplate.boundHashOps(cacheKey);
        bound.delete(key);
    }

    public boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }

    public boolean makeMoreTime(final String key, int minutes) {
        return redisTemplate.boundValueOps(key).expire(minutes, TimeUnit.SECONDS);
    }

    public Object get(final String key) {

        try {

            Object result = null;
            ValueOperations<Object, Object> operations = redisTemplate.opsForValue();
            result = operations.get(key);
            return result;
        }catch (Exception e){
            log.error("redisee---{}---{}---{}",e.getMessage(),e.getCause(),e.getStackTrace());
        }finally {
            if(redisTemplate.getConnectionFactory()!=null){
                RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
            }
        }
        return null;

    }


    public boolean set(final String key, Object value) {
        try {
            boolean result = false;
            try {
                ValueOperations<Object, Object> operations = redisTemplate.opsForValue();
                operations.set(key, value);
                result = true;
            } catch (Exception e) {
                log.error("set cache error", e);
            }
            return result;
        }catch (Exception e){
            log.error("redisee---{}---{}---{}",e.getMessage(),e.getCause(),e.getStackTrace());
        }finally {
            if(redisTemplate.getConnectionFactory()!=null){
                RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
            }
        }
        return false;

    }

    public boolean set(final String key, Object value, Integer expireTime) {
        boolean result = false;
        try {
            ValueOperations<Object, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            log.error("set cache error", e);
        }finally {
            if(redisTemplate.getConnectionFactory()!=null){
                RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
            }
        }
        return result;
    }

    public long increment(final String key, long delta,Integer expireTime) {
        long value = redisTemplate.opsForValue().increment(key, delta);
        redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
        return value;
    }

    public void setRedisTemplate(RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean hmset(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
