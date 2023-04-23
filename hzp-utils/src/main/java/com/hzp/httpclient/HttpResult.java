package com.hzp.httpclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by XuJijun on 2017-06-06.
 */
public class HttpResult {
    private static final ObjectMapper om = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(HttpResult.class);

    public static boolean isOk(HttpResult result) {
        return result != null && result.getStatus() == HttpStatus.SC_OK;
    }

    public static boolean isNotOk(HttpResult result) {
        return result == null || result.getStatus() != HttpStatus.SC_OK;
    }

    public static int status(HttpResult result) {
        return result == null ? 0 : result.getStatus();
    }

    private int status;
    private String payload;

    /**
     * 如果payload是json格式，把json转为对象后返回
     * 使用例子：Map jsonObject = httpResult.getJsonObject(Map.class);
     * 或：Pojo pojo = httpResult.getJsonObject(Pojo.class);
     */
    public <T> T getJsonObject(Class<T> clazz) {
        T object = null;
        try {
            object = om.readValue(payload, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return object;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

/*	public static void main(String[] args){
		HttpResult hr = new HttpResult();
		hr.setPayload("{\"abc\": 1, \"def\": 2}");
		Map jsonObject = hr.getJsonObject(Map.class);
		System.out.println(jsonObject);
	}*/
}
