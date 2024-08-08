package com.nft.cn.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RedisUtil {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;



    public final static long DEFAULT_TIMEOUT = 1 * 60 * 60;
    public final static long NOT_TIMEOUT = -1;

    public void incr(String key, long delta, long timeout) {
        if (delta < 0) {
            throw new RuntimeException("ee");
        }
        GenericToStringSerializer genericToStringSerializer = new GenericToStringSerializer(Object.class);
        stringRedisTemplate.setValueSerializer(genericToStringSerializer);
        stringRedisTemplate.setKeySerializer(new StringRedisSerializer());
        stringRedisTemplate.afterPropertiesSet();
        stringRedisTemplate.opsForValue().increment(key, delta);
        if (timeout != NOT_TIMEOUT) {
            stringRedisTemplate.expire(key, timeout, TimeUnit.MILLISECONDS);
        }
    }

    public static Long getCurrent2TodayEndMillisTime() {
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);
        return todayEnd.getTimeInMillis() - new Date().getTime();
    }

    public long getIncrValue(final String key) {

        return (long) stringRedisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) {
                RedisSerializer<String> serializer = stringRedisTemplate.getStringSerializer();
                byte[] rowkey = serializer.serialize(key);
                byte[] rowval = connection.get(rowkey);
                try {
                    String val = serializer.deserialize(rowval);
                    return Long.parseLong(val);
                } catch (Exception e) {
                    return 0L;
                }
            }
        });
    }

    public void setString(String key, String value) {
        setString(key, value, DEFAULT_TIMEOUT);
    }

    public void setString(String key, String value, long timeout) {

        try {
            stringRedisTemplate.opsForValue().set(key, value);
            if (timeout != NOT_TIMEOUT) {
                stringRedisTemplate.expire(key, timeout, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            log.error("set cache error", e);
        } finally {
            if (stringRedisTemplate.getConnectionFactory() != null) {
                RedisConnectionUtils.unbindConnection(stringRedisTemplate.getConnectionFactory());
            }
        }


    }

    public String getString(String key) {

        try {
            return stringRedisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            log.error("set cache error", e);
        } finally {
            if (stringRedisTemplate.getConnectionFactory() != null) {
                RedisConnectionUtils.unbindConnection(stringRedisTemplate.getConnectionFactory());
            }
        }
        return null;
    }

    public void setObject(String key, Object value) {
        setObject(key, value, DEFAULT_TIMEOUT);
    }

    public void setObject(String key, Object value, long timeout) {


        try {
            stringRedisTemplate.opsForValue().set(key, JSONObject.toJSONString(value));

            if (timeout != NOT_TIMEOUT) {
                stringRedisTemplate.expire(key, timeout, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            log.error("set cache error", e);
        } finally {
            if (stringRedisTemplate.getConnectionFactory() != null) {
                RedisConnectionUtils.unbindConnection(stringRedisTemplate.getConnectionFactory());
            }
        }


    }

    public <T> T getObject(String key, Class<T> clazz) {


        try {
            String value = stringRedisTemplate.opsForValue().get(key);

            return value == null ? null : JSON.parseObject(value, clazz);
        } catch (Exception e) {
            log.error("set cache error", e);
        } finally {
            if (stringRedisTemplate.getConnectionFactory() != null) {
                RedisConnectionUtils.unbindConnection(stringRedisTemplate.getConnectionFactory());
            }
        }
        return null;
    }

    public void setList(String key, List<?> value) {

        try {
            setList(key, value, DEFAULT_TIMEOUT);
        } catch (Exception e) {
            log.error("set cache error", e);
        } finally {
            if (stringRedisTemplate.getConnectionFactory() != null) {
                RedisConnectionUtils.unbindConnection(stringRedisTemplate.getConnectionFactory());
            }
        }
    }

    public void setList(String key, List<?> value, long timeout) {

        try {
            stringRedisTemplate.opsForValue().set(key, JSONArray.toJSONString(value));
            if (timeout != NOT_TIMEOUT) {
                stringRedisTemplate.expire(key, timeout, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            log.error("set cache error", e);
        } finally {
            if (stringRedisTemplate.getConnectionFactory() != null) {
                RedisConnectionUtils.unbindConnection(stringRedisTemplate.getConnectionFactory());
            }
        }
    }

    public boolean hasKey(String key) {
        try {
            return stringRedisTemplate.hasKey(key);
        } catch (Exception e) {
            log.error("set cache error", e);
        } finally {
            if (stringRedisTemplate.getConnectionFactory() != null) {
                RedisConnectionUtils.unbindConnection(stringRedisTemplate.getConnectionFactory());
            }
        }
        return false;

    }

    public <T> List<T> getList(String key, Class<T> clazz) {


        try {
            String value = stringRedisTemplate.opsForValue().get(key);

            return value == null ? null : JSON.parseArray(value, clazz);
        } catch (Exception e) {
            log.error("set cache error", e);
        } finally {
            if (stringRedisTemplate.getConnectionFactory() != null) {
                RedisConnectionUtils.unbindConnection(stringRedisTemplate.getConnectionFactory());
            }
        }
        return null;


    }

    public boolean expire(String key) {
        return stringRedisTemplate.expire(key, DEFAULT_TIMEOUT, TimeUnit.SECONDS);
    }

    public boolean expire(String key, long timeout) {
        return stringRedisTemplate.expire(key, timeout, TimeUnit.SECONDS);
    }

    public void delete(String key) {

        try {
            stringRedisTemplate.delete(key);
        } catch (Exception e) {
            log.error("set cache error", e);
        } finally {
            if (stringRedisTemplate.getConnectionFactory() != null) {
                RedisConnectionUtils.unbindConnection(stringRedisTemplate.getConnectionFactory());
            }
        }


    }

    public void deleteKeys(String keys) {

        Set<String> keySet = stringRedisTemplate.keys(keys);
        stringRedisTemplate.delete(keySet);
    }


    public boolean listPush(String key, Object value) {


        try {
            if (key == null || value == null) {
                return false;
            }
            try {
                long i = stringRedisTemplate.opsForList().rightPush(key, String.valueOf(value));
                return i != 0;
            } catch (Exception ex) {
                log.error("setList error.", ex);
            }
        } catch (Exception e) {
            log.error("set cache error", e);
        } finally {
            if (stringRedisTemplate.getConnectionFactory() != null) {
                RedisConnectionUtils.unbindConnection(stringRedisTemplate.getConnectionFactory());
            }
        }

        return false;
    }

    public long listSize(String key) {

        try {
            return stringRedisTemplate.opsForList().size(key);
        } catch (Exception e) {
            log.error("set cache error", e);
        } finally {
            if (stringRedisTemplate.getConnectionFactory() != null) {
                RedisConnectionUtils.unbindConnection(stringRedisTemplate.getConnectionFactory());
            }
        }
        return 0;

    }

    public List<String> getList(String key) {

        try {
            return stringRedisTemplate.opsForList().range(key, 0, stringRedisTemplate.opsForList().size(key));
        } catch (Exception e) {
            log.error("set cache error", e);
        } finally {
            if (stringRedisTemplate.getConnectionFactory() != null) {
                RedisConnectionUtils.unbindConnection(stringRedisTemplate.getConnectionFactory());
            }
        }
         return null;
    }

    public Object listPop(String key) {
        return stringRedisTemplate.opsForList().leftPop(key);
    }





}
