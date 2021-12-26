package com.haibao.shorturl.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 短连接
 * @author wuque
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShorterUrlEntity {

    protected Long id;
    protected Integer created;
    protected Integer updated;
    protected Integer isDeleted;

    private Long visitCount;

    private String longUrl;

    private String shorterUrl;

    private String shorterGetter;

    private String shorterGetterClz;
}
