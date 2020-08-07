package com.petrichor.rpc;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author petrichor
 * @date 2020/8/4 11:59
 */
public class ServiceDiscoveryImpl implements ServiceDiscovery {
    private final LoadBalance loadBalance;

    public ServiceDiscoveryImpl(LoadBalance loadBalance) {
        this.loadBalance = loadBalance;
    }

    @Override
    public InetSocketAddress discoveryService(String serviceName) {
        List<String> serviceUrlList = CuratorUtils.getChildNodes(serviceName);
        String targetServiceUrl = loadBalance.selectServiceAddress(serviceUrlList);
        String[] socketAddressArray = targetServiceUrl.split(":");
        String host = socketAddressArray[0];
        int port = Integer.parseInt(socketAddressArray[1]);
        return new InetSocketAddress(host, port);
    }
}
