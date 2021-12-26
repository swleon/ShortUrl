package com.haibao.shorturl.core.generator;

import cn.hutool.core.map.MapUtil;
import com.haibao.shorturl.core.IShorterStorage;
import com.haibao.shorturl.core.IStringGenerator;
import com.haibao.shorturl.core.IUrlShorterGenerator;
import com.haibao.shorturl.core.shorter.ShorterWithTransParms;

import java.util.List;
import java.util.Map;

/**
 * 用于生成指定长度的串,并指定 是否可以透传参数
 * @author wuque
 */
public class UrlShorterGeneratorTransParm implements IUrlShorterGenerator<ShorterWithTransParms> {

    private IStringGenerator generator;
    private List<IShorterStorage<ShorterWithTransParms>> shorterStorages;

    public IStringGenerator getGenerator() {
        return generator;
    }

    public void setGenerator(IStringGenerator generator) {
        this.generator = generator;
    }

    public List<IShorterStorage<ShorterWithTransParms>> getShorterStorages() {
        return shorterStorages;
    }

    public void setShorterStorages(List<IShorterStorage<ShorterWithTransParms>> shorterStorages) {
        this.shorterStorages = shorterStorages;
    }

    @Override
    public ShorterWithTransParms generate(Map map) {

        String url = MapUtil.getStr(map,"url");
        int enableTransParms = MapUtil.getInt(map,"enableTransParms",0);

        String shorter = generator.generate(url);
        while (shorterStorages.get(0).getShorter(shorter,ShorterWithTransParms.class) != null) {
            shorter = generator.generate(url);
        }
        ShorterWithTransParms newShorterGetter = new ShorterWithTransParms(shorter,enableTransParms);
        shorterStorages.forEach(shorterStorage ->{
            shorterStorage.save(url, newShorterGetter);
        });

        return newShorterGetter;
    }

}
