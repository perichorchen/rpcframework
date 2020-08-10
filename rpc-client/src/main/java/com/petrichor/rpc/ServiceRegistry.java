package com.petrichor.rpc;

import java.net.InetSocketAddress;

/**
 * 服务注册接口
 * @author petrichor
 * @date 2020/8/7 20:36
 */
public interface ServiceRegistry {

    //注册服务
    public  void registerService(String serviceName, InetSocketAddress inetSocketAddress);
}
