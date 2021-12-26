package com.haibao.shorturl.core.storage;

import com.haibao.shorturl.core.IShorterGetter;
import com.haibao.shorturl.core.IShorterStorage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认内存存储方式
 * @author wuque
 * @param <T>
 */
public class ShorterStorageMemory<T extends IShorterGetter> implements IShorterStorage<T> {

    /**
     * 存储shorter,url
     */
    static Map<IShorterGetter, String> shorterMap = new ConcurrentHashMap<IShorterGetter, String>();
    /**
     * 存储url,shorter
     */
    static Map<String, IShorterGetter> urlMap = new ConcurrentHashMap<String, IShorterGetter>();
    /**
     * 存储shorter.shorter,shorter
     */
    static Map<String, IShorterGetter> shorterUrlMap = new ConcurrentHashMap<String, IShorterGetter>();

    @Override
    public String getLongUrl(String shorterKey) {
        IShorterGetter shorter = shorterUrlMap.get(shorterKey);
        if (shorter != null) {
            return shorterMap.get(shorter);
        }
        return null;
    }

    @Override
    public T getShorter(String url,Class clz) {
        return (T)urlMap.get(url);
    }

    @Override
    public String getShorterUrl(String url,Class clz) {
        IShorterGetter shorterGetter = getShorter(url,clz);
        if(null != shorterGetter){
            return shorterGetter.getShorter();
        }
        return null;
    }

    @Override
    public void cleanLong(String url) {
        IShorterGetter shorter = urlMap.get(url);
        if (shorter != null) {
            urlMap.remove(url);
            shorterMap.remove(shorter);
            shorterUrlMap.remove(shorter.getShorter());
        }
    }

    @Override
    public void cleanShorter(String shorterKey) {
        IShorterGetter shorter = shorterUrlMap.get(shorterKey);
        if (shorter != null) {
            urlMap.remove(shorterMap.get(shorter));
            shorterMap.remove(shorter);
            shorterUrlMap.remove(shorter.getShorter());
        }

    }

    @Override
    public void save(String url, T shorter) {
        urlMap.put(url, shorter);
        shorterMap.put(shorter, url);
        shorterUrlMap.put(shorter.getShorter(), shorter);
    }

    @Override
    public void cleanAll() {
        shorterMap.clear();
        shorterUrlMap.clear();
        urlMap.clear();
    }
}
