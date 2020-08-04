package com.petrichor.rpc;

import lombok.Data;

import java.io.Serializable;

/**
 * @author petrichor
 * @date 2020/8/4 10:27
 */

@Data
public class RpcResponse<T> implements Serializable {
    private static final long serialVersionUID = 715745410605631233L;
    private String requestId;
    /**
     * 响应码
     */
    private Integer code;
    /**
     * 响应消息
     */
    private String message;
    /**
     * 响应数据
     */
    private T data;

    //构造成功响应体
    public static <T> RpcResponse<T> success(T data , String requestId){
        RpcResponse<T> response = new RpcResponse<>();
        response.setCode(200);
        response.setMessage("远程服务调用成功");
        response.setRequestId(requestId);

        if (null != data) {
            response.setData(data);
        }
        return response;
    }

    public static <T> RpcResponse<T> fail() {
        RpcResponse<T> response = new RpcResponse<>();
        response.setCode(500);
        response.setMessage("远程服务调用异常");
        return response;
    }
}
