package com.petrichor.rpc;


/**
 * @author petrichor
 * @date 2020/8/3 21:12
 * 不同客户端的接口
 */
public interface ClientType {
    /**
     * 发送消息到服务端
     * @param request 消息体
     * @return
     */
    Object sendRequest(Request request);
}
