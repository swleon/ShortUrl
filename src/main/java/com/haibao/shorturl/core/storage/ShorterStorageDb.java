package com.haibao.shorturl.core.storage;

import cn.hutool.core.util.StrUtil;
import com.haibao.shorturl.core.IShorterGetter;
import com.haibao.shorturl.core.IShorterStorage;
import com.haibao.shorturl.common.utils.GsonUtils;
import com.haibao.shorturl.service.IShorterStorageDbService;

/**
 * DB 维护 短链接
 * @author wuque
 * @param <T>
 */
public class ShorterStorageDb<T extends IShorterGetter> implements IShorterStorage<T> {

    IShorterStorageDbService shorterStorageDbService;
    boolean overwriteCache = false;

    public ShorterStorageDb(IShorterStorageDbService shorterStorageDbService,boolean overwriteCache) {
        this.shorterStorageDbService = shorterStorageDbService;
        this.overwriteCache = overwriteCache;
    }

    public ShorterStorageDb(IShorterStorageDbService shorterStorageDbService) {
        this.shorterStorageDbService = shorterStorageDbService;
    }

    @Override
    public String getLongUrl(String shorter) {
        return shorterStorageDbService.getLong(shorter);
    }

    @Override
    public T getShorter(String url,Class clz) {
        String v = shorterStorageDbService.getShorter(url);
        if(StrUtil.isNotEmpty(v)){
            return (T) GsonUtils.gsonToBean(v,clz);
        }
        return null;
    }

    @Override
    public String getShorterUrl(String url,Class clz) {

        return shorterStorageDbService.getShorterUrl(url);
    }

    @Override
    public void cleanLong(String url) {
        shorterStorageDbService.cleanLong(url);
    }

    @Override
    public void cleanShorter(String shorter) {
        shorterStorageDbService.cleanShorter(shorter);
    }

    @Override
    public void save(String url, T shorter) {
        shorterStorageDbService.save(url,shorter);
    }

    @Override
    public void cleanAll() {
        shorterStorageDbService.cleanAll();
    }
}
