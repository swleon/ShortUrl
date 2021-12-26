package com.haibao.shorturl.core;

/**
 * 用来存储字符串短地址
 * @author wuque
 */
public interface IShorterStorage<T extends IShorterGetter> {

    String getLongUrl(String shorter);

    T getShorter(String url,Class clz);

    String getShorterUrl(String url,Class cl);

    void cleanLong(String url);
    void cleanShorter(String shorter);

    void save(String url, T shorter);

    void cleanAll();
}
