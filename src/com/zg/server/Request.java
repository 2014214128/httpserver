package com.zg.server;


import java.io.*;

import java.util.*;

/**
 * 封装request
 */
public class Request {
    private static final String CRLF = "\r\n";
    //请求方式
    private String method;
    //请求资源
    private String url;
    //请求参数
    private Map<String, List<String>> parameterMapValues;

    private String requestInfo;

    private InputStream is;

    public Request() {
        method = "";
        url = "";
        parameterMapValues = new HashMap<>();
        requestInfo = "";
    }

    public String getUrl() {
        return url;
    }

    public Request(InputStream is) {
        this();
        this.is = is;
        try {
            byte[] data = new byte[20480];
            int len = is.read(data);
            if (len != -1) {
                requestInfo = decode(new String(data, 0, len).trim(), "UTF-8");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*分析请求信息*/
        parseRequestInfo();
    }
    /**
     * 根据页面的name,获取对应的多个值
     */
    public String[] getParameterValues(String name) {
        List<String> values = null;
        if ((values = parameterMapValues.get(name)) == null) {
            return null;
        } else {
            return values.toArray(new String[0]);
        }
    }
    /**
     * 根据页面的name,获取对应的单个值
     */
    public String getParameter(String name) {
        String[] values = getParameterValues(name);
        if (null == values) {
            return null;
        }
        return values[0];
    }
    /**
     * 分析请求信息
     */
    private void parseRequestInfo() {
        if (null == requestInfo || (requestInfo = requestInfo.trim()).equals("")) {
            return ;
        }
        /*GET /index.html?name=123&pwd=5456 HTTP/1.1*/
        //接收请求参数
        String paramStr = "";
        /*获取第一行数据*/
        String firstLine = requestInfo.substring(0,requestInfo.indexOf(CRLF));
        /*保存第一个/的下标*/
        int index = requestInfo.indexOf("/");
        this.method = firstLine.substring(0,index).trim();
        /*保存整体的url*/
        String urlStr = firstLine.substring(index, requestInfo.indexOf("HTTP/")).trim();
        if (this.method.equalsIgnoreCase("get")) {  //get方式提交数据
            if(urlStr.contains("?")) {  //有参数
                String[] urlArray = urlStr.split("\\?");
                this.url = urlArray[0];
                paramStr = urlArray[1];
            } else {  //没有参数
                this.url = url;
            }
        } else if (this.method.equalsIgnoreCase("post")) {  //post的方式提交数据
            this.url = urlStr;
        }
        /*不存在请求参数*/
        if (paramStr.equals("")) {
            return;
        }
        /*将请求参数封装到map中*/
        parseParams(paramStr);
    }

    /**
     * 将请求参数封装到map中
     */
    private void parseParams(String paramStr) {
        /*name=123&pwd=5456*/
        StringTokenizer token = new StringTokenizer(paramStr, "&");
        while (token.hasMoreTokens()) {
            String keyValue = token.nextToken();
            String[] keyValues = keyValue.split("=");
            if (keyValues.length == 1) {
                keyValues = Arrays.copyOf(keyValues, 2);
                keyValues[1] = null;
            }
            String key = keyValues[0].trim();
            String value = null == keyValues[1] ? null : decode(keyValues[1].trim(),"gbk");
            if (!parameterMapValues.containsKey(key)) {
                parameterMapValues.put(key, new ArrayList<>());
            }
            List<String> values = parameterMapValues.get(key);
            values.add(value);
        }
    }
    /**
     * 解决中文乱码问题
     */
    private String decode(String value, String code) {
        try {
            return java.net.URLDecoder.decode(value, code);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return  null;
    }

    public void close() {
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
