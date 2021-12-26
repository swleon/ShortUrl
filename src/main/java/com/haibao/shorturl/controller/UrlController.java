package com.haibao.shorturl.controller;

import cn.hutool.core.util.StrUtil;
import com.haibao.shorturl.common.utils.ServletUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * 短链接 接口
 * @author wuque
 */
@RestController
@RequestMapping("/api/vlink")
@Slf4j
public class UrlController {

    @Resource
    UrlComponent urlComponent;

    /**
     * 短链接访问，重定向
     * @return
     */
    @GetMapping(value = "/go/{shortUrl}")
    public ResponseEntity visit(@PathVariable(value = "shortUrl") String shortUrl) {

        assert StrUtil.isNotEmpty(shortUrl);

        String longLink = urlComponent.getLongUrl(shortUrl, ServletUtils.getRequest().getQueryString());

        if(StrUtil.isNotEmpty(longLink)){
            try {
                ServletUtils.getResponse().sendRedirect(longLink);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ResponseEntity.badRequest().body("Invalid short link！");
    }


    /**
     * 长链接转短链接
     * @param  url -> 长链接
     * @return
     */
    @GetMapping(value = "/to")
    public ResponseEntity<String> to(@RequestParam(name="url",required = true) String url) {

        return urlComponent.generatorShortUrl(url);
    }

    /**
     *  长链接转短链接
     * @param url
     * @param enableTransParms 是否允许透传参数 0允许 1不允许
     * @return
     */
    @GetMapping(value = "/toWithEnableTransParms")
    public ResponseEntity<String> to(@RequestParam(name="url",required = true) String url,
                                 @RequestParam(name="enableTransParms",defaultValue = "0") int enableTransParms) {

        return urlComponent.generatorShortUrl(url,enableTransParms);
    }

}
