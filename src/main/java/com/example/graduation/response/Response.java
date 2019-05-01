package com.example.graduation.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 返回结果集包装
 * 1. 返回一个对象User， return Response.success(user);
 * 2. 返回一堆键值对， return Response.success().put(k1,v1).put(k2,v2);
 * 3. 返回错误： return Response.fail().verror(String verr);
 * </p>
 *
 * @author yuxiaobin
 * @date 2018/1/23
 */
public class Response extends ResponseEntity {

    public static final String MSG = "msg";
    public static final String CODE = "code";
    public static final String MSG_SUCCESS = "success";
    public static final String MSG_FAIL = "fail";
    public static final String DATA = "data";
    public static final String VERROR = "msg";
    public static final String LIST_RESULT = "list";
    public static final String COUNT = "count";


    public static ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger("Response");

    private Response(HttpStatus status) {
        super(status);
    }

    private Response(Object body, HttpStatus status) {
        super(body, status);
    }

    private Response(MultiValueMap<String, String> headers, HttpStatus status) {
        super(headers, status);
    }

    private Response(Object body, MultiValueMap<String, String> headers, HttpStatus status) {
        super(body, headers, status);
    }

    public static Response success() {
        Map<String, Object> map = new HashMap<>();
        map.put(MSG, MSG_SUCCESS);
        map.put(CODE, 0);
        map.put(DATA, new HashMap<>(4));
        return new Response(map, HttpStatus.OK);
    }

    //用于记录分页总数
    public static Response success(int count) {
        Map<String, Object> map = new HashMap<>();
        map.put(MSG, MSG_SUCCESS);
        map.put(CODE, 0);
        map.put(DATA, new HashMap<>(4));
        map.put(COUNT, count);
        return new Response(map, HttpStatus.OK);
    }

    /**
     * 是否要将对象转成json对象
     *
     * @param data
     * @return
     */
    public static Response success(Object data) {
        Map result = new HashMap<>(2);
        result.put(MSG, MSG_SUCCESS);
        result.put(CODE, 0);
        if (objectMapper != null) {
            try {
                String jsonString = objectMapper.writeValueAsString(data);
                if (data instanceof List) {
                    List list = objectMapper.readValue(jsonString, List.class);
                    Map page = new HashMap<>(2);
                    page.put(LIST_RESULT, list);
                    result.put(DATA, page);
                } else {
                    Map map = objectMapper.readValue(jsonString, Map.class);
                    result.put(DATA, map);
                }
                return new Response(result, HttpStatus.OK);
            } catch (IOException e) {
                logger.debug("failed to convert object=[{}] to Map, exception=[{}]", data, e);
            }
        }
        addReturnVal(result, data);
        return new Response(result, HttpStatus.OK);
    }

    private static void addReturnVal(Map map, Object result) {
        if (result instanceof List) {
            Map page = new HashMap<>(2);
            page.put(LIST_RESULT, result);
            map.put(DATA, page);
        } else {
            map.put(DATA, result);
        }
    }

    public static Response fail() {
        Map<String, Object> map = new HashMap<>();
        map.put(MSG, MSG_FAIL);
        map.put(CODE, 1);
        return new Response(map, HttpStatus.OK);
    }

    public static Response fail(String verror) {
        Map map = new HashMap<>();
        map.put(MSG, MSG_FAIL);
        map.put(CODE, 1);
        map.put(VERROR, verror);
        return new Response(map, HttpStatus.OK);
    }

    public static Response fail(HttpStatus httpStatus) {
        return new Response(new HashMap(), httpStatus);
    }

    /*public static Response fail(Object data) {
        return new Response(data, HttpStatus.BAD_REQUEST);
    }*/

    public Response put(String key, Object value) {
        Object body = this.getBody();
        Map mp = ((Map) body);
        Object data = mp.get(DATA);
        Assert.isInstanceOf(Map.class, data, "should invoke Response.success()/fail() or success(Map)/fail(MAP) then invoke put(k,v)");
        if (data instanceof Map) {
            ((Map) data).put(key, value);
        }
        return this;
    }

    public Response putAll(Map<String, Object> maps) {
        Object body = this.getBody();
        Map mp = ((Map) body);
        Object data = mp.get(DATA);
        Assert.isInstanceOf(Map.class, data, "should invoke Response.success()/fail() or success(Map)/fail(MAP) then invoke put(k,v)");
        ((Map) data).putAll(maps);

        return this;
    }

    public Response putAllT(Map<Integer, Object> maps) {
        Object body = this.getBody();
        Map mp = ((Map) body);
        Object data = mp.get(DATA);
        Assert.isInstanceOf(Map.class, data, "should invoke Response.success()/fail() or success(Map)/fail(MAP) then invoke put(k,v)");
        ((Map) data).putAll(maps);

        return this;
    }


    public Response msg(String msg) {
        Object body = this.getBody();
        Assert.isInstanceOf(Map.class, body, "should invoke Response.success()/fail() or success(Map)/fail(MAP) then invoke msg(str)");
        ((Map) body).put(MSG, msg);
        return this;
    }

    public Response verror(String errMsg) {
        Object body = this.getBody();
        Assert.isInstanceOf(Map.class, body, "should invoke Response.success()/fail() or success(Map)/fail(MAP) then invoke verror(str)");
        ((Map) body).put(VERROR, errMsg);
        return this;
    }
}
