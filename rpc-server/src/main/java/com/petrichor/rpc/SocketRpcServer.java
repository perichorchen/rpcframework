package com.petrichor.rpc;


import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

/**
 * @author petrichor
 * @date 2020/8/5 15:10
 */
@Slf4j
public class SocketRpcServer {
    private final ExecutorService threadPool;
    private final String host;
    private final int port;
    private final ServiceRegistry  serviceRegistry;
    private final ServiceProvider  serviceProvider;

    public SocketRpcServer(ExecutorService threadPool, String host, int port, ServiceRegistry serviceRegistry, ServiceProvider serviceProvider) {
        this.threadPool = ThreadPoolFactoryUtils.createCustomThreadPoolIfAbsent(new CustomThreadPoolConfig(),"rpc-threadpool",false);
        this.host = host;
        this.port = port;
        this.serviceRegistry = serviceRegistry;
        this.serviceProvider = serviceProvider;
    }


    private void start(){
        try{
            ServerSocket serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(host,port));
            //CustomShutdownHook.getCustomShutdownHook.clearAll();
            Socket socket;
            while((socket=serverSocket.accept())!=null){
                log.info("client connected [{}]", socket.getInetAddress());
                threadPool.execute(new SocketRpcRequestHandlerRunnable(socket));
            }

            threadPool.shutdown();

        }catch (IOException e){
            log.error("IOException" , e);
        }
    }


    //服务发布方法暂存
    public <T> void publishService(T service, Class<T> serviceClass) {    //发布服务，有的用户调的老接口，服务版本控制怎么做
        serviceProvider.addServiceProvider(service, serviceClass);
        serviceRegistry.registerService(serviceClass.getCanonicalName(), new InetSocketAddress(host, port)); //注册服务
        start();
    }
}
