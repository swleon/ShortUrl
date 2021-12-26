package com.haibao.shorturl.core;

/**
 * 随机字符串发生器
 * @author wuque
 */
public interface IStringGenerator {

   String generate(String url);

    void setLength(int length);
}
