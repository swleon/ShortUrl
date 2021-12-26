package com.haibao.shorturl.service;


import com.haibao.shorturl.common.enums.CacheMetaEnum;

public interface ICacheService {

    String get(CacheMetaEnum cacheMetaEnum, Object... key);

    void set(CacheMetaEnum cacheMetaEnum, Object key, String value);
}
