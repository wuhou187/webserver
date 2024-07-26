package com.shsxt.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;

public class Request {
    /**GET /xxx?uname=wuhou HTTP/1.1*/
    //请求信息
    private String request;

    //请求方法
    private String method;

    //请求url
    private String url;

    //请求参数
    private String parameter;

    private final String CRLF = "\r\n";

    //处理请求参数
    private Map<String, List<String>> parameterMap;
    public Request(Socket client) throws IOException {
        this(client.getInputStream());
    }

    private Request(InputStream is) {
        try {
            parameterMap = new HashMap<String, List<String>>();
            byte[] dates = new byte[1024*1024];
            int len = is.read(dates);
            request = new String(dates , 0, len);
            //System.out.println(request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void parseRequest() {
        System.out.println("-----分解-----");
        System.out.println("1获取请求方式:开头到第一个/");
        method = request.substring(0, this.request.indexOf("/"));
        method = method.trim();
        System.out.println("2获取请求url:从第一个/到HTTP");
        System.out.println("可能有请求参数?前面的为url");
        int startIndex = this.request.indexOf("/") + 1;
        int endIndex = this.request.indexOf("HTTP");
        url = request.substring(startIndex, endIndex).trim();
        int parameterIndex = this.url.indexOf("?");
        if(parameterIndex >= 0) {
            String[] urlArray = url.split("\\?");
            url = urlArray[0];
            parameter = urlArray[1].trim();
            if(parameter.equals("")) {
                parameter = null;
            }
        }
        System.out.println("3如果是get已经获取了请求参数，如果是post则还在请求体当中");
        if(method.equals("POST")) {
            String pStr = request.substring(this.request.lastIndexOf(CRLF)).trim();
            if(parameter == null) {
                parameter = pStr;
            }
            else { //在已经有参数的情况下，追加添加参数
                parameter += "&" + pStr;
            }
        }
        System.out.println(method + "--->" + url + "--->" + decode(parameter, "utf-8"));

        //转成Map fav=1&fav=2&uname=wuhou&age=18&other=
        convertMap(parameter);
    }

    public void convertMap(String parameter) {
        //分割请求参数
        String[] keyValues = parameter.split("&");
        for(String parameterStr : keyValues) {
            //对请求参数再次进行分割
            String[] kv = parameterStr.split("=");
            kv = Arrays.copyOf(kv, 2);
            String key =kv[0];
            String values = kv[1] == null ? null : decode(kv[1], "utf-8");
            if(!(parameterMap.containsKey(key))) {
                parameterMap.put(key, new ArrayList<>());
            }
            parameterMap.get(key).add(values);
        }
    }

    //根据key获取多个值
    public String[] getValues(String key) {
        List<String> values = parameterMap.get(key);
        if(values == null) {
            return null;
        }
        return values.toArray(new String[0]);
    }

    //根据key返回一个值
    public String getValue(String key) {
        String[] values = getValues(key);
        if(values == null) {
            return null;
        }
        return values[0];
    }

    //处理中文
    private String decode(String value, String enc) {
        try {
            return URLDecoder.decode(value, enc);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public String getParameter() {
        return parameter;
    }
}
