package com.haibao.shorturl.service.impl;

import com.haibao.shorturl.common.enums.CacheMetaEnum;
import com.haibao.shorturl.service.ICacheService;
import com.haibao.shorturl.common.redis.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author wuque
 * @title: CacheServiceImpl
 * @projectName ShortUrl
 * @description: 缓存操作
 * @date 2021/12/259:59 下午
 */
@Service("cacheService")
@Slf4j
public class CacheServiceImpl implements ICacheService {

    @Resource
    RedisCache redisCache;

    @Override
    public String get(CacheMetaEnum cacheMetaEnum, Object... key) {
        return redisCache.getCacheObject(cacheMetaEnum.buildKey(key));
    }

    @Override
    public void set(CacheMetaEnum cacheMetaEnum, Object key, String value) {
        redisCache.setCacheObject(cacheMetaEnum.buildKey(key),value, cacheMetaEnum.getExpire(), TimeUnit.SECONDS);
    }
}
