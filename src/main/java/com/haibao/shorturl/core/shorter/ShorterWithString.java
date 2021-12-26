package com.haibao.shorturl.core.shorter;

import com.haibao.shorturl.core.IShorterGetter;

/**
 * 返回短码
 * @author wuque
 */
public class ShorterWithString implements IShorterGetter {

    private String shorter;

    public ShorterWithString() {
    }

    public ShorterWithString(String shorter) {
        setShorter(shorter);
    }

    @Override
    public String getShorter() {
        return shorter;
    }

    public void setShorter(String shorter) {
        this.shorter = shorter;
    }
}
