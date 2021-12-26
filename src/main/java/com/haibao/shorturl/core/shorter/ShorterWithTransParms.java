package com.haibao.shorturl.core.shorter;

import com.haibao.shorturl.core.IShorterGetter;

/**
 * 增加 短链透传 限制
 * @author wuque
 */
public class ShorterWithTransParms implements IShorterGetter {

    private String shorter;
    private Integer enableTransParms;

    public ShorterWithTransParms(String shorter, Integer enableTransParms) {
        this.shorter = shorter;
        this.enableTransParms = enableTransParms;
    }

    public Integer getEnableTransParms() {
        return enableTransParms;
    }

    public void setEnableTransParms(Integer enableTransParms) {
        this.enableTransParms = enableTransParms;
    }

    @Override
    public String getShorter() {
        return shorter;
    }

    public void setShorter(String shorter) {
        this.shorter = shorter;
    }
}
