package com.haibao.shorturl.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 缓存相关
 *
 * @author wuque
 */
@Getter
@AllArgsConstructor
public enum CacheMetaEnum {
    /**
     * 缓存key，过期时间，描述
     */
    CACHE_VLINK_SHORTER("vlink:v:%s", 30 * 60 * 24, "短连接->长链接"),
    CACHE_VLINK_LONG("vlink:l:%s", 30 * 60 * 24, "长链接->短连接"),
    ;

    private final String format;
    private final int expire;
    private final String desc;

    public String buildKey(Object... param) {
        return String.format(format, param);
    }

    public static CacheMetaEnum getEnumByFormat(String format) {
        CacheMetaEnum[] values = values();
        for (CacheMetaEnum item : values) {
            if (item.getFormat().equals(format)) {
                return item;
            }
        }
        return null;
    }

}
