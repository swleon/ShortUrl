package com.haibao.shorturl.core.generator;

import cn.hutool.core.util.IdUtil;
import com.haibao.shorturl.common.utils.NumericConvertUtils;
import com.haibao.shorturl.core.IStringGenerator;

/**
 * 雪花id 字符串 62进制转换 生成
 * @author wuque
 */
public class StringGeneratorSnowflake implements IStringGenerator {

    static Long datacenterId = IdUtil.getDataCenterId(1);
    static Long workId = IdUtil.getWorkerId(datacenterId,2);

    @Override
    public String generate(String url) {
        String shortUrl =
                NumericConvertUtils.compressNumber(
                        IdUtil.getSnowflake(workId,datacenterId).nextId(),62);
        return shortUrl;
    }

    @Override
    public void setLength(int length) {

    }

    public int getLength(){
        return 11;
    }
}
