package com.haibao.shorturl.common.utils;

import cn.hutool.core.util.URLUtil;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * URL 工具类
 * @author wuque
 */
@Slf4j
public class UrlUtils extends URLUtil {

    public static final String QUESTION_MARK = "?";
    public static final String AND_MARK = "&";
    public static final String EQUAL_MARK = "=";
    public static final String DELIMITER_MARK = "/";

    /**
     * 获取链接的参数
     * @param url 链接
     * @return
     */
    public static LinkedHashMap<String, String> getParamsMap(String url){
//        log.info("url=" + url);

        if(StringUtils.isBlank(url)){
            return Maps.newLinkedHashMap();
        }

        url = url.trim();
        int length = url.length();
        int index = url.indexOf(QUESTION_MARK);

        //url说明有问号
        if(index > -1){
            //url最后一个符号为？，如：http://wwww.baidu.com?
            if((length - 1) == index){
                return Maps.newLinkedHashMap();

            }else{
                //情况为：http://wwww.baidu.com?aa=11或http://wwww.baidu.com?aa=或http://wwww.baidu.com?aa
                String baseUrl = url.substring(0, index);
                String paramsString = url.substring(index + 1);

//                log.info("baseUrl=" + baseUrl);
//                log.info("paramsString=" + paramsString);

                if(!StringUtils.isBlank(paramsString)){
                    return parseQueryString(paramsString);
                }
            }
        }

        return Maps.newLinkedHashMap();
    }

    /**
     * queryString转map
     * @param queryString param1=xxx&param2=yyy格式
     * @return
     */
    public static LinkedHashMap<String, String> parseQueryString(String queryString) {
        LinkedHashMap<String, String> paramsMap = new LinkedHashMap<String, String>();
        String[] params = queryString.split(AND_MARK);

        for (String param : params) {
            if(!StringUtils.isBlank(param)){
                String[] oneParam = param.split(EQUAL_MARK);
                String paramName = oneParam[0];

                if(!StringUtils.isBlank(paramName)){
                    if(oneParam.length > 1){
                        //键可以去空格，值不能去空格
                        paramsMap.put(paramName.trim(), oneParam[1]);

                    }else{
                        paramsMap.put(paramName.trim(), "");
                    }
                }

            }
        }
        return paramsMap;
    }

    /**
     * 向url链接追加参数（会覆盖已经有的参数）
     * @param url 链接地址
     * @param params LinkedHashMap<String, String> 参数
     * @param isOverride 是否覆盖参数，true表示将新参数覆盖链接原参数，false不覆盖
     * @return
     */
    public static String addParams(String url, LinkedHashMap<String, String> params, boolean isOverride){
        if(StringUtils.isBlank(url)){
            return "";

        }else if(params == null || params.size() < 1){
            return url.trim();

        }else{

            url = url.trim();
            int index = url.indexOf(QUESTION_MARK);
            String baseUrl = "";

            if(index > -1){
                baseUrl = url.substring(0, index);

            }else{
                baseUrl = url;
            }

            LinkedHashMap<String, String> paramsMapInUrl = getParamsMap(url);

            if(paramsMapInUrl == null){
                paramsMapInUrl = new LinkedHashMap<String, String>();
            }

            if(!isOverride){
                //移除Url链接已经存在的参数
                LinkedHashMap<String, String> newParams = new LinkedHashMap<String, String>(params.size());

                for (Map.Entry<String, String> entry : params.entrySet()) {
                    if(!StringUtils.isBlank(entry.getKey())){
                        newParams.put(entry.getKey().trim(), entry.getValue());
                    }
                }

                for (Map.Entry<String, String> entry : newParams.entrySet()) {
                    for (Map.Entry<String, String> urlEntry : paramsMapInUrl.entrySet()) {
                        if(!StringUtils.isBlank(entry.getKey())){
                            if(entry.getKey().trim().equals(urlEntry.getKey())){
                                params.remove(entry.getKey().trim());
                            }
                        }
                    }
                }
            }


            for (Map.Entry<String, String> entry : params.entrySet()) {
                paramsMapInUrl.put(entry.getKey().trim(), entry.getValue());
            }

            //重新拼接链接
            if(paramsMapInUrl != null && paramsMapInUrl.size() > 0){
                StringBuffer paramBuffer = new StringBuffer(baseUrl);
                paramBuffer.append(QUESTION_MARK);
                Set<String> set = paramsMapInUrl.keySet();
                for (String paramName : set) {
                    paramBuffer.append(paramName).append(EQUAL_MARK)
                            .append(paramsMapInUrl.get(paramName) == null ? "" : paramsMapInUrl.get(paramName))
                            .append(AND_MARK);
                }
                paramBuffer.deleteCharAt(paramBuffer.length() - 1);
                return paramBuffer.toString();
            }
            return baseUrl;

        }
    }


    /**
     * 向url链接追加参数(单个) （会覆盖已经有的参数）
     * @param url 链接地址
     * @param name String 参数名
     * @param value String 参数值
     * @param isOverride 是否覆盖参数，true表示将新参数覆盖链接原参数，false不覆盖
     * @return
     */
    public static String addParam(String url, String name, String value, boolean isOverride){
        if(StringUtils.isBlank(url)){
            return "";

        }else if(StringUtils.isBlank(name)){
            return url.trim();

        }else{
            LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
            params.put(name.trim(), value);
            return addParams(url, params, isOverride);
        }
    }


    /**
     * 向url链接追加参数(单个) （会覆盖已经有的参数）
     * @param url 链接地址
     * @param name String 参数名
     * @param value String 参数值
     * @return
     */
    public static String addParam(String url, String name, String value){
        return addParam(url, name, value, true);
    }


    /**
     * 向url链接追加参数(单个) （不会覆盖已经有的参数）
     * @param url 链接地址
     * @param name String 参数名
     * @param value String 参数值
     * @return
     */
    public static String addParamNotExist(String url, String name, String value){
        return addParam(url, name, value, false);
    }


    /**
     * 向url链接追加参数（会覆盖已经有的参数）
     * @param url 链接地址
     * @param params LinkedHashMap<String, String> 参数
     * @return
     */
    public static String addParams(String url, LinkedHashMap<String, String> params){
        return addParams(url, params, true);
    }


    /**
     * 向url链接追加参数（不会覆盖已经有的参数）
     * @param url 链接地址
     * @param params LinkedHashMap<String, String> 参数
     * @return
     */
    public static String addParamsNotExist(String url, LinkedHashMap<String, String> params){
        return addParams(url, params, false);
    }


    /**
     * 移除url链接的多个参数
     * @param url String 链接地址
     * @param paramNames String... 参数
     * @return
     */
    public static String removeParams(String url, String... paramNames){
        if(StringUtils.isBlank(url)){
            return "";

        }else if(paramNames == null || paramNames.length < 1){
            return url.trim();

        }else{
            url = url.trim();
            int length = url.length();
            int index = url.indexOf(QUESTION_MARK);


            //url有问号
            if(index > -1){

                //url最后一个符号为？，如：http://wwww.baidu.com?
                if((length - 1) == index){
                    return url;

                }else{
                    LinkedHashMap<String, String> paramsMap = getParamsMap(url);

                    //删除参数
                    if(paramsMap != null && paramsMap.size() > 0){
                        for (String paramName : paramNames) {
                            if(!StringUtils.isBlank(paramName)){
                                paramsMap.remove(paramName.trim());
                            }
                        }
                    }

                    String baseUrl = url.substring(0, index);

                    //重新拼接链接
                    if(paramsMap != null && paramsMap.size() > 0){
                        StringBuffer paramBuffer = new StringBuffer(baseUrl);
                        paramBuffer.append(QUESTION_MARK);
                        Set<String> set = paramsMap.keySet();
                        for (String paramName : set) {
                            paramBuffer.append(paramName).append(EQUAL_MARK).append(paramsMap.get(paramName)).append(AND_MARK);
                        }
                        paramBuffer.deleteCharAt(paramBuffer.length() - 1);
                        return paramBuffer.toString();
                    }
                    return baseUrl;
                }
            }
            return url;
        }
    }


    public static void main(String[] args) {

        String a = "http://wwww.baidu.com";
        String b = "http://wwww.baidu.com?";
        String c = "http://wwww.baidu.com?aa=11";
        String d = "http://wwww.baidu.com?aa";
        String e = "http://wwww.baidu.com?aa=11&bb=222&cc=33";
        String f = "http://wwww.baidu.com?aa=11&bb=222&cc=33&dd=";
        String g = "http://wwww.baidu.com?aa=111&bb=222&cc=33&dd";


       /* List<String> params = new ArrayList<String>();
        params.add("aa");
        System.out.println("d="+removeParams(d, params));
        params.add("bb");
        System.out.println("e="+removeParams(e, params));
        params.add("cc");
        System.out.println("f="+removeParams(f, params));
        params.add("dd");
        System.out.println("g="+removeParams(g, params));*/


        System.out.println("g="+removeParams(g, "cc","aa","dd"));
        System.out.println("g2="+removeParams(g, "cc","aa","dd"));
        System.out.println("d="+removeParams(d, "cc"));
        System.out.println("d2="+removeParams(d, "aa"));
        System.out.println("a="+removeParams(a, "aa"));
        System.out.println("b="+removeParams(b, "aa"));

        /*
        System.out.println("a==="+addParam(a, "p", "cc"));
        System.out.println("b==="+addParam(b, "p", "cc"));
        System.out.println("c==="+addParam(c, "p", "cc"));
        System.out.println("d==="+addParam(d, "p", "cc"));

        System.out.println("g1==="+addParam(g, "bb", "000"));
        System.out.println("g2==="+addParamNotExist(g, "bb", "000"));*/


       /* LinkedHashMap<String, String> paramsMap = new LinkedHashMap<String, String>();
        paramsMap.put("jj", "j13");
        paramsMap.put("kkk", "k1243");
        paramsMap.put("mm", "");
        paramsMap.put("nn", null);
        paramsMap.put("bb", "new");

        //System.out.println("a==="+addParams(a, paramsMap));
        //System.out.println("b==="+addParams(b, paramsMap));
        //System.out.println("c==="+addParams(c, paramsMap));
        //System.out.println("d==="+addParams(d, paramsMap));

        System.out.println("f1==="+addParams(f, paramsMap));
        System.out.println("f2==="+addParamsNotExist(f, paramsMap));*/

        //System.out.println("g2="+getParamsMap(g));

//        System.out.println( UrlUtils.decode("https%3A%2F%2Fwww.weshop.com%2Fdetail.html%3FitemId%3D19hupjydnz22i"));
    }
}
