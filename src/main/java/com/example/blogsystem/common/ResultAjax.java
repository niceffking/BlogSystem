package com.example.blogsystem.common;

import lombok.Data;

/**
 * 后端返回给前端的状态信息
 */
@Data
public class ResultAjax {
    private int code;
    private String msg;
    private Object data;  // 可能是很多种类型的数据, 所以这里使用Object类
    public static ResultAjax success(Object data) {
        ResultAjax resultAjax = new ResultAjax();
        resultAjax.setCode(200);
        resultAjax.setMsg("");
        resultAjax.setData(data);
        return resultAjax;
    }

    public static ResultAjax success(int code, String msg,Object data) {
        ResultAjax resultAjax = new ResultAjax();
        resultAjax.setCode(code);
        resultAjax.setMsg(msg);
        resultAjax.setData(data);
        return resultAjax;
    }

    public static ResultAjax fail(int code, String msg) {
        ResultAjax resultAjax = new ResultAjax();
        resultAjax.setCode(code);
        resultAjax.setMsg(msg);
        resultAjax.setData(null);
        return  resultAjax;
    }

    public static ResultAjax fail(int code, String msg, Object data) {
        ResultAjax resultAjax = new ResultAjax();
        resultAjax.setCode(code);
        resultAjax.setMsg(msg);
        resultAjax.setData(data);
        return  resultAjax;
    }
}
