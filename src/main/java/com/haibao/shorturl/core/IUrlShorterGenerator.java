package com.haibao.shorturl.core;

import java.util.Map;

/**
 * @author wuque
 */
public interface IUrlShorterGenerator<T extends IShorterGetter> {

    /**
     * 产生一个短链接对象
     *
     * @param map
     * @return
     */
    T generate(Map map);
}
