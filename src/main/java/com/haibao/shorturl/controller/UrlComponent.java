package com.haibao.shorturl.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import com.haibao.shorturl.common.Constants;
import com.haibao.shorturl.common.utils.GsonUtils;
import com.haibao.shorturl.common.utils.IpUtil;
import com.haibao.shorturl.common.utils.ServletUtils;
import com.haibao.shorturl.common.utils.UrlUtils;
import com.haibao.shorturl.core.IShorterStorage;
import com.haibao.shorturl.core.generator.StringGeneratorSnowflake;
import com.haibao.shorturl.core.generator.UrlShorterGeneratorSimple;
import com.haibao.shorturl.core.generator.UrlShorterGeneratorTransParm;
import com.haibao.shorturl.core.shorter.ShorterWithString;
import com.haibao.shorturl.core.shorter.ShorterWithTransParms;
import com.haibao.shorturl.core.storage.ShorterStorageDb;
import com.haibao.shorturl.core.storage.ShorterStorageRedis;
import com.haibao.shorturl.service.ICacheService;
import com.haibao.shorturl.service.IShorterStorageDbService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;


/**
 * 短链接 服务
 *
 * @author wuque
 */
@Component("urlComponent")
@Slf4j
public class UrlComponent {

    @Resource
    ICacheService cacheService;
    @Resource
    IShorterStorageDbService shorterStorageDbService;

    BlockingQueue<Map<String,String>> blockingQueue = new ArrayBlockingQueue<>(10000,true);

    public static final String V_LINK = "https://v.xxxx.com";

    /**
     * 获取长链接
     *
     * @param shortUrl
     * @param parmStr
     * @return
     */
    public String getLongUrl(String shortUrl, String parmStr) {

        //计数
        count(shortUrl);

        if (StrUtil.isNotEmpty(parmStr)) {
            shortUrl += "?" + parmStr;
        }

        String url = UrlUtils.normalize(V_LINK + UrlUtils.DELIMITER_MARK + shortUrl);

        LinkedHashMap<String, String> parmMap = UrlUtils.getParamsMap(url);
        if (null != parmMap && !parmMap.isEmpty()) {
            List<String> keys = new ArrayList(parmMap.keySet());
            String parms[] = keys.toArray(new String[keys.size()]);
            url = UrlUtils.removeParams(url, parms);
        }
        String paths[] = UrlUtils.getPath(url).split(UrlUtils.DELIMITER_MARK);
        String shorterStr = paths[paths.length - 1];

        List<IShorterStorage> storageList = Lists.newLinkedList();
        storageList.add(new ShorterStorageRedis(cacheService));
        storageList.add(new ShorterStorageDb(shorterStorageDbService));

        String longUrl = getLongUrlByStorage(storageList, shorterStr);

        if (StrUtil.isEmpty(longUrl)) {
            return null;
        }

        //判断是否需要透出
        String shorter = shorterStorageDbService.getShorter(longUrl);
        if (StrUtil.isNotEmpty(shorter)) {
            Map map = GsonUtils.gsonToMaps(shorter);
            if (MapUtil.getInt(map, "enableTransParms",0) < 1 && !parmMap.isEmpty()) {
                    longUrl = UrlUtils.addParamsNotExist(longUrl, parmMap);
            }
        }

        return longUrl;
    }

    private String getLongUrlByStorage(List<IShorterStorage> storageList, String shorterUrl) {

        String url = null;
        for (IShorterStorage shorterStorage : storageList) {

            url = shorterStorage.getLongUrl(shorterUrl);
            if (StrUtil.isNotEmpty(url)) {
                break;
            }
        }
        return url;
    }


    /**
     * 生成短链接
     *
     * @param url
     * @return
     */
    public ResponseEntity generatorShortUrl(String url) {

        //判断是否已存在，若存在，则直接返回
        List<IShorterStorage<ShorterWithString>> list = Lists.newLinkedList();
        list.add(new ShorterStorageRedis<ShorterWithString>(cacheService));
        list.add(new ShorterStorageDb(shorterStorageDbService));

        String shorterUrl = getShorterUrlWithString(list, url);
        if (StrUtil.isEmpty(shorterUrl)) {
            UrlShorterGeneratorSimple simple = new UrlShorterGeneratorSimple();
            simple.setGenerator(new StringGeneratorSnowflake());
            simple.setShorterStorages(list);

            Map map = Maps.newHashMap();
            map.put("url", url);

            shorterUrl = simple.generate(map).getShorter();
        }

        return ResponseEntity.ok(V_LINK + UrlUtils.DELIMITER_MARK + shorterUrl);
    }


    public ResponseEntity<String> generatorShortUrl(String url, int enableTransParms) {
        //判断是否已存在，若存在，则直接返回
        List<IShorterStorage<ShorterWithTransParms>> list = Lists.newLinkedList();
        list.add(new ShorterStorageRedis<ShorterWithTransParms>(cacheService));
        list.add(new ShorterStorageDb(shorterStorageDbService, false));

        String shorterUrl = getShorterUrlWithTransParms(list, url);
        if (StrUtil.isEmpty(shorterUrl)) {
            UrlShorterGeneratorTransParm simple = new UrlShorterGeneratorTransParm();
            simple.setGenerator(new StringGeneratorSnowflake());
            simple.setShorterStorages(list);

            Map map = Maps.newHashMap();
            map.put("url", url);
            map.put("enableTransParms", enableTransParms);

            shorterUrl = simple.generate(map).getShorter();
        }

        return ResponseEntity.ok(V_LINK + UrlUtils.DELIMITER_MARK + shorterUrl);
    }

    private String getShorterUrlWithString(List<IShorterStorage<ShorterWithString>> storageList, String longUrl) {

        String shorterUrl = null;
        for (IShorterStorage shorterStorage : storageList) {
            shorterUrl = shorterStorage.getShorterUrl(longUrl,ShorterWithString.class);
            if (StrUtil.isNotEmpty(shorterUrl)) {
                break;
            }
        }
        return shorterUrl;
    }

    private String getShorterUrlWithTransParms(List<IShorterStorage<ShorterWithTransParms>> storageList, String longUrl) {

        String shorterUrl = null;
        for (IShorterStorage shorterStorage : storageList) {
            shorterUrl = shorterStorage.getShorterUrl(longUrl,ShorterWithTransParms.class);
            if (StrUtil.isNotEmpty(shorterUrl)) {
                break;
            }
        }
        return shorterUrl;
    }

    /**
     * 计数
     * @param shortUrl
     */
    private void count(String shortUrl) {
        Map map = Maps.newHashMap();
        map.put("url",shortUrl);
        map.put("ip", IpUtil.getRealIp(ServletUtils.getRequest()));
        map.put("time", DateUtil.currentSeconds());
        blockingQueue.add(map);
    }


    /**
     *
     */
    @PostConstruct
    public void countByBatch() {
        new Thread() {
            @Override
            public void run() {
                log.info("Open short link, queue asynchronous consumption...");

                List<Map> list = Lists.newArrayList();

                while (true) {
                    try {
                        list.clear();
                        Queues.drain(blockingQueue, list, 500, 3, TimeUnit.SECONDS);

                        if (list.size() > 0) {
                            shorterStorageDbService.visitCountBatch(list);
                        }

                        Thread.sleep(500);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                }
            }
        }.start();
    }

}
