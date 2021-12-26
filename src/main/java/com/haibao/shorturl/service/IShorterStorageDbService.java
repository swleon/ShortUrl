package com.haibao.shorturl.service;

import com.haibao.shorturl.core.IShorterGetter;

import java.util.List;
import java.util.Map;

/**
 * 短连接 db service
 * @author wuque
 */
public interface IShorterStorageDbService<T extends IShorterGetter> {

    String getLong(String shorterUrl);

    String getShorter(String longUrl);
    String getShorterUrl(String longUrl);

    void cleanLong(String longUrl);

    void cleanShorter(String shorter);

    void save(String url, T shorter);

    void cleanAll();

    void visitCountBatch(List<Map> list);
}
