package com.haibao.shorturl.mapper;

import com.haibao.shorturl.model.ShorterUrlEntity;

import java.util.List;
import java.util.Map;

public interface ShorterUrlMapper {

    int deleteByPrimaryKey(Long id);

    int insert(ShorterUrlEntity record);

    int insertSelective(ShorterUrlEntity record);

    ShorterUrlEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ShorterUrlEntity record);

    int updateByPrimaryKeyWithBLOBs(ShorterUrlEntity record);

    int updateByPrimaryKey(ShorterUrlEntity record);

    List<ShorterUrlEntity> select(ShorterUrlEntity shorterUrlEntityQuery);

    int updateVisitCountByShortUrlBatch(List<Map<String, Integer>> updateBatchList);
}
