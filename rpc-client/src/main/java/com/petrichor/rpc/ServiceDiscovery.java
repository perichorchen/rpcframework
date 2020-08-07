package com.petrichor.rpc;

import java.net.InetSocketAddress;

/**
 * @author petrichor
 * @date 2020/8/4 11:57
 */
public interface ServiceDiscovery {
    /**
     * 客户端服务发现，我要调的这个服务哪个节点有，并且可以负载均衡
     * @param serviceName
     * @return ip和port
     */
    public InetSocketAddress discoveryService(String serviceName);
}
