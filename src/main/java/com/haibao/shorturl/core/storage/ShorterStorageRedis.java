package com.haibao.shorturl.core.storage;

import cn.hutool.core.util.StrUtil;
import com.haibao.shorturl.core.IShorterGetter;
import com.haibao.shorturl.core.IShorterStorage;
import com.haibao.shorturl.common.enums.CacheMetaEnum;
import com.haibao.shorturl.common.utils.GsonUtils;
import com.haibao.shorturl.service.ICacheService;

/**
 * redis 维护 短链接
 * @author wuque
 * @param <T>
 */
public class ShorterStorageRedis<T extends IShorterGetter> implements IShorterStorage<T> {

    ICacheService cacheService;

    public ShorterStorageRedis(ICacheService cacheService) {
        this.cacheService = cacheService;
    }

    @Override
    public String getLongUrl(String shorter) {
        return cacheService.get(CacheMetaEnum.CACHE_VLINK_SHORTER,shorter);
    }

    @Override
    public T getShorter(String url,Class clz) {


        String v = cacheService.get(CacheMetaEnum.CACHE_VLINK_LONG,url.hashCode());
        if(StrUtil.isNotEmpty(v)){

            return (T) GsonUtils.gsonToBean(v,clz);
        }
        return null;
    }

    @Override
    public String getShorterUrl(String url,Class clz) {
        T t = getShorter(url,clz);
        if(null != t){
            return t.getShorter();
        }
        return null;
    }

    @Override
    public void cleanLong(String url) {

    }

    @Override
    public void cleanShorter(String shorter) {

    }

    @Override
    public void save(String url, T shorter) {
        cacheService.set(CacheMetaEnum.CACHE_VLINK_LONG, url.hashCode(), GsonUtils.gsonString(shorter));
        cacheService.set(CacheMetaEnum.CACHE_VLINK_SHORTER,shorter.getShorter(), url);
    }

    @Override
    public void cleanAll() {

    }

}
