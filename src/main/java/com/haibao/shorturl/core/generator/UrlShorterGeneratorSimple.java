package com.haibao.shorturl.core.generator;

import cn.hutool.core.map.MapUtil;
import com.haibao.shorturl.core.IShorterStorage;
import com.haibao.shorturl.core.IStringGenerator;
import com.haibao.shorturl.core.IUrlShorterGenerator;
import com.haibao.shorturl.core.shorter.ShorterWithString;

import java.util.List;
import java.util.Map;

/**
 * 用于生成指定长度的串
 * @author wuque
 */
public class UrlShorterGeneratorSimple implements IUrlShorterGenerator<ShorterWithString> {

    private IStringGenerator generator;
    private List<IShorterStorage<ShorterWithString>> shorterStorages;

    public IStringGenerator getGenerator() {
        return generator;
    }

    public void setGenerator(IStringGenerator generator) {
        this.generator = generator;
    }

    public List<IShorterStorage<ShorterWithString>> getShorterStorages() {
        return shorterStorages;
    }

    public void setShorterStorages(List<IShorterStorage<ShorterWithString>> shorterStorages) {
        this.shorterStorages = shorterStorages;
    }

    @Override
    public ShorterWithString generate(Map map) {

        String url = MapUtil.getStr(map,"url");

        String shorter = generator.generate(url);
        while (shorterStorages.get(0).getShorter(shorter,ShorterWithString.class) != null) {
            shorter = generator.generate(url);
        }
        ShorterWithString newShorterGetter = new ShorterWithString(shorter);
        shorterStorages.forEach(shorterStorage ->{
            shorterStorage.save(url, newShorterGetter);
        });

        return newShorterGetter;
    }
}
