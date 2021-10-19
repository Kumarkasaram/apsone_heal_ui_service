package com.heal.uiservice.pojo;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.TreeMap;

@Slf4j
@Data
public class RequestObject<T> {

    private T body;
    private Map<String, String> params = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    private Map<String, String> headers = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    private Map<String, String[]> queryParams = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    public void addQueryParam(String key, String[] value) {
        queryParams.put(key, value);
    }

    public void addHeaders(String key, String value) {
        headers.put(key, value);
    }

    public void addParams(String key, String value) {
        params.put(key, value);
    }
}
