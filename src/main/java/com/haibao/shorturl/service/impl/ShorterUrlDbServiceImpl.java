package com.haibao.shorturl.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.haibao.shorturl.core.IShorterGetter;
import com.haibao.shorturl.common.Constants;
import com.haibao.shorturl.common.enums.CacheMetaEnum;
import com.haibao.shorturl.common.utils.GsonUtils;
import com.haibao.shorturl.mapper.ShorterUrlMapper;
import com.haibao.shorturl.model.ShorterUrlEntity;
import com.haibao.shorturl.service.ICacheService;
import com.haibao.shorturl.service.IShorterStorageDbService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wuque
 */
@Service
@Slf4j
public class ShorterUrlDbServiceImpl implements IShorterStorageDbService {

    @Resource
    ShorterUrlMapper shorterUrlMapper;
    @Resource
    ICacheService cacheService;

    @Override
    public String getLong(String shorterUrl) {
        String longUrl = cacheService.get(CacheMetaEnum.CACHE_VLINK_SHORTER,shorterUrl);
        if(StrUtil.isNotEmpty(longUrl)){
            return longUrl;
        }

        ShorterUrlEntity shorterUrlEntityQuery = new ShorterUrlEntity();
        shorterUrlEntityQuery.setShorterUrl(shorterUrl);
        List<ShorterUrlEntity> shorterUrlEntityList = shorterUrlMapper.select(shorterUrlEntityQuery);
        if( null != shorterUrlEntityList && shorterUrlEntityList.size() > 0){
            longUrl = shorterUrlEntityList.get(0).getLongUrl();
            if(StrUtil.isNotEmpty(longUrl)){
                cacheService.setV2(CacheMetaEnum.CACHE_VLINK_SHORTER, shorterUrl, longUrl);
            }
        }
        return null;
    }

    @Override
    public String getShorter(String url) {
        String v = cacheService.get(CacheMetaEnum.CACHE_VLINK_LONG,url.hashCode());
        if(StrUtil.isNotEmpty(v)){
           return v;
        }

        ShorterUrlEntity shorterUrlEntityQuery = new ShorterUrlEntity();
        shorterUrlEntityQuery.setLongUrl(url);
        List<ShorterUrlEntity> shorterUrlEntityList = shorterUrlMapper.select(shorterUrlEntityQuery);

        if( null != shorterUrlEntityList && shorterUrlEntityList.size() > 0){
            v =  shorterUrlEntityList.get(0).getShorterGetter();
            cacheService.setV2(CacheMetaEnum.CACHE_VLINK_LONG,url.hashCode(),v);
        }
        return null;
    }

    @Override
    public String getShorterUrl(String url) {
        ShorterUrlEntity shorterUrlEntityQuery = new ShorterUrlEntity();
        shorterUrlEntityQuery.setLongUrl(url);
        List<ShorterUrlEntity> shorterUrlEntityList = shorterUrlMapper.select(shorterUrlEntityQuery);

        if( null != shorterUrlEntityList && shorterUrlEntityList.size() > 0){
            return shorterUrlEntityList.get(0).getShorterUrl();
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
    public void save(String url, IShorterGetter shorterGetter) {

        ShorterUrlEntity shorterUrlEntity = new ShorterUrlEntity();
        shorterUrlEntity.setLongUrl(url);
        shorterUrlEntity.setShorterUrl(shorterGetter.getShorter());
        shorterUrlEntity.setShorterGetter(GsonUtils.gsonString(shorterGetter));
        shorterUrlEntity.setShorterGetterClz(shorterGetter.getClass().getName());
        shorterUrlEntity.setVisitCount(0L);

        shorterUrlEntity.setCreated((int) DateUtil.currentSeconds());
        shorterUrlEntity.setUpdated((int) DateUtil.currentSeconds());
        shorterUrlEntity.setIsDeleted(Constants.IS_DELETED_NO);
        shorterUrlMapper.insert(shorterUrlEntity);
    }

    @Override
    public void cleanAll() {

    }

    @Override
    public void visitCountBatch(List list) {

        List<Map<String,Object>> list_ = list;

        //同一个ip 计算规则 后续看需求 todo
        //对url进行分组
        Map<String, List<Map<String, Object>>> glist =
                list_.stream().collect(
                        Collectors.groupingBy(e -> e.get("url").toString()));


        List<Map<String,Integer>> updateBatchList = Lists.newArrayList();
        glist.forEach((k,slist)->{
            Map nmap= Maps.newHashMap();
            nmap.put("shorterUrl",k);
            nmap.put("visitCount",slist.size());
            updateBatchList.add(nmap);
        });

        shorterUrlMapper.updateVisitCountByShortUrlBatch(updateBatchList);

    }

}
