package com.petrichor.rpc;

import java.net.InetSocketAddress;
import java.net.ServerSocket;

/**
 * @author petrichor
 * @date 2020/8/7 20:59
 */
public class ServiceRegistryImpl implements ServiceRegistry {
    @Override
    public void registerService(String serviceName, InetSocketAddress inetSocketAddress) {
        String servicePath = CuratorUtils.ZK_REISTER_ROOT_PATH + serviceName +inetSocketAddress.toString();
        CuratorUtils.createPersistentNode(servicePath);
    }
}
