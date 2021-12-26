package com.haibao.shorturl.service;


import com.haibao.shorturl.common.enums.CacheMetaEnum;

public interface ICacheService {

    String get(CacheMetaEnum cacheMetaEnum, Object... key);

    void setV2(CacheMetaEnum cacheMetaEnum, Object key, String value);
}
