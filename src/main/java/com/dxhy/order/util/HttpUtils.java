package com.dxhy.order.util;

import cn.hutool.http.HttpRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ZSC-DXHY
 */
@Slf4j
public class HttpUtils {


    /**
     * 执行post请求
     *
     * @param url
     * @param paramMap
     * @return
     * @throws IOException
     */
    public static String doPost(String url, Map<String, ?> paramMap) {
        Map<String, Object> requestMap = new HashMap<>(paramMap);
        return HttpRequest.post(url).form(requestMap).execute().body();
    }
    
    public static String doPost(String url, String request) {
        return HttpRequest.post(url).body(request).execute().body();
    }
    
    public static String doPostWithHeader(String url, String data, Map<String, String> header) {
        return HttpRequest.post(url).addHeaders(header).body(data).execute().body();
    }
    
    public static String doPostFormWithHeader(String url, Map<String, ?> paramMap, Map<String, String> header) {
        Map<String, Object> requestMap = new HashMap<>(paramMap);
        return HttpRequest.post(url).addHeaders(header).form(requestMap).execute().body();
    }
    
    public static String doGetWithHeader(String url, String data, Map<String, String> header) {
        return HttpRequest.get(url).addHeaders(header).body(data).execute().body();
    }
    
    public static String doGet(String url, String request) {
        return HttpRequest.get(url).body(request).execute().body();
    }


    public static void main(String[] args) {
        String url = "http://dzfpfw-pre.ele-cloud.com/accept/invoice/V2.0/invoiceIssue";
        String requestParam = "{\"checkCode\":\"B010\",\"data\":\"H4sIAAAAAAAAAAFwA4/8xX54AwQtqCBIFdgTashS4pYpJBrjxb3gH68Jm9ZvRnLwk1Nl0vLzWt6SoOHHfn/ozO9EYMd6G4rZ7LJzWrSuhhZ51Pv+RUqDvqhkbVP82U3YfGSv6HCCzaxu7m8ft96jooWa42zX2nSCS3v9wBvuFtMl/L6mQt2WkpbQlgInL+5X9ezdO45mVBgPergeUNmHaH3xfEuDqEAamJcmLRiF6VrtDwafLsbUtaZ+bIPlPDM6vODVMRaj7EuX03W3YVF3rk2EXuiVO4/NlYf8YRX0Ht4diVpvqPRJJkvxwrB2eXFzVxdJtGzfl1oQB0WHXFBKJkvxwrB2eXEYxdlkGNMIO9hOgzkaOxWZ41BggbtHKHqKs85TScSdLuqHnwdKEDB4PNqEEmP8tndjjNxs4bfuwhRdRfwZHNchqBH1kdOwdLGdE7eWu+O6aGHhkYg7buVhNwGq9CY30uesFwOJs4AFAC1WByA/ypOuVTPOoCDqUBAO5UV+71cpgxMho3nKy90DZVpZyVCXvNVeE/UUeSFj3bNra/E3Tv1cMbr51RvezlcbhWuNcWhabKMhiqMyG5BC41+y5gpp93OL6fGzsshlCezRa5Z9tvUfK7FJEOehFwB9jmzULS9x1oQ+6k/28twUzP1+DNtvJK7GF0jRYRJBlFm1uSZR1OdRUlayC0ukRlRuBNV0c9C2tVENgJh9+Qb8sPOfTY0pP3Ft2AGbz4x+PgIfybZM5UUqKceqdYEt8L/L0U2sZTkEQ8yo+3zuvcGbOIxWMpuTD0ax92Z2o1vkBq8dlZKoLFDzSxZjxtG4tP2wc2lBgeuT3hpcTgZqlZrKEcwlq6jyi/3edEw9RWl1THC3RJw9hJpT7OCnmi+9OFzekqDhx35/6KGekQsYvqRpsDqAMeXAuLau1Ebm6A8RBV48et351umd3ktWwcIa1MXUtQrnAvSX1MGikgXEsxPllkw8lscE1exsN2iPzHxKQwTWOnNqRvTcerqwygSSw8E2NnXFKs4CiUod5eTZkYZObt3rlw7KjT2kH+dtW+0eZ/fOer/+LK7ICr4wqX29aLIFaYNkjfwFUewJg4D6Y8IX9ke/WvoQPQhLGwFe++kTQirUOBYebE75dmpEsUeKm0j6/loHceQDZGPugLtgZ8RGLezqfqc+E0TmjrbDtxNzR7ib7aNwAwAA\",\"dataExchangeId\":\"668985576993734656001\",\"encryCode\":\"2\",\"key\":\"1660363994meWgwxyaBzFTbXXbuuM5mg==\",\"machineNo\":\"\",\"machineType\":\"C48\",\"returnCode\":\"0000\",\"returnMsg\":\"\",\"taxpayerNo\":\"110101MYJ2GPQQ4\",\"terminalCode\":\"0\",\"version\":\"1.3\"}";
        String result = HttpUtils.doPost(url, requestParam);
    }
}
